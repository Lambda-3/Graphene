/*
 * ==========================License-Start=============================
 * graphene-core : CoreCoreRelation
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

package org.lambda3.graphene.core.simplified_graph_extraction.rdf_output.model;


import org.lambda3.text.simplification.discourse.tree.Relation;

/**
 *
 */
public class CoreCoreRelation {
	private final String sourceCoreID;
	private final String targetCoreID;
	private final Relation relation;

	public CoreCoreRelation(String sourceCoreID, String targetCoreID, Relation relation) {
		this.sourceCoreID = sourceCoreID;
		this.targetCoreID = targetCoreID;
		this.relation = relation;
	}

	public String getSourceCoreID() {
		return sourceCoreID;
	}

	public String getTargetCoreID() {
		return targetCoreID;
	}

	public Relation getRelation() {
		return relation;
	}

	@Override
	public String toString() {
		return "RHET_REL:" + "\t\t" + sourceCoreID + "\t\t" + relation + "\t\t" + targetCoreID;
	}
}
