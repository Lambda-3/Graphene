/*
 * ==========================License-Start=============================
 * graphene-server : AbstractGrapheneResource
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

import javax.inject.Singleton;

@SuppressWarnings("WeakerAccess")
@Singleton
public abstract class AbstractGrapheneResource {

	final static int MAX_LOG_CHARS = 30;

	@SuppressWarnings("WeakerAccess")
	protected Graphene graphene;
	@SuppressWarnings("WeakerAccess")
	protected Config config = null;

	@SuppressWarnings("WeakerAccess")
	protected final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("WeakerAccess")
	protected void setGraphene(Graphene graphene) {
		this.graphene = graphene;
	}

	@SuppressWarnings("WeakerAccess")
	protected void setConfig(Config config) {
		if (this.config != null) {
			throw new IllegalAccessError("The resource already has a config.");
		}
		this.config = config;
	}


}
