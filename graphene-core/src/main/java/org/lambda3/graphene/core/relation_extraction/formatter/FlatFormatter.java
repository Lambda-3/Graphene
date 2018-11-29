package org.lambda3.graphene.core.relation_extraction.formatter;

public class FlatFormatter extends DefaultFormatter {

	public FlatFormatter() {
		this.headline = "";
		this.relation = "%s\t%d\t%s\t%s\t%s";
		this.context = "\tS:%s(%s)";
		this.resolvedLinked = "\tL:%s(%s||%s||%s)";
		this.linked = "\tL:%s(%s)";
	}
}
