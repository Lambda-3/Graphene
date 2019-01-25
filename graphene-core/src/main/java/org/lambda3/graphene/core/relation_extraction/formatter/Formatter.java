package org.lambda3.graphene.core.relation_extraction.formatter;

import org.lambda3.graphene.core.relation_extraction.model.Extraction;
import org.lambda3.text.simplification.discourse.model.Element;
import org.lambda3.text.simplification.discourse.model.LinkedContext;
import org.lambda3.text.simplification.discourse.model.Sentence;
import org.lambda3.text.simplification.discourse.model.SimpleContext;

import java.util.List;

public abstract class Formatter {

	protected String headline;
	protected String relation;
	protected String context;
	protected String resolvedLinked;
	protected String linked;
	protected String extra;

	protected String openRelation = "";
	protected String closeRelation = "";

	protected String openContext = "";
	protected String closeContext = "";

	protected String openLinkedContext = "";
	protected String closeLinkedContext = "";
	protected String separator = "";

	static final String[] EMPTY_ARRAY = new String[0];

	protected abstract String[] writeHeadline(StringBuilder sb, Sentence s);

	protected abstract String[] writeExtraction(StringBuilder sb, Extraction element, int contextLayer, String... params);

	protected abstract void writeSimpleContext(StringBuilder sb, SimpleContext sc, String... params);

	protected abstract void writeResolvedLinkedContext(StringBuilder sb,
													   List<Sentence> sentences,
													   LinkedContext lc, String... params);

	protected abstract void writeLinkedContext(StringBuilder sb, LinkedContext lc);

	protected abstract void writeExtra(StringBuilder sb, Element element);

	public String format(List<Sentence> sentences, boolean resolve) {
		StringBuilder sb = new StringBuilder();

		for (Sentence sentence : sentences) {
			String[] hp = writeHeadline(sb, sentence);
			for (Element element : sentence.getElements()) {
				String[] ep = null;
				if (!element.getListExtension(Extraction.class).isEmpty() || !element.getSimpleContexts().isEmpty()) {
					sb.append(openRelation);
				}
				for (Extraction extraction : element.getListExtension(Extraction.class)) {
					ep = writeExtraction(sb, extraction, element.getContextLayer(), hp);
					sb.append(separator);
				}

				for (SimpleContext sc : element.getSimpleContexts()) {
					for (Extraction extraction : sc.getListExtension(Extraction.class)) {
						ep = writeExtraction(sb, extraction, element.getContextLayer() + 1, hp);
						sb.append(separator);
					}
				}

				if (!element.getListExtension(Extraction.class).isEmpty() || !element.getSimpleContexts().isEmpty()) {
					sb.setLength(sb.length() - separator.length());
					sb.append(closeRelation);
				}

				if (!element.getSimpleContexts().isEmpty()) {
					sb.append(openContext);
					for (SimpleContext sc : element.getSimpleContexts()) {
						writeSimpleContext(sb, sc, ep);
						sb.append(separator);
					}
					sb.setLength(sb.length() - separator.length());
					sb.append(closeContext);
				}

				if (!element.getLinkedContexts().isEmpty()) {
					sb.append(openLinkedContext);
					for (LinkedContext lc : element.getLinkedContexts()) {
						if (resolve) {
							writeResolvedLinkedContext(sb, sentences, lc, ep);
						} else {
							writeLinkedContext(sb, lc);
						}
						sb.append(separator);
					}
					sb.setLength(sb.length() - separator.length());
					sb.append(closeLinkedContext);
				}
				writeExtra(sb, element);
			}
		}

		return sb.toString();
	}

	Element getElement(String id, List<Sentence> sentences) {
		for (Sentence sentence : sentences) {
			Element e = sentence.getElement(id);
			if (e != null) {
				return e;
			}
		}

		return null;
	}
}
