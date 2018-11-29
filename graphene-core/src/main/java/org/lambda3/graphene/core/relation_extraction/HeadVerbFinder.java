package org.lambda3.graphene.core.relation_extraction;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import org.lambda3.text.simplification.discourse.utils.parseTree.ParseTreeExtractionUtils;
import org.lambda3.text.simplification.discourse.utils.parseTree.ParseTreeVisualizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 *
 */
public class HeadVerbFinder {

	private HeadVerbFinder() {
		//don't allow creating object.
	}

	public static Optional<String> findHeadVerb(Tree parseTree) {
		TregexPattern pattern = TregexPattern.compile("ROOT <<: (__ < (VP=vp [ <+(VP) (VP=lowestvp !< VP) | ==(VP=lowestvp !< VP) ]))");
		TregexMatcher matcher = pattern.matcher(parseTree);
		while (matcher.findAt(parseTree)) {
			Tree lowestvp = matcher.getNode("lowestvp");

			return Optional.of(ParseTreeExtractionUtils.getContainingWords(lowestvp).get(0).word());
		}
		return Optional.empty();
	}

}
