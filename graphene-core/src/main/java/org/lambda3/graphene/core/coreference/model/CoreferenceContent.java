package org.lambda3.graphene.core.coreference.model;

/*-
 * ==========================License-Start=============================
 * CoreferenceContent.java - Graphene Core - Lambda^3 - 2017
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


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.lambda3.text.simplification.discourse.model.Content;

//TODO move the this class to the DiscourseSimplification project.
public class CoreferenceContent extends Content {

	private String originalText;
	private String substitutedText;

	public CoreferenceContent() {
	}

	public CoreferenceContent(String originalText, String substitutedText) {
		this.originalText = originalText;
		this.substitutedText = substitutedText;
	}

	public String getOriginalText() {
		return originalText;
	}

	public void setOriginalText(String originalText) {
		this.originalText = originalText;
	}

	public String getSubstitutedText() {
		return substitutedText;
	}

	public void setSubstitutedText(String substitutedText) {
		this.substitutedText = substitutedText;
	}

	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof CoreferenceContent) {
			CoreferenceContent otherContent = (CoreferenceContent) other;

			return new EqualsBuilder()
				.append(getOriginalText(), otherContent.getOriginalText())
				.append(getSubstitutedText(), otherContent.getSubstitutedText())
				.isEquals();
		}
		return false;
	}

	@Override
	public String toString() {
		return "CoreferenceContent{" +
			"originalText='" + originalText + '\'' +
			", substitutedText='" + substitutedText + '\'' +
			'}';
	}
}
