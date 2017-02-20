/*
 * ==========================License-Start=============================
 * graphene-core : ExtractionContent
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

package org.lambda3.graphene.core.graph_extraction.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.lambda3.graphene.core.Content;

import java.util.List;

/**
 *
 */
public class ExtractionContent extends Content {
	private final List<ExtractionSentence> sentences;
	private boolean coreferenced;

	public ExtractionContent(List<ExtractionSentence> sentences) {
		this.coreferenced = false;
		this.sentences = sentences;
	}

	@SuppressWarnings("WeakerAccess")
	public boolean isCoreferenced() {
		return coreferenced;
	}

	public void setCoreferenced(boolean coreferenced) {
		this.coreferenced = coreferenced;
	}

	@SuppressWarnings("WeakerAccess")
	public List<ExtractionSentence> getSentences() {
		return sentences;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof ExtractionContent)) return false;

		ExtractionContent that = (ExtractionContent) o;

		return new EqualsBuilder()
				.append(isCoreferenced(), that.isCoreferenced())
				.append(getSentences(), that.getSentences())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(getSentences())
				.append(isCoreferenced())
				.toHashCode();
	}

	@Override
	public String toString() {
		return "ExtractionContent{" +
				"sentences=" + sentences +
				", coreferenced=" + coreferenced +
				'}';
	}
}
