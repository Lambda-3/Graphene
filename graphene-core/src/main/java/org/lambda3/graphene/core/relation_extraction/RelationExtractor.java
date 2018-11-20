package org.lambda3.graphene.core.relation_extraction;

/*-
 * ==========================License-Start=============================
 * RelationExtractor.java - Graphene Core - Lambda^3 - 2017
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
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import org.lambda3.graphene.core.relation_extraction.model.Extraction;
import org.lambda3.graphene.core.relation_extraction.model.ExtractionType;
import org.lambda3.graphene.core.relation_extraction.model.Triple;
import org.lambda3.text.simplification.discourse.model.Element;
import org.lambda3.text.simplification.discourse.model.Sentence;
import org.lambda3.text.simplification.discourse.model.SimpleContext;
import org.lambda3.text.simplification.discourse.model.SimplificationContent;
import org.lambda3.text.simplification.discourse.runner.discourse_tree.RelationType;
import org.lambda3.text.simplification.discourse.utils.parseTree.ParseTreeExtractionUtils;
import org.lambda3.text.simplification.discourse.utils.words.WordsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class RelationExtractor {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final HeadVerbFinder HEAD_VERB_FINDER = new HeadVerbFinder();

	private static final TregexPattern PURPOSE_PATTERN = TregexPattern.compile("VP=vp !>> VP [ <+(VP) (VP !< VP < (PP|NP|S|SBAR=arg2 !$,, (PP|NP|S|SBAR))) | ==(VP !< VP < (PP|NP|S|SBAR=arg2 !$,, (PP|NP|S|SBAR))) ]");
	private static final TregexPattern ATTRIBUTION_PATTERN = TregexPattern.compile("S !>> S < (NP=arg1 $.. (VP=vp [ <+(VP) (VP=lowestvp !< VP) | ==(VP=lowestvp !< VP) ]))");

	private static final TregexPattern HEAD_RELATION_EXTRACTION_PATTERN = TregexPattern.compile("S !>> S < (NP=arg1 $.. (VP=vp [ <+(VP) (VP=lowestvp !< VP) | ==(VP=lowestvp !< VP) ]))");
	// this will generate nicer predicates that conform to matrix predicates (e.g. "failed to increase"), but lead to a minor decrease in P and R in benchmark test
	// TregexPattern pattern = TregexPattern.compile("S !>> S < (NP=arg1 $.. (VP=vp [ <+(S|VP) (VP=lowestvp !< VP !< S) | ==(VP=lowestvp !< VP !< S) ]))");

	private static final TregexPattern NESTED_RELATION_EXTRACTION_PATTERN = TregexPattern.compile("NP=arg1 $.. (VP=vp [ <+(VP) (VP=lowestvp !< VP) | ==(VP=lowestvp !< VP) ])");
	// this will generate nicer predicates that conform to matrix predicates (e.g. "failed to increase"), but lead to a minor decrease in P and R in benchmark test
	// TregexPattern pattern = TregexPattern.compile("NP=arg1 $.. (VP=vp [ <+(S|VP) (VP=lowestvp !< VP !< S) | ==(VP=lowestvp !< VP !< S) ])");

	private boolean exploitCore;
	private boolean exploitContexts;
	private boolean separateNounBased;
	private boolean separatePurposes;
	private boolean separateAttributions;
	private TregexPattern mainPattern;

	public RelationExtractor(Config config) {
		this.exploitCore = config.getBoolean("exploit-core");
		this.exploitContexts = config.getBoolean("exploit-contexts");
		this.separateNounBased = config.getBoolean("separate-noun-based");
		this.separatePurposes = config.getBoolean("separate-purposes");
		this.separateAttributions = config.getBoolean("separate-attributions");

		String re = config.getString("relation-extractor");
		String[] parts = re.split(".");
		if (parts[parts.length - 1].toLowerCase().contains("head")) {
			this.mainPattern = HEAD_RELATION_EXTRACTION_PATTERN;
		} else if (parts[parts.length - 1].toLowerCase().contains("head")) {
			this.mainPattern = NESTED_RELATION_EXTRACTION_PATTERN;
		} else {
			throw new RuntimeException("Unsupported relation extraction pattern = " + re);
		}
	}

	public void extract(SimplificationContent content) {
		for (Sentence sentence : content.getSentences()) {
			for (Element element : sentence.getElements()) {
				verbBasedExtractions(element);
			}
		}
	}

	//element.getParseTree()
	public List<Triple> mainExtraction(Tree tree) {
		List<Triple> triples = new ArrayList<>();

		TregexMatcher matcher = this.mainPattern.matcher(tree);
		while (matcher.find()) {
			Tree arg1 = matcher.getNode("arg1");
			Tree vp = matcher.getNode("vp");
			Tree lowestvp = matcher.getNode("lowestvp");

			// has arg2 ?
			TregexPattern arg2Pattern = TregexPattern.compile(lowestvp.value() + " < (PP|NP|S|SBAR=arg2 !$,, (PP|NP|S|SBAR))");
			TregexMatcher arg2Matcher = arg2Pattern.matcher(lowestvp);
			if (arg2Matcher.findAt(lowestvp)) {
				Tree arg2 = arg2Matcher.getNode("arg2");

				List<Word> arg1Words = ParseTreeExtractionUtils.getContainingWords(arg1);
				List<Word> relationWords = ParseTreeExtractionUtils.getWordsInBetween(tree, vp, arg2, true, false);
				List<Word> arg2Words = ParseTreeExtractionUtils.getFollowingWords(vp, arg2, true);

				triples.add(new Triple(
					WordsUtils.wordsToString(arg1Words),
					WordsUtils.wordsToString(relationWords),
					WordsUtils.wordsToString(arg2Words)
				));
			} else {
				List<Word> arg1Words = ParseTreeExtractionUtils.getContainingWords(arg1);
				List<Word> relationWords = ParseTreeExtractionUtils.getContainingWords(vp);
				List<Word> arg2Words = new ArrayList<>();

				triples.add(new Triple(
					WordsUtils.wordsToString(arg1Words),
					WordsUtils.wordsToString(relationWords),
					WordsUtils.wordsToString(arg2Words)
				));
			}
		}

		return triples;
	}

	public void verbBasedExtractions(Element element) {
		Optional<String> headVerb = HEAD_VERB_FINDER.findHeadVerb(element.getParseTree());

		List<Triple> triples = mainExtraction(element.getParseTree());
		for (Triple t : triples) {
			boolean isCoreExtraction = headVerb.filter(s -> t.property.contains(s) || t.object.equals(s)).isPresent();

			if (isCoreExtraction || this.exploitCore) {
				boolean asLinkedContext = !isCoreExtraction; //redudant?
				RelationType classification = (isCoreExtraction ? null /*what to put here?*/ : RelationType.UNKNOWN);

				element.addListExtension(new Extraction(ExtractionType.VERB_BASED, t,
					asLinkedContext, classification, isCoreExtraction));
			}
		}
	}

	public List<Triple> purposeBasedExtractions(Tree tree, String subject) {
		List<Triple> triples = new LinkedList<>();

		TregexMatcher matcher = PURPOSE_PATTERN.matcher(tree);
		while (matcher.find()) {
			Tree vp = matcher.getNode("vp");
			Tree arg2 = matcher.getNode("arg2");
			List<Word> relationWords = ParseTreeExtractionUtils.getWordsInBetween(tree, vp, arg2, true, false);
			List<Word> arg2Words = ParseTreeExtractionUtils.getFollowingWords(vp, arg2, true);
			Triple triple = new Triple(subject, WordsUtils.wordsToString(relationWords), WordsUtils.wordsToString(arg2Words));
			triples.add(triple);
		}

		return triples;
	}


	public List<Triple> attributionBasedExtraction(Tree tree, String object) {
		List<Triple> triples = new LinkedList<>();

		TregexMatcher matcher = ATTRIBUTION_PATTERN.matcher(tree);
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
				relationWords = ParseTreeExtractionUtils.getWordsInBetween(tree, vp, arg2, true, false);
			} else {
				arg1Words = ParseTreeExtractionUtils.getContainingWords(arg1);
				relationWords = ParseTreeExtractionUtils.getContainingWords(vp);
			}
			Triple triple = new Triple(WordsUtils.wordsToString(arg1Words), WordsUtils.wordsToString(relationWords), object);
			triples.add(triple);
		}

		return triples;
	}

	private void processSimpleContext(Element element, SimpleContext simpleContext) {

		RelationType relationType = simpleContext.getRelation();

		Tree parseTree = simpleContext.getParseTree();
		Optional<String> headVerbParseTree = HEAD_VERB_FINDER.findHeadVerb(simpleContext.getParseTree());

		List<Triple> triples = mainExtraction(parseTree);

		for (Triple triple : triples) {
			Boolean isCoreExtraction = isCoreExtraction(headVerbParseTree, triple);

			if (isCoreExtraction) {
				if (relationType.equals(RelationType.NOUN_BASED) && separateNounBased) {
					simpleContext.addListExtension(new Extraction(
						ExtractionType.NOUN_BASED,
						triple,
						true,
						relationType,
						isCoreExtraction
					));
				}
			} else if (exploitContexts) {
				simpleContext.addListExtension(new Extraction(
					ExtractionType.VERB_BASED,
					triple,
					false,
					RelationType.UNKNOWN,
					isCoreExtraction
				));
			}
		}


		Tree phrase = simpleContext.getPhrase();
		Optional<String> headVerbPhrase = HEAD_VERB_FINDER.findHeadVerb(phrase);

		if (relationType.equals(RelationType.PURPOSE) && separatePurposes) {

			List<Triple> purposeBasedTriples = purposeBasedExtractions(phrase, element.getText());
			for (Triple pt : purposeBasedTriples) {
				simpleContext.addListExtension(new Extraction(
					ExtractionType.VERB_BASED,
					pt,
					true, relationType,
					isCoreExtraction(headVerbPhrase, pt)
				));
			}

		} else if (simpleContext.getRelation().equals(RelationType.ATTRIBUTION) && separateAttributions) {
			List<Triple> attributionBasedExtractions = attributionBasedExtraction(simpleContext.getPhrase(), element.getText());

			for (Triple at : attributionBasedExtractions) {
				simpleContext.addListExtension(new Extraction(
					ExtractionType.VERB_BASED,
					at,
					true,
					relationType,
					isCoreExtraction(headVerbPhrase, at)
				));
			}
		}
	}

	protected boolean isCoreExtraction(Optional<String> headVerb, Triple triple) {
		return headVerb.filter(s -> triple.property.contains(s) || triple.object.equals(s)).isPresent();

	}
}
