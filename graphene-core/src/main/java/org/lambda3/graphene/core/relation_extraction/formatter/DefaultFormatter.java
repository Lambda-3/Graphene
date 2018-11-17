package org.lambda3.graphene.core.relation_extraction.formatter;

import org.lambda3.graphene.core.relation_extraction.model.AbstractTriple;
import org.lambda3.graphene.core.relation_extraction.model.Extraction;
import org.lambda3.text.simplification.discourse.model.LinkedContext;
import org.lambda3.text.simplification.discourse.model.OutSentence;
import org.lambda3.text.simplification.discourse.model.SimpleContext;

import java.util.List;

public class DefaultFormatter extends Formatter<Extraction> {

	public DefaultFormatter() {
		this.headline = "\n#%s\n";
		this.relation = "\n%s\t%d\t%s\t%s\t%s\n";
		this.context = "\tS:%s\t%s\n";
		this.resolvedLinked = "\tL:%s\t%s\t%s\t%s\n";
		this.linked = "\tL:%s\t%s\n";
	}

	@Override
	protected String[] writeHeadline(StringBuilder sb, OutSentence<Extraction> s) {
		sb.append(String.format(headline, s.getOriginalSentence()));
		return EMPTY_ARRAY;
	}

	@Override
	protected String[] writeElement(StringBuilder sb, Extraction element, String... params) {
		AbstractTriple t = element.getTriple();
		sb.append(String.format(relation, element.id, element.getContextLayer(),
			t.getSubject(), t.getProperty(), t.getObject()));
		return EMPTY_ARRAY;
	}

	@Override
	protected void writeSimpleContext(StringBuilder sb, SimpleContext sc, String... params) {
		sb.append(String.format(context, sc.getRelation(), sc.getPhraseText()));
	}

	@Override
	protected void writeResolvedLinkedContext(StringBuilder sb, List<OutSentence<Extraction>> sentences, LinkedContext lc, String... params) {
		Extraction target = getElement(lc.getTargetID(), sentences);
		AbstractTriple tt = target.getTriple();
		sb.append(String.format(resolvedLinked, lc.getRelation(), tt.getSubject(), tt.getProperty(), tt.getObject()));
	}

	@Override
	protected void writeLinkedContext(StringBuilder sb, LinkedContext lc) {
		sb.append(String.format(linked, lc.getRelation(), lc.getTargetID()));
	}

	@Override
	protected void writeExtra(StringBuilder sb, Extraction element) {
		//do nothing
	}
}
