/*
 * ==========================License-Start=============================
 * graphene-core : ExCoreSentence
 *
 * Copyright © 2017 Lambda³
 *
 * GNU General Public License 3
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * ==========================License-End==============================
 */

package org.lambda3.graphene.core.relation_extraction.model;

import java.util.Optional;

/**
 *  Context mediated by nouns
 */
public class ExNContext {
    private String text;
    private Classification classification;
    private ExSPO spo; // optional

    // for deserialization
    public ExNContext() {
    }

    public ExNContext(String text) {
        this.text = text;
        this.classification = Classification.UNKNOWN;
        this.spo = null;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public void setSpo(ExSPO spo) {
        this.spo = spo;
    }

    public String getText() {
        return text;
    }

    public Classification getClassification() {
        return classification;
    }

    public Optional<ExSPO> getSpo() {
        return Optional.ofNullable(spo);
    }
}
