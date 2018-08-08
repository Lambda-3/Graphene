package org.lambda3.graphene.server.resources;

/*-
 * ==========================License-Start=============================
 * AdminResource.java - Graphene Server - Lambda^3 - 2017
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


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Path("/_admin")
public class AdminResource extends AbstractGrapheneResource {
	private static List<String> EXAMPLE_QUERIES = Arrays.asList(
		"Although the Treasury will announce details of the November refunding on Monday, the funding will be delayed if Congress and President Bush fail to increase the Treasury's borrowing capacity.",
		"After I started my career as a professional photographer, I shot hundreds of people to make a living, using my DSLR camera.",
		"After graduating from Columbia University in 1983, Barack Obama worked as a community organizer in Chicago. After that, he became a civil rights attorney.",
		"On Monday, Asian stocks fell anew and the yen rose to session highs in the afternoon as worries about North Korea simmered, after a senior Pyongyang official said the U.S. is becoming \"more vicious and more aggressive\" under President Donald Trump."
	);

	@GET
	@Path("version")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVersionInfo() {

		CacheControl cc = new CacheControl();
		cc.setMaxAge(60 * 60 * 24); // seconds
		cc.setPrivate(true);

		return Response
                .status(Response.Status.OK)
                .cacheControl(cc)
                .entity(graphene.getVersionInfo())
                .build();
    }

	@GET
	@Path("examples")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExampleQueries() {

		CacheControl cc = new CacheControl();
		cc.setMaxAge(60 * 60 * 24); // seconds
		cc.setPrivate(true);

		return Response
			.status(Response.Status.OK)
			.cacheControl(cc)
			.entity(EXAMPLE_QUERIES)
			.build();
	}
}
