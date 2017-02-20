/*
 * ==========================License-Start=============================
 * graphene-core : CoreferenceContent
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

package org.lambda3.graphene.core.coreference.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.lambda3.graphene.core.Content;

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
