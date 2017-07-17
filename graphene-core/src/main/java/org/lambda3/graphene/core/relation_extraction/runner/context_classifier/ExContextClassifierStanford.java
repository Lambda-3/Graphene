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


import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.time.SUTime;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.util.CoreMap;
import org.lambda3.graphene.core.relation_extraction.model.Classification;
import org.lambda3.graphene.core.relation_extraction.model.ClassificationResult;
import org.lambda3.graphene.core.relation_extraction.model.TimeInformation;
import org.lambda3.graphene.core.relation_extraction.runner.ExContextClassifier;
import org.lambda3.text.simplification.discourse.utils.ner.NERString;
import org.lambda3.text.simplification.discourse.utils.ner.NERStringParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ExContextClassifierStanford implements ExContextClassifier {
    private static final Logger LOG = LoggerFactory.getLogger(ExContextClassifierStanford.class);

    private static final Properties PROPS = new java.util.Properties();
	private static final AnnotationPipeline PIPELINE = new AnnotationPipeline();
	static {
		PIPELINE.addAnnotator(new TokenizerAnnotator(false));
		PIPELINE.addAnnotator(new TimeAnnotator("sutime", PROPS));
	}

    private static Optional<ClassificationResult> isTemporal(String object) {
		Annotation annotation = new Annotation(object);
//            annotation.set(CoreAnnotations.DocDateAnnotation.class, "2013-07-14"); // not yet supported by Graphene
		PIPELINE.annotate(annotation);
		List<CoreMap> timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);

		// only fetch first occurrence
		if (timexAnnsAll.size() > 0) {
			CoreMap cm = timexAnnsAll.get(0);
			SUTime.Temporal temporal = cm.get(TimeExpression.Annotation.class).getTemporal();

			Classification classification = Classification.TEMPORAL;
			switch (temporal.getTimexType()) {
				case TIME:
					classification = Classification.TEMPORAL_TIME;
					break;
				case DURATION:
					classification = Classification.TEMPORAL_DURATION;
				break;
				case DATE:
					classification = Classification.TEMPORAL_DATE;
					break;
				case SET:
					classification = Classification.TEMPORAL_SET;
					break;
			}

			ClassificationResult cr = new ClassificationResult(classification);
			cr.setTimeInformation(new TimeInformation(temporal.getTimexValue()));

			return Optional.of(cr);
		}

		return Optional.empty();
    }

    private static boolean isSpatial(String object) {
        NERString ner = NERStringParser.parse(object);

        return ner.getTokens().stream().anyMatch(t -> t.getCategory().equals("LOCATION"));
    }

    @Override
    public ClassificationResult classify(String object) {

        // TEMPORAL
		Optional<ClassificationResult> cr = isTemporal(object);
        if (cr.isPresent()) {
        	return cr.get();
        }

        // SPATIAL
        if (isSpatial(object)) {
			return new ClassificationResult(Classification.SPATIAL);
        }

		return new ClassificationResult(Classification.UNKNOWN);
    }

}
