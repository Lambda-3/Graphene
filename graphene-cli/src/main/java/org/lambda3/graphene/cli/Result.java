/*
 * ==========================License-Start=============================
 * graphene-cli : Result
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

package org.lambda3.graphene.cli;

import org.lambda3.graphene.core.Content;

/**
 *
 */
public class Result {

	private final String name;
	private final Content content;

	public Result(String name, Content content) {
		this.name = name;
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public Content getContent() {
		return content;
	}
}
