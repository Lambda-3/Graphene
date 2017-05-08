/*
 * ==========================License-Start=============================
 * graphene-core : RelationExtractionTest
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

import org.lambda3.graphene.core.relation_extraction.model.ExContent;
import org.lambda3.graphene.core.relation_extraction.representation.RepGenerator;
import org.lambda3.graphene.core.relation_extraction.representation.RepStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 *
 */
public class RelationExtractionTest {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void testSerializationAndDeserialization() throws IOException {
		RelationExtraction relationExtraction = new RelationExtraction();

		ExContent serializeContent = relationExtraction.doRelationExtraction("Peter went to Berlin and went to Paris.");

		// serialize
		String json = serializeContent.serializeToJSON();

		log.info(json);

		// deserialization
		ExContent deserializeContent = ExContent.deserializeFromJSON(json, ExContent.class);
	}

	@Test
	public void testRDFOutput() throws IOException {
		RelationExtraction relationExtraction = new RelationExtraction();

		ExContent content = relationExtraction.doRelationExtraction("Peter went to Berlin and went to Paris.");

		log.info(RepGenerator.getRDFRepresentation(content, RepStyle.DEFAULT));
		log.info(RepGenerator.getRDFRepresentation(content, RepStyle.FLAT));
		log.info(RepGenerator.getRDFRepresentation(content, RepStyle.EXPANDED));
		log.info(RepGenerator.getRDFRepresentation(content, RepStyle.N_TRIPLES));
	}
}
