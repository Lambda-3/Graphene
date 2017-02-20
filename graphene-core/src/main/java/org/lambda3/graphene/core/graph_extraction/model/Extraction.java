/*
 * ==========================License-Start=============================
 * graphene-core : Extraction
 *
 * Copyright © 2017 Lambda³
 *
 * GNU General Public License 3
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * ==========================License-End==============================
 */

package org.lambda3.graphene.core.graph_extraction.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Extraction {
	private String subject;
	private String predicate;
	private String object;
	private String context;

	private List<String> temporalContexts = new ArrayList<>();
	private List<String> spatialContexts = new ArrayList<>();
	private List<String> additionalContexts = new ArrayList<>();

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public List<String> getTemporalContexts() {
		return temporalContexts;
	}

	public void setTemporalContexts(List<String> temporalContexts) {
		this.temporalContexts = temporalContexts;
	}

	public List<String> getSpatialContexts() {
		return spatialContexts;
	}

	public void setSpatialContexts(List<String> spatialContexts) {
		this.spatialContexts = spatialContexts;
	}

	public List<String> getAdditionalContexts() {
		return additionalContexts;
	}

	public void setAdditionalContexts(List<String> additionalContexts) {
		this.additionalContexts = additionalContexts;
	}
}
