package org.lambda3.graphene.core.relation_extraction.model;

public abstract class AbstractTriple {

	public abstract String getSubject();
	public abstract String getProperty();
	public abstract String getObject();

	@Override
	public String toString() {
		return String.format("%s\t%s\t%s", getSubject(), getProperty(), getObject());
	}
}
