package org.lambda3.graphene.server;

/*-
 * ==========================License-Start=============================
 * GrapheneRESTServerTest.java - Graphene Server - Lambda^3 - 2017
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
import org.glassfish.jersey.test.JerseyTestNg;
import org.lambda3.graphene.core.Graphene;
import org.lambda3.graphene.core.coreference.model.CoreferenceContent;
import org.lambda3.graphene.server.beans.CoreferenceRequestBean;
import org.testng.annotations.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class GrapheneRESTServerTest extends JerseyTestNg.ContainerPerClassTest {

	@Override
	protected Application configure() {

		Config config = ConfigFactory
				.load("application-server.local")
				.resolveWith(ConfigFactory.load())
				.withFallback(ConfigFactory.load("reference-core"));
		Graphene graphene = new Graphene(config);

		return GrapheneRESTServer.generateResourceConfig(config, graphene);
	}

	@Test
	public void testCoreferenceRequest() {
		String text = "Angela Merkel is the current chancellor of Germany. She will be reelected later this year.";

		CoreferenceContent expected = new CoreferenceContent(
				"Angela Merkel is the current chancellor of Germany. She will be reelected later this year.",
				"Angela Merkel is the current chancellor of Germany.\nAngela Merkel will be reelected later this year."
		);

		CoreferenceRequestBean requestBean = new CoreferenceRequestBean();
		requestBean.setText(text);
		final Response response = target("/coreference")
				.path("text")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(requestBean, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(response.getStatus(), 200);

		assertTrue(response.hasEntity());

		CoreferenceContent actual = response.readEntity(CoreferenceContent.class);

		assertEquals(actual, expected);
	}

}
