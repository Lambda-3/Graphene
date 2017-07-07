package org.lambda3.graphene.core.relation_extraction.representation.generators;

/*-
 * ==========================License-Start=============================
 * NTriplesGenerator.java - Graphene Core - Lambda^3 - 2017
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


import org.lambda3.graphene.core.relation_extraction.model.*;
import org.lambda3.graphene.core.relation_extraction.representation.RepGenerator;
import org.lambda3.graphene.core.utils.IDGenerator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class NTriplesGenerator extends RepGenerator {

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
        String escapedText = text.replace("\"", "\\\"").replace("\n", "\\\n").replace("\r", "\\\r").replace("\\", "");
        String langStr = (languageTag != null) ? "@" + languageTag : "";

        return "\"" + escapedText + "\"" + langStr + "^^" + "<" + XML_NAMESPACE + "string" + ">";
    }

    private static String rdfResource(String text) {
        return "<" + RDF_NAMESPACE + text + ">";
    }

    private static String rdfsResource(String text) {
        return "<" + RDFS_NAMESPACE + text + ">";
    }

    private static String grapheneSentenceResource(String text) {
        return "<" + GRAPHENE_SENTENCE_NAMESPACE + text + ">";
    }

    private static String grapheneExtractionResource(String text) {
        return "<" + GRAPHENE_EXTRACTION_NAMESPACE + text + ">";
    }

    private static String grapheneTextResource(String text) {
        String escapedText = null;
        try {
            escapedText = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("Unsupported encoding.");
        }

        return "<" + GRAPHENE_TEXT_NAMESPACE + escapedText + ">";
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
            res.add(rdfTriple(sentenceBlankNode, grapheneSentenceResource("original-text"), rdfLiteral(exSentence.getOriginalSentence(), null)));
            res.add("");

            for (ExElement element : exSentence.getElements()) {
                if (!showText && !element.getSpo().isPresent()) {
                    continue;
                }

                // element
                String elementBlankNode = rdfBlankNode(element.getId());
                res.add(rdfTriple(sentenceBlankNode, grapheneSentenceResource("has-extraction"), elementBlankNode));
				res.add(rdfTriple(elementBlankNode, grapheneExtractionResource("context-layer"), rdfLiteral(element.getContextLayer())));
				if (showText) {
					res.add(rdfTriple(elementBlankNode, grapheneExtractionResource("text"), grapheneTextResource(element.getText())));
				} else {
					ExSPO spo = element.getSpo().get();
					res.add(rdfTriple(elementBlankNode, grapheneExtractionResource("subject"), grapheneTextResource(spo.getSubject())));
					res.add(rdfTriple(elementBlankNode, grapheneExtractionResource("predicate"), grapheneTextResource(spo.getPredicate())));
					res.add(rdfTriple(elementBlankNode, grapheneExtractionResource("object"), grapheneTextResource(spo.getObject())));

					// element-values
					res.add(rdfTriple(grapheneTextResource(spo.getSubject()), rdfResource("value"), rdfLiteral(spo.getSubject(), null)));
					res.add(rdfTriple(grapheneTextResource(spo.getPredicate()), rdfResource("value"), rdfLiteral(spo.getPredicate(), null)));
					res.add(rdfTriple(grapheneTextResource(spo.getObject()), rdfResource("value"), rdfLiteral(spo.getObject(), null)));
				}

                // vContexts
                for (ExVContext context : element.getVContexts()) {
                    String vContextAbbrev = vContextAbbrev(context);
                    res.add(rdfTriple(elementBlankNode, grapheneExtractionResource(vContextAbbrev), grapheneTextResource(context.getText())));

                    // vContext-value
                    res.add(rdfTriple(grapheneTextResource(context.getText()), rdfResource("value"), rdfLiteral(context.getText(), null)));
                }

                // element contexts
                for (ExElementRelation relation : element.getRelations()) {
                    ExElement target = relation.getTargetElement(content);
                    if (target.getSpo().isPresent()) {
                        String targetBlankNode = rdfBlankNode(target.getId());
                        String elementAbbrev = elementAbbrev(target, relation.getClassification());
                        res.add(rdfTriple(elementBlankNode, grapheneExtractionResource(elementAbbrev), targetBlankNode));
                    }
                }

                // nContexts (as separate statements)
                for (ExNContext context : element.getNContexts()) {
					if (!showText && !context.getSpo().isPresent()) {
						continue;
					}

					String nContextAbbrev = nContextAbbrev(context);
					String nContextId = IDGenerator.generateUUID();
					String nContextBlankNode = rdfBlankNode(nContextId);
					res.add(rdfTriple(elementBlankNode, grapheneExtractionResource(nContextAbbrev), nContextBlankNode));

					// nContext
					res.add("");
					res.add(rdfTriple(nContextBlankNode, grapheneExtractionResource("context-layer"), rdfLiteral(element.getContextLayer() + 1)));
					if (showText) {
						res.add(rdfTriple(nContextBlankNode, grapheneExtractionResource("text"), grapheneTextResource(context.getText())));
					} else {
						ExSPO spo = context.getSpo().get();
						res.add(rdfTriple(nContextBlankNode, grapheneExtractionResource("subject"), grapheneTextResource(spo.getSubject())));
						res.add(rdfTriple(nContextBlankNode, grapheneExtractionResource("predicate"), grapheneTextResource(spo.getPredicate())));
						res.add(rdfTriple(nContextBlankNode, grapheneExtractionResource("object"), grapheneTextResource(spo.getObject())));

						// nContext-values
						res.add(rdfTriple(grapheneTextResource(spo.getSubject()), rdfResource("value"), rdfLiteral(spo.getSubject(), null)));
						res.add(rdfTriple(grapheneTextResource(spo.getPredicate()), rdfResource("value"), rdfLiteral(spo.getPredicate(), null)));
						res.add(rdfTriple(grapheneTextResource(spo.getObject()), rdfResource("value"), rdfLiteral(spo.getObject(), null)));
					}
                }

                res.add("");
            }
        }

        return res;
    }
}
