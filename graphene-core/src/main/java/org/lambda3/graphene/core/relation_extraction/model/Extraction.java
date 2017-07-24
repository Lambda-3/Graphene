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
	private ExtractionType type;
	private String id;
	private String text;
	private int sentenceIdx;
	private int contextLayer;
	private List<LinkedContext> linkedContexts;
	private List<SimpleContext> simpleContexts;
	private SPO spo; // optional

	// for deserialization
	public Extraction() {
	}

	public Extraction(ExtractionType type, String text, int sentenceIdx, int contextLayer) {
		this.id = IDGenerator.generateUUID();
		this.type = type;
		this.text = text;
		this.sentenceIdx = sentenceIdx;
		this.contextLayer = contextLayer;
		this.linkedContexts = new ArrayList<>();
		this.simpleContexts = new ArrayList<>();
		this.spo = null;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getSentenceIdx() {
		return sentenceIdx;
	}

	public int getContextLayer() {
		return contextLayer;
	}

	public List<LinkedContext> getLinkedContexts() {
		return linkedContexts;
	}

	public List<SimpleContext> getSimpleContexts() {
		return simpleContexts;
	}

	public Optional<SPO> getSpo() {
		return Optional.ofNullable(spo);
	}

	public void setSpo(SPO spo) {
		this.spo = spo;
	}

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof Extraction) && (((Extraction) obj).getId().equals(getId())));
	}

	@Override
	public String toString() {
		return "Extraction{" +
			"type=" + type +
			", id='" + id + '\'' +
			", text='" + text + '\'' +
			", sentenceIdx=" + sentenceIdx +
			", contextLayer=" + contextLayer +
			", spo=" + spo +
			'}';
	}
}
