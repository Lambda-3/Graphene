package org.lambda3.graphene.core.relation_extraction.formatter;

import org.lambda3.graphene.core.relation_extraction.model.Extraction;
import org.lambda3.text.simplification.discourse.model.Element;
import org.lambda3.text.simplification.discourse.model.LinkedContext;
import org.lambda3.text.simplification.discourse.model.Sentence;
import org.lambda3.text.simplification.discourse.model.SimpleContext;

import java.util.List;

public class JSONFormatter extends Formatter {

	@Override
	protected String[] writeHeadline(StringBuilder sb, Sentence s) {
		return new String[0];
	}

	@Override
	protected String[] writeExtraction(StringBuilder sb, Extraction element, int contextLayer, String... params) {
		return new String[0];
	}

	@Override
	protected void writeSimpleContext(StringBuilder sb, SimpleContext sc, String... params) {

	}

	@Override
	protected void writeResolvedLinkedContext(StringBuilder sb, List<Sentence> sentences, LinkedContext lc, String... params) {

	}

	@Override
	protected void writeLinkedContext(StringBuilder sb, LinkedContext lc) {

	}

	@Override
	protected void writeExtra(StringBuilder sb, Element element) {

	}
}
