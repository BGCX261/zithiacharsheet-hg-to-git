package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation1;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation4;


/**
 * The object in the model that tracks experience points. The initial
 * version of this is missing some needed information, but it's a
 * start.
 */
public class ZithiaCosts {
    
    private final TweakableIntValue statCost;
    private final TweakableIntValue skillCost;
    private final TweakableIntValue weaponSkillCost;
    private final TweakableIntValue talentCost;
    private final TweakableIntValue totalCost;
    
    /**
     * Constructor.
     */
    public ZithiaCosts(StatValues statValues,
                       SkillList skillList, 
                       WeaponTraining weaponTraining,
                       TalentList talentList)
    {
        Equation1 identity = new Equation1() {
            public int getValue(int x) {
                return x;
            }
        };
        statCost = new EquationIntValue(statValues.getCost(), identity);
        skillCost = new EquationIntValue(skillList.getCost(), identity);
        weaponSkillCost = new EquationIntValue(weaponTraining.getTotalCost(), identity);
        talentCost = new EquationIntValue(talentList.getCost(), identity);
        totalCost = new EquationIntValue(statCost, skillCost, weaponSkillCost, talentCost, new Equation4() {
            public int getValue(int statCost, int skillCost, int weaponSkillCost, int talentCost) {
                return statCost + skillCost + weaponSkillCost + talentCost;
            }
        });
    }


    public TweakableIntValue getStatCost() {
        return statCost;
    }
    
    public TweakableIntValue getSkillCost() {
        return skillCost;
    }
    
    public TweakableIntValue getWeaponSkillCost() {
        return weaponSkillCost;
    }
    
    public TweakableIntValue getTalentCost() {
        return talentCost;
    }
    
    public TweakableIntValue getTotalCost() {
        return totalCost;
    }
}
