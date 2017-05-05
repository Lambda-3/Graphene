/*
 * ==========================License-Start=============================
 * graphene-core : SimplifiedGraphExtraction
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

package org.lambda3.graphene.core.relation_extraction;

import org.lambda3.graphene.core.relation_extraction.model.*;
import org.lambda3.graphene.core.relation_extraction.runner.DiscourseExtractionRunner;
import org.lambda3.graphene.core.relation_extraction.runner.SPORunner;
import org.lambda3.graphene.core.relation_extraction.runner.SimplificationRunner;
import org.lambda3.text.simplification.discourse.utils.sentences.SentencesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 *
 */
public class RelationExtraction {
	private final Logger log = LoggerFactory.getLogger(getClass());

	public RelationExtraction() {
		log.info("RelationExtraction initialized");
	}

	public ExContent doRelationExtraction(String text) {
        return doRelationExtraction(SentencesUtils.splitIntoSentences(text));
    }

    @SuppressWarnings("WeakerAccess")
    public ExContent doRelationExtraction(List<String> sentences) {
        log.info("Running RelationExtraction on {} sentences", sentences.size());

        // Step 1) do discourse extraction
        ExContent content = DiscourseExtractionRunner.doDiscourseExtraction(sentences);

        // Step 2) do simplification
        SimplificationRunner.doSimplification(content);

        // Step 3) do SPO
        SPORunner.doSPOExtraction(content);

        return content;
    }

}
