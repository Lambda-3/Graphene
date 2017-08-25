package org.lambda3.graphene.core.relation_extraction;

/*-
 * ==========================License-Start=============================
 * RelationExtractionRunner.java - Graphene Core - Lambda^3 - 2017
 * Graphene
 * %%
 * Copyright (C) 2017 Lambda^3
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * ==========================License-End===============================
 */


import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import org.lambda3.graphene.core.relation_extraction.model.*;
import org.lambda3.graphene.core.relation_extraction.model.LinkedContext;
import org.lambda3.graphene.core.relation_extraction.model.SimpleContext;
import org.lambda3.text.simplification.discourse.model.Element;
import org.lambda3.text.simplification.discourse.model.OutSentence;
import org.lambda3.text.simplification.discourse.model.SimplificationContent;
import org.lambda3.text.simplification.discourse.runner.discourse_tree.Relation;
import org.lambda3.text.simplification.discourse.utils.parseTree.ParseTreeExtractionUtils;
import org.lambda3.text.simplification.discourse.utils.words.WordsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class RelationExtractionRunner {
	private final boolean exploitCore;
	private final boolean exploitContexts;
	private final boolean separateNounBased;
	private final boolean separateEnablements;
	private final boolean separateAttributions;

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final RelationExtractor extractor;

	private final HashMap<Element, List<Extraction>> elementCoreExtractionMap;

	private class NewExtraction {
		private boolean asLinkedContext;
		private Relation classification;
		private Extraction extraction;

		public NewExtraction(boolean asLinkedContext, Relation classification, Extraction extraction) {
			this.asLinkedContext = asLinkedContext;
			this.classification = classification;
			this.extraction = extraction;
		}

		public boolean isAsLinkedContext() {
			return asLinkedContext;
		}

		public Relation getClassification() {
			return classification;
		}

		public Extraction getExtraction() {
			return extraction;
		}

		@Override
		public String toString() {
			return extraction.toString();
		}
	}

	public RelationExtractionRunner(Config config) {

		// load boolean values
		this.exploitCore = config.getBoolean("exploit-core");
		this.exploitContexts = config.getBoolean("exploit-contexts");
		this.separateNounBased = config.getBoolean("separate-noun-based");
		this.separateEnablements = config.getBoolean("separate-enablements");
		this.separateAttributions = config.getBoolean("separate-attributions");

		// instantiate extractor
		String extractorClassName = config.getString("relation-extractor");
		try {
			Class<?> extractorClass = Class.forName(extractorClassName);
			Constructor<?> extractorConst = extractorClass.getConstructor();
			this.extractor = (RelationExtractor) extractorConst.newInstance();
		} catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
			logger.error("Failed to create instance of {}", extractorClassName);
			throw new ConfigException.BadValue("relation-extractor." + extractorClassName, "Failed to create instance.");
		}

		this.elementCoreExtractionMap = new LinkedHashMap<>();
	}

	private NewExtraction createYieldedExtraction(int sentenceIdx, BinaryExtraction ex) {
		return new NewExtraction(false, Relation.UNKNOWN, new Extraction(
			ExtractionType.VERB_BASED,
			ex.getConfidence().orElse(null),
			sentenceIdx,
			-1,
			ex.getRelation(),
			ex.getArg1(),
			ex.getArg2()
		));
	}

	private void processElement(Element element, List<Extraction> coreExtractions, List<NewExtraction> newExtractions) {
		for (BinaryExtraction ex : extractor.extract(element.getParseTree())) {
			if (ex.isCoreExtraction()) {
				coreExtractions.add(
					new Extraction(
						ExtractionType.VERB_BASED,
						ex.getConfidence().orElse(null),
						element.getSentenceIdx(),
						element.getContextLayer(),
						ex.getRelation(),
						ex.getArg1(),
						ex.getArg2()
					)
				);
			} else if (exploitCore) {
				// yield additional extractions
				newExtractions.add(createYieldedExtraction(element.getSentenceIdx(), ex));
			}
		}
	}

	private void processSimpleContext(Element element, org.lambda3.text.simplification.discourse.model.SimpleContext simpleContext, List<NewExtraction> newExtractions, List<SimpleContext> simpleContexts) {

		// yield additional extractions
		if (exploitContexts) {
			for (BinaryExtraction ex : extractor.extract(simpleContext.getParseTree())) {
				if (!ex.isCoreExtraction()) {
					newExtractions.add(createYieldedExtraction(element.getSentenceIdx(), ex));
				}
			}
		}

		// rephrase as separate extractions
		if (simpleContext.getRelation().equals(Relation.NOUN_BASED) && separateNounBased) {

			// NOUN BASED
			List<BinaryExtraction> extractions = extractor.extract(simpleContext.getParseTree());
			extractions.stream().filter(ex -> ex.isCoreExtraction()).forEach(ex ->
				newExtractions.add(new NewExtraction(true, simpleContext.getRelation(), new Extraction(
					ExtractionType.NOUN_BASED,
					ex.getConfidence().orElse(null),
					element.getSentenceIdx(),
					element.getContextLayer(),
					ex.getRelation(),
					ex.getArg1(),
					ex.getArg2()
				)))
			);

		} else if (simpleContext.getRelation().equals(Relation.ENABLEMENT) && separateEnablements) {

			// ENABLEMENT
			TregexPattern pattern = TregexPattern.compile("VP=vp !>> VP [ <+(VP) (VP !< VP < (PP|NP|S|SBAR=arg2 !$,, (PP|NP|S|SBAR))) | ==(VP !< VP < (PP|NP|S|SBAR=arg2 !$,, (PP|NP|S|SBAR))) ]");
			TregexMatcher matcher = pattern.matcher(simpleContext.getPhrase());
			while (matcher.find()) {
				Tree vp = matcher.getNode("vp");
				Tree arg2 = matcher.getNode("arg2");
				List<Word> relationWords = ParseTreeExtractionUtils.getWordsInBetween(simpleContext.getPhrase(), vp, arg2, true, false);
				List<Word> arg2Words = ParseTreeExtractionUtils.getFollowingWords(vp, arg2, true);
				newExtractions.add(new NewExtraction(true, simpleContext.getRelation(), new Extraction(
					ExtractionType.VERB_BASED,
					null,
					element.getSentenceIdx(),
					element.getContextLayer() + 1,
					WordsUtils.wordsToString(relationWords),
					element.getText(),
					WordsUtils.wordsToString(arg2Words)
				)));
			}

		} else if (simpleContext.getRelation().equals(Relation.ATTRIBUTION) && separateAttributions) {

			// ATTRIBUTION
			TregexPattern pattern = TregexPattern.compile("S !>> S < (NP=arg1 $.. (VP=vp [ <+(VP) (VP=lowestvp !< VP) | ==(VP=lowestvp !< VP) ]))");
			TregexMatcher matcher = pattern.matcher(simpleContext.getPhrase());
			while (matcher.find()) {
				Tree arg1 = matcher.getNode("arg1");
				Tree vp = matcher.getNode("vp");
				Tree lowestvp = matcher.getNode("lowestvp");

				List<Word> arg1Words;
				List<Word> relationWords;

				// has arg2 ?
				TregexPattern arg2Pattern = TregexPattern.compile(lowestvp.value() + " < (PP|NP|S|SBAR=arg2 !$,, (PP|NP|S|SBAR))");
				TregexMatcher arg2Matcher = arg2Pattern.matcher(lowestvp);
				if (arg2Matcher.findAt(lowestvp)) {
					Tree arg2 = arg2Matcher.getNode("arg2");

					arg1Words = ParseTreeExtractionUtils.getContainingWords(arg1);
					relationWords = ParseTreeExtractionUtils.getWordsInBetween(simpleContext.getPhrase(), vp, arg2, true, false);
				} else {
					arg1Words = ParseTreeExtractionUtils.getContainingWords(arg1);
					relationWords = ParseTreeExtractionUtils.getContainingWords(vp);
				}
				newExtractions.add(new NewExtraction(true, simpleContext.getRelation(), new Extraction(
					ExtractionType.VERB_BASED,
					null,
					element.getSentenceIdx(),
					element.getContextLayer() + 1,
					WordsUtils.wordsToString(relationWords),
					WordsUtils.wordsToString(arg1Words),
					element.getText()
				)));
			}
		}

		// add as simple context
		SimpleContext c = new SimpleContext(
			WordsUtils.wordsToString(ParseTreeExtractionUtils.getContainingWords(simpleContext.getPhrase())),
			simpleContext.getRelation()
		);
		simpleContext.getTimeInformation().ifPresent(t -> c.setTimeInformation(t));
		simpleContexts.add(c);
	}

	private void processLinkedContext(Element element, org.lambda3.text.simplification.discourse.model.LinkedContext linkedContext, List<LinkedContext> linkedContexts, SimplificationContent simplificationContent, ExContent exContent) {
		List<Extraction> targets = processElement(linkedContext.getTargetElement(simplificationContent), simplificationContent, exContent);
		targets.forEach(t -> linkedContexts.add(new LinkedContext(
			t.getId(),
			linkedContext.getRelation()
		)));
	}

	private List<Extraction> processElement(Element element, SimplificationContent simplificationContent, ExContent exContent) {
		if (elementCoreExtractionMap.containsKey(element)) {
			return elementCoreExtractionMap.get(element);
		} else {
			List<Extraction> coreExtractions = new ArrayList<>();
			List<NewExtraction> newExtractions = new ArrayList<>(); // will be transformed to linked contexts
			List<SimpleContext> simpleContexts = new ArrayList<>();
			List<LinkedContext> linkedContexts = new ArrayList<>();

			// process element
			processElement(element, coreExtractions, newExtractions);

			// process simple contexts
			element.getSimpleContexts().forEach(c -> processSimpleContext(element, c, newExtractions, simpleContexts));

			// add core extractions
			coreExtractions.forEach(e -> exContent.addExtraction(e));

			// add new extractions
			newExtractions.forEach(e -> exContent.addExtraction(e.getExtraction()));

			// add to map
			elementCoreExtractionMap.put(element, coreExtractions);

			// process linked contexts (recursion)
			for (org.lambda3.text.simplification.discourse.model.LinkedContext linkedContext : element.getLinkedContexts()) {
				processLinkedContext(element, linkedContext, linkedContexts, simplificationContent, exContent);
			}

			// add contexts (simple and linked) to core extractions
			for (Extraction coreExtraction : coreExtractions) {

				// simple
				simpleContexts.forEach(c -> coreExtraction.addSimpleContext(c));

				// linked
				linkedContexts.forEach(c -> coreExtraction.addLinkedContext(c));
				newExtractions.stream().filter(e -> e.isAsLinkedContext()).forEach(e -> coreExtraction.addLinkedContext(new LinkedContext(e.getExtraction().getId(), e.getClassification())));
			}

//			logger.info("CORE-EXTRACTIONS:");
//			coreExtractions.forEach(e -> logger.info(e.toString()));
//			logger.info("NEW-EXTRACTIONS:");
//			newExtractions.forEach(e -> logger.info(e.toString()));

			return coreExtractions;
		}
	}

	public ExContent doRelationExtraction(SimplificationContent simplificationContent) {
		ExContent exContent = new ExContent();
		elementCoreExtractionMap.clear();

		for (OutSentence outSentence : simplificationContent.getSentences()) {
			exContent.addSentence(new ExSentence(outSentence.getOriginalSentence(), outSentence.getSentenceIdx()));
		}

		for (Element element : simplificationContent.getElements()) {
			logger.info("\n######################################################################\n");
			logger.info(element.toString());

			processElement(element, simplificationContent, exContent);
		}

		return exContent;
	}
}
