/*
 * ==========================License-Start=============================
 * graphene-core : CoreExtraction
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

/**
 *
 */
public class CoreExtraction {
	private final String coreID;
	private final String subject;
	private final String predicate;
	private final String object;

	public CoreExtraction(String coreID, String subject, String predicate, String object) {
		this.coreID = coreID;
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}

	public String getCoreID() {
		return coreID;
	}

	public String getSubject() {
		return subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public String getObject() {
		return object;
	}

	@Override
	public String toString() {
		return "CORE_EXT:" + "\t\t" + coreID + "\t\t" + subject + "\t\t" + predicate + "\t\t" + object;
	}
}
