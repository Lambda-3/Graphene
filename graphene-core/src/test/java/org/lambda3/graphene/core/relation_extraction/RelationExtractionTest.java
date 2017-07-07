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
import org.lambda3.graphene.core.coreference.Coreference;
import org.lambda3.graphene.core.relation_extraction.model.ExContent;
import org.lambda3.graphene.core.relation_extraction.representation.RepGenerator;
import org.lambda3.graphene.core.relation_extraction.representation.RepStyle;
import org.lambda3.graphene.core.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class RelationExtractionTest {
	private final static Logger LOG = LoggerFactory.getLogger(RelationExtractionTest.class);

	private static RelationExtraction relationExtraction;

	@BeforeClass
	public static void beforeAll() {
		Config config = ConfigFactory
			.load("reference.local")
			.withFallback(ConfigFactory.load("reference"));

		relationExtraction = new RelationExtraction(config.getConfig("graphene.relation-extraction"));
	}

	@Test
	public void testSerializationAndDeserialization() throws IOException {
		ExContent serializeContent = relationExtraction.doRelationExtraction("Peter went to Berlin and went to Paris.");

		// serialize
		String json = serializeContent.serializeToJSON();

		LOG.info(json);

		// deserialization
		ExContent deserializeContent = ExContent.deserializeFromJSON(json, ExContent.class);
	}

	@Test
	public void testRDFOutput() throws IOException {
		ExContent content = relationExtraction.doRelationExtraction("Peter went to Berlin and went to Paris.");

		LOG.info(RepGenerator.getRDFRepresentation(content, RepStyle.DEFAULT));
		LOG.info(RepGenerator.getRDFRepresentation(content, RepStyle.FLAT));
		LOG.info(RepGenerator.getRDFRepresentation(content, RepStyle.EXPANDED));
		LOG.info(RepGenerator.getRDFRepresentation(content, RepStyle.N_TRIPLES));
	}
}
