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

import org.lambda3.text.simplification.discourse.AbstractElement;

import java.util.Optional;

public class Extraction extends AbstractElement {
	private ExtractionType type;
	private Double confidence; //optional
	private String relation;
	private String arg1;
	private String arg2;
	// for deserialization

	public Extraction() {
	}

	public Extraction(ExtractionType type, Double confidence, int sentenceIdx, int contextLayer, String relation, String arg1, String arg2) {
		super(sentenceIdx, contextLayer);
		this.type = type;
		this.confidence = confidence;
		this.relation = relation;
		this.arg1 = arg1;
		this.arg2 = arg2;
	}

	public ExtractionType getType() {
		return type;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Extraction that = (Extraction) o;

		if (sentenceIdx != that.sentenceIdx) return false;
		if (relation != null ? !relation.equals(that.relation) : that.relation != null) return false;
		if (arg1 != null ? !arg1.equals(that.arg1) : that.arg1 != null) return false;
		return arg2 != null ? arg2.equals(that.arg2) : that.arg2 == null;
	}

	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder();
		strb.append(id).append("\t").append(type).append("\t").append(contextLayer).append("\t").append(arg1).append("\t").append(relation).append("\t").append(arg2);
		simpleContexts.forEach(c -> {
			strb.append("\tS:").append(c.getRelation()).append("(").append(c.getPhraseText()).append(")");
		});
		linkedContexts.forEach(c -> {
			strb.append("\tL:").append(c.getRelation()).append("(").append(c.getTargetID()).append(")");
		});

		return strb.toString();
	}
}
