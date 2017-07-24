package org.lambda3.graphene.core.relation_extraction.runner;

/*-
 * ==========================License-Start=============================
 * SimplificationRunner.java - Graphene Core - Lambda^3 - 2017
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


import edu.stanford.nlp.trees.Tree;
import org.lambda3.graphene.core.relation_extraction.model.*;
import org.lambda3.graphene.core.relation_extraction.runner.context_classifier.ExContextClassifierStanford;
import org.lambda3.text.simplification.discourse.utils.words.WordsUtils;
import org.lambda3.text.simplification.sentence.transformation.CoreContextSentence;
import org.lambda3.text.simplification.sentence.transformation.SentenceSimplifyingException;
import org.lambda3.text.simplification.sentence.transformation.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimplificationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SimplificationRunner.class);
    private static final ExContextClassifier CONTEXT_CLASSIFIER = new ExContextClassifierStanford();
    private static final Pattern SIMPLE_CONTEXT_PATTERN = Pattern.compile("^\\W*this\\W+\\w+\\W+(?<text>.*\\w+.*)$", Pattern.CASE_INSENSITIVE);

	public SimplificationRunner() {
	}

	private static Optional<Extraction> contains(List<Extraction> extractions, Extraction extraction) {
		for (Extraction ex : extractions) {
			if (ex.getSentenceIdx() == extraction.getSentenceIdx() && ex.getText().equals(extraction.getText())) {
				return Optional.of(ex);
			}
		}
		return Optional.empty();
	}

	private static Optional<SimpleContext> isSimpleContext(String text) {
        Matcher matcher = SIMPLE_CONTEXT_PATTERN.matcher(text);

        if (matcher.matches()) {
            SimpleContext res = new SimpleContext(matcher.group("text"));

            return Optional.of(res);
        } else {
            return Optional.empty();
        }
    }

    private static void processExtraction(Extraction extraction, List<Extraction> newExtractions) {
        LOG.debug("Simplifying: '{}'", extraction.getText());

        // apply sentence simplification
        Transformer transformer = new Transformer();
		CoreContextSentence s;
        try {
            s = transformer.simplify(extraction.getText());
		} catch (SentenceSimplifyingException e) {
			LOG.warn("Failed to simplify text: \"{}\"", extraction.getText());
			return;
		}

		// set simplified text
		if ((s.getCore() != null) && (s.getCore().size() > 0)) {
			Tree c = s.getCore().get(0);
			if (c != null) {
				String text = WordsUtils.wordsToProperSentenceString(c.yieldWords());
				extraction.setText(text);
			}
		}

		// process (sentence simplification) contexts
		if (s.getContext() != null) {
			for (Tree c : s.getContext()) {
				if (c != null) {
					String text = WordsUtils.wordsToProperSentenceString(c.yieldWords());

					// create SimpleContext
					Optional<SimpleContext> sc = isSimpleContext(text);
					if (sc.isPresent()) {
						SimpleContext newContext = sc.get();

						// classify SimpleContext
						ClassificationResult cr = CONTEXT_CLASSIFIER.classify(newContext.getText());
						newContext.setClassification(cr.getClassification());
						cr.getTimeInformation().ifPresent(t -> newContext.setTimeInformation(t));

						// add new SimpleContext
						extraction.addSimpleContext(newContext);
					} else {

						// create new Extraction
						Extraction newExtraction = new Extraction(
							ExtractionType.NOUN_BASED,
							text,
							extraction.getSentenceIdx(),
							extraction.getContextLayer() + 1
						);

						String linkID;

						// new Extraction already contained?
						Optional<Extraction> e = contains(newExtractions, newExtraction);
						if (e.isPresent()) {
							// link to already existing Extraction (instead of creating a new one)
							linkID = e.get().getId();
						} else {
							linkID = newExtraction.getId();

							// add new Extraction
							newExtractions.add(newExtraction);
						}

						// link
						extraction.addLinkedContext(new LinkedContext(linkID, Classification.NOUN_BASED));
					}
				}
			}
		}

    }

    public void doSimplification(ExContent content) {

        // simplify all Extractions
		ArrayList<Extraction> newExtractions = new ArrayList<>();
        for (Extraction extraction : content.getExtractions()) {
            processExtraction(extraction, newExtractions);
        }

        // add new Extractions
		newExtractions.forEach(e -> content.addExtraction(e));
    }
}
