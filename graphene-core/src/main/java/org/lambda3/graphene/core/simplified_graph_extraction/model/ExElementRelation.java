/*
 * ==========================License-Start=============================
 * graphene-core : IDCoreSentenceRelation
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

package org.lambda3.graphene.core.simplified_graph_extraction.model;


/**
 *
 */
public class ExElementRelation {
	private String targetElementID;
	private Classification classification;

	// for deserialization
	public ExElementRelation() {
	}

	public ExElementRelation(String targetElementID, Classification classification) {
		this.targetElementID = targetElementID;
		this.classification = classification;
	}

	public String getTargetElementID() {
		return targetElementID;
	}

	public ExElement getTargetElement(ExContent content) {
        return content.getElement(targetElementID);
    }

	public Classification getClassification() {
		return classification;
	}

    @Override
	public boolean equals(Object o) {
		return ((o instanceof ExElementRelation)
				&& (((ExElementRelation) o).targetElementID.equals(targetElementID))
				&& (((ExElementRelation) o).classification.equals(classification)));
    }
}
