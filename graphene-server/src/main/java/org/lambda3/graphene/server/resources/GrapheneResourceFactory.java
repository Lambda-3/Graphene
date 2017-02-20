/*
 * ==========================License-Start=============================
 * graphene-server : GrapheneResourceFactory
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

package org.lambda3.graphene.server.resources;

import com.typesafe.config.Config;
import org.lambda3.graphene.core.Graphene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory that creates instances of #AbstractGrapheneResource
 */
public class GrapheneResourceFactory {

	private final static Logger LOG = LoggerFactory.getLogger(GrapheneResourceFactory.class);

	private final Config config;
	private final Graphene graphene;

	/**
	 * Creates a new factory that is able to instantiate GrapheneResoures.
	 * It needs a Config and the instance of Graphene to use.
	 *
	 * @param config   Configuration
	 * @param graphene Graphene instance that is used in the resource
	 */
	public GrapheneResourceFactory(Config config, Graphene graphene) {
		this.config = config;
		this.graphene = graphene;
	}

	/**
	 * Returns a new instance of the given GrapheneResource.
	 *
	 * @param resource class of the new GrapheneResource
	 * @param <T>      The resource must extend #AbstractGrapheneResource
	 * @return a new new instance with the Config and Graphene loaded in the constructor.
	 * @throws IllegalArgumentException Thrown if the resource can't be instantiated
	 */
	public <T extends AbstractGrapheneResource> T createResource(Class<T> resource) throws IllegalArgumentException {
		try {
			T res = resource.newInstance();
			res.setConfig(config);
			res.setGraphene(graphene);
			return res;
		} catch (InstantiationException e) {
			LOG.error("The resource could not be instantiated.", e);
		} catch (IllegalAccessException e) {
			LOG.error("The resource can't be accessed.", e);
		}

		LOG.error("Cannot load class");
		throw new IllegalArgumentException("This is not a valid resource");
	}

}
