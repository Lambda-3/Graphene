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

package org.lambda3.graphene.core.relation_extraction.runner;

import org.lambda3.graphene.core.relation_extraction.model.ExContent;
import org.lambda3.graphene.core.relation_extraction.model.ExElement;
import org.lambda3.graphene.core.relation_extraction.model.ExNContext;
import org.lambda3.graphene.core.relation_extraction.model.ExSPO;
import org.lambda3.text.simplification.discourse.utils.SPOSplitter;

import java.util.Optional;

/**
 *
 */
public class SPORunner {

    public static void doSPOExtraction(ExContent content) {

        // process all elements
        for (ExElement element : content.getElements()) {
            Optional<SPOSplitter.Result> elemspo = SPOSplitter.split(element.getText());
            if (elemspo.isPresent()) {
                element.setSpo(new ExSPO(
                        elemspo.get().getSubject(),
                        elemspo.get().getPredicate(),
                        elemspo.get().getObject()
                ));
            }

            // process NContexts
            for (ExNContext context : element.getNContexts()) {
                Optional<SPOSplitter.Result> contspo = SPOSplitter.split(context.getText());
                if (contspo.isPresent()) {
                    context.setSpo(new ExSPO(
                            contspo.get().getSubject(),
                            contspo.get().getPredicate(),
                            contspo.get().getObject()
                    ));
                }
            }
        }
    }
}
