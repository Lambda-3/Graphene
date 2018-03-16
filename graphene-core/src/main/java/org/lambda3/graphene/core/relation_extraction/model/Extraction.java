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


import org.lambda3.graphene.core.utils.IDGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Extraction {
	private String id;
	private ExtractionType type;
	private Double confidence; //optional
	private int sentenceIdx;
	private int contextLayer;
	private String relation;
	private String arg1;
	private String arg2;
	private List<LinkedContext> linkedContexts;
	private List<SimpleContext> simpleContexts;

	// for deserialization
	public Extraction() {
	}

	public Extraction(ExtractionType type, Double confidence, int sentenceIdx, int contextLayer, String relation, String arg1, String arg2) {
		this.id = IDGenerator.generateUUID();
		this.type = type;
		this.confidence = confidence;
		this.sentenceIdx = sentenceIdx;
		this.contextLayer = contextLayer;
		this.relation = relation;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.linkedContexts = new ArrayList<>();
		this.simpleContexts = new ArrayList<>();
	}

	public void addLinkedContext(LinkedContext context) {
		if (!linkedContexts.contains(context)) {
			linkedContexts.add(context);
		}
	}

	public void addSimpleContext(SimpleContext context) {
		if (!simpleContexts.contains(context)) {
			simpleContexts.add(context);
		}
	}

	public String getId() {
		return id;
	}

	public ExtractionType getType() {
		return type;
	}

	public Optional<Double> getConfidence() {
		return Optional.ofNullable(confidence);
	}

	public int getSentenceIdx() {
		return sentenceIdx;
	}

	public int getContextLayer() {
		return contextLayer;
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

	public List<LinkedContext> getLinkedContexts() {
		return linkedContexts;
	}

	public List<SimpleContext> getSimpleContexts() {
		return simpleContexts;
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
		strb.append(id + "\t" + type + "\t" + contextLayer + "\t" + arg1 + "\t" + relation + "\t" + arg2);
		simpleContexts.forEach(c -> {
			strb.append("\tS:" + c.getClassification() + "(" + c.getText() + ")");
		});
		linkedContexts.forEach(c -> {
			strb.append("\tL:" + c.getClassification() + "(" + c.getTargetID() + ")");
		});

		return strb.toString();
	}
}
