package org.lambda3.graphene.core.relation_extraction.model;

/*-
 * ==========================License-Start=============================
 * ExElementRelation.java - Graphene Core - Lambda^3 - 2017
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
