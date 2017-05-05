/*
 * ==========================License-Start=============================
 * graphene-server : GraphExtractionRequestBean
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

package org.lambda3.graphene.server.beans;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;

/**
 *
 */
public class RelationExtractionRequestBean extends AbstractRequestBean {

	@NotNull
	private String text;

	@DefaultValue("true")
	private boolean doCoreference;

    @DefaultValue("rdf")
    private String format;

	public String getText() {
		return text;
	}
	@SuppressWarnings({"unused"})
	public void setText(String text) {
		this.text = text;
	}

	public boolean isDoCoreference() {
		return doCoreference;
	}
	@SuppressWarnings({"unused"})
	public void setDoCoreference(boolean doCoreference) {
		this.doCoreference = doCoreference;
	}

	public String getFormat() {
		return format;
	}
	@SuppressWarnings({"unused"})
	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public String toString() {
		return "RelationExtractionRequestBean{" +
				"text='" + truncateText(text) + '\'' +
				", doCoreference=" + doCoreference +
				", format=" + format +
				'}';
	}
}
