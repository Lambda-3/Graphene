/*
 * ==========================License-Start=============================
 * graphene-core : Simplification
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

package org.lambda3.graphene.core.simplification;

import org.lambda3.graphene.core.simplification.model.*;
import org.lambda3.text.simplification.discourse.processing.Processor;
import org.lambda3.text.simplification.discourse.sentence_simplification.element.DContext;
import org.lambda3.text.simplification.discourse.sentence_simplification.element.DCore;
import org.lambda3.text.simplification.discourse.sentence_simplification.relation.DContextRelation;
import org.lambda3.text.simplification.discourse.sentence_simplification.relation.DCoreRelation;
import org.lambda3.text.simplification.discourse.utils.sentences.SentencesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Entry point to all simplification commands.
 */
public class Simplification {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Processor processor = new Processor();

	public Simplification() {
		log.info("Simplification initialized");
	}

	private static CoreSentence getCoreSentence(DCore dCore, Map<DCore, CoreSentence> dCoreMapping, Map<Integer, List<CoreSentence>> sentenceMapping) {
		CoreSentence res;

		if (dCoreMapping.containsKey(dCore)) {
			res = dCoreMapping.get(dCore);
		} else {
			res = new CoreSentence(
					dCore.getText(),
					DiscourseType.DISCOURSE_CORE,
					dCore.getNotSimplifiedText(),
					dCore.getSContexts()
							.stream()
							.map(c -> new ContextSentence(c.getText(), c.getRelation()))
							.collect(Collectors.toList())
			);

			// add to dCoreMapping
			dCoreMapping.put(dCore, res);

		}

		addToSentenceMapping(sentenceMapping, dCore.getSentenceIndex(), res);

		return res;
	}

	private static void addToSentenceMapping(Map<Integer, List<CoreSentence>> sentenceMapping, Integer sentenceIndex, CoreSentence result) {
		// add to sentenceMapping
		if (sentenceMapping.containsKey(sentenceIndex)) {
			sentenceMapping.get(sentenceIndex).add(result);
		} else {
			List<CoreSentence> lst = new ArrayList<>();
			lst.add(result);
			sentenceMapping.put(sentenceIndex, lst);
		}
	}

	private static CoreSentence getCoreSentence(DContext dContext, Map<DContext, CoreSentence> dContextMapping, Map<Integer, List<CoreSentence>> sentenceMapping) {
		CoreSentence res;

		if (dContextMapping.containsKey(dContext)) {
			res = dContextMapping.get(dContext);
		} else {
			res = new CoreSentence(
					dContext.getText(),
					DiscourseType.DISCOURSE_CONTEXT,
					dContext.getNotSimplifiedText(),
					dContext.getSContexts().stream().map(c -> new ContextSentence(c.getText(), c.getRelation())).collect(Collectors.toList())
			);

			// add to dContextMapping
			dContextMapping.put(dContext, res);

			addToSentenceMapping(sentenceMapping, dContext.getSentenceIndex(), res);
		}

		return res;
	}

	public SimplificationContent doSimplification(String text) {
		return doSimplification(SentencesUtils.splitIntoSentences(text));
	}

	@SuppressWarnings("WeakerAccess")
	public SimplificationContent doSimplification(List<String> sentences) {
		log.info("Running Simplification on {} sentences", sentences.size());

		List<DCore> dCores = processor.process(sentences, Processor.ProcessingType.WHOLE);

		Map<DCore, CoreSentence> dCoreMapping = new LinkedHashMap<>(); // maps (former) DCores to CoreSentences
		Map<DContext, CoreSentence> dContextMapping = new LinkedHashMap<>(); // maps (former) DContexts to CoreSentences
		Map<Integer, List<CoreSentence>> sentenceMapping = new LinkedHashMap<>(); // maps sentence indices to CoreSentences

		Map<CoreSentence, List<CoreSentenceRelation>> coreRelationsMap = new LinkedHashMap<>();
		for (DCore dCore : dCores) {
			CoreSentence coreSentence = getCoreSentence(dCore, dCoreMapping, sentenceMapping);

			// add core relations based on DCoreRelations
			for (DCoreRelation dCoreRelation : dCore.getDCoreRelations()) {
				CoreSentence target = getCoreSentence(dCoreRelation.getDCore(), dCoreMapping, sentenceMapping);

				// add to coreRelationsMap
				coreRelationsMap.putIfAbsent(coreSentence, new ArrayList<>());
				coreRelationsMap.get(coreSentence).add(new CoreSentenceRelation(target, dCoreRelation.getRelation()));
			}

			// add core relations based on DContextRelations
			for (DContextRelation dContextRelation : dCore.getDContextRelations()) {
				CoreSentence target = getCoreSentence(dContextRelation.getDContext(), dContextMapping, sentenceMapping);

				// add to coreRelationsMap
				coreRelationsMap.putIfAbsent(coreSentence, new ArrayList<>());
				coreRelationsMap.get(coreSentence).add(new CoreSentenceRelation(target, dContextRelation.getRelation()));
			}
		}

		// create SimplificationSentences
		List<SimplificationSentence> simplificationSentences = new ArrayList<>();
		for (Integer idx : sentenceMapping.keySet()) {
			SimplificationSentence simplificationSentence = new SimplificationSentence(
					((0 <= idx) && (idx < sentences.size())) ? sentences.get(idx) : "???",
					sentenceMapping.get(idx)
			);
			simplificationSentences.add(simplificationSentence);
		}

		return new SimplificationContent(simplificationSentences, coreRelationsMap);
	}

}
