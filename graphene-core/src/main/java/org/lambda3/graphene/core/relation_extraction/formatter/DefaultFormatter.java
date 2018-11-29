package org.lambda3.graphene.core.relation_extraction.formatter;

import org.lambda3.graphene.core.relation_extraction.model.Extraction;
import org.lambda3.graphene.core.relation_extraction.model.Triple;
import org.lambda3.text.simplification.discourse.model.Element;
import org.lambda3.text.simplification.discourse.model.LinkedContext;
import org.lambda3.text.simplification.discourse.model.Sentence;
import org.lambda3.text.simplification.discourse.model.SimpleContext;

import java.util.List;

public class DefaultFormatter extends Formatter {

	public DefaultFormatter() {
		this.headline = "\n#%s\n";
		this.relation = "\n%s\t%d\t%s\t%s\t%s\n";
		this.context = "\tS:%s\t%s\n";
		this.resolvedLinked = "\tL:%s\t%s\t%s\t%s\n";
		this.linked = "\tL:%s\t%s\n";
	}

	@Override
	protected String[] writeHeadline(StringBuilder sb, Sentence s) {
		sb.append(String.format(headline, s.original));
		return EMPTY_ARRAY;
	}

	@Override
	protected String[] writeExtraction(StringBuilder sb, Extraction element, int contextLayer, String... params) {
		sb.append(String.format(relation, element.id, contextLayer,
			element.triple.subject, element.triple.property, element.triple.object));
		return EMPTY_ARRAY;
	}

	@Override
	protected void writeSimpleContext(StringBuilder sb, SimpleContext sc, String... params) {
		sb.append(String.format(context, sc.getRelation(), sc.getAsFullSentence()));
	}

	@Override
	protected void writeResolvedLinkedContext(StringBuilder sb, List<Sentence> sentences, LinkedContext lc, String... params) {
		Element target = getElement(lc.getTargetID(), sentences);

		for (Extraction extraction : target.getListExtension(Extraction.class)) {
			Triple tt = extraction.triple;
			sb.append(String.format(resolvedLinked, lc.getRelation(), tt.subject, tt.property, tt.object));
		}
	}

	@Override
	protected void writeLinkedContext(StringBuilder sb, LinkedContext lc) {
		sb.append(String.format(linked, lc.getRelation(), lc.getTargetID()));
	}

	@Override
	protected void writeExtra(StringBuilder sb, Element element) {

	}
}
