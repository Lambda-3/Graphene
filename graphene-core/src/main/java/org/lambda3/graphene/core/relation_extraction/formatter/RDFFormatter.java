package org.lambda3.graphene.core.relation_extraction.formatter;

import org.lambda3.graphene.core.relation_extraction.model.Extraction;
import org.lambda3.graphene.core.relation_extraction.model.Triple;
import org.lambda3.graphene.core.utils.IDGenerator;
import org.lambda3.text.simplification.discourse.model.Element;
import org.lambda3.text.simplification.discourse.model.LinkedContext;
import org.lambda3.text.simplification.discourse.model.Sentence;
import org.lambda3.text.simplification.discourse.model.SimpleContext;

import java.util.List;

import static org.lambda3.graphene.core.utils.RDFHelper.*;

public class RDFFormatter extends DefaultFormatter {

	public RDFFormatter() {
		this.headline = "\n#%s\n\n%s\n";
		this.relation = "\n%s\n%s\n%s\n%s\n%s\n%s\n";
		this.extra = "\n\n%s\n%s\n%s";
	}

	public String[] writeHeadline(StringBuilder sb, Sentence s) {
		String sentenceId = IDGenerator.generateUUID();
		String sentenceBN = blankNode(sentenceId);
		String triple = triple(sentenceBN, sentenceNameSpace("original-text"), rdfLiteral(s.original, null));
		sb.append(String.format(this.headline, s.original, triple));

		return new String[] {sentenceBN};
	}

	public String[] writeExtraction(StringBuilder sb, Extraction element, int intContextLayer, String... params) {
		String sentenceBN = params[0];
		String extractionBN = blankNode(element.id);

		String hasExtraction = triple(sentenceBN, sentenceNameSpace("has-extraction"), extractionBN);

		String extractionType = triple(extractionBN, extractionNameSpace("extraction-type"), rdfLiteral(element.type.name(), null));

		String contextLayer = triple(extractionBN, extractionNameSpace("context-layer"), rdfLiteral(intContextLayer));

		String subject = triple(extractionBN, extractionNameSpace("subject"), textResource(element.triple.subject));
		String predicate = triple(extractionBN, extractionNameSpace("predicate"), textResource(element.triple.property));
		String object = triple(extractionBN, extractionNameSpace("object"), textResource(element.triple.object));

		sb.append(String.format(this.relation, hasExtraction, extractionType, contextLayer, subject, predicate, object));

		return new String[] {extractionBN};
	}

	public void writeSimpleContext(StringBuilder sb, SimpleContext sc, String... params) {
		String extractionBN = params[0];
		String vContextAbbrev = String.format("S-%s", sc.getRelation());

		sb.append("\n");
		sb.append(triple(extractionBN, extractionNameSpace(vContextAbbrev), textResource(sc.getPhraseText())));
	}

	public void writeResolvedLinkedContext(StringBuilder sb, List<Sentence> sentences, LinkedContext lc, String... params) {
		String extractionBN = params[0];
		Element target = getElement(lc.getTargetID(), sentences);

		String targetBN = blankNode(target.id);
		String elementAbbrev = String.format("L-%s", lc.getRelation());

		sb.append("\n");
		sb.append(triple(extractionBN, extractionNameSpace(elementAbbrev), targetBN));
	}

	public void writeLinkedContext(StringBuilder sb, LinkedContext lc) {
		//do nothing
	}

	public void writeExtra(StringBuilder sb, Element element) {

		for (Extraction extraction : element.getListExtension(Extraction.class)) {
			Triple t = extraction.triple;
			String ts = getValueTriple(t.subject);
			String tp = getValueTriple(t.property);
			String to = getValueTriple(t.object);

			sb.append(String.format(this.extra, ts, tp, to));

			for (SimpleContext simpleContext : element.getSimpleContexts()) {
				String tsc = getValueTriple(simpleContext.getPhraseText());
				sb.append(String.format("\n%s", tsc));
			}
			sb.append("\n");
		}
	}

	private String getValueTriple(String content) {
		return triple(
			textResource(content),
			rdfResource("value"),
			rdfLiteral(content, null)
		);
	}

	@Override
	public String format(List<Sentence> Sentences, boolean resolve) {
		return super.format(Sentences, true);
	}
}
