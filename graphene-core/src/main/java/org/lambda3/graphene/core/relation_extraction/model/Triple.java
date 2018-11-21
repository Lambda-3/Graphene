package org.lambda3.graphene.core.relation_extraction.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.lambda3.text.simplification.discourse.model.Extensible;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Triple extends Extensible {
	public final String subject;
	public final String property;
	public final String object;

	private Triple() {
		//for deserialization
		this.subject = null;
		this.property = null;
		this.object = null;
	}

	public Triple(String subject, String property, String object) {
		this.subject = subject;
		this.property = property;
		this.object = object;
	}

	public static Triple get(String subject, String property, String object) {
		return new Triple(subject, property, object);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Triple that = (Triple) o;
		return Objects.equals(subject, that.subject) &&
			Objects.equals(property, that.property) &&
			Objects.equals(object, that.object);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), subject, property, object);
	}

	@Override
	public String toString() {
		return String.format("%s\t%s\t%s", subject, property, object);
	}
}
