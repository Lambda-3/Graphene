/*
 * ==========================License-Start=============================
 * graphene-server : GrapheneServerException
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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 */
public class GrapheneServerException extends WebApplicationException {

	/**
	 * Creates a new exception with the given message and the given Response status.
	 *
	 * @param status:  Response Status (should be 300-5xx
	 * @param message: Message that is shown to the user.
	 */
	public GrapheneServerException(Response.Status status, String message) {
		super(Response
				.status(status)
				.entity(new ExceptionMessage(message))
				.type(MediaType.APPLICATION_JSON)
				.build()
		);
	}

	/**
	 * Creates a new exception with the given message. The default status is 500.
	 *
	 * @param message: Message that is shown to the user.
	 */
	public GrapheneServerException(String message) {
		this(Response.Status.INTERNAL_SERVER_ERROR, message);
	}

	public static class ExceptionMessage {
		private String message;

		public ExceptionMessage() {
		}

		ExceptionMessage(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

}
