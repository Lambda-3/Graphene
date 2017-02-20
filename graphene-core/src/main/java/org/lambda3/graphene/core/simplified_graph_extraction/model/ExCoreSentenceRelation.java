/*
 * ==========================License-Start=============================
 * graphene-core : ExCoreSentenceRelation
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

import org.lambda3.text.simplification.discourse.tree.Relation;

/**
 *
 */
public class ExCoreSentenceRelation {
	private ExCoreSentence target;
	private Relation relation;

	// for deserialization
	public ExCoreSentenceRelation() {
	}

	public ExCoreSentenceRelation(ExCoreSentence target, Relation relation) {
		this.target = target;
		this.relation = relation;
	}

	public ExCoreSentence getTarget() {
		return target;
	}

	public Relation getRelation() {
		return relation;
	}

	@Override
	public boolean equals(Object o) {
		return ((o instanceof ExCoreSentenceRelation)
				&& (((ExCoreSentenceRelation) o).relation.equals(relation))
				&& (((ExCoreSentenceRelation) o).target.equals(target)));
	}
}
