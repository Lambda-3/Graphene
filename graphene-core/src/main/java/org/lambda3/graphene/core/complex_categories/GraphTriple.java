package org.lambda3.graphene.core.complex_categories;

import org.lambda3.graphene.core.relation_extraction.model.AbstractTriple;

import java.util.Objects;

public class GraphTriple extends AbstractTriple {

	private ComplexCategory subject;
	private String property;
	private ComplexCategory object;

	public GraphTriple(ComplexCategory subject, String property, ComplexCategory object) {
		this.subject = subject;
		this.property = property;
		this.object = object;
	}

	@Override
	public String getSubject() {
		return this.subject.toString();
	}

	@Override
	public String getProperty() {
		return property;
	}

	@Override
	public String getObject() {
		return this.object.toString();
	}

	public ComplexCategory getSubjectGraph() {
		return this.subject;
	}

	public ComplexCategory getObjectGraph() {
		return this.object;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GraphTriple that = (GraphTriple) o;
		return Objects.equals(subject, that.subject) &&
			Objects.equals(property, that.property) &&
			Objects.equals(object, that.object);
	}

	@Override
	public int hashCode() {

		return Objects.hash(subject, property, object);
	}
}
