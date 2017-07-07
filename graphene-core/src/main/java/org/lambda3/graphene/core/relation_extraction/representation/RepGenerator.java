package org.lambda3.graphene.core.relation_extraction.representation;

/*-
 * ==========================License-Start=============================
 * RepGenerator.java - Graphene Core - Lambda^3 - 2017
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


import org.lambda3.graphene.core.relation_extraction.model.*;
import org.lambda3.graphene.core.relation_extraction.representation.generators.DefaultGenerator;
import org.lambda3.graphene.core.relation_extraction.representation.generators.ExpandedGenerator;
import org.lambda3.graphene.core.relation_extraction.representation.generators.FlatGenerator;
import org.lambda3.graphene.core.relation_extraction.representation.generators.NTriplesGenerator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class RepGenerator {
    protected static final String ELEMENT_STR = "ELEM";
    protected static final String NCONTEXT_STR = "NCON";
    protected static final String VCONTEXT_STR = "VCON";

    protected boolean showText;

	public RepGenerator() {
		this.showText = false;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
	}

	protected static String elementAbbrev(ExElement element, Classification classification) {
        return ELEMENT_STR + "-" + classification.name();
    }

    protected static String nContextAbbrev(ExNContext nContext) {
        return NCONTEXT_STR + "-" + nContext.getClassification().name();
    }

    protected static String vContextAbbrev(ExVContext vContext) {
        return VCONTEXT_STR + "-" + vContext.getClassification().name();
    }

    protected Optional<String> elemContextRep(ExElement element, Classification classification, boolean linked, boolean asArg) {
        if (!this.showText && !element.getSpo().isPresent()) {
            return Optional.empty();
        }
        String abbrev = elementAbbrev(element, classification);

        if (linked) {
            if (asArg) {
                return Optional.of(abbrev + "(" + element.getId() + ")");
            } else {
                return Optional.of(abbrev + "\t" + element.getId());
            }
        } else {
            if (asArg) {
            	if (this.showText) {
					return Optional.of(abbrev + "(" + element.getText() + ")");
				} else {
					ExSPO spo = element.getSpo().get();
					return Optional.of(abbrev + "(" + spo.getSubject() + "\t" + spo.getPredicate() + "\t" + spo.getObject() + ")");
				}
            } else {
            	if (this.showText) {
					return Optional.of(abbrev + "\t" + element.getText());
				} else {
					ExSPO spo = element.getSpo().get();
					return Optional.of(abbrev + "\t" + spo.getSubject() + "\t" + spo.getPredicate() + "\t" + spo.getObject());
				}
            }
        }
    }

    protected Optional<String> nContextRep(ExNContext context, boolean asArg) {
        if (!this.showText && !context.getSpo().isPresent()) {
            return Optional.empty();
        }
        String abbrev = nContextAbbrev(context);

        if (asArg) {
        	if (this.showText) {
        		return Optional.of(abbrev + "(" + context.getText() + ")");
			} else {
				ExSPO spo = context.getSpo().get();
				return Optional.of(abbrev + "(" + spo.getSubject() + "\t" + spo.getPredicate() + "\t" + spo.getObject() + ")");
			}
        } else {
        	if (this.showText) {
				return Optional.of(abbrev + "\t" + context.getText());
			} else {
				ExSPO spo = context.getSpo().get();
				return Optional.of(abbrev + "\t" + spo.getSubject() + "\t" + spo.getPredicate() + "\t" + spo.getObject());
			}
        }
    }

    protected static String vContextRep(ExVContext context, boolean asArg) {
        String abbrev = vContextAbbrev(context);

        if (asArg) {
            return abbrev + "(" + context.getText() + ")";
        } else {
            return abbrev + "\t" + context.getText();
        }
    }

    public static String getRDFRepresentation(ExContent content, RepStyle style) {
        return getRDFRepresentation(content, style, 5);
    }

    public static String getRDFRepresentation(ExContent content, RepStyle style, Integer maxContextDepth) {
		RepGenerator generator;
		switch (style) {
			case DEFAULT:
				generator = new DefaultGenerator();
				return generator.format(content).stream().collect(Collectors.joining("\n"));
			case DEFAULT_TEXT:
				generator = new DefaultGenerator();
				generator.setShowText(true);
				return generator.format(content).stream().collect(Collectors.joining("\n"));
			case FLAT:
				generator = new FlatGenerator();
				return generator.format(content).stream().collect(Collectors.joining("\n"));
			case FLAT_TEXT:
				generator = new FlatGenerator();
				generator.setShowText(true);
				return generator.format(content).stream().collect(Collectors.joining("\n"));
			case EXPANDED:
				generator = new ExpandedGenerator((maxContextDepth == null) ? 3 : maxContextDepth);
				return generator.format(content).stream().collect(Collectors.joining("\n"));
			case EXPANDED_TEXT:
				generator = new ExpandedGenerator((maxContextDepth == null) ? 3 : maxContextDepth);
				generator.setShowText(true);
				return generator.format(content).stream().collect(Collectors.joining("\n"));
			case N_TRIPLES:
				generator = new NTriplesGenerator();
				return generator.format(content).stream().collect(Collectors.joining("\n"));
			case N_TRIPLES_TEXT:
				generator = new NTriplesGenerator();
				generator.setShowText(true);
				return generator.format(content).stream().collect(Collectors.joining("\n"));
			default:
				throw new AssertionError("Unknown representation style.");
		}
    }

    public abstract List<String> format(ExContent content);
}
