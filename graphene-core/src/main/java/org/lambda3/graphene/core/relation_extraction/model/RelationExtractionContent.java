package org.lambda3.graphene.core.relation_extraction.model;

/*-
 * ==========================License-Start=============================
 * RelationExtractionContent.java - Graphene Core - Lambda^3 - 2017
 * Graphene
 * %%
 * Copyright (C) 2017 Lambda^3
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * ==========================License-End===============================
 */


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.lambda3.graphene.core.utils.IDGenerator;
import org.lambda3.graphene.core.utils.RDFHelper;
import org.lambda3.text.simplification.discourse.model.Content;
import org.lambda3.text.simplification.discourse.model.LinkedContext;
import org.lambda3.text.simplification.discourse.model.OutSentence;
import org.lambda3.text.simplification.discourse.model.SimpleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RelationExtractionContent extends Content {
	private boolean coreferenced;
	private List<OutSentence<Extraction>> sentences;

	// for deserialization
	public RelationExtractionContent() {
		this.coreferenced = false;
		this.sentences = new ArrayList<>();
	}

	public void setCoreferenced(boolean coreferenced) {
		this.coreferenced = coreferenced;
	}

	public void addSentence(OutSentence<Extraction> sentence) {
		this.sentences.add(sentence);
	}

	public Optional<String> containsExtraction(Extraction extraction) {
		return sentences.get(extraction.getSentenceIdx()).containsElement(extraction);
	}

	public void addExtraction(Extraction extraction) {
		sentences.get(extraction.getSentenceIdx()).addElement(extraction);
	}

	public boolean isCoreferenced() {
		return coreferenced;
	}

	public List<OutSentence<Extraction>> getSentences() {
		return sentences;
	}

	public Extraction getExtraction(String id) {
		for (OutSentence<Extraction> sentence : sentences) {
			Extraction e = sentence.getElement(id);
			if (e != null) {
				return e;
			}
		}

		return null;
	}

	public List<Extraction> getExtractions() {
		List<Extraction> res = new ArrayList<>();
		sentences.forEach(s -> res.addAll(s.getElements()));

		return res;
	}

	public String defaultFormat(boolean resolve) {
		StringBuilder strb = new StringBuilder();
		for (OutSentence<Extraction> sentence : getSentences()) {
			strb.append("\n# " + sentence.getOriginalSentence() + "\n");
			for (Extraction extraction : sentence.getElements()) {
				strb.append("\n" + extraction.id + "\t" + extraction.getContextLayer() + "\t" + extraction.getTriple() + "\n");
				for (SimpleContext simpleContext : extraction.getSimpleContexts()) {
					strb.append("\t" + "S:" + simpleContext.getRelation() + "\t" + simpleContext.getPhraseText() + "\n");
				}
				for (LinkedContext linkedContext : extraction.getLinkedContexts()) {
					if (resolve) {
						Extraction target = getExtraction(linkedContext.getTargetID());
						strb.append("\t" + "L:" + linkedContext.getRelation() + "\t" + target.getTriple() + "\n");
					} else {
						strb.append("\t" + "L:" + linkedContext.getRelation() + "\t" + linkedContext.getTargetID() + "\n");
					}
				}
			}
		}

		return strb.toString();
	}

	public String flatFormat(boolean resolve) {
		final String separator = "||";

		StringBuilder strb = new StringBuilder();
		for (OutSentence<Extraction> sentence : getSentences()) {
			for (Extraction extraction : sentence.getElements()) {
				strb.append(sentence.getOriginalSentence() + "\t" + extraction.id + "\t" + extraction.getContextLayer() + "\t" + extraction.getTriple() );
				for (SimpleContext simpleContext : extraction.getSimpleContexts()) {
					strb.append("\t" + "S:" + simpleContext.getRelation() + "(" + simpleContext.getPhraseText() + ")");
				}
				for (LinkedContext linkedContext : extraction.getLinkedContexts()) {
					if (resolve) {
						Extraction target = getExtraction(linkedContext.getTargetID());
						strb.append("\t" + "L:" + linkedContext.getRelation() + "(" + target.getTriple().getSubject() + separator + target.getTriple().getProperty() + separator + target.getTriple().getObject() + ")");
					} else {
						strb.append("\t" + "L:" + linkedContext.getRelation() + "(" + linkedContext.getTargetID() + ")");
					}
				}
				strb.append("\n");
			}
		}

		return strb.toString();
	}

	public String rdfFormat() {
		StringBuilder strb = new StringBuilder();
		for (OutSentence<Extraction> sentence : getSentences()) {
			strb.append("\n# " + sentence.getOriginalSentence() + "\n");
			String sentenceId = IDGenerator.generateUUID();
			String sentenceBN = RDFHelper.blankNode(sentenceId);
			strb.append("\n" + RDFHelper.triple(sentenceBN, RDFHelper.sentenceNameSpace("original-text"), RDFHelper.rdfLiteral(sentence.getOriginalSentence(), null)) + "\n");
			for (Extraction extraction : sentence.getElements()) {
				String extractionBN = RDFHelper.blankNode(extraction.id);
				strb.append("\n" + RDFHelper.triple(sentenceBN, RDFHelper.sentenceNameSpace("has-extraction"), extractionBN));
				strb.append("\n" + RDFHelper.triple(extractionBN, RDFHelper.extractionNameSpace("extraction-type"), RDFHelper.rdfLiteral(extraction.getType().name(), null)));
				strb.append("\n" + RDFHelper.triple(extractionBN, RDFHelper.extractionNameSpace("context-layer"), RDFHelper.rdfLiteral(extraction.getContextLayer())));
				strb.append("\n" + RDFHelper.triple(extractionBN, RDFHelper.extractionNameSpace("subject"), RDFHelper.textResource(extraction.getTriple().getSubject())));
				strb.append("\n" + RDFHelper.triple(extractionBN, RDFHelper.extractionNameSpace("predicate"), RDFHelper.textResource(extraction.getTriple().getProperty())));
				strb.append("\n" + RDFHelper.triple(extractionBN, RDFHelper.extractionNameSpace("object"), RDFHelper.textResource(extraction.getTriple().getObject())));
				strb.append("\n");

				for (SimpleContext simpleContext : extraction.getSimpleContexts()) {
					String vContextAbbrev = "S-" + simpleContext.getRelation();
					strb.append("\n" + RDFHelper.triple(extractionBN, RDFHelper.extractionNameSpace(vContextAbbrev), RDFHelper.textResource(simpleContext.getPhraseText())));
				}
				for (LinkedContext linkedContext : extraction.getLinkedContexts()) {
					Extraction target = getExtraction(linkedContext.getTargetID());
					String targetBN =  RDFHelper.blankNode(target.id);
					String elementAbbrev = "L-" + linkedContext.getRelation();
					strb.append("\n" + RDFHelper.triple(extractionBN, RDFHelper.extractionNameSpace(elementAbbrev), targetBN));
				}
				strb.append("\n");

				// Values
				strb.append("\n" + RDFHelper.triple(RDFHelper.textResource(extraction.getTriple().getSubject()), RDFHelper.rdfResource("value"), RDFHelper.rdfLiteral(extraction.getTriple().getSubject(), null)));
				strb.append("\n" + RDFHelper.triple(RDFHelper.textResource(extraction.getTriple().getProperty()), RDFHelper.rdfResource("value"), RDFHelper.rdfLiteral(extraction.getTriple().getProperty(), null)));
				strb.append("\n" + RDFHelper.triple(RDFHelper.textResource(extraction.getTriple().getObject()), RDFHelper.rdfResource("value"), RDFHelper.rdfLiteral(extraction.getTriple().getObject(), null)));

				for (SimpleContext simpleContext : extraction.getSimpleContexts()) {
					strb.append("\n" + RDFHelper.triple(RDFHelper.textResource(simpleContext.getPhraseText()), RDFHelper.rdfResource("value"), RDFHelper.rdfLiteral(simpleContext.getPhraseText(), null)));
				}
				strb.append("\n");
			}
		}

		return strb.toString();
	}

    @Override
    public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof RelationExtractionContent)) return false;

		RelationExtractionContent that = (RelationExtractionContent) o;

		return new EqualsBuilder()
                .append(isCoreferenced(), that.isCoreferenced())
                .append(getSentences(), that.getSentences())
                .isEquals();
    }

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
                .append(isCoreferenced())
                .append(getSentences())
                .toHashCode();
    }

    @Override
    public String toString() {
        return "RelationExtractionContent{" +
                "coreferenced=" + coreferenced +
                ", sentences=" + sentences +
                '}';
    }
}
