package org.lambda3.graphene.core.relation_extraction.model;

/*-
 * ==========================License-Start=============================
 * RelationExtractionContent.java - Graphene Core - Lambda^3 - 2017
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


import org.lambda3.text.simplification.discourse.model.Content;
import org.lambda3.text.simplification.discourse.model.OutSentence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RelationExtractionContent extends Content {
	private boolean coreferenced;
	private List<OutSentence<Extraction>> sentences;

	// for deserialization
	public RelationExtractionContent() {
		this.coreferenced = false;
		this.sentences = new ArrayList<>();
	}

	public void setCoreferenced(boolean coreferenced) {
		this.coreferenced = coreferenced;
	}

	public void addSentence(OutSentence<Extraction> sentence) {
		this.sentences.add(sentence);
	}

	public Optional<String> containsExtraction(Extraction extraction) {
		return sentences.get(extraction.getSentenceIdx()).containsElement(extraction);
	}

	public void addExtraction(Extraction extraction) {
		sentences.get(extraction.getSentenceIdx()).addElement(extraction);
	}

	public boolean isCoreferenced() {
		return coreferenced;
	}

	public List<OutSentence<Extraction>> getSentences() {
		return sentences;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RelationExtractionContent that = (RelationExtractionContent) o;
		return coreferenced == that.coreferenced &&
			Objects.equals(sentences, that.sentences);
	}

	@Override
	public int hashCode() {
		return Objects.hash(coreferenced, sentences);
	}

	@Override
	public String toString() {
		return "RelationExtractionContent{" +
			"coreferenced=" + coreferenced +
			", sentences=" + sentences +
			'}';
	}
}
