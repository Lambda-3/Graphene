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


import org.lambda3.text.simplification.discourse.runner.discourse_tree.Relation;

public class LinkedContext {
	private String targetID;
	private Relation classification;

	// for deserialization
	public LinkedContext() {
	}

	public LinkedContext(String targetID, Relation classification) {
		this.targetID = targetID;
		this.classification = classification;
	}

	public String getTargetID() {
		return targetID;
	}

	public Extraction getTargetExtraction(RelationExtractionContent content) {
		return content.getExtraction(targetID);
	}

	public Relation getClassification() {
		return classification;
	}

	@Override
	public boolean equals(Object o) {
		return ((o instanceof LinkedContext)
			&& (((LinkedContext) o).targetID.equals(targetID))
			&& (((LinkedContext) o).classification.equals(classification)));
	}
}
