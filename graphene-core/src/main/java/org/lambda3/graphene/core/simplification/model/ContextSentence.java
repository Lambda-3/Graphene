/*
 * ==========================License-Start=============================
 * graphene-core : ContextSentence
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

package org.lambda3.graphene.core.simplification.model;


import org.lambda3.text.simplification.discourse.tree.Relation;

import java.util.UUID;

/**
 *
 */
public class ContextSentence {
	private String id;
	private String text;
	private Relation relation;

	// for deserialization
	public ContextSentence() {
	}

	public ContextSentence(String text, Relation relation) {
		this.id = "CONTEXT-" + UUID.randomUUID();
		this.text = text;
		this.relation = relation;
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
}
