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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RelationExtractionContent extends Content {
	private boolean coreferenced;
	private List<ExSentence> sentences;

	// for deserialization
	public RelationExtractionContent() {
		this.coreferenced = false;
		this.sentences = new ArrayList<>();
	}

	public void setCoreferenced(boolean coreferenced) {
		this.coreferenced = coreferenced;
	}

	public void addSentence(ExSentence sentence) {
		this.sentences.add(sentence);
	}

	public Optional<String> containsExtraction(Extraction extraction) {
		return sentences.get(extraction.getSentenceIdx()).containsExtraction(extraction);
	}

	public void addExtraction(Extraction extraction) {
		sentences.get(extraction.getSentenceIdx()).addExtraction(extraction);
	}

	public boolean isCoreferenced() {
		return coreferenced;
	}

	public List<ExSentence> getSentences() {
		return sentences;
	}

	public Extraction getExtraction(String id) {
		for (ExSentence sentence : sentences) {
			Extraction e = sentence.getExtraction(id);
			if (e != null) {
				return e;
			}
		}

		return null;
	}

	public List<Extraction> getExtractions() {
		List<Extraction> res = new ArrayList<>();
		sentences.forEach(s -> res.addAll(s.getExtractions()));

		return res;
	}

	public String defaultFormat(boolean resolve) {
		StringBuilder strb = new StringBuilder();
		for (ExSentence sentence : getSentences()) {
			strb.append("\n# " + sentence.getOriginalSentence() + "\n");
			for (Extraction extraction : sentence.getExtractions()) {
				strb.append("\n" + extraction.getId() + "\t" + extraction.getContextLayer() + "\t" + extraction.getArg1() + "\t" + extraction.getRelation() + "\t" + extraction.getArg2() + "\n");
				for (SimpleContext simpleContext : extraction.getSimpleContexts()) {
					strb.append("\t" + "S:" + simpleContext.getClassification() + "\t" + simpleContext.getText() + "\n");
				}
				for (LinkedContext linkedContext : extraction.getLinkedContexts()) {
					if (resolve) {
						Extraction target = getExtraction(linkedContext.getTargetID());
						strb.append("\t" + "L:" + linkedContext.getRelation() + "\t" + target.getArg1() + "\t" + target.getRelation() + "\t" + target.getArg2() + "\n");
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
		for (ExSentence sentence : getSentences()) {
			for (Extraction extraction : sentence.getExtractions()) {
				strb.append(sentence.getOriginalSentence() + "\t" + extraction.getId() + "\t" + extraction.getContextLayer() + "\t" + extraction.getArg1() + "\t" + extraction.getRelation() + "\t" + extraction.getArg2());
				for (SimpleContext simpleContext : extraction.getSimpleContexts()) {
					strb.append("\t" + "S:" + simpleContext.getClassification() + "(" + simpleContext.getText() + ")");
				}
				for (LinkedContext linkedContext : extraction.getLinkedContexts()) {
					if (resolve) {
						Extraction target = getExtraction(linkedContext.getTargetID());
						strb.append("\t" + "L:" + linkedContext.getRelation() + "(" + target.getArg1() + separator + target.getRelation() + separator + target.getArg2() + ")");
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
		for (ExSentence sentence : getSentences()) {
			strb.append("\n# " + sentence.getOriginalSentence() + "\n");
			String sentenceId = IDGenerator.generateUUID();
			String sentenceBN = RDFHelper.rdfBlankNode(sentenceId);
			strb.append("\n" + RDFHelper.rdfTriple(sentenceBN, RDFHelper.grapheneSentenceResource("original-text"), RDFHelper.rdfLiteral(sentence.getOriginalSentence(), null)) + "\n");
			for (Extraction extraction : sentence.getExtractions()) {
				String extractionBN = RDFHelper.rdfBlankNode(extraction.getId());
				strb.append("\n" + RDFHelper.rdfTriple(sentenceBN, RDFHelper.grapheneSentenceResource("has-extraction"), extractionBN));
				strb.append("\n" + RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource("extraction-type"), RDFHelper.rdfLiteral(extraction.getType().name(), null)));
				strb.append("\n" + RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource("context-layer"), RDFHelper.rdfLiteral(extraction.getContextLayer())));
				strb.append("\n" + RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource("subject"), RDFHelper.grapheneTextResource(extraction.getArg1())));
				strb.append("\n" + RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource("predicate"), RDFHelper.grapheneTextResource(extraction.getRelation())));
				strb.append("\n" + RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource("object"), RDFHelper.grapheneTextResource(extraction.getArg2())));
				strb.append("\n");

				for (SimpleContext simpleContext : extraction.getSimpleContexts()) {
					String vContextAbbrev = "S-" + simpleContext.getClassification();
					strb.append("\n" + RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource(vContextAbbrev), RDFHelper.grapheneTextResource(simpleContext.getText())));
				}
				for (LinkedContext linkedContext : extraction.getLinkedContexts()) {
					Extraction target = getExtraction(linkedContext.getTargetID());
					String targetBN =  RDFHelper.rdfBlankNode(target.getId());
					String elementAbbrev = "L-" + linkedContext.getRelation();
					strb.append("\n" + RDFHelper.rdfTriple(extractionBN, RDFHelper.grapheneExtractionResource(elementAbbrev), targetBN));
				}
				strb.append("\n");

				// Values
				strb.append("\n" + RDFHelper.rdfTriple(RDFHelper.grapheneTextResource(extraction.getArg1()), RDFHelper.rdfResource("value"), RDFHelper.rdfLiteral(extraction.getArg1(), null)));
				strb.append("\n" + RDFHelper.rdfTriple(RDFHelper.grapheneTextResource(extraction.getRelation()), RDFHelper.rdfResource("value"), RDFHelper.rdfLiteral(extraction.getRelation(), null)));
				strb.append("\n" + RDFHelper.rdfTriple(RDFHelper.grapheneTextResource(extraction.getArg2()), RDFHelper.rdfResource("value"), RDFHelper.rdfLiteral(extraction.getArg2(), null)));

				for (SimpleContext simpleContext : extraction.getSimpleContexts()) {
					strb.append("\n" + RDFHelper.rdfTriple(RDFHelper.grapheneTextResource(simpleContext.getText()), RDFHelper.rdfResource("value"), RDFHelper.rdfLiteral(simpleContext.getText(), null)));
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
