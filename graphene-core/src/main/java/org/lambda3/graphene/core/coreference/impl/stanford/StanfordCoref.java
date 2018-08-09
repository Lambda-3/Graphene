package org.lambda3.graphene.core.coreference.impl.stanford;

import com.typesafe.config.Config;
import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.lambda3.graphene.core.coreference.CoreferenceResolver;
import org.lambda3.graphene.core.coreference.model.CoreferenceContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class StanfordCoref extends CoreferenceResolver {
	private class Sentence {

		private class Word {
			private String text;
			private boolean keep;

			public Word(String text) {
				this.text = text;
				this.keep = true;
			}
		}

		private List<Word> words;

		public Sentence() {
			this.words = new ArrayList<>();
		}

		public void addWord(String word) {
			this.words.add(new Word(word));
		}

		public void replaceWords(int startIdx, int endIdx, String replacement) {
			for (int i = startIdx; i < endIdx; i++) {
				if (i == startIdx) {
					this.words.get(i).text = replacement;
				} else {
					this.words.get(i).keep = false;
				}
			}
		}

		public String toString() {
			return words.stream()
				.filter(w -> w.keep)
				.map(w -> w.text)
				.collect(Collectors.joining(" "));
		}
	}

	private static final Properties PROPS = new Properties();
	static {
		PROPS.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,mention,parse,coref");
	}
	private static final StanfordCoreNLP PIPELINE = new StanfordCoreNLP(PROPS);



	public StanfordCoref(Config config) {
		super(config);
	}

	private static String getReplacement(String corefMention, String coreMention) {
		if (corefMention.trim().toLowerCase().matches("his|her")) {
			return coreMention + "'s";
		}
		if (corefMention.trim().toLowerCase().matches("their|our")) {
			return coreMention + "s'";
		}
		return coreMention;
	}

	@Override
	public CoreferenceContent doCoreferenceResolution(String text) {
		Annotation document = new Annotation(text);
		PIPELINE.annotate(document);

		// extract sentences
		List<Sentence> sentences = new ArrayList<>();
		for (CoreMap coreMap : document.get(CoreAnnotations.SentencesAnnotation.class)) {
			Sentence sentence = new Sentence();
			for (CoreLabel coreLabel : coreMap.get(CoreAnnotations.TokensAnnotation.class)) {
				sentence.addWord(coreLabel.word());
			}
			sentences.add(sentence);
		}

		// replace coreferences
		for (CorefChain cc : document.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
			String coreMention = cc.getRepresentativeMention().mentionSpan;
			for (CorefChain.CorefMention corefMention : cc.getMentionsInTextualOrder()) {
				sentences.get(corefMention.sentNum-1).replaceWords(corefMention.startIndex-1, corefMention.endIndex-1, getReplacement(corefMention.mentionSpan, coreMention));
			}
		}

		return new CoreferenceContent(text, sentences.stream().map(s -> s.toString()).collect(Collectors.joining(" ")));
	}
}
