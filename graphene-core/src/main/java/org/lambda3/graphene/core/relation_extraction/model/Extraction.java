package org.lambda3.graphene.core.relation_extraction.model;

/*-
 * ==========================License-Start=============================
 * Extraction.java - Graphene Core - Lambda^3 - 2017
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

public class Extraction {
	public final ExtractionType type;
	public final double confidence; //optional
	public final Triple triple;

	private boolean asLinkedContext;
	private Relation classification;


	protected Extraction() {
		// for deserialization
		this.type = null;
		this.confidence = -1;
		this.triple = null;
	}

	public Extraction(ExtractionType type, double confidence, Triple triple) {
		this.type = type;
		this.confidence = confidence;
		this.triple = triple;
	}
}
