package org.lambda3.graphene.core.coreference;

/*-
 * ==========================License-Start=============================
 * PyCobaltCorefTest.java - Graphene Core - Lambda^3 - 2017
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
import org.lambda3.graphene.core.coreference.impl.pycobalt.PyCobaltCoref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PyCobaltCorefTest {
	private static final Logger LOG = LoggerFactory.getLogger(PyCobaltCorefTest.class);

	private CoreferenceResolver coreference;

	@BeforeClass
	public void beforeAll() {
		Config config = ConfigFactory
                .load("reference.local")
                .withFallback(ConfigFactory.load("reference"));

		coreference = new PyCobaltCoref(config.getConfig("graphene.coreference.settings"));
	}


	@Test
	public void testSubstituteCoreferencesInText() {
		
		if(coreference.isActivated()) {
			String sentence = "Bernhard is working in two projects. He works for MARIO and PACE.";

			CoreferenceContent expected = new CoreferenceContent(sentence, "Bernhard is working in two projects.\nBernhard works for MARIO and PACE.");

			CoreferenceContent result = coreference.doCoreferenceResolution(sentence);

			Assert.assertEquals(expected, result);
		} else {
			LOG.info("PyCobaltCoref is not activated! PyCobaltCorefTest skipped.");
		}

	}

}
