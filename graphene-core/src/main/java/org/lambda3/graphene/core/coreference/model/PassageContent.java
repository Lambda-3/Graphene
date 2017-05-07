/*
 * ==========================License-Start=============================
 * graphene-core : PassageContent
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.lambda3.graphene.core.Content;

/**
 *
 */
public class PassageContent extends Content {

	private String passage;
	private String type;
	private String term;
	private String id;

	public PassageContent(String passage, String type, String term, String id) {

		this.passage = passage;
		this.type = type;
		this.term = term;
		this.id = id;
	}

	@SuppressWarnings("WeakerAccess")
	public String getPassage() {
		return passage;
	}

	public void setPassage(String passage) {
		this.passage = passage;
	}

	@SuppressWarnings("WeakerAccess")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@SuppressWarnings("WeakerAccess")
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	@SuppressWarnings("WeakerAccess")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof PassageContent) {
			PassageContent otherContent = (PassageContent) other;

			return new EqualsBuilder()
					.append(getPassage(), otherContent.getPassage())
					.append(getType(), otherContent.getType())
					.append(getTerm(), otherContent.getTerm())
					.append(getId(), otherContent.getId())
					.isEquals();
		}
		return false;
	}

	@Override
	public String toString() {
		return "PassageContent{" +
				"id='" + id + '\'' +
				", passage='" + passage + '\'' +
				", type='" + type + '\'' +
				", term='" + term + '\'' +
				'}';
	}
}
