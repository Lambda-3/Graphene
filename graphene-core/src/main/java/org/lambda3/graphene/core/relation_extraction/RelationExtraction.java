package org.lambda3.graphene.core.relation_extraction;

/*-
 * ==========================License-Start=============================
 * RelationExtraction.java - Graphene Core - Lambda^3 - 2017
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
import org.lambda3.graphene.core.relation_extraction.model.ExContent;
import org.lambda3.graphene.core.relation_extraction.runner.DiscourseExtractionRunner;
import org.lambda3.graphene.core.relation_extraction.runner.SPORunner;
import org.lambda3.graphene.core.relation_extraction.runner.SimplificationRunner;
import org.lambda3.text.simplification.discourse.utils.sentences.SentencesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RelationExtraction {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final DiscourseExtractionRunner discourseExtractionRunner;
	private final SimplificationRunner simplificationRunner;
	private final SPORunner spoRunner;

	private final boolean simplificationEnabled;

	public RelationExtraction(Config config) {
		this.discourseExtractionRunner = new DiscourseExtractionRunner(config.getConfig("discourse-simplification"));
		Config simplificationConfig = config.getConfig("sentence-simplification");
		this.simplificationEnabled = !simplificationConfig.hasPath("enabled") || simplificationConfig.getBoolean("enabled");
		this.simplificationRunner = new SimplificationRunner();
		this.spoRunner = new SPORunner();
		log.info("RelationExtraction initialized");
	}

	public ExContent doRelationExtraction(String text, boolean isolateSentences) {
        return doRelationExtraction(SentencesUtils.splitIntoSentences(text), isolateSentences);
    }

    @SuppressWarnings("WeakerAccess")
    public ExContent doRelationExtraction(List<String> sentences, boolean isolateSentences) {
        log.info("Running RelationExtraction on {} sentences", sentences.size());

        // Step 1) do discourse extraction
        ExContent content = this.discourseExtractionRunner.doDiscourseExtraction(sentences, isolateSentences);

        // Step 2) do simplification
		if (this.simplificationEnabled) {
			this.simplificationRunner.doSimplification(content);
		}

        // Step 3) do SPO
        this.spoRunner.doSPOExtraction(content);

        return content;
    }

}
