package org.lambda3.graphene.core.relation_extraction.model;

import java.util.Objects;

public class DefaultTriple extends AbstractTriple {
	private String subject;
	private String property;
	private String object;

	private DefaultTriple() {
		//for deserialization
	}

	public DefaultTriple(String subject, String property, String object) {
		this.subject = subject;
		this.property = property;
		this.object = object;
	}

	@Override
	public String getSubject() {
		return subject;
	}

	@Override
	public String getProperty() {
		return property;
	}

	@Override
	public String getObject() {
		return object;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DefaultTriple that = (DefaultTriple) o;
		return Objects.equals(subject, that.subject) &&
			Objects.equals(property, that.property) &&
			Objects.equals(object, that.object);
	}

	@Override
	public int hashCode() {
		return Objects.hash(subject, property, object);
	}
}
