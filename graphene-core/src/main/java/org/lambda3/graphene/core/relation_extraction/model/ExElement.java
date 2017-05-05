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


import org.lambda3.graphene.core.utils.IDGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public class ExElement {
    private String id;
    private String text;
    private String notSimplifiedText;
    private int sentenceIdx;
    private int contextLayer;
    private List<ExElementRelation> relations;
    private List<ExNContext> nContexts;
    private List<ExVContext> vContexts;
    private ExSPO spo; // optional

    // for deserialization
    public ExElement() {
    }

    public ExElement(String text, int sentenceIdx, int contextLayer) {
        this.id = IDGenerator.generateUUID();
        this.text = text;
        this.notSimplifiedText = text;
        this.sentenceIdx = sentenceIdx;
        this.contextLayer = contextLayer;
        this.relations = new ArrayList<>();
        this.nContexts = new ArrayList<>();
        this.vContexts = new ArrayList<>();
        this.spo = null;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void addRelation(ExElementRelation relation) {
        if (!relations.contains(relation)) {
            relations.add(relation);
        }
    }

    public void addNContext(ExNContext context) {
        if (!nContexts.contains(context)) {
            nContexts.add(context);
        }
    }

    public void addVContext(ExVContext context) {
        if (!vContexts.contains(context)) {
            vContexts.add(context);
        }
    }

    public void setSpo(ExSPO spo) {
        this.spo = spo;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getNotSimplifiedText() {
        return notSimplifiedText;
    }

    public int getSentenceIdx() {
        return sentenceIdx;
    }

    public int getContextLayer() {
        return contextLayer;
    }

    public List<ExElementRelation> getRelations() {
        return relations;
    }

    public List<ExNContext> getNContexts() {
        return nContexts;
    }

    public List<ExVContext> getVContexts() {
        return vContexts;
    }

    public Optional<ExSPO> getSpo() {
        return Optional.ofNullable(spo);
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof ExElement) && (((ExElement) obj).getId().equals(getId())));
    }
}
