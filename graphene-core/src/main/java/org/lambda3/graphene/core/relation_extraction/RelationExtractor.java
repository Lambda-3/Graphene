package org.lambda3.graphene.core.relation_extraction;

/*-
 * ==========================License-Start=============================
 * RelationExtractor.java - Graphene Core - Lambda^3 - 2017
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


import edu.stanford.nlp.trees.Tree;
import org.lambda3.text.simplification.discourse.utils.parseTree.ParseTreeVisualizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HEAD;
import java.util.List;
import java.util.Optional;

public abstract class RelationExtractor {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final HeadVerbFinder HEAD_VERB_FINDER = new HeadVerbFinder();

	protected abstract List<BinaryExtraction> doExtraction(Tree parseTree);

	public List<BinaryExtraction> extract(Tree parseTree) {
		Optional<String> headVerb = HEAD_VERB_FINDER.findHeadVerb(parseTree);

		List<BinaryExtraction> extractions = doExtraction(parseTree);
		extractions.stream().forEach(e -> {
			if (headVerb.isPresent()) {
				e.setCoreExtraction(e.getRelation().contains(headVerb.get()));
			} else {
				e.setCoreExtraction(false);
			}
		});

		return extractions;
	}
}
