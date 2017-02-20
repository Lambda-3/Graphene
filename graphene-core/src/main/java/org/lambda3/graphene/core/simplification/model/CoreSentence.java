/*
 * ==========================License-Start=============================
 * graphene-core : CoreSentence
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

import java.util.List;
import java.util.UUID;

/**
 *
 */
public class CoreSentence {
	private String id;
	private String text;
	private DiscourseType discourseType;
	private String notSimplifiedText;
	private List<ContextSentence> contextSentences;

	// for deserialization
	public CoreSentence() {
	}

	public CoreSentence(String text, DiscourseType discourseType, String notSimplifiedText, List<ContextSentence> contextSentences) {
		this.id = "CORE-" + UUID.randomUUID();
		this.text = text;
		this.discourseType = discourseType;
		this.notSimplifiedText = notSimplifiedText;
		this.contextSentences = contextSentences;
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public DiscourseType getDiscourseType() {
		return discourseType;
	}

	public String getNotSimplifiedText() {
		return notSimplifiedText;
	}

	public List<ContextSentence> getContextSentences() {
		return contextSentences;
	}

}
