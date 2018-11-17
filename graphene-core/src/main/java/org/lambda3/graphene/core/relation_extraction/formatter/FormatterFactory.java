package org.lambda3.graphene.core.relation_extraction.formatter;

import org.lambda3.graphene.core.relation_extraction.model.Extraction;

import java.util.HashMap;
import java.util.Map;

public class FormatterFactory {

	private static Map<String, Formatter<Extraction>> instances = new HashMap<>();

	static {
		instances.put("default", new DefaultFormatter());
		instances.put("flat", new FlatFormatter());
		instances.put("rdf", new RDFFormatter());
		instances.put("json", new JSONFormatter());
	}

	public static Formatter<Extraction> get(String name) {
		return instances.get(name);
	}
}
