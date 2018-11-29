package org.lambda3.graphene.core.utils;

import org.lambda3.graphene.core.relation_extraction.model.Extraction;
import org.lambda3.graphene.core.relation_extraction.model.Triple;
import org.lambda3.text.simplification.discourse.model.Element;
import org.lambda3.text.simplification.discourse.model.Sentence;
import org.lambda3.text.simplification.discourse.model.SimpleContext;
import org.lambda3.text.simplification.discourse.model.SimplificationContent;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

public class GrapheneStream {

	public static Stream<Triple> triples(SimplificationContent content) {
		return extractions(content).map(e -> e.triple);
	}

	public static Stream<Extraction> extractions(SimplificationContent content) {
		Set<Stream<Extraction>> sl = new LinkedHashSet<>();

		for (Sentence s : content.getSentences()) {
			for (Element e : s.getElements()) {
				sl.add(e.getListExtension(Extraction.class).stream());
				for(SimpleContext sc : e.getSimpleContexts()) {
					sl.add(sc.getListExtension(Extraction.class).stream());
				}
			}
		}

		return Stream.of(sl.toArray(new Stream[0])).flatMap(i -> i);
	}
}

