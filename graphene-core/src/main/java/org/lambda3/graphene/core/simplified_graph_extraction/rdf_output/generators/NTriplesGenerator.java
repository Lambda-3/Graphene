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

package org.lambda3.graphene.core.simplified_graph_extraction.rdf_output.generators;

import org.lambda3.graphene.core.simplified_graph_extraction.model.*;
import org.lambda3.graphene.core.simplified_graph_extraction.rdf_output.RDFGenerator;
import org.lambda3.graphene.core.utils.IDGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class NTriplesGenerator extends RDFGenerator {

    // official
    private static final String RDF_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private static final String RDFS_NAMESPACE = "http://www.w3.org/2000/01/rdf-schema#";
    private static final String XML_NAMESPACE = "http://www.w3.org/2001/XMLSchema#";

    // graphene
    private static final String GRAPHENE_SENTENCE_NAMESPACE = "http://lambda3.org/graphene/sentence#";
    private static final String GRAPHENE_EXTRACTION_NAMESPACE = "http://lambda3.org/graphene/extraction#";
    private static final String GRAPHENE_TEXT_NAMESPACE = "http://lambda3.org/graphene/text#";

    private static String rdfLiteral(int number) {
        return "\"" + String.valueOf(number) + "\"" + "^^" + "<" + XML_NAMESPACE + "integer" + ">";
    }

    private static String rdfLiteral(String text, String languageTag) {
        String langStr = (languageTag != null)? "@" + languageTag : "";
        return "\"" + text + "\"" + langStr + "^^" + "<" + XML_NAMESPACE + "string" + ">";
    }

    private static String rdfRescource(String text) {
        return "<" + RDF_NAMESPACE + text + ">";
    }

    private static String rdfsRescource(String text) {
        return "<" + RDFS_NAMESPACE + text + ">";
    }

    private static String grapheneSentenceRescource(String text) {
        return "<" + GRAPHENE_SENTENCE_NAMESPACE + text + ">";
    }

    private static String grapheneExtractionRescource(String text) {
        return "<" + GRAPHENE_EXTRACTION_NAMESPACE + text + ">";
    }

    private static String grapheneTextRescource(String text) {
        return "<" + GRAPHENE_TEXT_NAMESPACE + text + ">";
    }

    private static String rdfBlankNode(String id) {
        return "_:" + id;
    }

    private static String rdfTriple(String subject, String predicate, String object) {
        return subject + " " + predicate + " " + object + " .";
    }

    @Override
    public List<String> format(ExContent content) {
        List<String> res = new ArrayList<>();

        for (ExSentence exSentence : content.getSentences()) {

            // sentence
            res.add("# " + exSentence.getOriginalSentence());
            res.add("");
            String sentenceId = IDGenerator.generateUUID();
            String sentenceBlankNode = rdfBlankNode(sentenceId);
            res.add(rdfTriple(sentenceBlankNode, grapheneSentenceRescource("original-text"), rdfLiteral(exSentence.getOriginalSentence(), null)));
            res.add("");

            for (ExElement element : exSentence.getElements()) {
                if (!element.getSpo().isPresent()) {
                    continue;
                }
                ExSPO spo = element.getSpo().get();

                // element
                String elementBlankNode = rdfBlankNode(element.getId());

                res.add(rdfTriple(sentenceBlankNode, grapheneSentenceRescource("has-extraction"), elementBlankNode));
                res.add(rdfTriple(elementBlankNode, grapheneExtractionRescource("subject"), grapheneTextRescource(element.getSpo().get().getSubject())));
                res.add(rdfTriple(elementBlankNode, grapheneExtractionRescource("predicate"), grapheneTextRescource(element.getSpo().get().getPredicate())));
                res.add(rdfTriple(elementBlankNode, grapheneExtractionRescource("object"), grapheneTextRescource(element.getSpo().get().getObject())));
                res.add(rdfTriple(elementBlankNode, grapheneExtractionRescource("context-layer"), rdfLiteral(element.getContextLayer())));

                // element-values
                res.add(rdfTriple(grapheneTextRescource(element.getSpo().get().getSubject()), rdfRescource("value"), rdfLiteral(element.getSpo().get().getSubject(), null)));
                res.add(rdfTriple(grapheneTextRescource(element.getSpo().get().getPredicate()), rdfRescource("value"), rdfLiteral(element.getSpo().get().getPredicate(), null)));
                res.add(rdfTriple(grapheneTextRescource(element.getSpo().get().getObject()), rdfRescource("value"), rdfLiteral(element.getSpo().get().getObject(), null)));

                // vContexts
                for (ExVContext context : element.getVContexts()) {
                    String vContextAbbrev = vContextAbbrev(context);
                    res.add(rdfTriple(elementBlankNode, grapheneExtractionRescource(vContextAbbrev), grapheneTextRescource(context.getText())));

                    // vContext-value
                    res.add(rdfTriple(grapheneTextRescource(context.getText()), rdfRescource("value"), rdfLiteral(context.getText(), null)));
                }

                // element contexts
                for (ExElementRelation relation : element.getRelations()) {
                    ExElement target = relation.getTargetElement(content);
                    if (target.getSpo().isPresent()) {
                        String targetBlankNode = rdfBlankNode(target.getId());
                        String elementAbbrev = elementAbbrev(target, relation.getClassification());
                        res.add(rdfTriple(elementBlankNode, grapheneExtractionRescource(elementAbbrev), targetBlankNode));
                    }
                }

                // nContexts (as separate statements)
                for (ExNContext context : element.getNContexts()) {
                    if (context.getSpo().isPresent()) {
                        String nContextAbbrev = nContextAbbrev(context);
                        String nContextId = IDGenerator.generateUUID();
                        String nContextBlankNode = rdfBlankNode(nContextId);
                        res.add(rdfTriple(elementBlankNode, grapheneExtractionRescource(nContextAbbrev), nContextBlankNode));

                        // nContext
                        res.add("");
                        res.add(rdfTriple(nContextBlankNode, grapheneExtractionRescource("subject"), grapheneTextRescource(context.getSpo().get().getSubject())));
                        res.add(rdfTriple(nContextBlankNode, grapheneExtractionRescource("predicate"), grapheneTextRescource(context.getSpo().get().getPredicate())));
                        res.add(rdfTriple(nContextBlankNode, grapheneExtractionRescource("object"), grapheneTextRescource(context.getSpo().get().getObject())));
                        res.add(rdfTriple(nContextBlankNode, grapheneExtractionRescource("context-layer"), rdfLiteral(element.getContextLayer() + 1)));

                        // nContext-values
                        res.add(rdfTriple(grapheneTextRescource(context.getSpo().get().getSubject()), rdfRescource("value"), rdfLiteral(context.getSpo().get().getSubject(), null)));
                        res.add(rdfTriple(grapheneTextRescource(context.getSpo().get().getPredicate()), rdfRescource("value"), rdfLiteral(context.getSpo().get().getPredicate(), null)));
                        res.add(rdfTriple(grapheneTextRescource(context.getSpo().get().getObject()), rdfRescource("value"), rdfLiteral(context.getSpo().get().getObject(), null)));
                    }
                }

                res.add("");
            }
        }

        return res;
    }
}
