package org.lambda3.graphene.core.relation_extraction.runner;

/*-
 * ==========================License-Start=============================
 * DiscourseExtractionRunner.java - Graphene Core - Lambda^3 - 2017
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
import org.lambda3.graphene.core.relation_extraction.model.*;
import org.lambda3.text.simplification.discourse.processing.OutSentence;
import org.lambda3.text.simplification.discourse.processing.Processor;
import org.lambda3.text.simplification.discourse.relation_extraction.Element;
import org.lambda3.text.simplification.discourse.relation_extraction.ElementRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DiscourseExtractionRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DiscourseExtractionRunner.class);
    private final Processor discourseExtractor;

	public DiscourseExtractionRunner(Config config) {
		this.discourseExtractor = new Processor(config);
	}

	private static Optional<SimpleContext> isSimpleContext(Element element) {
        if (!element.isProperSentence()) {
            SimpleContext newContext = new SimpleContext(element.getText());

            return Optional.of(newContext);
        } else {
            return Optional.empty();
        }
    }

    private static Extraction getElement(Element element, Map<Element, Extraction> elementMapping) {
        Extraction res;

        if (elementMapping.containsKey(element)) {
            res = elementMapping.get(element);
        } else {
            res = new Extraction(ExtractionType.VERB_BASED, element.getText(), element.getSentenceIdx(), element.getContextLayer());

            // add to map
            elementMapping.put(element, res);
        }

        return res;
    }

    public ExContent doDiscourseExtraction(List<String> sentences, boolean isolateSentences) {
        LOG.info("Running discourse extraction on {} sentences", sentences.size());

		Processor.ProcessingType processingType = (isolateSentences)? Processor.ProcessingType.SEPARATE : Processor.ProcessingType.WHOLE;
        List<OutSentence> outSentences = discourseExtractor.process(sentences, processingType);

        HashMap<Element, Extraction> elementMapping = new LinkedHashMap<>(); // maps (former) Elements to Extractions
        List<ExSentence> exSentences = new ArrayList<>();

        for (OutSentence outSentence : outSentences) {
            ExSentence exSentence = new ExSentence(outSentence.getOriginalSentence(), outSentence.getSentenceIdx());

            for (Element element : outSentence.getElements()) {

                // create Extraction
                if (!isSimpleContext(element).isPresent()) {
                    Extraction newExtraction = getElement(element, elementMapping);

                    // add new Extraction
                    exSentence.addExtraction(newExtraction);

                    // process relations
                    for (ElementRelation elementRelation : element.getRelations()) {

                        // create SimpleContext
                        Optional<SimpleContext> ct = isSimpleContext(elementRelation.getTarget());
                        if (ct.isPresent()) {
                            SimpleContext newContext = ct.get();

                            // set classification
                            newContext.setClassification(Classification.convert(elementRelation.getRelation()));

                            // add SimpleContext
                            newExtraction.addSimpleContext(newContext);
                        } else

                        // create LinkedContext
                        {
                            Extraction target = getElement(elementRelation.getTarget(), elementMapping);

                            // add LinkedContext
                            newExtraction.addLinkedContext(new LinkedContext(target.getId(), Classification.convert(elementRelation.getRelation())));
                        }
                    }
                }
            }

            exSentences.add(exSentence);
        }

        return new ExContent(exSentences);
    }
}
