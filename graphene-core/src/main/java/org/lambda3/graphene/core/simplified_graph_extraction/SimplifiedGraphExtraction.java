/*
 * ==========================License-Start=============================
 * graphene-core : SimplifiedGraphExtraction
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

package org.lambda3.graphene.core.simplified_graph_extraction;

import org.lambda3.graphene.core.graph_extraction.model.Extraction;
import org.lambda3.graphene.core.graph_extraction.runner.OpenIERunner;
import org.lambda3.graphene.core.simplification.model.*;
import org.lambda3.graphene.core.simplified_graph_extraction.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class SimplifiedGraphExtraction {
	private final Logger log = LoggerFactory.getLogger(getClass());

	public SimplifiedGraphExtraction() {
		log.info("SimplifiedGraphExtraction initialized");
	}

	public ExSimplificationContent extract(SimplificationContent simplificationContent) {

		if (log.isDebugEnabled()) {
			log.debug("extract graph for simplified content");
		}

		List<ExSimplificationSentence> exSimplificationSentences = new ArrayList<>();

		// 1) create (deep) copy of tree
		Map<CoreSentence, ExCoreSentence> coreMapping = new LinkedHashMap<>();

		// for each simplificationSentence
		for (SimplificationSentence simplificationSentence : simplificationContent.getSimplifiedSentences()) {
			List<ExCoreSentence> exCoreSentences = new ArrayList<>();

			// for each simplificationSentence.coreSentence
			for (CoreSentence coreSentence : simplificationSentence.getCoreSentences()) {
				List<ExContextSentence> exContextSentences = new ArrayList<>();
				List<Extraction> coreExtractions = OpenIERunner.extract(coreSentence.getText()).getExtractions();

				// for each simplificationSentence.coreSentence.contextSentence
				for (ContextSentence contextSentence : coreSentence.getContextSentences()) {
					List<Extraction> contextExtractions = OpenIERunner.extract(contextSentence.getText()).getExtractions();

					exContextSentences.add(new ExContextSentence(contextSentence.getText(), contextSentence.getRelation(), contextExtractions));
				}

				ExCoreSentence exCoreSentence = new ExCoreSentence(coreSentence.getText(), coreSentence.getDiscourseType(), coreSentence.getNotSimplifiedText(), exContextSentences, coreExtractions);
				coreMapping.put(coreSentence, exCoreSentence);
				exCoreSentences.add(exCoreSentence);
			}

			exSimplificationSentences.add(new ExSimplificationSentence(simplificationSentence.getOriginalSentence(), exCoreSentences));
		}

		// 2) create coreRelationsMap
		Map<ExCoreSentence, List<ExCoreSentenceRelation>> coreRelationsMap = new LinkedHashMap<>();
		for (CoreSentence coreSentence : simplificationContent.getCoreRelationsMap().keySet()) {
			List<ExCoreSentenceRelation> lst = new ArrayList<>();
			for (CoreSentenceRelation relation : simplificationContent.getCoreRelationsMap().get(coreSentence)) {
				lst.add(new ExCoreSentenceRelation(coreMapping.get(relation.getTarget()), relation.getRelation()));
			}
			coreRelationsMap.put(coreMapping.get(coreSentence), lst);
		}

		ExSimplificationContent res = new ExSimplificationContent(exSimplificationSentences, coreRelationsMap);
		res.setCoreferenced(simplificationContent.isCoreferenced());

		return res;
	}
}
