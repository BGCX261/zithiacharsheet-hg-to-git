package com.mcherm.zithiacharsheet.client.model.weapon;


/**
 * This is a skill with a single weapon, such as Broadsword.
 * Instances are immutable.
 */
public class SingleWeaponSkill extends WeaponSkill {
    private final Weapon weapon;
    private final int basicTrainingCost;
    private final int firstLevelCost;
    
    public SingleWeaponSkill(Weapon weapon, int basicTrainingCost, int firstLevelCost) {
        this.weapon = weapon;
        this.basicTrainingCost = basicTrainingCost;
        this.firstLevelCost = firstLevelCost;
    }

    public Weapon getWeapon() {
        return weapon;
    }
    
    @Override
    public String getId() {
        return weapon.getId();
    }
    
    @Override
    public String getName() {
        return weapon.getName();
    }
    
    @Override
    public int getBasicTrainingCost() {
        return basicTrainingCost;
    }

    @Override
    public int getFirstLevelCost() {
        return firstLevelCost;
    }

    @Override
    public int getSpan() {
        return 1;
    }

}
