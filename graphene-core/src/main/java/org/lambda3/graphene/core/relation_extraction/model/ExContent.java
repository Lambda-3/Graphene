package org.lambda3.graphene.core.relation_extraction.model;

/*-
 * ==========================License-Start=============================
 * ExContent.java - Graphene Core - Lambda^3 - 2017
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


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.lambda3.graphene.core.Content;

import java.util.ArrayList;
import java.util.List;

public class ExContent extends Content {
	private boolean coreferenced;
	private List<ExSentence> sentences;

	// for deserialization
	public ExContent() {
	}

    public ExContent(List<ExSentence> sentences) {
        this.coreferenced = false;
        this.sentences = sentences;
    }

    public void addExtraction(Extraction element) {
        sentences.get(element.getSentenceIdx()).addExtraction(element);
    }

    public boolean isCoreferenced() {
        return coreferenced;
    }

    public void setCoreferenced(boolean coreferenced) {
        this.coreferenced = coreferenced;
    }

    public List<ExSentence> getSentences() {
        return sentences;
    }

    public Extraction getExtraction(String id) {
        for (ExSentence sentence : sentences) {
            Extraction e = sentence.getExtraction(id);
            if (e != null) {
                return e;
            }
        }

        return null;
    }

    public List<Extraction> getExtractions() {
        List<Extraction> res = new ArrayList<>();
        sentences.forEach(s -> res.addAll(s.getExtractions()));

        return res;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof ExContent)) return false;

		ExContent that = (ExContent) o;

		return new EqualsBuilder()
                .append(isCoreferenced(), that.isCoreferenced())
                .append(getSentences(), that.getSentences())
                .isEquals();
    }

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
                .append(isCoreferenced())
                .append(getSentences())
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ExContent{" +
                "coreferenced=" + coreferenced +
                ", sentences=" + sentences +
                '}';
    }
}
