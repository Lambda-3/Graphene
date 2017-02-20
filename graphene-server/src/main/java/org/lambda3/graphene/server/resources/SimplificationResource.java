/*
 * ==========================License-Start=============================
 * graphene-server : SimplificationResource
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

import org.lambda3.graphene.core.simplification.model.SimplificationContent;
import org.lambda3.graphene.server.beans.SimplificationRequestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/simplification")
public class SimplificationResource extends AbstractGrapheneResource {

	private final static Logger LOG = LoggerFactory.getLogger(SimplificationResource.class);

	@POST
	@Path("text")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response simplifyText(@Valid SimplificationRequestBean bean) {

		if (LOG.isDebugEnabled()) {
			if (LOG.isTraceEnabled()) {
				LOG.debug("New Simplification POST request: {}", bean.getText());
			}
			LOG.debug("New Simplification POST request: {}", bean.getText().substring(0, Math.min(bean.getText().length(), MAX_LOG_CHARS)));
		}

		SimplificationContent sc = graphene.doSimplification(bean.getText(), bean.isDoCoreference());

		return Response
				.status(Response.Status.OK)
				.entity(sc)
				.build();
	}
}
