package org.lambda3.graphene.core;

/*-
 * ==========================License-Start=============================
 * Graphene.java - Graphene Core - Lambda^3 - 2017
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
import com.typesafe.config.ConfigFactory;
import org.lambda3.graphene.core.coreference.Coreference;
import org.lambda3.graphene.core.coreference.model.CoreferenceContent;
import org.lambda3.graphene.core.coreference.model.Link;
import org.lambda3.graphene.core.relation_extraction.RelationExtractionRunner;
import org.lambda3.graphene.core.relation_extraction.model.ExContent;
import org.lambda3.graphene.core.utils.ConfigUtils;
import org.lambda3.text.simplification.discourse.model.SimplificationContent;
import org.lambda3.text.simplification.discourse.processing.DiscourseSimplifier;
import org.lambda3.text.simplification.discourse.processing.ProcessingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Graphene {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Config config;

	private final Coreference coreference;
	private final DiscourseSimplifier discourseSimplificationRunner;
	private final RelationExtractionRunner relationExtractionRunner;

	public Graphene() {
		this(ConfigFactory.load());
	}

	public Graphene(Config config) {
		this.config = config
			.withFallback(ConfigFactory.load("build"))
			.getConfig("graphene");

		this.coreference = new Coreference(this.config.getConfig("coreference"));
		this.discourseSimplificationRunner = new DiscourseSimplifier(this.config.getConfig("discourse-simplification"));
		this.relationExtractionRunner = new RelationExtractionRunner(this.config.getConfig("relation-extraction"));

		log.info("Graphene initialized");
		log.info("\n{}", ConfigUtils.prettyPrint(this.config));
	}

	public CoreferenceContent doCoreference(String text) {
		log.debug("doCoreference for text");
		final CoreferenceContent content = coreference.substituteCoreferences(text);
		log.debug("Coreference for text finished");
		return content;
	}

	public CoreferenceContent doCoreference(String text, String uri) {
		log.debug("doCoreference for text with uri");
		final CoreferenceContent content = coreference.substituteCoreferences(text, uri);
		log.debug("Coreference for text with uri finished");
		return content;
	}

	public CoreferenceContent doCoreference(String text, String uri, List<Link> links) {
		log.debug("doCoreference for text with uri and links");
		final CoreferenceContent content = coreference.substituteCoreferences(text, uri, links);
		log.debug("Coreference for text with uri and links finished");
		return content;
	}

	public SimplificationContent doDiscourseSimplification(String text, boolean isolateSentences) {
		log.debug("doDiscourseSimplification for text");
		final SimplificationContent simplificationContent = discourseSimplificationRunner.doDiscourseSimplification(text, (isolateSentences)? ProcessingType.SEPARATE : ProcessingType.WHOLE);
		log.debug("Discourse Simplification for text finished");
		return simplificationContent;
	}

	public ExContent doRelationExtraction(String text, boolean doCoreference, boolean isolateSentences) {
        if (doCoreference) {
            final CoreferenceContent cc = doCoreference(text);
            text = cc.getSubstitutedText();
        }

        final SimplificationContent simplificationContent = doDiscourseSimplification(text, isolateSentences);

        log.debug("doRelationExtraction for text");
        final ExContent ec = relationExtractionRunner.doRelationExtraction(simplificationContent);
		ec.setCoreferenced(doCoreference);
		log.debug("Relation Extraction for text finished");
		return ec;
	}

	public ExContent doRelationExtraction(SimplificationContent simplificationContent, boolean coreferenced) {
		log.debug("doRelationExtraction for simplificationContent");
		final ExContent ec = relationExtractionRunner.doRelationExtraction(simplificationContent);
		ec.setCoreferenced(coreferenced);
		log.debug("Relation Extraction for simplificationContent finished");
		return ec;
	}

	public VersionInfo getVersionInfo() {
		if (log.isDebugEnabled()) {
			log.debug("getVersionInfo");
		}
		return new VersionInfo(
                config.getString("version.name"),
                config.getString("version.version"),
                config.getString("version.build-number")
        );
	}

}
