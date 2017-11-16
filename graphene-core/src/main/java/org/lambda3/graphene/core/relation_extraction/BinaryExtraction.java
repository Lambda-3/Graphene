package org.lambda3.graphene.core.relation_extraction;

/*-
 * ==========================License-Start=============================
 * BinaryExtraction.java - Graphene Core - Lambda^3 - 2017
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


import java.util.Optional;

public class BinaryExtraction {
	private Double confidence; //optional
	private String relation;
	private String arg1;
	private String arg2;
	private boolean isCoreExtraction;

	public BinaryExtraction(Double confidence, String relation, String arg1, String arg2) {
		this.confidence = confidence;
		this.relation = relation;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.isCoreExtraction = false;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public Optional<Double> getConfidence() {
		return Optional.ofNullable(confidence);
	}

	public String getRelation() {
		return relation;
	}

	public String getArg1() {
		return arg1;
	}

	public String getArg2() {
		return arg2;
	}

	public void setCoreExtraction(boolean coreExtraction) {
		isCoreExtraction = coreExtraction;
	}

	public boolean isCoreExtraction() {
		return isCoreExtraction;
	}
}
