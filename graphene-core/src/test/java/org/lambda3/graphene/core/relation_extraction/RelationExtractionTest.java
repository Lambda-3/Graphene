package org.lambda3.graphene.core.relation_extraction;

/*-
 * ==========================License-Start=============================
 * RelationExtractionTest.java - Graphene Core - Lambda^3 - 2017
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
import org.lambda3.graphene.core.relation_extraction.model.ExContent;
import org.lambda3.text.simplification.discourse.model.OutSentence;
import org.lambda3.text.simplification.discourse.model.SimplificationContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class RelationExtractionTest {
	private final static Logger LOG = LoggerFactory.getLogger(RelationExtractionTest.class);

	private static RelationExtractionRunner relationExtractionRunner;

	@BeforeClass
	public static void beforeAll() {
		Config config = ConfigFactory
			.load("reference.local")
			.withFallback(ConfigFactory.load("reference"));

		relationExtractionRunner = new RelationExtractionRunner(config.getConfig("graphene.relation-extraction"));
	}

	@Test
	public void testSerializationAndDeserialization() throws IOException {
		SimplificationContent sc = new SimplificationContent();
		sc.addSentence(new OutSentence(0, "Peter went to Berlin and went to Paris."));

		ExContent serializeContent = relationExtractionRunner.doRelationExtraction(sc);

		// serialize
		String json = serializeContent.serializeToJSON();

		LOG.info(json);

		// deserialization
		ExContent deserializeContent = ExContent.deserializeFromJSON(json, ExContent.class);
	}

	@Test
	public void testOutput() throws IOException {
		SimplificationContent sc = new SimplificationContent();
		sc.addSentence(new OutSentence(0, "Peter went to Berlin and went to Paris."));

		ExContent content = relationExtractionRunner.doRelationExtraction(sc);

		LOG.info(content.defaultFormat(false));
		LOG.info(content.defaultFormat(true));
		LOG.info(content.flatFormat(false));
		LOG.info(content.flatFormat(true));
		LOG.info(content.rdfFormat());
	}
}
