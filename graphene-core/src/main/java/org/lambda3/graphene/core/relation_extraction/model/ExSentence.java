package org.lambda3.graphene.core.relation_extraction.model;

/*-
 * ==========================License-Start=============================
 * ExSentence.java - Graphene Core - Lambda^3 - 2017
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


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ExSentence {
	private String originalSentence;
	private int sentenceIdx;
    private HashMap<String, ExElement> elementMap; // all elements extracted from this sentence

	// for deserialization
	public ExSentence() {
	}

    public ExSentence(String originalSentence, int sentenceIdx) {
        this.originalSentence = originalSentence;
        this.sentenceIdx = sentenceIdx;
        this.elementMap = new LinkedHashMap<>();
    }

    public void addElement(ExElement exElement) {
        elementMap.putIfAbsent(exElement.getId(), exElement);
    }

    public String getOriginalSentence() {
        return originalSentence;
    }

    public int getSentenceIdx() {
        return sentenceIdx;
    }

    public ExElement getElement(String id) {
	    return elementMap.getOrDefault(id, null);
    }

    public List<ExElement> getElements() {
	    return elementMap.values().stream().collect(Collectors.toList());
    }
}
