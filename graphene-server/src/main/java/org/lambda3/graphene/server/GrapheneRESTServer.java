package org.lambda3.graphene.server;

/*-
 * ==========================License-Start=============================
 * GrapheneRESTServer.java - Graphene Server - Lambda^3 - 2017
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
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.validation.ValidationFeature;
import org.lambda3.graphene.core.Graphene;
import org.lambda3.graphene.server.filter.CORSFilter;
import org.lambda3.graphene.server.resources.AdminResource;
import org.lambda3.graphene.server.resources.CoreferenceResource;
import org.lambda3.graphene.server.resources.GrapheneResourceFactory;
import org.lambda3.graphene.server.resources.RelationExtractionResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

class GrapheneRESTServer {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final Server server;

	GrapheneRESTServer() {

		Config config = ConfigFactory.load()
				.withFallback(ConfigFactory.load("reference-core"))
				.withFallback(ConfigFactory.load("application-server"));

		log.debug("initializing Graphene");
		Graphene graphene = new Graphene(config);
		log.debug("Graphene initialized");

		ResourceConfig rc = generateResourceConfig(config, graphene);

		String uri = "http://" + config.getString("graphene.server.host-name");
		uri += config.hasPath("graphene.server.port") ? ":" + config.getInt("graphene.server.port") : "";
		uri += "/";
		uri += config.hasPath("graphene.server.path") ? config.getString("graphene.server.path") : "";

		log.info("Server will run at: '{}'", uri);

		server = JettyHttpContainerFactory.createServer(
				URI.create(uri),
				rc,
				false);

		log.info("Server successfully initialized, waiting for start.");
	}

	static ResourceConfig generateResourceConfig(Config config, Graphene graphene) {
		ResourceConfig rc = new ResourceConfig();

		// settings
		rc.property(ServerProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
		rc.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true); // TODO: remove in production

		// basic features
		rc.register(CORSFilter.class);
		rc.register(JacksonFeature.class);
		rc.register(ValidationFeature.class);

		// custom resources
		GrapheneResourceFactory factory = new GrapheneResourceFactory(config, graphene);

		rc.register(factory.createResource(AdminResource.class));
		rc.register(factory.createResource(CoreferenceResource.class));
		rc.register(factory.createResource(RelationExtractionResource.class));

		return rc;
	}

	void start() {
		log.debug("Trying to start GrapheneRESTServer");
		try {
			server.start();
			log.info("GrapheneRESTServer should be started");
		} catch (Exception e) {
			log.error("During starting the server the following exception was thrown: ", e);
		}
	}

	void stop() {
		if (server != null) {
			log.info("Trying to shutdown the server...");
			try {
				server.stop();
				log.info("Server was shut down.");
			} catch (Exception e) {
				log.error("There was an error during shutting down the server: {}", e.getMessage());
			}
		} else {
			log.error("Server should be stopped, but wasn't initialized yet.");
		}
	}
}
