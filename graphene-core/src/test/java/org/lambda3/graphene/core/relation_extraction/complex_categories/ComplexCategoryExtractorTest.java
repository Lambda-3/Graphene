package org.lambda3.graphene.core.relation_extraction.complex_categories;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lambda3.graphene.core.Graphene;
import org.lambda3.graphene.core.relation_extraction.formatter.FormatterFactory;
import org.lambda3.graphene.core.relation_extraction.model.Triple;
import org.lambda3.graphene.core.utils.GrapheneStream;
import org.lambda3.text.simplification.discourse.model.SimplificationContent;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ComplexCategoryExtractorTest {

	private ComplexCategoryExtractor extractor = new ComplexCategoryExtractor();

	@Test
	public void LeadersfromBrazilTest() {
		ComplexCategory cc = extractor.getComplexCategory("Leaders from Brazil");
		Assert.assertEquals(cc.getCores().size(), 1);
		Assert.assertEquals(cc.core.effectiveTerm, "leaders");

		Assert.assertEquals(cc.core.specTypes.length, 1);
		Assert.assertEquals(cc.core.specTypes[0], "from-IN");
		Assert.assertEquals(cc.core.specContents[0].effectiveTerm, "brazil");
	}

	@Test
	public void windowsTest() {
		ComplexCategory cc = extractor.getComplexCategory("metamorphic computer virus written in assembly language for Microsoft Windows");
		windowsTest(cc);
	}

	private void windowsTest(ComplexCategory cc) {
		Assert.assertEquals(cc.getCores().size(), 1);
		Assert.assertEquals(cc.core.effectiveTerm, "virus");

		Assert.assertEquals(cc.core.specTypes.length, 3);

		List<Boolean> ok = new LinkedList<>();
		for (int i = 0; i < cc.core.specTypes.length; i++) {
			switch (cc.core.specContents[i].effectiveTerm) {
				case "computer":
					Assert.assertEquals(cc.core.specTypes[i], Chunk.DIRECT_SPEC);
					ok.add(true);
					break;
				case "metamorphic":
					Assert.assertEquals(cc.core.specTypes[i], Chunk.DIRECT_SPEC);
					ok.add(true);
					break;
				default:
					Assert.assertEquals(cc.core.specTypes[i], "written in-VBN");
					Assert.assertEquals(cc.core.specContents[i].effectiveTerm, "language");

					Chunk ccl = cc.core.specContents[i];
					for (int j = 0; j < ccl.specTypes.length; j++) {
						if (ccl.specContents[j].effectiveTerm.equals("assembly")) {
							Assert.assertEquals(ccl.specTypes[j], Chunk.DIRECT_SPEC);
							ok.add(true);
						} else {
							Assert.assertEquals(ccl.specTypes[j], "for-IN");
							Assert.assertEquals(ccl.specContents[j].effectiveTerm, "windows");

							Assert.assertEquals(ccl.specContents[j].specTypes[0], Chunk.DIRECT_SPEC);
							Assert.assertEquals(ccl.specContents[j].specContents[0].effectiveTerm, "microsoft");

							ok.add(true);
						}
					}
					ok.add(true);
					break;
			}
		}
		Assert.assertEquals(ok.size(), 5);
		ok.forEach(Assert::assertTrue);
	}

	@Test(enabled = false)
	public void tvTest() {
		//TODO write me
		ComplexCategory cc = extractor.getComplexCategory("television channels and stations disestablished in 1990");
		System.out.println(cc);

		assert cc != null;
		System.out.println("Cores: " + cc.getCores());

		System.out.print("Qualifiers: ");
		for (String[] spec : cc.getCore().getPlanSpecs())
			System.out.println(Arrays.toString(spec));

		System.out.println("---");
	}

	@Test(enabled = false)
	public void queensTest() {
		//TODO write me
		ComplexCategory cc = extractor.getComplexCategory("early irish kings & queens");
		System.out.println(cc);

		assert cc != null;
		System.out.println("Cores: " + cc.getCores());

		System.out.print("Qualifiers: ");
		for (String[] spec : cc.getCore().getPlanSpecs())
			System.out.println(Arrays.toString(spec));

		System.out.println("---");
	}

	@Test(enabled = false)
	public void navalTest() {
		//TODO write me
		ComplexCategory cc = extractor.getComplexCategory("World War I Naval Ships Of France");
		System.out.println(cc);

		assert cc != null;
		System.out.println("Cores: " + cc.getCores());

		System.out.print("Qualifiers: ");
		for (String[] spec : cc.getCore().getPlanSpecs())
			System.out.println(Arrays.toString(spec));

		System.out.println("---");
	}

	@Test(enabled = false)
	public void americanTest() {
		//TODO write me
		ComplexCategory cc = extractor.getComplexCategory("american players from new mexico");
		System.out.println(cc);

		assert cc != null;
		System.out.println("Cores: " + cc.getCores());

		System.out.print("Qualifiers: ");
		for (String[] spec : cc.getCore().getPlanSpecs())
			System.out.println(Arrays.toString(spec));

		System.out.println("---");
	}

	@Test
	public void presidentTest() {
		ComplexCategory cc = extractor.getComplexCategory("The current President of The United States ");
		presidentTest(cc);
	}

	private void presidentTest(ComplexCategory cc) {
		// The current President of The United States
		Assert.assertEquals(cc.getCores().size(), 1);
		Assert.assertEquals(cc.core.effectiveTerm, "president");

		Assert.assertEquals(cc.core.specTypes.length, 2);

		List<Boolean> ok = new LinkedList<>();
		for (int i = 0; i < cc.core.specTypes.length; i++) {
			if (cc.core.specContents[i].effectiveTerm.equals("current")) {
				Assert.assertEquals(cc.core.specTypes[i], Chunk.DIRECT_SPEC);
				ok.add(true);
			} else {
				Assert.assertEquals(cc.core.specTypes[i], "of-IN");
				Assert.assertEquals(cc.core.specContents[i].effectiveTerm, "states");

				Chunk ccl = cc.core.specContents[i];
				Assert.assertEquals(ccl.specContents[0].effectiveTerm, "united");
				Assert.assertEquals(ccl.specTypes[0], Chunk.DIRECT_SPEC);
				ok.add(true);
			}
		}

		Assert.assertEquals(ok.size(), 2);
		ok.forEach(Assert::assertTrue);
	}

	@Test
	public void extractWin32() throws IOException {
		Graphene graphene = new Graphene();
		String text = "Win32/Simile (also known as Etap and MetaPHOR) is a metamorphic " +
			"computer virus written in assembly language for Microsoft Windows.";

		SimplificationContent content = graphene.doRelationExtraction(text,
			false, true, true);

		List<Triple> tl = GrapheneStream.triples(content).collect(Collectors.toList());
		Assert.assertEquals(tl.size(), 1);

		Triple the = tl.get(0);
		//TODO sentence simplification is lowercasing this subject.
		Assert.assertEquals(the.subject, "Win32/Simile".toLowerCase());
		Assert.assertEquals(the.property, "is");
		Assert.assertEquals(the.object, "a metamorphic computer virus written in assembly language for Microsoft Windows");

		windowsTest(the.getExtension(ComplexCategory.class, ComplexCategoryExtractor.OBJECT));

		ObjectMapper mapper = new ObjectMapper();

		File temp = File.createTempFile("ext-discourse-simplification", ".json");
		temp.deleteOnExit();

		mapper.writeValue(temp, the);
		Triple nthe = mapper.readValue(temp, Triple.class);

		Assert.assertEquals(the, nthe);
	}

	@Test
	public void extractText() {
		Graphene graphene = new Graphene();
		String text = "The current President of The United States withdrew his sponsorship after the second " +
			"Tour de Trump in 1990 because his business ventures were experiencing financial woes.";

		SimplificationContent content = graphene.doRelationExtraction(text,
			false, true, true);
		String output = FormatterFactory.get("default").format(content.getSentences(), false);
		System.out.println(output);

		List<Triple> tl = GrapheneStream.triples(content).collect(Collectors.toList());
		//TODO should it be more? What about the sc relation extraction.
		Assert.assertEquals(tl.size(), 2);

		List<Boolean> ok = new LinkedList<>();

		for (Triple triple : tl) {
			assert triple.property != null;
			if (triple.property.equals("withdrew")) {
				//TODO subject again lowercased unecessarily "The"
				Assert.assertEquals(triple.subject, "the current President of The United States");
				Assert.assertEquals(triple.object, "his sponsorship");

				presidentTest(triple.getExtension(ComplexCategory.class, ComplexCategoryExtractor.SUBJECT));

				ComplexCategory cc = triple.getExtension(ComplexCategory.class, ComplexCategoryExtractor.OBJECT);
				Assert.assertEquals(cc.core.effectiveTerm, "sponsorship");
				Assert.assertEquals(cc.core.specTypes.length, 1);
				Assert.assertEquals(cc.core.specTypes[0], Chunk.DIRECT_SPEC);
				Assert.assertEquals(cc.core.specContents[0].effectiveTerm, "his");
				ok.add(true);
			} else {
				Assert.assertEquals(triple.subject, "his business ventures");
				Assert.assertEquals(triple.property, "were experiencing");
				Assert.assertEquals(triple.object, "financial woes");

				ComplexCategory cc = triple.getExtension(ComplexCategory.class, ComplexCategoryExtractor.SUBJECT);
				Assert.assertEquals(cc.core.effectiveTerm, "ventures");
				Assert.assertEquals(cc.core.specTypes.length, 2);
				for (int i = 0; i < cc.core.specContents.length; i++) {
					if (cc.core.specContents[i].effectiveTerm.equals("business")) {
						Assert.assertEquals(cc.core.specTypes[i], Chunk.DIRECT_SPEC);
						ok.add(true);
					} else {
						Assert.assertEquals(cc.core.specContents[i].effectiveTerm, "his");
						Assert.assertEquals(cc.core.specTypes[i], Chunk.DIRECT_SPEC);
						ok.add(true);
					}
				}

				cc = triple.getExtension(ComplexCategory.class, ComplexCategoryExtractor.OBJECT);
				Assert.assertEquals(cc.core.effectiveTerm, "woes");
				Assert.assertEquals(cc.core.specTypes.length, 1);
				Assert.assertEquals(cc.core.specTypes[0], Chunk.DIRECT_SPEC);
				Assert.assertEquals(cc.core.specContents[0].effectiveTerm, "financial");
			}
		}

		Assert.assertEquals(ok.size(), 3);
		ok.forEach(Assert::assertTrue);
	}

	@Test
	public void serializationTest() throws IOException {
		ComplexCategory cc = extractor.getComplexCategory("metamorphic computer virus written in assembly language for Microsoft Windows");

		File temp = File.createTempFile("ext-discourse-simplification", ".json");
		temp.deleteOnExit();

		ObjectMapper mapper = new ObjectMapper();

		mapper.writeValue(temp, cc);
		ComplexCategory ncc = mapper.readValue(temp, ComplexCategory.class);

		Assert.assertEquals(cc, ncc);
	}
}
