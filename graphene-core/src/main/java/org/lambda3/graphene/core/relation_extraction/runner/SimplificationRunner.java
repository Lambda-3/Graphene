/*
 * ==========================License-Start=============================
 * graphene-core : SimplificationRunner
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

package org.lambda3.graphene.core.relation_extraction.runner;

import edu.stanford.nlp.trees.Tree;
import org.lambda3.graphene.core.relation_extraction.model.*;
import org.lambda3.text.simplification.discourse.utils.words.WordsUtils;
import org.lambda3.text.simplification.sentence.transformation.CoreContextSentence;
import org.lambda3.text.simplification.sentence.transformation.SentenceSimplifyingException;
import org.lambda3.text.simplification.sentence.transformation.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class SimplificationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SimplificationRunner.class);
    private static final Pattern VCONTEXT_PATTERN = Pattern.compile("^\\W*this\\W+\\w+\\W+(?<text>.*\\w+.*)$", Pattern.CASE_INSENSITIVE);

    private static Optional<ExVContext> isVContext(String text) {
        Matcher matcher = VCONTEXT_PATTERN.matcher(text);

        if (matcher.matches()) {
            ExVContext res = new ExVContext(matcher.group("text"));

            return Optional.of(res);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<ExNContext> isNContext(String text) {
        Matcher matcher = VCONTEXT_PATTERN.matcher(text);

        if (!matcher.matches()) {
            ExNContext res = new ExNContext(text);

            return Optional.of(res);
        } else {
            return Optional.empty();
        }
    }

    private static List<ExElement> createNewElements(ExElement exElement) {
        LOG.debug("Simplifying: '{}'", exElement.getText());

        List<ExElement> newElements = new ArrayList<>();

        // apply sentence simplification
        Transformer transformer = new Transformer();
        try {
            CoreContextSentence s = transformer.simplify(exElement.getText());

            // set simplified text
            if ((s.getCore() != null) && (s.getCore().size() > 0)) {
                Tree c = s.getCore().get(0);
                if (c != null) {
                    String text = WordsUtils.wordsToProperSentenceString(c.yieldWords());
                    exElement.setText(text);
                }
            }

            // process (sentence simplification) contexts
            if (s.getContext() != null) {
                for (Tree c : s.getContext()) {
                    if (c != null) {
                        String text = WordsUtils.wordsToProperSentenceString(c.yieldWords());

                        Optional<ExNContext> ict = isNContext(text);
                        Optional<ExVContext> dct = isVContext(text);

                        // create NContext
                        if (ict.isPresent()) {
                            ExNContext newContext = ict.get();

                            // classify context
                            Classification classification = Classification.UNKNOWN;
                            newContext.setClassification(classification);

                            // add new context
                            exElement.addNContext(newContext);
                        } else

                        // create DContext
                        if (dct.isPresent()) {
                            ExVContext newContext = dct.get();

                            // classify context
                            Classification classification = ExContextClassifier.classify(newContext.getText());
                            newContext.setClassification(classification);

                            // add new context
                            exElement.addVContext(newContext);
                        }
                    }
                }
            }
        } catch (SentenceSimplifyingException e) {
            // nothing
        }

        return newElements;
    }

    public static void doSimplification(ExContent content) {

        // simplify all elements
        List<ExElement> newElements = new ArrayList<>();
        for (ExElement exElement : content.getElements()) {
            List<ExElement> nes = createNewElements(exElement);
            newElements.addAll(nes);
        }

        // add newElements
        newElements.forEach(content::addElement);
    }
}
