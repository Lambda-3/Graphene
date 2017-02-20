/*
 * ==========================License-Start=============================
 * graphene-core : RDFOutput
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

import org.lambda3.graphene.core.simplified_graph_extraction.rdf_output.model.*;

import java.util.List;

/**
 *
 */
public class RDFOutput {
	private final List<CoreDiscourseType> coreDiscourseTypes;
	private final List<CoreExtraction> coreExtractions;
	private final List<ContextExtraction> contextExtractions;
	private final List<CoreCoreRelation> coreCoreRelations;
	private final List<CoreContextRelation> coreContextRelations;

	public RDFOutput(List<CoreDiscourseType> coreDiscourseTypes, List<CoreExtraction> coreExtractions, List<ContextExtraction> contextExtractions, List<CoreCoreRelation> coreCoreRelations, List<CoreContextRelation> coreContextRelations) {
		this.coreDiscourseTypes = coreDiscourseTypes;
		this.coreExtractions = coreExtractions;
		this.contextExtractions = contextExtractions;
		this.coreCoreRelations = coreCoreRelations;
		this.coreContextRelations = coreContextRelations;
	}

	public List<CoreDiscourseType> getCoreDiscourseTypes() {
		return coreDiscourseTypes;
	}

	public List<CoreExtraction> getCoreExtractions() {
		return coreExtractions;
	}

	public List<ContextExtraction> getContextExtractions() {
		return contextExtractions;
	}

	public List<CoreCoreRelation> getCoreCoreRelations() {
		return coreCoreRelations;
	}

	public List<CoreContextRelation> getCoreContextRelations() {
		return coreContextRelations;
	}
}
