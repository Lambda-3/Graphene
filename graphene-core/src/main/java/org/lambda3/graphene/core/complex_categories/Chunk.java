package org.lambda3.graphene.core.complex_categories;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

	public static final String DIRECT_SPEC = "spec";
	public static final String PREPOSION_SUFFIX = "-IN";
	public static final String CONJUNCTION_SUFFIX = "-CC";

	public String pureTerm;
	public String effectiveTerm;
	public String tag;
	public String[] specTypes;
	public Chunk[] specContents;

	public Chunk(String pureTerm, String effectiveTerm, String tag, String[] specTypes, Chunk[] specContents) {
		this.pureTerm = pureTerm;
		this.effectiveTerm = effectiveTerm;
		this.tag = tag;
		this.specTypes = specTypes;
		this.specContents = specContents;
	}

	public List<String[]> getPlanSpecs() {

		List<String[]> specs = new ArrayList<>();
		if (specTypes != null) {
			List<String[]> specsTemp = new ArrayList<>();

			for (int i = 0; i < specTypes.length; i++) {
				if (!specTypes[i].endsWith(CONJUNCTION_SUFFIX)) {
					getOrderedSpecs(specsTemp, 0, specContents[i], specTypes[i]);

					if (specTypes[i].equals(DIRECT_SPEC))
						specs.addAll(0, specsTemp);
					else
						specs.addAll(specsTemp);
					specsTemp.clear();
				}
			}
		}

		return specs;
	}

	public void getOrderedSpecs(List<String[]> specs, int base, Chunk chunk, String relation) {

		specs.add(base, new String[] { chunk.pureTerm, relation });

		if (chunk.specTypes != null) {
			int end = base + 1;

			String type;
			for (int i = 0; i < chunk.specTypes.length; i++) {
				type = chunk.specTypes[i];
				if (type.equals(DIRECT_SPEC))
					getOrderedSpecs(specs, base, chunk.specContents[i], DIRECT_SPEC);
				else if (!type.endsWith(CONJUNCTION_SUFFIX))
					getOrderedSpecs(specs, end++, chunk.specContents[i], type);
			}
		}

	}

	@Override
	public String toString() {
		List<String[]> details = new ArrayList<>();
		StringBuilder builder = new StringBuilder(pureTerm);
		String type;

		if (specTypes != null)
			for (int i = 0; i < specTypes.length; i++) {
				type = specTypes[i];
				if (type.endsWith(CONJUNCTION_SUFFIX)) {
					builder.append(" ").append(type, 0, type.lastIndexOf("-"));
					// builder.append(" " + type);
					builder.append(" ");
					builder.append(specContents[i].toString());
					builder.append(" ");
				} else {
					details.clear();
					getOrderedSpecs(details, 0, specContents[i], type);
					StringBuilder partial = new StringBuilder();
					for (String[] pair : details) {
						partial.append(" ");
						partial.append(pair[ComplexCategory.CONTENT]);
						partial.append(" ");
					}

					if (type.equals(DIRECT_SPEC))
						builder.insert(0, partial.toString());
					else
						builder.append(partial.toString());

				}
			}

		return builder.toString().trim().replaceAll("\\s+", " ");
	}

	public boolean hasSpecs() {
		return specTypes == null || specTypes.length == 0;
	}

	public static void main(String[] args) {
		StringBuilder sbuilder = new StringBuilder("test");
		sbuilder.append(" second");
		sbuilder.insert(0, "first     ");

		System.out.println(sbuilder.toString().trim().replaceAll("\\s+", " "));
	}
}
