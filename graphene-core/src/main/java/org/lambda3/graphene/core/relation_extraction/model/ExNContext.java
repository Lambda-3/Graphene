package org.lambda3.graphene.core.relation_extraction.model;

/*-
 * ==========================License-Start=============================
 * ExNContext.java - Graphene Core - Lambda^3 - 2017
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

public class ExNContext {
	private String text;
	private Classification classification;
	private ExSPO spo; // optional

	// for deserialization
	public ExNContext() {
	}

	public ExNContext(String text) {
		this.text = text;
		this.classification = Classification.UNKNOWN;
		this.spo = null;
	}

	public String getText() {
		return text;
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	public Optional<ExSPO> getSpo() {
		return Optional.ofNullable(spo);
	}

	public void setSpo(ExSPO spo) {
		this.spo = spo;
	}
}
