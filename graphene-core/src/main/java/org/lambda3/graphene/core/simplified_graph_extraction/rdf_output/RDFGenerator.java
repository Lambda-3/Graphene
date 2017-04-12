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

package org.lambda3.graphene.core.simplified_graph_extraction.rdf_output;

import org.lambda3.graphene.core.simplified_graph_extraction.model.*;

import java.util.Optional;

/**
 *
 */
public class RDFGenerator {
    private static final String ELEMENT_STR = "ELEM";
    private static final String NCONTEXT_STR = "NCON";
    private static final String VCONTEXT_STR = "VCON";

    private static String elementAbbrev(ExElement element, Classification classification) {
        return ELEMENT_STR + ":" + classification.name();
    }

    private static String nContextAbbrev(ExNContext nContext) {
        return NCONTEXT_STR + ":" + nContext.getClassification().name();
    }

    private static String vContextAbbrev(ExVContext vContext) {
        return VCONTEXT_STR + ":" + vContext.getClassification().name();
    }

    private static Optional<String> elemContextRep(ExElement element, Classification classification, boolean linked, boolean asArg) {
        if (!element.getSpo().isPresent()) {
            return Optional.empty();
        }
        ExSPO spo = element.getSpo().get();
        String abbrev = elementAbbrev(element, classification);

        if (linked) {
            if (asArg) {
                return Optional.of(abbrev + "(" + element.getId() + ")");
            } else {
                return Optional.of(abbrev + "\t" + element.getId());
            }
        } else {
            if (asArg) {
                return Optional.of(abbrev + "(" + spo.getSubject() + "\t" + spo.getPredicate() + "\t" + spo.getObject() + ")");
            } else {
                return Optional.of(abbrev + "\t" + spo.getSubject() + "\t" + spo.getPredicate() + "\t" + spo.getObject());
            }
        }
    }

    private static Optional<String> nContextRep(ExNContext context, boolean asArg) {
        if (!context.getSpo().isPresent()) {
            return Optional.empty();
        }
        ExSPO spo = context.getSpo().get();
        String abbrev = nContextAbbrev(context);

        if (asArg) {
            return Optional.of(abbrev + "(" + spo.getSubject() + "\t" + spo.getPredicate() + "\t" + spo.getObject() + ")");
        } else {
            return Optional.of(abbrev + "\t" + spo.getSubject() + "\t" + spo.getPredicate() + "\t" + spo.getObject());
        }
    }

    private static String vContextRep(ExVContext context, boolean asArg) {
        String abbrev = vContextAbbrev(context);

        if (asArg) {
            return abbrev + "(" + context.getText() + ")";
        } else {
            return abbrev + "\t" + context.getText();
        }
    }

    public static String getRDFRepresentation(ExContent content, RDFStyle style, int maxContextDepth) {
        StringBuilder strb = new StringBuilder();

        for (ExSentence exSentence : content.getSentences()) {

            // sentence
            strb.append(exSentence.getOriginalSentence() + "\n\n");

            // all elements
            for (ExElement element : exSentence.getElements()) {
                if (style.equals(RDFStyle.DEFAULT)) {
                    addDefaultElementRepresentation(strb, content, element);
                } else if (style.equals(RDFStyle.FLAT)) {
                    addFlatElementRepresentation(strb, content, element);
                } else if (style.equals(RDFStyle.EXPANDED)) {
                    addExpandedElementRepresentation(strb, 0, maxContextDepth, content, element, null);
                } else {
                    throw new AssertionError("Unknown RDF style.");
                }
            }

            if (style.equals(RDFStyle.FLAT)) {
                strb.append("\n");
            }
        }

        return strb.toString();
    }

    public static String getRDFRepresentation(ExContent content, RDFStyle style) {
        return getRDFRepresentation(content, style, 5);
    }

    private static void addDefaultElementRepresentation(StringBuilder strb, ExContent content, ExElement element) {
        if (!element.getSpo().isPresent()) {
            return;
        }
        ExSPO spo = element.getSpo().get();

        // element
        strb.append(element.getId() + "\t" + element.getContextLayer() + "\t" + spo.getSubject() + "\t" + spo.getPredicate() + "\t" + spo.getObject() + "\n");

        // vContexts
        for (ExVContext context : element.getVContexts()) {
            String vContextRep = vContextRep(context, false);
            strb.append("\t" + vContextRep + "\n");
        }

        // nContexts
        for (ExNContext context : element.getNContexts()) {
            Optional<String> nContextRep = nContextRep(context, false);
            if (nContextRep.isPresent()) {
                strb.append("\t" + nContextRep.get() + "\n");
            }
        }

        // element contexts
        for (ExElementRelation relation : element.getRelations()) {
            ExElement target = relation.getTargetElement(content);
            Optional<String> elemContextRep = elemContextRep(target, relation.getClassification(), true, false);
            if (elemContextRep.isPresent()) {
                strb.append("\t" + elemContextRep.get() + "\n");
            }
        }

        strb.append("\n");
    }

    private static void addFlatElementRepresentation(StringBuilder strb, ExContent content, ExElement element) {
        if (!element.getSpo().isPresent()) {
            return;
        }
        ExSPO spo = element.getSpo().get();

        // element
        strb.append(element.getId() + "\t" + element.getContextLayer() + "\t" + spo.getSubject() + "\t" + spo.getPredicate() + "\t" + spo.getObject());

        // vContexts
        for (ExVContext context : element.getVContexts()) {
            String vContextRep = vContextRep(context, true);
            strb.append("\t" + vContextRep);
        }

        // nContexts
        for (ExNContext context : element.getNContexts()) {
            Optional<String> nContextRep = nContextRep(context, true);
            if (nContextRep.isPresent()) {
                strb.append("\t" + nContextRep.get());
            }
        }

        // element contexts
        for (ExElementRelation relation : element.getRelations()) {
            ExElement target = relation.getTargetElement(content);
            Optional<String> elemContextRep = elemContextRep(target, relation.getClassification(), true, true);
            if (elemContextRep.isPresent()) {
                strb.append("\t" + elemContextRep.get());
            }
        }

        strb.append("\n");
    }

    private static void addExpandedElementRepresentation(StringBuilder strb, int contextDepth, int maxContextDepth, ExContent content, ExElement element, Classification classification) {
        if (!element.getSpo().isPresent()) {
            return;
        }
        ExSPO spo = element.getSpo().get();
        String indent = "";
        for (int i = 0; i < contextDepth; i++) {
            indent += "\t";
        }

        // element
        if (contextDepth == 0) {
            strb.append(indent + element.getContextLayer() + "\t" + spo.getSubject() + "\t" + spo.getPredicate() + "\t" + spo.getObject() + "\n");
        } else {
            strb.append(indent + elemContextRep(element, classification, false, false).get() + "\n");
        }

        if (contextDepth < maxContextDepth) {
            // vContexts
            for (ExVContext context : element.getVContexts()) {
                String vContextRep = vContextRep(context, false);
                strb.append(indent + "\t" + vContextRep + "\n");
            }

            // nContexts
            for (ExNContext context : element.getNContexts()) {
                Optional<String> nContextRep = nContextRep(context, false);
                if (nContextRep.isPresent()) {
                    strb.append(indent + "\t" + nContextRep.get() + "\n");
                }
            }

            // element contexts
            for (ExElementRelation relation : element.getRelations()) {
                ExElement target = relation.getTargetElement(content);
                addExpandedElementRepresentation(strb, contextDepth + 1, maxContextDepth, content, target, relation.getClassification());
            }
        }

        if (contextDepth == 0) {
            strb.append("\n");
        }
    }
}
