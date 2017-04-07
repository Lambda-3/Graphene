/*
 * ==========================License-Start=============================
 * graphene-core : OpenIERunner
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

package org.lambda3.graphene.core.graph_extraction.runner;

import edu.knowitall.openie.*;
import edu.knowitall.tool.parse.ClearParser;
import edu.knowitall.tool.postag.ClearPostagger;
import edu.knowitall.tool.srl.ClearSrl;
import edu.knowitall.tool.tokenize.ClearTokenizer;
import org.lambda3.graphene.core.graph_extraction.model.Extraction;
import org.lambda3.graphene.core.graph_extraction.model.ExtractionSentence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.Seq;
import scala.collection.convert.WrapAsJava$;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class OpenIERunner {

	private final static Logger LOG = LoggerFactory.getLogger(OpenIERunner.class);

	private final static OpenIE OPENIE = initializeOpenIE();

	private synchronized static OpenIE initializeOpenIE() {
		LOG.debug("Initializing OpenIERunner...");
		long started = System.currentTimeMillis();
		OpenIE openIE =
				new OpenIE(
						new ClearParser(
								new ClearPostagger(
										new ClearTokenizer()
								)
						),
						new ClearSrl(),
						false,
						true
				);
		LOG.debug("OpenIERunner initialized after {} seconds", (System.currentTimeMillis() - started) / 1000);

		return openIE;
	}

	/**
	 * Extracts n-ary relations from the given sentence.
	 *
	 * @param sentence One sentence that is given to OpenIERunner to extract n-ary relations
	 * @return Instance of {@link ExtractionSentence} that contains all extractions from this sentence.
	 */
	public static ExtractionSentence extract(String sentence) {

		List<Extraction> extractions = new ArrayList<>();

		Seq<Instance> rawExtractions = OPENIE.extract(sentence);

		for (Instance instance : convertSeqToList(rawExtractions)) {
			Extraction extraction = new Extraction();

//			extraction.setOriginalSentence(instance.sentence());
			extraction.setSubject(instance.extraction().arg1().text());
			extraction.setPredicate(instance.extraction().rel().text());

			if (instance.extraction().context().isDefined()
					&& !instance.extraction().context().isEmpty()) {
				extraction.setContext(instance.extraction().context().get().text());
			}

			List<String> objects = new ArrayList<>();
			List<String> temporal = new ArrayList<>();
			List<String> spatial = new ArrayList<>();
			List<String> additional = new ArrayList<>();

			for (Argument argument : convertSeqToList(instance.extraction().arg2s())) {
				if (argument instanceof SimpleArgument) {
					objects.add(argument.text());
				} else if (argument instanceof TemporalArgument) {
					temporal.add(argument.text());
				} else if (argument instanceof SpatialArgument) {
					spatial.add(argument.text());
				} else {
					additional.add(argument.text());
				}
			}

			if (objects.size() > 0) {
				// multiple objects, only take first one
				// TODO: check if it makes sense to use all
				extraction.setObject(objects.get(0));
			}

			extraction.getTemporalContexts().addAll(temporal);
			extraction.getSpatialContexts().addAll(spatial);
			extraction.getAdditionalContexts().addAll(additional);

			extractions.add(extraction);

		}

		return new ExtractionSentence(sentence, extractions);
	}

	private static <A> List<A> convertSeqToList(Seq<A> seq) {
		return WrapAsJava$.MODULE$.seqAsJavaList(seq);
	}
}
