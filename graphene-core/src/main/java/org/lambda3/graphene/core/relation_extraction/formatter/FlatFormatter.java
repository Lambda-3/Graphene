package org.lambda3.graphene.core.relation_extraction.formatter;

import org.lambda3.graphene.core.relation_extraction.model.Extraction;
import org.lambda3.text.simplification.discourse.model.Sentence;

public class FlatFormatter extends DefaultFormatter {

	public FlatFormatter() {
		this.headline = "";
		this.relation = "\n%s\t%s\t%d\t%s\t%s\t%s";
		this.context = "\tS:%s(%s)";
		this.resolvedLinked = "\tL:%s(%s||%s||%s)";
		this.linked = "\tL:%s(%s)";
	}

	@Override
	protected String[] writeHeadline(StringBuilder sb, Sentence s) {
		sb.append(String.format(headline, s.original));
		return new String[]{s.original};
	}

	@Override
	protected String[] writeExtraction(StringBuilder sb, Extraction element, int contextLayer, String... params) {
		String sentence = params[0];
		sb.append(String.format(relation, sentence, element.id, contextLayer,
			element.triple.subject, element.triple.property, element.triple.object));
		return EMPTY_ARRAY;
	}
}
