package org.lambda3.graphene.core.discourse_simplification.model;

import org.lambda3.text.simplification.discourse.model.OutSentence;
import org.lambda3.text.simplification.discourse.model.SimplificationContent;

public class DiscourseSimplificationContent extends SimplificationContent {
	private boolean coreferenced;

	// for deserialization
	public DiscourseSimplificationContent() {
		super();
		this.coreferenced = false;
	}

	public DiscourseSimplificationContent(SimplificationContent simplificationContent) {
		for (OutSentence outSentence : simplificationContent.getSentences()) {
			this.addSentence(outSentence);
		}
	}

	public boolean isCoreferenced() {
		return coreferenced;
	}

	public void setCoreferenced(boolean coreferenced) {
		this.coreferenced = coreferenced;
	}
}
