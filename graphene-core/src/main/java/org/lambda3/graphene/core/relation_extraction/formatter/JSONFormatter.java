package org.lambda3.graphene.core.relation_extraction.formatter;

import com.google.gson.Gson;
import org.lambda3.graphene.core.relation_extraction.complex_categories.ComplexCategory;
import org.lambda3.graphene.core.relation_extraction.complex_categories.ComplexCategoryExtractor;
import org.lambda3.graphene.core.relation_extraction.model.Extraction;
import org.lambda3.graphene.core.relation_extraction.model.Triple;
import org.lambda3.text.simplification.discourse.model.Sentence;

import java.util.List;

public class JSONFormatter extends DefaultFormatter {

	private Gson gson = new Gson();

	public JSONFormatter() {
		this.headline = "    \"headline\": \"%s\"";
		this.relation = "        {\"id\": \"%s\", \"contextLayer\": %d, \"s\": \"%s\", \"p\": \"%s\", \"o\": \"%s\"}\n";
		this.context = "        {\"relation\": \"%s\", \"text\": \"%s\"}\n";
		this.resolvedLinked = "{\"relation\": \"%s\", \"s\": \"%s\", \"p\": \"%s\", \"o\": \"%s\"}\n";
		this.linked = "\tL:%s\t%s\n";

		this.openRelation = ",\n    \"relations\": {\n";
		this.closeRelation = "    }";

		this.openContext = ",\n    \"contexts\": {\n";
		this.closeContext = this.closeRelation;

		this.openLinkedContext = ",\n    \"linkedContexts\": {\n";
		this.closeLinkedContext = this.closeRelation;
		this.separator = ", ";
	}

	@Override
	protected String[] writeExtraction(StringBuilder sb, Extraction element, int contextLayer, String... params) {
		Triple triple = element.triple;

		ComplexCategory scc = triple.getExtension(ComplexCategory.class, ComplexCategoryExtractor.SUBJECT);
		ComplexCategory occ = triple.getExtension(ComplexCategory.class, ComplexCategoryExtractor.OBJECT);

		String ss = scc != null ? gson.toJson(scc) : triple.subject;
		String os = occ != null ? gson.toJson(scc) : triple.object;

		sb.append(String.format(relation, element.id, contextLayer, ss, element.triple.property, os));
		return EMPTY_ARRAY;
	}

	@Override
	public String format(List<Sentence> sentences, boolean resolve) {
		return String.format("{\n%s\n}", super.format(sentences, resolve));
	}

}
