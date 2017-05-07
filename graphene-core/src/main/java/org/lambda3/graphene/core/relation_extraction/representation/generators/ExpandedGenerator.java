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

package org.lambda3.graphene.core.relation_extraction.representation.generators;

import org.lambda3.graphene.core.relation_extraction.model.*;
import org.lambda3.graphene.core.relation_extraction.representation.RepGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public class ExpandedGenerator extends RepGenerator {
    private int maxContextDepth;

    public ExpandedGenerator(int maxContextDepth) {
        this.maxContextDepth = maxContextDepth;
    }

    private static void addElementRec(List<String> res, int contextDepth, int maxContextDepth, ExContent content, ExElement element, Classification classification) {
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
            res.add(indent + element.getContextLayer() + "\t" + spo.getSubject() + "\t" + spo.getPredicate() + "\t" + spo.getObject());
        } else {
            res.add(indent + elemContextRep(element, classification, false, false).get());
        }

        if (contextDepth < maxContextDepth) {
            // vContexts
            for (ExVContext context : element.getVContexts()) {
                String vContextRep = vContextRep(context, false);
                res.add(indent + "\t" + vContextRep);
            }

            // nContexts
            for (ExNContext context : element.getNContexts()) {
                Optional<String> nContextRep = nContextRep(context, false);
                if (nContextRep.isPresent()) {
                    res.add(indent + "\t" + nContextRep.get());
                }
            }

            // element contexts
            for (ExElementRelation relation : element.getRelations()) {
                ExElement target = relation.getTargetElement(content);
                addElementRec(res, contextDepth + 1, maxContextDepth, content, target, relation.getClassification());
            }
        }
    }

    @Override
    public List<String> format(ExContent content) {
        List<String> res = new ArrayList<>();

        for (ExSentence exSentence : content.getSentences()) {

            // sentence
            res.add("# " + exSentence.getOriginalSentence());
            res.add("");

            for (ExElement element : exSentence.getElements()) {
                addElementRec(res, 0, maxContextDepth, content, element, null);
                res.add("");
            }
        }

        return res;
    }
}
