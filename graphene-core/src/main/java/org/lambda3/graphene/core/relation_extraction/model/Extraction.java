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

import edu.stanford.nlp.trees.Tree;
import org.lambda3.text.simplification.discourse.model.Element;

import java.util.Objects;
import java.util.Optional;

public class Extraction<T extends AbstractTriple> extends Element {
	private ExtractionType type;
	private Double confidence; //optional
	private T triple;

	protected Extraction() {
		// for deserialization
	}

	public Extraction(Tree parseTree, int sentenceIdx, int contextLayer, ExtractionType type, Double confidence, T triple) {
		super(parseTree, sentenceIdx, contextLayer);
		this.type = type;
		this.confidence = confidence;
		this.triple = triple;
	}

	public ExtractionType getType() {
		return type;
	}

	public Optional<Double> getConfidence() {
		return Optional.ofNullable(confidence);
	}

	public T getTriple() {
		return triple;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Extraction<?> that = (Extraction<?>) o;
		return type == that.type &&
			Objects.equals(confidence, that.confidence) &&
			Objects.equals(triple, that.triple);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, confidence, triple);
	}
}
