package org.lambda3.graphene.core.relation_extraction.complex_categories;

import java.util.*;
import java.util.stream.Collectors;

public class Chunk {

	public static final String DIRECT_SPEC = "spec";
	public static final String PREPOSION_SUFFIX = "-IN";
	public static final String CONJUNCTION_SUFFIX = "-CC";

	public String pureTerm;
	public String effectiveTerm;
	public String tag;
	public String[] specTypes;
	public Chunk[] specContents;

	private Chunk() {
		this(null, null, null, null, null);
	}

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

		specs.add(base, new String[]{chunk.pureTerm, relation});

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Chunk chunk = (Chunk) o;
		return Objects.equals(pureTerm, chunk.pureTerm) &&
			Objects.equals(effectiveTerm, chunk.effectiveTerm) &&
			Objects.equals(tag, chunk.tag) &&
			Arrays.equals(specTypes, chunk.specTypes) &&
			Arrays.equals(specContents, chunk.specContents);
	}

	@Override
	public int hashCode() {

		int result = Objects.hash(pureTerm, effectiveTerm, tag);
		result = 31 * result + Arrays.hashCode(specTypes);
		result = 31 * result + Arrays.hashCode(specContents);
		return result;
	}
}
