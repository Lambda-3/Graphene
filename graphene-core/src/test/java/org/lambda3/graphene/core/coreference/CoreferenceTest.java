package org.lambda3.graphene.core.coreference;

/*-
 * ==========================License-Start=============================
 * CoreferenceTest.java - Graphene Core - Lambda^3 - 2017
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
import org.lambda3.graphene.core.coreference.model.CoreferenceContent;
import org.lambda3.graphene.core.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CoreferenceTest {
	private static final Logger LOG = LoggerFactory.getLogger(CoreferenceTest.class);

	private static Coreference coreference;

	@BeforeClass
	public static void beforeAll() {
		Config config = ConfigFactory
                .load("reference.local")
                .withFallback(ConfigFactory.load("reference"));

		coreference = new Coreference(config.getConfig("graphene.coreference"));
	}


	@Test
	public void testSubstituteCoreferencesInText() {
		String sentence = "Bernhard is working in two projects. He works for MARIO and PACE.";

		CoreferenceContent expected = new CoreferenceContent(sentence, "Bernhard is working in two projects.\nBernhard works for MARIO and PACE.");

		CoreferenceContent result = coreference.substituteCoreferences(sentence);

		Assert.assertEquals(expected, result);

	}

	@Test
	public void testSubstituteCoreferencesInTextWithURI() {

		String sentence = "Barack Obama was born in 1961. He is now 55 years old.";
		String uri = "http://dbpedia.org/Barack_Obama";

		CoreferenceContent expected = new CoreferenceContent(sentence, "Barack Obama was born in 1961.\nBarack Obama is now 55 years old.");

		// TODO: Will fail with current PyCobalt version (1.1.x-1.2.x) because sentences are split and returned as list.
		CoreferenceContent actual = coreference.substituteCoreferences(sentence, uri);

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testSubstituteCoreferencesInTextWithURIAndLinks() {
		Assert.fail("not implemented");
	}

	@Test
	public void testSubstituteIntoPassages() {
		Assert.fail("not implemented");
	}

}
