/*
 * ==========================License-Start=============================
 * graphene-core : RDFOutput
 *
 * Copyright © 2017 Lambda³
 *
 * GNU General Public License 3
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * ==========================License-End==============================
 */

package org.lambda3.graphene.core.simplified_graph_extraction.runner;

import org.lambda3.graphene.core.simplified_graph_extraction.model.*;
import org.lambda3.text.simplification.discourse.processing.OutSentence;
import org.lambda3.text.simplification.discourse.processing.Processor;
import org.lambda3.text.simplification.discourse.relation_extraction.Element;
import org.lambda3.text.simplification.discourse.relation_extraction.ElementRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 *
 */
public class DiscourseExtractionRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DiscourseExtractionRunner.class);
    private static final Processor DISCOURSE_EXTRACTOR = new Processor();

    private static Optional<ExVContext> isVContext(Element element) {
        if (!element.isProperSentence()) {
            ExVContext newContext = new ExVContext(element.getText());

            return Optional.of(newContext);
        } else {
            return Optional.empty();
        }
    }

    private static ExElement getElement(Element element, Map<Element, ExElement> elementMapping) {
        ExElement res;

        if (elementMapping.containsKey(element)) {
            res = elementMapping.get(element);
        } else {
            res = new ExElement(element.getText(), element.getSentenceIdx(), element.getContextLayer());

            // add to map
            elementMapping.put(element, res);
        }

        return res;
    }

    public static ExContent doDiscourseExtraction(List<String> sentences) {
        LOG.info("Running discourse extraction on {} sentences", sentences.size());

        List<OutSentence> outSentences = DISCOURSE_EXTRACTOR.process(sentences, Processor.ProcessingType.WHOLE);

        HashMap<Element, ExElement> elementMapping = new LinkedHashMap<>(); // maps (former) Elements to ExElements
        List<ExSentence> exSentences = new ArrayList<>();

        for (OutSentence outSentence : outSentences) {
            ExSentence exSentence = new ExSentence(outSentence.getOriginalSentence(), outSentence.getSentenceIdx());

            for (Element element : outSentence.getElements()) {

                // create element
                if (!isVContext(element).isPresent()) {
                    ExElement newElement = getElement(element, elementMapping);

                    // add new element
                    exSentence.addElement(newElement);

                    // add relations
                    for (ElementRelation elementRelation : element.getRelations()) {

                        // create VContext
                        Optional<ExVContext> ct = isVContext(elementRelation.getTarget());
                        if (ct.isPresent()) {
                            ExVContext newContext = ct.get();

                            // set classification
                            newContext.setClassification(Classification.convert(elementRelation.getRelation()));

                            // add context
                            newElement.addVContext(newContext);
                        } else

                        // create relation
                        {
                            ExElement target = getElement(elementRelation.getTarget(), elementMapping);

                            // add relation
                            newElement.addRelation(new ExElementRelation(target.getId(), Classification.convert(elementRelation.getRelation())));
                        }
                    }
                }
            }

            exSentences.add(exSentence);
        }

        return new ExContent(exSentences);
    }
}
