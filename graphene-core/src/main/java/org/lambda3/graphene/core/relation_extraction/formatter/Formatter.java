package org.lambda3.graphene.core.relation_extraction.formatter;

import org.lambda3.text.simplification.discourse.AbstractElement;

public interface Formatter<E extends AbstractElement> {

	String format(E element);
}
