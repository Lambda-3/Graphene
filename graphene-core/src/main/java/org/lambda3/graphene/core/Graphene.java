/*
 * ==========================License-Start=============================
 * graphene-core : Graphene
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

package org.lambda3.graphene.core;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.lambda3.graphene.core.coreference.Coreference;
import org.lambda3.graphene.core.coreference.model.CoreferenceContent;
import org.lambda3.graphene.core.coreference.model.Link;
import org.lambda3.graphene.core.graph_extraction.GraphExtraction;
import org.lambda3.graphene.core.graph_extraction.model.ExtractionContent;
import org.lambda3.graphene.core.simplified_graph_extraction.SimplifiedGraphExtraction;
import org.lambda3.graphene.core.simplified_graph_extraction.model.ExContent;
import org.lambda3.graphene.core.simplified_graph_extraction.rdf_output.RDFGenerator;
import org.lambda3.graphene.core.simplified_graph_extraction.rdf_output.RDFStyle;
import org.lambda3.graphene.core.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Graphene {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Config config;

	private final Coreference coreference;
	private final GraphExtraction graphExtraction;
	private final SimplifiedGraphExtraction simplifiedGraphExtraction;

	public Graphene() {
		this(ConfigFactory.load());
	}

	public Graphene(Config config) {

		this.config = config
				.withFallback(ConfigFactory.load("build"))
				.getConfig("graphene");

		this.coreference = new Coreference(this.config.getConfig("coreference"));
		this.graphExtraction = new GraphExtraction();
		this.simplifiedGraphExtraction = new SimplifiedGraphExtraction();

		log.info("Graphene initialized");
		log.info("\n{}", ConfigUtils.prettyPrint(this.config));
	}

	public CoreferenceContent doCoreference(String text) {
		log.debug("doCoreference for text");
		final CoreferenceContent content = coreference.substituteCoreferences(text);
		log.debug("Coreference for text finished");
		return content;
	}

	public CoreferenceContent doCoreference(String text, String uri) {
		log.debug("doCoreference for text with uri");
		final CoreferenceContent content = coreference.substituteCoreferences(text, uri);
		log.debug("Coreference for text with uri finished");
		return content;
	}

	public CoreferenceContent doCoreference(String text, String uri, List<Link> links) {
		log.debug("doCoreference for text with uri and links");
		final CoreferenceContent content = coreference.substituteCoreferences(text, uri, links);
		log.debug("Coreference for text with uri and links finished");
		return content;
	}

	public ExtractionContent doGraphExtraction(String text, boolean doCoreference) {
		log.debug("doGraphExtraction for text");

		if (doCoreference) {
			CoreferenceContent cc = doCoreference(text);
			text = cc.getSubstitutedText();
		}

		ExtractionContent ec = graphExtraction.extract(text);
		ec.setCoreferenced(doCoreference);

		log.debug("Extraction for text finished");
		return ec;
	}

	public ExContent doSimplificationAndGraphExtraction(String text, boolean doCoreference) {
		log.debug("doSimplificationAndGraphExtraction for text");

		ExContent ec = simplifiedGraphExtraction.doExtraction(text);
		ec.setCoreferenced(doCoreference);

		log.debug("Extraction and Simplification for text finished");

		return ec;
	}

	public String getRDFRepresentation(ExContent exContent, RDFStyle rdfStyle) {
		log.debug("generate RDF as string for exContent");

		return RDFGenerator.getRDFRepresentation(exContent, rdfStyle);
	}


	public VersionInfo getVersionInfo() {
		if (log.isDebugEnabled()) {
			log.debug("getVersionInfo");
		}
		return new VersionInfo(
				config.getString("version.name"),
				config.getString("version.version"),
				config.getString("version.build-number")
		);
	}

}
