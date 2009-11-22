/*
 * Copyright 2009 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
import com.mcherm.zithiacharsheet.client.modeler.SummableList;

/**
 * This is the set of skills that a character.
 */
public class SkillList extends SummableList<SkillValue> {
    
    private final StatValues statValues;

    /**
     * Constructor. Creates an empty list of skills.
     */
    public SkillList(StatValues statValues) {
        super(new Extractor<SkillValue>() {
            public ObservableInt extractValue(SkillValue item) {
                return item.getCost();
            }
        });
        this.statValues = statValues;
    }
    
    /**
     * Returns the cost of the skills.
     */
    public ObservableInt getCost() {
        return getSum();
    }

    /**
     * Call this to add a new skill to the list. Returns the new SkillValue.
     */
    public SkillValue addNewSkill(ZithiaSkill skill) {
        SkillValue skillValue = new SkillValue(skill, this.statValues);
        add(skillValue);
        return skillValue;
    }
    
}
