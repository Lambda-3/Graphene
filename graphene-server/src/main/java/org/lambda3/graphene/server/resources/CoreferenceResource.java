/*
 * ==========================License-Start=============================
 * graphene-server : CoreferenceResource
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

import org.lambda3.graphene.core.coreference.model.CoreferenceContent;
import org.lambda3.graphene.core.coreference.model.Link;
import org.lambda3.graphene.server.beans.CoreferenceRequestBean;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/coreference")
public class CoreferenceResource extends AbstractGrapheneResource {

	private static List<Link> convertBeanLinksToInternalLinks(List<CoreferenceRequestBean.Link> internalLinks) {
		return internalLinks
				.stream()
				.map(link -> new Link(link.getAnchorText(), link.getUri()))
				.collect(Collectors.toList());
	}

	@POST
	@Path("text")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postText(@Valid CoreferenceRequestBean bean) {

		if (log.isDebugEnabled()) {
			int len = bean.getText().length() > 50 ? 50 : bean.getText().length();
			String dots = bean.getText().length() > len ? "…" : "";
			log.debug("New Coreference POST request: '{}'{}", bean.getText().substring(0, len), dots);
		}

		final String text = bean.getText();
		final Optional<String> uri = Optional.ofNullable(bean.getUri());
		final Optional<List<CoreferenceRequestBean.Link>> links = Optional.ofNullable(bean.getLinks());


		CoreferenceContent cc;
		if (links.isPresent() && uri.isPresent()) {
			cc = graphene.doCoreference(text, uri.get(), convertBeanLinksToInternalLinks(links.get()));
		} else {
			cc = uri
					.map(s -> graphene.doCoreference(text, s))
					.orElseGet(() -> graphene.doCoreference(text));
		}

		return Response
				.status(Response.Status.OK)
				.entity(cc)
				.build();

	}
}
