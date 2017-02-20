/*
 * ==========================License-Start=============================
 * graphene-core : GraphExtraction
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

package org.lambda3.graphene.core.graph_extraction;

import org.lambda3.graphene.core.graph_extraction.model.ExtractionContent;
import org.lambda3.graphene.core.graph_extraction.model.ExtractionSentence;
import org.lambda3.graphene.core.graph_extraction.runner.OpenIERunner;
import org.lambda3.text.simplification.discourse.utils.sentences.SentencesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Entry point to all coreference resolution related.
 */
public class GraphExtraction {
	private final Logger log = LoggerFactory.getLogger(getClass());

	public GraphExtraction() {
		log.info("GraphExtraction initialized");
	}

	@SuppressWarnings("WeakerAccess")
	public ExtractionContent extract(List<String> sentences) {
		log.info("Running Graph Extraction on {} sentences", sentences.size());

		List<ExtractionSentence> extrSentences = sentences.stream().map(OpenIERunner::extract).collect(Collectors.toList());

		return new ExtractionContent(extrSentences);
	}

	public ExtractionContent extract(String text) {
		return extract(SentencesUtils.splitIntoSentences(text));
	}

}
