package org.lambda3.graphene.core;

/*-
 * ==========================License-Start=============================
 * Content.java - Graphene Core - Lambda^3 - 2017
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


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public abstract class Content {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	static {
		MAPPER.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
		MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
	}

	public static <T extends Content> T deserializeFromJSON(String json, Class<T> clazz) throws IOException {
		return MAPPER.readValue(json, clazz);
	}

	public static <T extends Content> T deserializeFromJSON(File file, Class<T> clazz) throws IOException {
		return MAPPER.readValue(file, clazz);
	}

	@Override
	public abstract boolean equals(Object other);

	@Override
	public abstract String toString();

	public String prettyPrint() throws JsonProcessingException {
		return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}

	public String serializeToJSON() throws JsonProcessingException {
		return MAPPER.writeValueAsString(this);
	}

	public void serializeToJSON(File file) throws IOException {
		MAPPER.writeValue(file, this);
	}
}
