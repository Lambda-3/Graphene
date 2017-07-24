package org.lambda3.graphene.core.relation_extraction.runner;

/*-
 * ==========================License-Start=============================
 * SPORunner.java - Graphene Core - Lambda^3 - 2017
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


import org.lambda3.graphene.core.relation_extraction.model.ExContent;
import org.lambda3.graphene.core.relation_extraction.model.Extraction;
import org.lambda3.graphene.core.relation_extraction.model.SPO;
import org.lambda3.text.simplification.discourse.utils.SPOSplitter;

import java.util.Optional;

public class SPORunner {

	public SPORunner() {
	}

	public void doSPOExtraction(ExContent content) {

        // process all Extractions
        for (Extraction extraction : content.getExtractions()) {
            Optional<SPOSplitter.Result> spo = SPOSplitter.split(extraction.getText());
            if (spo.isPresent()) {
                extraction.setSpo(new SPO(
                        spo.get().getSubject(),
                        spo.get().getPredicate(),
                        (spo.get().getObject().length() > 0)? spo.get().getObject() : null
                ));
            }
        }
    }
}
