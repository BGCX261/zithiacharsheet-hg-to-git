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

import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue;
import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValueImpl;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation1;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation2;


/**
 * An actual instance of a skill in a particular individual.
 * So, for instance, this might be Rogan Harsha's Stealth.
 */
public class SkillValue {

    private final ZithiaSkill skill;
    private final SettableIntValue levels;
    private final TweakableIntValue roll; // may be null if no roll is applicable to this Skill
    private final TweakableIntValue cost;
    
    /**
     * Constructor.
     * 
     * @param skill the skill that will be created
     * @param statValues the StatValues it will be created for.
     */
    public SkillValue(final ZithiaSkill skill, StatValues statValues) {
        this.skill = skill;
        levels = new SettableIntValueImpl(0);
        ZithiaStat statForSkill = skill.getStat();
        if (statForSkill == null) {
            roll = null;
        } else {
            ObservableInt statRoll = statValues.getStat(statForSkill).getRoll();
            roll = new EquationIntValue(statRoll, levels, new Equation2() {
                public int getValue(int statRoll, int levels) {
                    return statRoll + levels;
                }
            });
        }
        cost = new EquationIntValue(levels, new Equation1() {
            public int getValue(int levels) {
                return skill.getCost(levels);
            }
        });
    }
    
    public ZithiaSkill getSkill() {
        return skill;
    }
    
    public SettableIntValue getLevels() {
        return levels;
    }
    
    public TweakableIntValue getRoll() {
        return roll;
    }
    
    public TweakableIntValue getCost() {
        return cost;
    }
    
}
