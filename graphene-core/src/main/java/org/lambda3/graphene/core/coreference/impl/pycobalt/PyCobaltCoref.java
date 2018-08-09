package org.lambda3.graphene.core.coreference.impl.pycobalt;

/*-
 * ==========================License-Start=============================
 * PyCobaltCoref.java - Graphene Core - Lambda^3 - 2017
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
import org.glassfish.jersey.jackson.JacksonFeature;
import org.lambda3.graphene.core.coreference.CoreferenceResolver;
import org.lambda3.graphene.core.coreference.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PyCobaltCoref extends CoreferenceResolver {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final WebTarget textTarget;


	public PyCobaltCoref(Config config) {
		super(config);

		log.info("Will use remote coreference resource at: '{}'", config.getString("pycobalt.url"));

		final Client webClient = ClientBuilder.newClient();
		webClient.register(JacksonFeature.class);

		this.textTarget = webClient
                .target(config.getString("pycobalt.url"))
                .path(config.getString("pycobalt.text-path"));

		log.info("Coreference initialized");
	}

	private InternalCoreferenceResponse sendRequest(WebTarget target, InternalCoreferenceRequest request) {

		log.debug("Sending coreference request to {}", target.getUri().toString());

		final Response response = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

		if (response.hasEntity()) {
			if (response.getStatusInfo().getStatusCode() == Response.Status.OK.getStatusCode()) {

				return response.readEntity(InternalCoreferenceResponse.class);

			} else {
				log.error(
                        "A request was sent, but the response code was '{}: {}'",
                        response.getStatusInfo().getStatusCode(),
                        response.getStatusInfo().getReasonPhrase());
            }
		} else {
			log.error("The response has no entity.");
		}

		throw new RuntimeException("The response contained no content.");
	}


	@Override
	public CoreferenceContent doCoreferenceResolution(String text) {
		InternalCoreferenceResponse response = sendRequest(textTarget, new InternalCoreferenceRequest(text));

		return new CoreferenceContent(text, response.getText());
	}
}
