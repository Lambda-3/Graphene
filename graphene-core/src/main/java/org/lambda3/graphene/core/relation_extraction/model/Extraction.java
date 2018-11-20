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

import org.lambda3.text.simplification.discourse.runner.discourse_tree.RelationType;
import org.lambda3.text.simplification.discourse.utils.IDGenerator;

public class Extraction {

	private static final double UNKNOWN_CONFIDENCE = -1d;

	public final String id = IDGenerator.generateUUID();
	public final ExtractionType type;
	public final double confidence; //optional
	public final Triple triple;
	public final boolean asLinkedContext;
	public final RelationType relationType;
	public final boolean isCoreExtraction;

	private Extraction() {
		// for deserialization
		this(null, null, false, null, false);
	}

	public Extraction(ExtractionType type, Triple triple, boolean asLinkedContext,
					  RelationType relationType, boolean isCoreExtraction) {
		this(type, UNKNOWN_CONFIDENCE, triple, asLinkedContext, relationType, isCoreExtraction);
	}

	public Extraction(ExtractionType type, double confidence, Triple triple, boolean asLinkedContext,
					  RelationType classification, boolean isCoreExtraction) {
		this.type = type;
		this.confidence = confidence;
		this.triple = triple;
		this.asLinkedContext = asLinkedContext;
		this.relationType = classification;
		this.isCoreExtraction = isCoreExtraction;
	}
}
