package org.lambda3.graphene.core.relation_extraction.model;

/*-
 * ==========================License-Start=============================
 * Classification.java - Graphene Core - Lambda^3 - 2017
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


import org.lambda3.text.simplification.discourse.tree.Relation;

public enum Classification {

	// from Discourse-Extraction
	UNKNOWN_COORDINATION,
	UNKNOWN_SUBORDINATION,
	BACKGROUND,
	CAUSE,
	CONDITION,
	CONTRAST,
	ELABORATION,
	ENABLEMENT,
	EXPLANATION,
	JOINT_CONJUNCTION,
	JOINT_DISJUNCTION,
	TEMPORAL_BEFORE,
	TEMPORAL_AFTER,
	TEMPORAL_SEQUENCE,
	INTRA_SENT_ATTR,
	JOINT_NP_CONJUNCTION,
	JOINT_NP_DISJUNCTION,

	// new ones
	UNKNOWN,
	TEMPORAL,
	SPATIAL;

	public static Classification convert(Relation relation) {
		switch (relation) {
			case UNKNOWN_COORDINATION:
				return UNKNOWN_COORDINATION;
			case UNKNOWN_SUBORDINATION:
				return UNKNOWN_SUBORDINATION;
			case BACKGROUND:
				return BACKGROUND;
			case CAUSE:
				return CAUSE;
			case CONDITION:
				return CONDITION;
			case CONTRAST:
				return CONTRAST;
			case ELABORATION:
				return ELABORATION;
			case ENABLEMENT:
				return ENABLEMENT;
			case EXPLANATION:
				return EXPLANATION;
			case JOINT_CONJUNCTION:
				return JOINT_CONJUNCTION;
			case JOINT_DISJUNCTION:
				return JOINT_DISJUNCTION;
			case TEMPORAL_BEFORE:
				return TEMPORAL_BEFORE;
			case TEMPORAL_AFTER:
				return TEMPORAL_AFTER;
			case TEMPORAL_SEQUENCE:
				return TEMPORAL_SEQUENCE;
			case INTRA_SENT_ATTR:
				return INTRA_SENT_ATTR;
			case JOINT_NP_CONJUNCTION:
				return JOINT_NP_CONJUNCTION;
			case JOINT_NP_DISJUNCTION:
				return JOINT_NP_DISJUNCTION;
			default:
				throw new AssertionError("Could not convert relation");
		}
	}
}
