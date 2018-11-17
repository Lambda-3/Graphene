package org.lambda3.graphene.core.relation_extraction.formatter;

import org.lambda3.text.simplification.discourse.AbstractElement;
import org.lambda3.text.simplification.discourse.model.LinkedContext;
import org.lambda3.text.simplification.discourse.model.OutSentence;
import org.lambda3.text.simplification.discourse.model.SimpleContext;

import java.util.List;

public abstract class Formatter<E extends AbstractElement> {

	protected String headline;
	protected String relation;
	protected String context;
	protected String resolvedLinked;
	protected String linked;
	protected String extra;

	static final String[] EMPTY_ARRAY = new String[0];
	protected abstract String[] writeHeadline(StringBuilder sb, OutSentence<E> s);
	protected abstract String[] writeElement(StringBuilder sb, E element, String... params);
	protected abstract void writeSimpleContext(StringBuilder sb, SimpleContext sc, String... params);
	protected abstract void writeResolvedLinkedContext(StringBuilder sb,
													   List<OutSentence<E>> sentences,
													   LinkedContext lc, String... params);
	protected abstract void writeLinkedContext(StringBuilder sb, LinkedContext lc);
	protected abstract void writeExtra(StringBuilder sb, E element);

	public String format(List<OutSentence<E>> sentences, boolean resolve) {
		StringBuilder sb = new StringBuilder();

		for (OutSentence<E> sentence : sentences) {
			String[] hp = writeHeadline(sb, sentence);
			for (E element : sentence.getElements()) {
				String[] ep = writeElement(sb, element, hp);

				for (SimpleContext sc : element.getSimpleContexts()) {
					writeSimpleContext(sb, sc, ep);
				}
				for (LinkedContext lc : element.getLinkedContexts()) {
					if (resolve) {
						writeResolvedLinkedContext(sb, sentences, lc, ep);
					} else {
						writeLinkedContext(sb, lc);
					}
				}

				writeExtra(sb, element);
			}
		}

		return sb.toString();
	}

	E getElement(String id, List<OutSentence<E>> sentences) {
		for (OutSentence<E> sentence : sentences) {
			E e = sentence.getElement(id);
			if (e != null) {
				return e;
			}
		}

		return null;
	}
}
