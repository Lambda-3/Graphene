package org.lambda3.graphene.core.relation_extraction.formatter;

import edu.stanford.nlp.naturalli.OpenIE;
import org.lambda3.graphene.core.relation_extraction.model.Extraction;

import java.util.HashMap;
import java.util.Map;

public class FormatterFactory {

	public enum OutputFormat {
		DEFAULT,
		DEFAULT_RESOLVED,
		FLAT,
		FLAT_RESOLVED,
		RDF,
		JSON,
		SERIALIZED
	}

	private static Map<OutputFormat, Formatter> instances = new HashMap<>();

	static {
		instances.put(OutputFormat.DEFAULT, new DefaultFormatter());
		instances.put(OutputFormat.DEFAULT_RESOLVED, instances.get(OutputFormat.DEFAULT));
		instances.put(OutputFormat.FLAT, new FlatFormatter());
		instances.put(OutputFormat.FLAT_RESOLVED, instances.get(OutputFormat.FLAT));
		instances.put(OutputFormat.RDF, new RDFFormatter());
		instances.put(OutputFormat.JSON, new JSONFormatter());
	}

	public static Formatter get(OutputFormat name) {
		return instances.get(name);
	}
}
