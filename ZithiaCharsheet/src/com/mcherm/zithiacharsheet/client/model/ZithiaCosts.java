package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation1;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation3;


/**
 * The object in the model that tracks experience points. The initial
 * version of this is missing some needed information, but it's a
 * start.
 */
public class ZithiaCosts {
    
    private final TweakableIntValue statCost;
    private final TweakableIntValue skillCost;
    private final TweakableIntValue weaponSkillCost;
    private final TweakableIntValue totalCost;
    
    /**
     * Constructor.
     */
    public ZithiaCosts(StatValues statValues, SkillList skillList, WeaponTraining weaponTraining) {
        Equation1 identity = new Equation1() {
            public int getValue(int x) {
                return x;
            }
        };
        statCost = new EquationIntValue(statValues.getCost(), identity);
        skillCost = new EquationIntValue(skillList.getCost(), identity);
        weaponSkillCost = new EquationIntValue(weaponTraining.getTotalCost(), identity);
        totalCost = new EquationIntValue(statCost, skillCost, weaponSkillCost, new Equation3() {
            public int getValue(int statCost, int skillCost, int weaponSkillCost) {
                return statCost + skillCost + weaponSkillCost;
            }
        });
    }


    public TweakableIntValue getStatCost() {
        return statCost;
    }
    
    public TweakableIntValue getSkillCost() {
        return skillCost;
    }
    
    public TweakableIntValue getWeaponsSkillCost() {
        return weaponSkillCost;
    }
    
    public TweakableIntValue getTotalCost() {
        return totalCost;
    }
}
