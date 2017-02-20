/*
 * ==========================License-Start=============================
 * graphene-core : ExSimplificationContent
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

package org.lambda3.graphene.core.simplified_graph_extraction.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.lambda3.graphene.core.Content;
import org.lambda3.graphene.core.simplification.model.IDCoreSentenceRelation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class ExSimplificationContent extends Content {
	private boolean coreferenced;
	private List<ExSimplificationSentence> simplifiedSentences;
	private Map<ExCoreSentence, List<ExCoreSentenceRelation>> coreRelationsMap;

	// for deserialization
	public ExSimplificationContent() {
	}

	public ExSimplificationContent(List<ExSimplificationSentence> simplifiedSentences, Map<ExCoreSentence, List<ExCoreSentenceRelation>> coreRelationsMap) {
		this.coreferenced = false;
		this.simplifiedSentences = simplifiedSentences;
		this.coreRelationsMap = coreRelationsMap;
	}

	@SuppressWarnings("WeakerAccess")
	public boolean isCoreferenced() {
		return coreferenced;
	}

	public void setCoreferenced(boolean coreferenced) {
		this.coreferenced = coreferenced;
	}

	public List<ExSimplificationSentence> getSimplifiedSentences() {
		return simplifiedSentences;
	}

	@SuppressWarnings("WeakerAccess")
	public Map<ExCoreSentence, List<ExCoreSentenceRelation>> getCoreRelationsMap() {
		return coreRelationsMap;
	}

	public List<ExCoreSentenceRelation> getCoreSentenceRelations(ExCoreSentence coreSentence) {
		return coreRelationsMap.getOrDefault(coreSentence, new ArrayList<>());
	}

	// custom serialization of coreRelationsMap
	@JsonProperty("coreRelationsMap")
	public Map<String, List<IDCoreSentenceRelation>> getIDCoreRelationsMap() {
		Map<String, List<IDCoreSentenceRelation>> res = new LinkedHashMap<>();

		for (ExCoreSentence exCoreSentence : coreRelationsMap.keySet()) {
			res.put(exCoreSentence.getId(), coreRelationsMap.get(exCoreSentence).stream().map(
					r -> new IDCoreSentenceRelation(r.getTarget().getId(), r.getRelation())
			).collect(Collectors.toList()));
		}

		return res;
	}

	// custom deserialization of coreRelationsMap
	@JsonProperty("coreRelationsMap")
	public void setIDCoreRelationsMap(Map<String, List<IDCoreSentenceRelation>> idCoreRelationsMap) {
		this.coreRelationsMap = new LinkedHashMap<>();

		// create core ID Map
		Map<String, ExCoreSentence> idMap = new LinkedHashMap<>();
		for (ExSimplificationSentence exSimplificationSentence : simplifiedSentences) {
			for (ExCoreSentence exCoreSentence : exSimplificationSentence.getCoreSentences()) {
				idMap.put(exCoreSentence.getId(), exCoreSentence);
			}
		}

		// resolve dependencies
		for (String id : idCoreRelationsMap.keySet()) {
			ExCoreSentence source = idMap.get(id);
			List<ExCoreSentenceRelation> relations = idCoreRelationsMap.get(id).stream().map(
					r -> new ExCoreSentenceRelation(idMap.get(r.getTargetID()), r.getRelation())
			).collect(Collectors.toList());

			this.coreRelationsMap.put(source, relations);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof ExSimplificationContent)) return false;

		ExSimplificationContent that = (ExSimplificationContent) o;

		return new EqualsBuilder()
				.append(isCoreferenced(), that.isCoreferenced())
				.append(getSimplifiedSentences(), that.getSimplifiedSentences())
				.append(getCoreRelationsMap(), that.getCoreRelationsMap())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(isCoreferenced())
				.append(getSimplifiedSentences())
				.append(getCoreRelationsMap())
				.toHashCode();
	}

	@Override
	public String toString() {
		return "ExSimplificationContent{" +
				"coreferenced=" + coreferenced +
				", simplifiedSentences=" + simplifiedSentences +
				", coreRelationsMap=" + coreRelationsMap +
				'}';
	}
}
