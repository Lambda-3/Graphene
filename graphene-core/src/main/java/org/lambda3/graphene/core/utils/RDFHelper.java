package org.lambda3.graphene.core.utils;

/*-
 * ==========================License-Start=============================
 * RDFHelper.java - Graphene Core - Lambda^3 - 2017
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


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RDFHelper {

	// official
	private static final String RDF_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String RDFS_NAMESPACE = "http://www.w3.org/2000/01/rdf-schema#";
	private static final String XML_NAMESPACE = "http://www.w3.org/2001/XMLSchema#";

	// graphene
	private static final String GRAPHENE_SENTENCE_NAMESPACE = "http://lambda3.org/graphene/sentenceNameSpace#";
	private static final String GRAPHENE_EXTRACTION_NAMESPACE = "http://lambda3.org/graphene/extraction#";
	private static final String GRAPHENE_TEXT_NAMESPACE = "http://lambda3.org/graphene/text#";

	public static String rdfLiteral(int number) {
		return "\"" + String.valueOf(number) + "\"" + "^^" + "<" + XML_NAMESPACE + "integer" + ">";
	}

	public static String rdfLiteral(String text, String languageTag) {
		String escapedText = text.replace("\"", "\\\"").replace("\n", "\\\n").replace("\r", "\\\r").replace("\\", "");
		String langStr = (languageTag != null) ? "@" + languageTag : "";

		return "\"" + escapedText + "\"" + langStr + "^^" + "<" + XML_NAMESPACE + "string" + ">";
	}

	public static String rdfResource(String text) {
		return "<" + RDF_NAMESPACE + text + ">";
	}

	public static String rdfsResource(String text) {
		return "<" + RDFS_NAMESPACE + text + ">";
	}

	public static String sentenceNameSpace(String text) {
		return "<" + GRAPHENE_SENTENCE_NAMESPACE + text + ">";
	}

	public static String extractionNameSpace(String text) {
		return "<" + GRAPHENE_EXTRACTION_NAMESPACE + text + ">";
	}

	public static String textResource(String text) {
		String escapedText;
		try {
			escapedText = URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError("Unsupported encoding.");
		}

		return "<" + GRAPHENE_TEXT_NAMESPACE + escapedText + ">";
	}

	public static String blankNode(String id) {
		return "_:" + id;
	}

	public static String triple(String subject, String predicate, String object) {
		return subject + " " + predicate + " " + object + " .";
	}

}
