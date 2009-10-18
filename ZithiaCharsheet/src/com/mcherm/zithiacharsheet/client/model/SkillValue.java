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
     * @param zithiaCharacter the character it will be created for. This MAY be
     *   only partially initialized, but the stats and race must have been initialized
     *   already.
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
