/*
 * ==========================License-Start=============================
 * graphene-core : CoreDiscourseType
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

import org.lambda3.graphene.core.simplification.model.DiscourseType;

/**
 *
 */
public class CoreDiscourseType {
	private final String coreID;
	private final DiscourseType discourseType;

	public CoreDiscourseType(String coreID, DiscourseType discourseType) {
		this.coreID = coreID;
		this.discourseType = discourseType;
	}

	public String getCoreID() {
		return coreID;
	}

	public DiscourseType getDiscourseType() {
		return discourseType;
	}

	@Override
	public String toString() {
		return "DIS_TYPE:" + "\t\t" + coreID + "\t\t" + "DISCOURSE_TYPE" + "\t\t" + discourseType;
	}
}
