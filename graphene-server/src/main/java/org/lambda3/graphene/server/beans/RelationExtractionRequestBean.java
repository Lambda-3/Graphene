package org.lambda3.graphene.server.beans;

/*-
 * ==========================License-Start=============================
 * RelationExtractionRequestBean.java - Graphene Server - Lambda^3 - 2017
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


import org.lambda3.graphene.core.relation_extraction.formatter.FormatterFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;

public class RelationExtractionRequestBean extends AbstractRequestBean {

	@NotNull
	private String text;

	@DefaultValue("true")
	private boolean doCoreference;

	@DefaultValue("false")
	private boolean doComplexCategories;

	@DefaultValue("false")
	private boolean isolateSentences;

	@DefaultValue("DEFAULT")
	private FormatterFactory.OutputFormat format;

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

	public boolean isDoComplexCategories() {
		return doComplexCategories;
	}

	@SuppressWarnings({"unused"})
	public void setDoComplexCategories(boolean doComplexCategories) {
		this.doComplexCategories = doComplexCategories;
	}

	public boolean isIsolateSentences() {
		return isolateSentences;
	}

	@SuppressWarnings({"unused"})
	public void setIsolateSentences(boolean isolateSentences) {
		this.isolateSentences = isolateSentences;
	}

	public FormatterFactory.OutputFormat getFormat() {
		return format;
	}

	@SuppressWarnings({"unused"})
	public void setFormat(FormatterFactory.OutputFormat format) {
		this.format = format;
	}

	@Override
	public String toString() {
		return "RelationExtractionRequestBean{" +
			"text='" + truncateText(text) + '\'' +
			", doCoreference=" + doCoreference +
			", isolateSentences=" + isolateSentences +
			", format=" + format +
			'}';
	}
}
