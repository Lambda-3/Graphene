package org.lambda3.graphene.core.relation_extraction.representation.generators;

/*-
 * ==========================License-Start=============================
 * DefaultGenerator.java - Graphene Core - Lambda^3 - 2017
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultGenerator extends RepGenerator {

    @Override
    public List<String> format(ExContent content) {
        List<String> res = new ArrayList<>();

        for (ExSentence exSentence : content.getSentences()) {

            // sentence
            res.add("# " + exSentence.getOriginalSentence());
            res.add("");

            for (Extraction element : exSentence.getExtractions()) {
                if (!this.showText && !element.getSpo().isPresent()) {
                    continue;
                }

                // Extraction
				StringBuilder extractionLine = new StringBuilder();
				if (showText) {
					extractionLine
						.append(element.getId())
						.append("\t")
						.append(element.getType())
						.append("\t")
						.append(element.getContextLayer())
						.append("\t")
						.append(element.getText());
				} else {
					SPO spo = element.getSpo().get();
					extractionLine
						.append(element.getId())
						.append("\t")
						.append(element.getType())
						.append("\t")
						.append(element.getContextLayer())
						.append("\t")
						.append(spo.getSubject())
						.append("\t")
						.append(spo.getPredicate())
						.append("\t")
						.append(spo.getObject().orElse(""));
				}
				res.add(extractionLine.toString());

                // SimpleContexts
                for (SimpleContext context : element.getSimpleContexts()) {
                    String vContextRep = simpleContextRep(context, false);
                    res.add("\t" + vContextRep);
                }

                // LinkedContexts
                for (LinkedContext context : element.getLinkedContexts()) {
                    Extraction target = context.getTargetElement(content);
                    Optional<String> elemContextRep = linkedContextRep(target, context.getClassification(), true, false);
                    elemContextRep.ifPresent(s -> res.add("\t" + s));
                }

                res.add("");
            }
        }

        return res;
    }
}
