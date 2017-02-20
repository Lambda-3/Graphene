/*
 * ==========================License-Start=============================
 * graphene-core : SimplifiedGraphExtractionTest
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

package org.lambda3.graphene.core.simplified_graph_extraction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lambda3.graphene.core.simplification.Simplification;
import org.lambda3.graphene.core.simplification.model.IDCoreSentenceRelation;
import org.lambda3.graphene.core.simplification.model.SimplificationContent;
import org.lambda3.graphene.core.simplified_graph_extraction.model.ExSimplificationContent;
import org.lambda3.graphene.core.simplified_graph_extraction.rdf_output.RDFGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class SimplifiedGraphExtractionTest {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void testSerializationAndSeserialization() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Simplification simplification = new Simplification();
		SimplifiedGraphExtraction simplifiedGraphExtraction = new SimplifiedGraphExtraction();

		SimplificationContent simplificationContent = simplification.doSimplification("Peter went to Berlin and went to Paris.");
		ExSimplificationContent serializeContent = simplifiedGraphExtraction.extract(simplificationContent);

		// serialize
		String json = mapper.writeValueAsString(serializeContent);

		log.info(json);

		// deserialization
		ExSimplificationContent deserializeContent = mapper.readValue(json, ExSimplificationContent.class);

		// compare hashmaps
		Map<String, List<IDCoreSentenceRelation>> serializedIDCoreRelationsMap = serializeContent.getIDCoreRelationsMap();
		Map<String, List<IDCoreSentenceRelation>> deserializedIDCoreRelationsMap = deserializeContent.getIDCoreRelationsMap();
		Assert.assertEquals(serializedIDCoreRelationsMap.size(), deserializedIDCoreRelationsMap.size());

		for (String id : serializedIDCoreRelationsMap.keySet()) {
			Assert.assertTrue(serializedIDCoreRelationsMap.get(id).containsAll(deserializedIDCoreRelationsMap.get(id)));
			Assert.assertTrue(deserializedIDCoreRelationsMap.get(id).containsAll(serializedIDCoreRelationsMap.get(id)));
		}
	}

	@Test
	public void testRDFOutput() throws IOException {
		Simplification simplification = new Simplification();
		SimplifiedGraphExtraction simplifiedGraphExtraction = new SimplifiedGraphExtraction();

		SimplificationContent simplificationContent = simplification.doSimplification("Peter went to Berlin and went to Paris.");
		ExSimplificationContent esc = simplifiedGraphExtraction.extract(simplificationContent);

		RDFGenerator.generateOutput(esc);
	}
}
