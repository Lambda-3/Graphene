package org.lambda3.graphene.core.relation_extraction.complex_categories;

import java.util.*;

public class ComplexCategory {

	public static final int CONTENT = 0;
	public static final int RELATION = 1;

	public final String name;
	public final Chunk core;

	private ComplexCategory() {
		this(null, null);
	}

	public ComplexCategory(String name, Chunk core) {
		this.name = name;
		this.core = core;
	}

	@Override
	public String toString() {
		return core.toString();
	}

	public Set<String> getCores() {
		if (core != null) {
			Set<String> cores = new HashSet<>();
			cores.add(core.effectiveTerm);

			if (core.specTypes != null) {
				for (int i = 0; i < core.specTypes.length; i++) {
					String type = core.specTypes[i];
					if (type.endsWith("-CC"))
						cores.add(core.specContents[i].effectiveTerm);
				}
			}

			return cores;
		}

		return null;
	}

	public Chunk getCore() {
		return core;
	}

	public static String getVerbContent(String[] spec) {
		String verb = null;

		int index = spec[ComplexCategory.RELATION].lastIndexOf("-");
		String pos = spec[ComplexCategory.RELATION].substring(index + 1);

		if (pos.startsWith("VB")) {
			String content = spec[ComplexCategory.RELATION].substring(0, index);
			String[] parts = content.split(" ");
			int firstIndex = pos.indexOf("/");

			if (firstIndex > 0) {
				int lastIndex = pos.lastIndexOf("/");
				if (lastIndex != firstIndex)
					verb = parts[2];
				else
					verb = parts[1];
			} else
				verb = parts[0];

			verb = verb.trim();
		}

		return verb;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ComplexCategory that = (ComplexCategory) o;
		return Objects.equals(name, that.name) &&
			Objects.equals(core, that.core);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, core);
	}

}
