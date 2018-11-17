package org.lambda3.graphene.core.relation_extraction.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.lambda3.graphene.core.relation_extraction.complex_categories.GraphTriple;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
	@JsonSubTypes.Type(value = DefaultTriple.class, name = "DefaultTriple"),
	@JsonSubTypes.Type(value = GraphTriple.class, name = "GraphTriple") }
)
public abstract class AbstractTriple {

	public abstract String getSubject();
	public abstract String getProperty();
	public abstract String getObject();

	@Override
	public String toString() {
		return String.format("%s\t%s\t%s", getSubject(), getProperty(), getObject());
	}
}
