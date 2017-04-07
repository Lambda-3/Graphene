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
import org.lambda3.graphene.core.simplified_graph_extraction.model.ExContent;
import org.lambda3.graphene.core.simplified_graph_extraction.rdf_output.RDFGenerator;
import org.lambda3.graphene.core.simplified_graph_extraction.rdf_output.RDFStyle;
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
		SimplifiedGraphExtraction simplifiedGraphExtraction = new SimplifiedGraphExtraction();

		ExContent serializeContent = simplifiedGraphExtraction.doExtraction("Peter went to Berlin and went to Paris.");

		// serialize
//		String json = mapper.writeValueAsString(serializeContent);
		String json = serializeContent.serializeToJSON();

		log.info(json);

		// deserialization
//		ExContent deserializeContent = mapper.readValue(json, ExContent.class);
		ExContent deserializeContent = ExContent.deserializeFromJSON(json, ExContent.class);
	}

	@Test
	public void testRDFOutput() throws IOException {
        SimplifiedGraphExtraction simplifiedGraphExtraction = new SimplifiedGraphExtraction();

        ExContent content = simplifiedGraphExtraction.doExtraction("Peter went to Berlin and went to Paris.");

        log.info(RDFGenerator.getRDFRepresentation(content, RDFStyle.DEFAULT));
        log.info(RDFGenerator.getRDFRepresentation(content, RDFStyle.FLAT));
        log.info(RDFGenerator.getRDFRepresentation(content, RDFStyle.EXPANDED));
	}
}
