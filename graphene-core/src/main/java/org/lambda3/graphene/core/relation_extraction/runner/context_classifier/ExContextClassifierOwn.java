package org.lambda3.graphene.core.relation_extraction.runner.context_classifier;

/*-
 * ==========================License-Start=============================
 * ExContextClassifier.java - Graphene Core - Lambda^3 - 2017
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


import org.lambda3.graphene.core.relation_extraction.model.Classification;
import org.lambda3.graphene.core.relation_extraction.model.ClassificationResult;
import org.lambda3.graphene.core.relation_extraction.runner.ExContextClassifier;
import org.lambda3.text.simplification.discourse.utils.ner.NERString;
import org.lambda3.text.simplification.discourse.utils.ner.NERStringParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExContextClassifierOwn implements ExContextClassifier {
    private static final Logger LOG = LoggerFactory.getLogger(ExContextClassifierOwn.class);

    private static final String PATTERN_PREFIX = "^.*(?<!\\w)";
    private static final String PATTERN_SUFFIX = "(?!\\w).*$";

    private static final List<String> MONTH_PATTERNS = Stream.of(
            "january", "jan\\.",
            "february", "feb\\.",
            "march", "mar\\.",
            "april", "apr\\.",
            "may",
            "june",
            "july",
            "august", "aug\\.",
            "september", "sept\\.",
            "october", "oct\\.",
            "november", "nov\\.",
            "december", "dec\\."
    ).map(p -> PATTERN_PREFIX + p + PATTERN_SUFFIX).collect(Collectors.toList());

    private static final List<String> DAY_PATTERNS = Stream.of(
            "monday", "mon\\.",
            "tuesday", "tues\\.",
            "wednesday", "wed\\.",
            "thursday", "thurs\\.",
            "friday", "fri\\.",
            "saturday", "sat\\.",
            "sunday", "sun\\."
    ).map(p -> PATTERN_PREFIX + p + PATTERN_SUFFIX).collect(Collectors.toList());

    private static final String YEAR_PATTERN = PATTERN_PREFIX + "[1-2]\\d\\d\\d" + PATTERN_SUFFIX;
    private static final String BC_AD_PATTERN = PATTERN_PREFIX + "(\\d+\\s+(bc|ad)|ad\\s+\\d+)" + PATTERN_SUFFIX;
    private static final String CENTURY_PATTERN = PATTERN_PREFIX + "(1st|2nd|3rd|\\d+th)\\s+century" + PATTERN_SUFFIX;
    private static final String TIME_PATTERN = PATTERN_PREFIX + "([0-1]?\\d|2[0-4])\\s*:\\s*[0-5]\\d" + PATTERN_SUFFIX;

    private static boolean isTemporal(String object) {

        return ((MONTH_PATTERNS.stream().anyMatch(object::matches))
                || (DAY_PATTERNS.stream().anyMatch(object::matches))
                || (object.matches(YEAR_PATTERN))
                || (object.matches(BC_AD_PATTERN))
                || (object.matches(CENTURY_PATTERN))
                || (object.matches(TIME_PATTERN)));
    }

    private static boolean isSpatial(String object) {
        NERString ner = NERStringParser.parse(object);

        return ner.getTokens().stream().anyMatch(t -> t.getCategory().equals("LOCATION"));
    }

    @Override
    public ClassificationResult classify(String object) {

        // TEMPORAL
        if (isTemporal(object)) {
        	return new ClassificationResult(Classification.TEMPORAL);
        }

        // SPATIAL
        if (isSpatial(object)) {
			return new ClassificationResult(Classification.SPATIAL);
        }

		return new ClassificationResult(Classification.UNKNOWN);
    }

}
