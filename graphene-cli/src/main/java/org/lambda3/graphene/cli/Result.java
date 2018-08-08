package org.lambda3.graphene.cli;

/*-
 * ==========================License-Start=============================
 * Result.java - Graphene CLI - Lambda^3 - 2017
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


import org.lambda3.text.simplification.discourse.model.Content;

class Result {

	private final String name;
	private final Content content;

	Result(String name, Content content) {
		this.name = name;
		this.content = content;
	}

	String getName() {
		return name;
	}

	Content getContent() {
		return content;
	}
}
