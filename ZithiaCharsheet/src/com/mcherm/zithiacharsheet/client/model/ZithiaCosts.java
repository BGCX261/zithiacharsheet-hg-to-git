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
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValueImpl;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation0;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation1;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation2;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation3;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation4;


/**
 * The object in the model that tracks experience points. The initial
 * version of this is missing some needed information, but it's a
 * start.
 */
public class ZithiaCosts {

    private final TweakableIntValue raceCost;
    private final TweakableIntValue statCost;
    private final TweakableIntValue skillCost;
    private final TweakableIntValue weaponSkillCost;
    private final TweakableIntValue talentCost;
    private final TweakableIntValue totalCost;
    private final SettableIntValue  basePts;
    private final SettableIntValue  loanPts;
    private final TweakableIntValue expSpent;
    private final SettableIntValue  expEarned;
    private final TweakableIntValue paidForLoan;
    private final TweakableIntValue expUnspent;
    
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
        raceCost = new EquationIntValue(new Equation0() {
            public int getValue() {
                return 0;
            }
        });
        statCost = new EquationIntValue(statValues.getCost(), identity);
        skillCost = new EquationIntValue(skillList.getCost(), identity);
        weaponSkillCost = new EquationIntValue(weaponTraining.getTotalCost(), identity);
        talentCost = new EquationIntValue(talentList.getCost(), identity);
        totalCost = new EquationIntValue(statCost, skillCost, weaponSkillCost, talentCost, new Equation4() {
            public int getValue(int statCost, int skillCost, int weaponSkillCost, int talentCost) {
                return statCost + skillCost + weaponSkillCost + talentCost;
            }
        });
        basePts = new SettableIntValueImpl(30);
        loanPts = new SettableIntValueImpl(0);
        expSpent = new EquationIntValue(totalCost, basePts, loanPts, new Equation3() {
            public int getValue(int totalCost, int basePts, int loanPts) {
                return totalCost - (basePts + loanPts);
            }
        });
        expEarned = new SettableIntValueImpl(0);
        paidForLoan = new EquationIntValue(loanPts, expEarned, new Equation2() {
            public int getValue(int loanPts, int expEarned) {
                // NOTE: both divisions are intended to round down when an odd number is used
                return Math.min(expEarned / 2, loanPts + loanPts / 2);
            }
        });
        // NOTE: When expUnspent is <0 it means the character is not paid for properly
        expUnspent = new EquationIntValue(expEarned, paidForLoan, expSpent, new Equation3() {
            public int getValue(int expEarned, int paidForLoan, int expSpent) {
                return expEarned - (expSpent + paidForLoan);
            }
        });
    }


    public TweakableIntValue getRaceCost() {
        return raceCost;
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

    public SettableIntValue getBasePts() {
        return basePts;
    }

    public SettableIntValue getLoanPts() {
        return loanPts;
    }

    public TweakableIntValue getExpSpent() {
        return expSpent;
    }

    public SettableIntValue getExpEarned() {
        return expEarned;
    }

    public TweakableIntValue getPaidForLoan() {
        return paidForLoan;
    }

    public TweakableIntValue getExpUnspent() {
        return expUnspent;
    }

}
