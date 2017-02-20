/*
 * ==========================License-Start=============================
 * graphene-core : SimplificationTest
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

package org.lambda3.graphene.core.simplification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lambda3.graphene.core.simplification.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimplificationTest {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void testSimplifyOneSentence() {
		Simplification simplification = new Simplification();
		SimplificationContent sc = simplification.doSimplification("Peter went to Berlin and went to Paris. After that, he returned to Berlin.");
		Assert.assertEquals(2, sc.getSimplifiedSentences().size());

		SimplificationSentence sentence1 = sc.getSimplifiedSentences().get(0);
		Assert.assertEquals(2, sentence1.getCoreSentences().size());
		CoreSentence sent1Core1 = sentence1.getCoreSentences().get(0);
		CoreSentence sent1Core2 = sentence1.getCoreSentences().get(1);

		SimplificationSentence sentence2 = sc.getSimplifiedSentences().get(1);
		Assert.assertEquals(1, sentence2.getCoreSentences().size());
		CoreSentence sent2Core1 = sentence2.getCoreSentences().get(0);

		Assert.assertTrue(sc.getCoreRelationsMap().get(sent1Core1).stream().map(CoreSentenceRelation::getTarget).collect(Collectors.toList()).containsAll(Arrays.asList(sent1Core2, sent2Core1)));
		Assert.assertTrue(sc.getCoreRelationsMap().get(sent1Core2).stream().map(CoreSentenceRelation::getTarget).collect(Collectors.toList()).containsAll(Arrays.asList(sent1Core1, sent2Core1)));
		Assert.assertTrue(sc.getCoreRelationsMap().get(sent2Core1).stream().map(CoreSentenceRelation::getTarget).collect(Collectors.toList()).containsAll(Arrays.asList(sent1Core1, sent1Core2)));
	}

	@Test
	public void testSerializationAndSerialization() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Simplification simplification = new Simplification();
		SimplificationContent serializeContent = simplification.doSimplification("Peter went to Berlin and went to Paris.");

		// serialize
		String json = mapper.writeValueAsString(serializeContent);

		log.info(json);

		// deserialization
		SimplificationContent deserializeContent = mapper.readValue(json, SimplificationContent.class);

		// compare maps
		Map<String, List<IDCoreSentenceRelation>> serializedIDCoreRelationsMap = serializeContent.getIDCoreRelationsMap();
		Map<String, List<IDCoreSentenceRelation>> deserializedIDCoreRelationsMap = deserializeContent.getIDCoreRelationsMap();
		Assert.assertEquals(serializedIDCoreRelationsMap.size(), deserializedIDCoreRelationsMap.size());

		for (String id : serializedIDCoreRelationsMap.keySet()) {
			Assert.assertTrue(serializedIDCoreRelationsMap.get(id).containsAll(deserializedIDCoreRelationsMap.get(id)));
			Assert.assertTrue(deserializedIDCoreRelationsMap.get(id).containsAll(serializedIDCoreRelationsMap.get(id)));
		}
	}

}