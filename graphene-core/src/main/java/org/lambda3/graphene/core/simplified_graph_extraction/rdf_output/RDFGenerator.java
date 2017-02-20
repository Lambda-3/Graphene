/*
 * ==========================License-Start=============================
 * graphene-core : RDFGenerator
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

package org.lambda3.graphene.core.simplified_graph_extraction.rdf_output;

import org.lambda3.graphene.core.simplified_graph_extraction.model.ExContextSentence;
import org.lambda3.graphene.core.simplified_graph_extraction.model.ExCoreSentence;
import org.lambda3.graphene.core.simplified_graph_extraction.model.ExSimplificationContent;
import org.lambda3.graphene.core.simplified_graph_extraction.model.ExSimplificationSentence;
import org.lambda3.graphene.core.simplified_graph_extraction.rdf_output.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class RDFGenerator {

	public static String generateOuputStr(ExSimplificationContent exSimplificationContent) {
		StringBuilder strb = new StringBuilder();

		for (ExSimplificationSentence exSimplificationSentence : exSimplificationContent.getSimplifiedSentences()) {
			strb.append("\n");
			strb.append("# original sentence: '").append(exSimplificationSentence.getOriginalSentence()).append("'").append("\n");

			for (ExCoreSentence exCoreSentence : exSimplificationSentence.getCoreSentences()) {
				strb.append("\n");
				strb.append("## core sentence: '").append(exCoreSentence.getText()).append("'").append("\n");

				// core discourse type
				strb.append(new CoreDiscourseType(exCoreSentence.getId(), exCoreSentence.getDiscourseType())).append("\n");

				// core extractions
				exCoreSentence.getExtractions().stream().map(
						e -> new CoreExtraction(exCoreSentence.getId(), e.getSubject(), e.getPredicate(), e.getObject())
				).forEach(e -> strb.append(e.toString()).append("\n"));

				// core relations
				exSimplificationContent.getCoreSentenceRelations(exCoreSentence).stream().map(
						e -> new CoreCoreRelation(exCoreSentence.getId(), e.getTarget().getId(), e.getRelation())
				).forEach(e -> strb.append(e.toString()).append("\n"));

				// contexts
				for (ExContextSentence exContextSentence : exCoreSentence.getContextSentences()) {
					strb.append("### context sentence: '").append(exContextSentence.getText()).append("'").append("\n");

					// context extractions
					exContextSentence.getExtractions().stream().map(
							e -> new ContextExtraction(exContextSentence.getId(), e.getSubject(), e.getPredicate(), e.getObject())
					).forEach(e -> strb.append(e.toString()).append("\n"));

					// context relation
					strb.append(new CoreContextRelation(exCoreSentence.getId(), exContextSentence.getId(), exContextSentence.getRelation()).toString()).append("\n");
				}
			}
		}

		return strb.toString();
	}

	public static RDFOutput generateOutput(ExSimplificationContent exSimplificationContent) {
		List<CoreExtraction> coreExtractions = new ArrayList<>();
		List<CoreDiscourseType> coreDiscourseTypes = new ArrayList<>();
		List<ContextExtraction> contextExtractions = new ArrayList<>();
		List<CoreCoreRelation> coreCoreRelations = new ArrayList<>();
		List<CoreContextRelation> coreContextRelations = new ArrayList<>();

		for (ExSimplificationSentence exSimplificationSentence : exSimplificationContent.getSimplifiedSentences()) {
			for (ExCoreSentence exCoreSentence : exSimplificationSentence.getCoreSentences()) {

				// core discourse type
				coreDiscourseTypes.add(new CoreDiscourseType(exCoreSentence.getId(), exCoreSentence.getDiscourseType()));

				// core extractions
				coreExtractions.addAll(exCoreSentence.getExtractions().stream().map(
						e -> new CoreExtraction(exCoreSentence.getId(), e.getSubject(), e.getPredicate(), e.getObject())
				).collect(Collectors.toList()));

				// core relations
				coreCoreRelations.addAll(exSimplificationContent.getCoreSentenceRelations(exCoreSentence).stream().map(
						e -> new CoreCoreRelation(exCoreSentence.getId(), e.getTarget().getId(), e.getRelation())
				).collect(Collectors.toList()));

				// contexts
				for (ExContextSentence exContextSentence : exCoreSentence.getContextSentences()) {

					// context extractions
					contextExtractions.addAll(exContextSentence.getExtractions().stream().map(
							e -> new ContextExtraction(exContextSentence.getId(), e.getSubject(), e.getPredicate(), e.getObject())
					).collect(Collectors.toList()));

					// context relation
					coreContextRelations.add(new CoreContextRelation(exCoreSentence.getId(), exContextSentence.getId(), exContextSentence.getRelation()));
				}
			}
		}

		return new RDFOutput(coreDiscourseTypes, coreExtractions, contextExtractions, coreCoreRelations, coreContextRelations);
	}
}
