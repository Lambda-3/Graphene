/*
 * ==========================License-Start=============================
 * graphene-core : SimplificationContent
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

package org.lambda3.graphene.core.simplification.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.lambda3.graphene.core.Content;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimplificationContent extends Content {
	private boolean coreferenced;
	private List<SimplificationSentence> simplifiedSentences;
	private Map<CoreSentence, List<CoreSentenceRelation>> coreRelationsMap;

	// for deserialization
	@SuppressWarnings("unused")
	public SimplificationContent() {
	}

	public SimplificationContent(List<SimplificationSentence> simplifiedSentences, Map<CoreSentence, List<CoreSentenceRelation>> coreRelationsMap) {
		this.coreferenced = false;
		this.simplifiedSentences = simplifiedSentences;
		this.coreRelationsMap = coreRelationsMap;
	}

	public boolean isCoreferenced() {
		return coreferenced;
	}

	public void setCoreferenced(boolean coreferenced) {
		this.coreferenced = coreferenced;
	}

	public List<SimplificationSentence> getSimplifiedSentences() {
		return simplifiedSentences;
	}

	public Map<CoreSentence, List<CoreSentenceRelation>> getCoreRelationsMap() {
		return coreRelationsMap;
	}

	@SuppressWarnings("unused")
	public List<CoreSentenceRelation> getCoreSentenceRelations(CoreSentence coreSentence) {
		return coreRelationsMap.getOrDefault(coreSentence, new ArrayList<>());
	}

	// custom serialization of coreRelationsMap
	@JsonProperty("coreRelationsMap")
	public Map<String, List<IDCoreSentenceRelation>> getIDCoreRelationsMap() {
		Map<String, List<IDCoreSentenceRelation>> res = new LinkedHashMap<>();

		for (CoreSentence coreSentence : coreRelationsMap.keySet()) {
			res.put(coreSentence.getId(), coreRelationsMap.get(coreSentence).stream().map(
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
		Map<String, CoreSentence> idMap = new LinkedHashMap<>();
		for (SimplificationSentence simplifiedSentence : simplifiedSentences) {
			for (CoreSentence coreSentence : simplifiedSentence.getCoreSentences()) {
				idMap.put(coreSentence.getId(), coreSentence);
			}
		}

		// resolve dependencies
		for (String id : idCoreRelationsMap.keySet()) {
			CoreSentence source = idMap.get(id);
			List<CoreSentenceRelation> relations = idCoreRelationsMap.get(id).stream().map(
					r -> new CoreSentenceRelation(idMap.get(r.getTargetID()), r.getRelation())
			).collect(Collectors.toList());

			this.coreRelationsMap.put(source, relations);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof SimplificationContent)) return false;

		SimplificationContent that = (SimplificationContent) o;

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
		return "SimplificationContent{" +
				"coreferenced=" + coreferenced +
				", simplifiedSentences=" + simplifiedSentences +
				", coreRelationsMap=" + coreRelationsMap +
				'}';
	}
}
