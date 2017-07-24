package org.lambda3.graphene.core.relation_extraction.model;

/*-
 * ==========================License-Start=============================
 * LinkedContext.java - Graphene Core - Lambda^3 - 2017
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


public class LinkedContext {
	private String targetExtractionID;
	private Classification classification;

	// for deserialization
	public LinkedContext() {
	}

	public LinkedContext(String targetExtractionID, Classification classification) {
		this.targetExtractionID = targetExtractionID;
		this.classification = classification;
	}

	public String getTargetExtractionID() {
		return targetExtractionID;
	}

	public Extraction getTargetElement(ExContent content) {
		return content.getExtraction(targetExtractionID);
	}

	public Classification getClassification() {
		return classification;
	}

	@Override
	public boolean equals(Object o) {
		return ((o instanceof LinkedContext)
			&& (((LinkedContext) o).targetExtractionID.equals(targetExtractionID))
			&& (((LinkedContext) o).classification.equals(classification)));
	}
}
