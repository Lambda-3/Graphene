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
import org.lambda3.graphene.core.relation_extraction.RelationExtraction;
import org.lambda3.graphene.core.relation_extraction.model.ExContent;
import org.lambda3.graphene.core.relation_extraction.representation.RepGenerator;
import org.lambda3.graphene.core.relation_extraction.representation.RepStyle;
import org.lambda3.graphene.core.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Graphene {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Config config;

	private final Coreference coreference;
	private final RelationExtraction relationExtraction;

	public Graphene() {
		this(ConfigFactory.load());
	}

	public Graphene(Config config) {

		this.config = config
				.withFallback(ConfigFactory.load("build"))
				.getConfig("graphene");

		this.coreference = new Coreference(this.config.getConfig("coreference"));
		this.relationExtraction = new RelationExtraction();

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

	public ExContent doRelationExtraction(String text, boolean doCoreference) {
		log.debug("doRelationExtraction for text");
		final ExContent ec = relationExtraction.doRelationExtraction(text);
		ec.setCoreferenced(doCoreference);
		log.debug("Relation Extraction for text finished");
		return ec;
	}

	public String getRepresentation(ExContent exContent, RepStyle repStyle, int maxContextDepth) {
		log.debug("generate output representation for exContent");
		return RepGenerator.getRDFRepresentation(exContent, repStyle, maxContextDepth);
	}

    public String getRepresentation(ExContent exContent, RepStyle repStyle) {
        log.debug("generate output representation for exContent");
        return RepGenerator.getRDFRepresentation(exContent, repStyle);
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
