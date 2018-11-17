package org.lambda3.graphene.core.relation_extraction.formatter;

import org.lambda3.graphene.core.relation_extraction.model.Extraction;
import org.lambda3.text.simplification.discourse.model.LinkedContext;
import org.lambda3.text.simplification.discourse.model.OutSentence;
import org.lambda3.text.simplification.discourse.model.SimpleContext;

import java.util.List;

public class JSONFormatter extends Formatter<Extraction> {
	@Override
	protected String[] writeHeadline(StringBuilder sb, OutSentence<Extraction> s) {
		return new String[0];
	}

	@Override
	protected String[] writeElement(StringBuilder sb, Extraction element, String... params) {
		return new String[0];
	}

	@Override
	protected void writeSimpleContext(StringBuilder sb, SimpleContext sc, String... params) {

	}

	@Override
	protected void writeResolvedLinkedContext(StringBuilder sb, List<OutSentence<Extraction>> outSentences, LinkedContext lc, String... params) {

	}

	@Override
	protected void writeLinkedContext(StringBuilder sb, LinkedContext lc) {

	}

	@Override
	protected void writeExtra(StringBuilder sb, Extraction element) {

	}
}
