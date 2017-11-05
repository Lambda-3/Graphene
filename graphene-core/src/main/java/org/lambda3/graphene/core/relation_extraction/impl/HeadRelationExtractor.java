package org.lambda3.graphene.core.relation_extraction.impl;

/*-
 * ==========================License-Start=============================
 * HeadRelationExtractor.java - Graphene Core - Lambda^3 - 2017
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


import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import org.lambda3.graphene.core.relation_extraction.BinaryExtraction;
import org.lambda3.graphene.core.relation_extraction.RelationExtractor;
import org.lambda3.text.simplification.discourse.utils.parseTree.ParseTreeExtractionUtils;
import org.lambda3.text.simplification.discourse.utils.words.WordsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HeadRelationExtractor extends RelationExtractor {

	@Override
	public List<BinaryExtraction> doExtraction(Tree parseTree) {
		List<BinaryExtraction> extractions = new ArrayList<>();

		TregexPattern pattern = TregexPattern.compile("S !>> S < (NP=arg1 $.. (VP=vp [ <+(VP) (VP=lowestvp !< VP) | ==(VP=lowestvp !< VP) ]))");
		// this will generate nicer predicates that conform to matrix predicates (e.g. "failed to increase"), but lead to a minor decrease in P and R in benchmark test
//		TregexPattern pattern = TregexPattern.compile("S !>> S < (NP=arg1 $.. (VP=vp [ <+(S|VP) (VP=lowestvp !< VP !< S) | ==(VP=lowestvp !< VP !< S) ]))");
		TregexMatcher matcher = pattern.matcher(parseTree);
		while (matcher.find()) {
			Tree arg1 = matcher.getNode("arg1");
			Tree vp = matcher.getNode("vp");
			Tree lowestvp = matcher.getNode("lowestvp");

			// has arg2 ?
			TregexPattern arg2Pattern = TregexPattern.compile(lowestvp.value() + " < (PP|NP|S|SBAR=arg2 !$,, (PP|NP|S|SBAR))");
			TregexMatcher arg2Matcher = arg2Pattern.matcher(lowestvp);
			if (arg2Matcher.findAt(lowestvp)) {
				Tree arg2 = arg2Matcher.getNode("arg2");

				List<Word> arg1Words = ParseTreeExtractionUtils.getContainingWords(arg1);
				List<Word> relationWords = ParseTreeExtractionUtils.getWordsInBetween(parseTree, vp, arg2, true, false);
				List<Word> arg2Words = ParseTreeExtractionUtils.getFollowingWords(vp, arg2, true);

				extractions.add(new BinaryExtraction(
					null,
					WordsUtils.wordsToString(relationWords),
					WordsUtils.wordsToString(arg1Words),
					WordsUtils.wordsToString(arg2Words)
				));
			} else {
				List<Word> arg1Words = ParseTreeExtractionUtils.getContainingWords(arg1);
				List<Word> relationWords = ParseTreeExtractionUtils.getContainingWords(vp);
				List<Word> arg2Words = new ArrayList<>();

				extractions.add(new BinaryExtraction(
					null,
					WordsUtils.wordsToString(relationWords),
					WordsUtils.wordsToString(arg1Words),
					WordsUtils.wordsToString(arg2Words)
				));
			}
		}

		return extractions;
	}
}
