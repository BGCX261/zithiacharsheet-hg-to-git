package com.mcherm.zithiacharsheet.client.model;

import java.util.Arrays;

import com.mcherm.zithiacharsheet.client.model.CalculatedIntValue.ValueCalculator;


/**
 * The object in the model that tracks experience points. The initial
 * version of this is missing some needed information, but it's a
 * start.
 */
public class ZithiaCosts {
    
    private final ObservableInt statCost;
    private final ObservableInt skillCost;
    private final ObservableInt weaponSkillCost;
    private final ObservableInt totalCost;
    
    /**
     * Constructor.
     * 
     * @param zithiaCharacter the character it will be created for. This MAY be
     *   only partially initialized, but certain pieces must have been initialized
     *   already - see the ZithiaCharacter constructor for the proper order. This
     *   should only be called from there.
     */
    public ZithiaCosts(final ZithiaCharacter zithiaCharacter) {
        statCost = new CalculatedIntValue(
            zithiaCharacter.getStats(),
            new ValueCalculator() {
                public int calculateValue(Iterable<? extends Observable> inputs) {
                    int cost = 0;
                    for (Observable input : inputs) {
                        StatValue statValue = (StatValue) input;
                        cost += statValue.getCost();
                    }
                    return cost;
                }
            }
        );
        skillCost = zithiaCharacter.getSkills();
        weaponSkillCost = zithiaCharacter.getWeaponTraining().getTotalCost();
        totalCost = new CalculatedIntValue(
            Arrays.asList(new ObservableInt[] {statCost, skillCost, weaponSkillCost}),
            new ValueCalculator() {
                public int calculateValue(Iterable<? extends Observable> inputs) {
                    int cost = 0;
                    for (Observable input : inputs) {
                        ObservableInt value = (ObservableInt) input;
                        cost += value.getValue();
                    }
                    return cost;
                }
            }
        );
    }


    public ObservableInt getStatCost() {
        return statCost;
    }
    
    public ObservableInt getSkillCost() {
        return skillCost;
    }
    
    public ObservableInt getWeaponsSkillCost() {
        return weaponSkillCost;
    }
    
    public ObservableInt getTotalCost() {
        return totalCost;
    }
}
