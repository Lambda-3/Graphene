/*
 * ==========================License-Start=============================
 * graphene-server : JerseyTestTest
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

package org.lambda3.graphene.server;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTestNg;
import org.testng.annotations.Test;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Random;

import static org.testng.Assert.assertEquals;

/**
 * Simple example test if the test setup works. This test does not test any Graphene
 * logic.
 * It uses TestNG, because JUnit (and especially JUnit5) are not compatible.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class JerseyTestTest extends JerseyTestNg.ContainerPerClassTest {
    private static final Random RAND = new Random();

	protected Application configure() {
		return new ResourceConfig(TestResource.class);
	}

	@Test
	public void testRandomIncrease() throws Exception {
		final Response response = target()
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get();
		final Number rnd = response.readEntity(Number.class);

		final Response incResponse = target()
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(rnd, MediaType.APPLICATION_JSON_TYPE));

		final Number increased = incResponse.readEntity(Number.class);

		assertEquals(rnd.getNumber() + 1, increased.getNumber());
	}

	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Singleton
	public static class TestResource {

		@GET
		public Number getRandom() {
			return new Number(RAND.nextInt(100000));
		}

		@POST
		public Number increase(Number bean) {
			return new Number(bean.getNumber() + 1);
		}
	}

	public static class Number {
		private int number;

		public Number() {
		}

		public Number(int number) {
			this.number = number;
		}

		public int getNumber() {
			return number;
		}
	}

}
