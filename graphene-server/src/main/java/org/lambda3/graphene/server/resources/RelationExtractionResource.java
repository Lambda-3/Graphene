package org.lambda3.graphene.server.resources;

/*-
 * ==========================License-Start=============================
 * RelationExtractionResource.java - Graphene Server - Lambda^3 - 2017
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


import org.lambda3.graphene.core.relation_extraction.model.ExContent;
import org.lambda3.graphene.core.relation_extraction.representation.RepStyle;
import org.lambda3.graphene.server.beans.RelationExtractionRequestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/relationExtraction")
public class RelationExtractionResource extends AbstractGrapheneResource {

	private final static Logger LOG = LoggerFactory.getLogger(RelationExtractionResource.class);

	@POST
	@Path("text")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response relationExtractionFromText(@Valid RelationExtractionRequestBean bean) {

		LOG.debug("New RelationExtractionRequest: {}", bean);

		ExContent content = graphene.doRelationExtraction(bean.getText(), bean.isDoCoreference());

		return Response
				.status(Response.Status.OK)
				.entity(content)
				.build();
	}

    @POST
    @Path("text")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response relationExtractionFromTextAsText(@Valid RelationExtractionRequestBean bean) {

		LOG.debug("New RelationExtractionRequest: {}", bean);

        ExContent content = graphene.doRelationExtraction(bean.getText(), bean.isDoCoreference());

		RepStyle style;

		switch (bean.getFormat()) {
			case RDF:
				style = RepStyle.N_TRIPLES;
				break;
			case DEFAULT:
				style = RepStyle.DEFAULT;
				break;
			case FLAT:
				style = RepStyle.FLAT;
				break;
			case EXPANDED:
				style = RepStyle.EXPANDED;
				break;
			default:
				style = RepStyle.N_TRIPLES;
				break;
		}

        String rep = graphene.getRepresentation(content, style);

        return Response
                .status(Response.Status.OK)
                .entity(rep)
                .build();
    }
}
