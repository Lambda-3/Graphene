package org.lambda3.graphene.server.resources;

/*-
 * ==========================License-Start=============================
 * CoreferenceResource.java - Graphene Server - Lambda^3 - 2017
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


import com.fasterxml.jackson.core.JsonProcessingException;
import org.lambda3.graphene.core.coreference.model.CoreferenceContent;
import org.lambda3.graphene.server.beans.AbstractRequestBean;
import org.lambda3.graphene.server.beans.CoreferenceRequestBean;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/coreference")
public class CoreferenceResource extends AbstractGrapheneResource {

	@POST
	@Path("text")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response coreferenceResolutionFromText(@Valid CoreferenceRequestBean bean) throws JsonProcessingException {

		log.debug("New Coreference POST request: '{}'{}", AbstractRequestBean.truncateText(bean.getText()));

		CoreferenceContent cc = graphene.doCoreference(bean.getText());

		return Response
				.status(Response.Status.OK)
				.entity(cc.serializeToJSON())
				.build();
	}


	@POST
	@Path("text")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response coreferenceResolutionFromTextAsText(@Valid CoreferenceRequestBean bean) {

		log.debug("New Coreference POST request: '{}'{}", AbstractRequestBean.truncateText(bean.getText()));

		CoreferenceContent cc = graphene.doCoreference(bean.getText());

		String rep = cc.getSubstitutedText();

		return Response
			.status(Response.Status.OK)
			.entity(rep)
			.build();
	}
}
