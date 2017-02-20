/*
 * ==========================License-Start=============================
 * graphene-core : ExContextSentence
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


import org.lambda3.graphene.core.graph_extraction.model.Extraction;
import org.lambda3.text.simplification.discourse.tree.Relation;

import java.util.List;
import java.util.UUID;

/**
 *
 */
public class ExContextSentence {
	private String id;
	private String text;
	private Relation relation;
	private List<Extraction> extractions;

	// for deserialization
	public ExContextSentence() {
	}

	public ExContextSentence(String text, Relation relation, List<Extraction> extractions) {
		this.id = "CONTEXT-" + UUID.randomUUID();
		this.text = text;
		this.relation = relation;
		this.extractions = extractions;
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public Relation getRelation() {
		return relation;
	}

	public List<Extraction> getExtractions() {
		return extractions;
	}
}
