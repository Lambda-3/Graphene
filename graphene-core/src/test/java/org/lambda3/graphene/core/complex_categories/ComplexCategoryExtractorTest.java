package org.lambda3.graphene.core.complex_categories;

import org.lambda3.graphene.core.Graphene;
import org.lambda3.graphene.core.discourse_simplification.model.DiscourseSimplificationContent;
import org.lambda3.graphene.core.relation_extraction.model.RelationExtractionContent;
import org.testng.annotations.Test;

public class ComplexCategoryExtractorTest {

	@Test
	public void extract() {
		Graphene graphene = new Graphene();
		String text = "Win32/Simile (also known as Etap and MetaPHOR) is a metamorphic " +
			"computer virus written in assembly language for Microsoft Windows.";

		DiscourseSimplificationContent discourse = graphene.doDiscourseSimplification(text, false, true);
		RelationExtractionContent relations = graphene.doRelationExtraction(text, false, false);
		System.out.println(discourse);
		System.out.println(relations);

		text = "Trump withdrew his sponsorship after the second Tour de Trump in 1990 because his business " +
			"ventures were experiencing financial woes.";
		discourse = graphene.doDiscourseSimplification(text, false, true);
		relations = graphene.doRelationExtraction(text, false, false);
		System.out.println(discourse);
		System.out.println(relations);
	}
}
