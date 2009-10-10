package com.mcherm.zithiacharsheet.client.model;

import java.util.Arrays;
import java.util.List;

import com.mcherm.zithiacharsheet.client.model.CalculatedIntValue.ValueCalculator;


/**
 * The object in the model that tracks experience points. The initial
 * version of this is missing some needed information, but it's a
 * start.
 */
public class ZithiaCosts {
    
    private final CalculatedIntValue statCost;
    private final CalculatedIntValue skillCost;
    private final CalculatedIntValue totalCost;
    
    /**
     * Constructor.
     * 
     * @param zithiaCharacter the character it will be created for. This MAY be
     *   only partially initialized, but certain pieces must have been initialized
     *   already - see the ZithiaCharacter constructor for the proper order. This
     *   should only be called from there.
     */
    public ZithiaCosts(ZithiaCharacter zithiaCharacter) {
        statCost = new CalculatedIntValue(
            zithiaCharacter.getStats(),
            new ValueCalculator() {
                public int calculateValue(List<? extends Observable> inputs) {
                    int cost = 0;
                    for (Observable input : inputs) {
                        StatValue statValue = (StatValue) input;
                        cost += statValue.getCost();
                    }
                    return cost;
                }
            }
        );
        // FIXME: Flaw here: doesn't add in new skills when that's needed
        skillCost = new CalculatedIntValue(
            zithiaCharacter.getSkills().getSkillValues(),
            new ValueCalculator() {
                public int calculateValue(List<? extends Observable> inputs) {
                    int cost = 0;
                    for (Observable input : inputs) {
                        SkillValue skillValue = (SkillValue) input;
                        cost += skillValue.getCost();
                    }
                    return cost;
                }
            }
        );
        totalCost = new CalculatedIntValue(
            Arrays.asList(new CalculatedIntValue[] {statCost, skillCost}),
            new ValueCalculator() {
                public int calculateValue(List<? extends Observable> inputs) {
                    int cost = 0;
                    for (Observable input : inputs) {
                        CalculatedIntValue value = (CalculatedIntValue) input;
                        cost += value.getValue();
                    }
                    return cost;
                }
            }
        );
    }


    public CalculatedIntValue getStatCost() {
        return statCost;
    }
    
    public CalculatedIntValue getSkillCost() {
        return skillCost;
    }
    
    public CalculatedIntValue getTotalCost() {
        return totalCost;
    }
}
