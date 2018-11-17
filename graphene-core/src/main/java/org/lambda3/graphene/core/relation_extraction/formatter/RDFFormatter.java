package org.lambda3.graphene.core.relation_extraction.formatter;

import org.lambda3.graphene.core.relation_extraction.model.Extraction;
import org.lambda3.graphene.core.utils.IDGenerator;
import org.lambda3.graphene.core.utils.RDFHelper;
import org.lambda3.text.simplification.discourse.model.LinkedContext;
import org.lambda3.text.simplification.discourse.model.OutSentence;
import org.lambda3.text.simplification.discourse.model.SimpleContext;

import java.util.List;

public class RDFFormatter extends DefaultFormatter {

	public RDFFormatter() {
		this.headline = "\n#%s\n\n%s\n";
		this.relation = "\n%s\n%s\n%s\n%s\n%s\n%s\n";
		this.extra = "\n\n%s\n%s\n%s";
	}

	public String[] writeHeadline(StringBuilder sb, OutSentence<Extraction> s) {
		String sentenceId = IDGenerator.generateUUID();
		String sentenceBN = RDFHelper.rdfBlankNode(sentenceId);
		String triple = RDFHelper.rdfTriple(sentenceBN, RDFHelper.grapheneSentenceResource("original-text"), RDFHelper.rdfLiteral(s.getOriginalSentence(), null));
		sb.append(String.format(this.headline, s.getOriginalSentence(), triple));

		return new String[] {sentenceBN};
	}

	public String[] writeElement(StringBuilder sb, Extraction element, String... params) {
		String sentenceBN = params[0];
		String extractionBN = RDFHelper.rdfBlankNode(element.id);

		String hasExtraction = RDFHelper.rdfTriple(sentenceBN, RDFHelper.grapheneSentenceResource("has-extraction"), extractionBN);
		String extractionType = RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource("extraction-type"), RDFHelper.rdfLiteral(element.getType().name(), null));
		String contextLayer = RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource("context-layer"), RDFHelper.rdfLiteral(element.getContextLayer()));
		String subject = RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource("subject"), RDFHelper.grapheneTextResource(element.getTriple().getSubject()));
		String predicate = RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource("predicate"), RDFHelper.grapheneTextResource(element.getTriple().getProperty()));
		String object = RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource("object"), RDFHelper.grapheneTextResource(element.getTriple().getObject()));

		sb.append(String.format(this.relation, hasExtraction, extractionType, contextLayer, subject, predicate, object));

		return new String[] {extractionBN};
	}

	public void writeSimpleContext(StringBuilder sb, SimpleContext sc, String... params) {
		String extractionBN = params[0];
		String vContextAbbrev = String.format("S-%s", sc.getRelation());

		sb.append("\n");
		sb.append(RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource(vContextAbbrev), RDFHelper.grapheneTextResource(sc.getPhraseText())));
	}

	public void writeResolvedLinkedContext(StringBuilder sb, List<OutSentence<Extraction>> sentences, LinkedContext lc, String... params) {
		String extractionBN = params[0];
		Extraction target = getElement(lc.getTargetID(), sentences);
		String targetBN = RDFHelper.rdfBlankNode(target.id);
		String elementAbbrev = String.format("L-%s", lc.getRelation());

		sb.append("\n");
		sb.append(RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource(elementAbbrev), targetBN));
	}

	public void writeLinkedContext(StringBuilder sb, LinkedContext lc) {
		//do nothing
	}

	public void writeExtra(StringBuilder sb, Extraction element) {
		String ts = getValueTriple(element.getTriple().getSubject());
		String tp = getValueTriple(element.getTriple().getProperty());
		String to = getValueTriple(element.getTriple().getObject());

		sb.append(String.format(this.extra, ts, tp, to));

		for (SimpleContext simpleContext : element.getSimpleContexts()) {
			String tsc = getValueTriple(simpleContext.getPhraseText());
			sb.append(String.format("\n%s", tsc));
		}
		sb.append("\n");
	}

	private String getValueTriple(String content) {
		return RDFHelper.rdfTriple(
			RDFHelper.grapheneTextResource(content),
			RDFHelper.rdfResource("value"),
			RDFHelper.rdfLiteral(content, null)
		);
	}
}
