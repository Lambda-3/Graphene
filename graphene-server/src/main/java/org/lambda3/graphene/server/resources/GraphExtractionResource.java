/*
 * ==========================License-Start=============================
 * graphene-server : GraphExtractionResource
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

import org.lambda3.graphene.core.Content;
import org.lambda3.graphene.core.simplified_graph_extraction.model.ExSimplificationContent;
import org.lambda3.graphene.core.simplified_graph_extraction.rdf_output.RDFOutput;
import org.lambda3.graphene.server.beans.GraphExtractionRDFRequestBean;
import org.lambda3.graphene.server.beans.GraphExtractionRequestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/graphExtraction")
public class GraphExtractionResource extends AbstractGrapheneResource {

	private final static Logger LOG = LoggerFactory.getLogger(GraphExtractionResource.class);

	@POST
	@Path("text")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response graphExtractionFromText(@Valid GraphExtractionRequestBean bean) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("New GraphExtractionRequest: {}", bean);
		}

		Content content;
		if (bean.isDoSimplification()) {
			content = graphene.doSimplificationAndGraphExtraction(bean.getText(), bean.isDoCoreference());
		} else {
			content = graphene.doGraphExtraction(bean.getText(), bean.isDoCoreference());
		}

		return Response
				.status(Response.Status.OK)
				.entity(content)
				.build();
	}

	@POST
	@Path("rdf")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response rdfOutputFromText(@Valid GraphExtractionRDFRequestBean bean) {

		ExSimplificationContent ec = graphene.doSimplificationAndGraphExtraction(bean.getText(), bean.isDoCoreference());
		RDFOutput rdfOutput = graphene.getRDFOutput(ec);

		return Response
				.status(Response.Status.OK)
				.entity(rdfOutput)
				.build();
	}

	@POST
	@Path("rdf")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response rdfOutputFromText2(@Valid GraphExtractionRDFRequestBean bean) {

		ExSimplificationContent ec = graphene.doSimplificationAndGraphExtraction(bean.getText(), bean.isDoCoreference());
		String rdfOutput = graphene.getRDFOutputStr(ec);

		return Response
				.status(Response.Status.OK)
				.entity(rdfOutput)
				.build();
	}
}
