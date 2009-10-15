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
        statCost = new CalculatedIntValue<StatValue>(
            zithiaCharacter.getStats(),
            new ValueCalculator<StatValue>() {
                public int calculateValue(Iterable<? extends StatValue> inputs) {
                    int cost = 0;
                    for (StatValue statValue : inputs) {
                        cost += statValue.getCost();
                    }
                    return cost;
                }
            }
        );
        skillCost = zithiaCharacter.getSkills().getSum();
        weaponSkillCost = zithiaCharacter.getWeaponTraining().getTotalCost();
        totalCost = new CalculatedIntValue<ObservableInt>(
            Arrays.asList(new ObservableInt[] {statCost, skillCost, weaponSkillCost}),
            // FIXME: Use the standard sum calculator.
            new ValueCalculator<ObservableInt>() {
                public int calculateValue(Iterable<? extends ObservableInt> inputs) {
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
