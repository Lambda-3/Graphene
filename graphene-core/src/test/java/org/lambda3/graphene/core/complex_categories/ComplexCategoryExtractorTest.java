package org.lambda3.graphene.core.complex_categories;

import org.lambda3.graphene.core.Graphene;
import org.lambda3.graphene.core.discourse_simplification.model.DiscourseSimplificationContent;
import org.lambda3.graphene.core.relation_extraction.model.RelationExtractionContent;
import org.testng.annotations.Test;

import java.util.Arrays;

public class ComplexCategoryExtractorTest {

	@Test(enabled = false)
	public void extract() {
		Graphene graphene = new Graphene();
		String text = "Win32/Simile (also known as Etap and MetaPHOR) is a metamorphic " +
			"computer virus written in assembly language for Microsoft Windows.";

		DiscourseSimplificationContent discourse = graphene.doDiscourseSimplification(text, false, true);
		RelationExtractionContent relations = graphene.doRelationExtraction(text, false, false, false);
		System.out.println(discourse);
		System.out.println(relations);

		text = "The current President of The United States withdrew his sponsorship after the second Tour de Trump in 1990 because his business " +
			"ventures were experiencing financial woes.";
		discourse = graphene.doDiscourseSimplification(text, false, true);
		relations = graphene.doRelationExtraction(text, false, false, false);
		System.out.println(discourse);
		System.out.println(relations);
	}

	@Test
	public void extractionTest() {
		String[] queries = { "Leaders from Brazil", "american players from new mexico",
			"World War I Naval Ships Of France", "early irish kings & queens",
			"television channels and stations disestablished in 1990",
		"metamorphic computer virus written in assembly language for Microsoft Windows"};

		ComplexCategoryExtractor extractor = new ComplexCategoryExtractor();
		for (String query : queries) {
			ComplexCategory yclass = extractor.getComplexCategory(query);
			System.out.println(yclass);

			assert yclass != null;
			System.out.println("Cores: " + yclass.getCores());

			System.out.print("Qualifiers: ");
			for (String[] spec : yclass.getCore().getPlanSpecs())
				System.out.println(Arrays.toString(spec));

			System.out.println("---");
		}
	}
}
