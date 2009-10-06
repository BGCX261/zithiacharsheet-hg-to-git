package com.mcherm.zithiacharsheet.client.model.weapon;

/**
 * This is a skill with some group of weapons.
 * <p>
 * Instances are immutable.
 * <p>
 * FIXME: rename this to "WeaponGroupSkill" if WeaponCluster gets renamed.
 */
public class WeaponClusterSkill extends WeaponSkill {
    private final WeaponCluster weaponCluster;
    private final int basicTrainingCost;
    private final int firstLevelCost;
    
    public WeaponClusterSkill(WeaponCluster weaponCluster, int basicTrainingCost, int firstLevelCost) {
        this.weaponCluster = weaponCluster;
        this.basicTrainingCost = basicTrainingCost;
        this.firstLevelCost = firstLevelCost;
    }
    
    public WeaponCluster getWeaponCluster() {
        return weaponCluster;
    }

    @Override
    public String getName() {
        return weaponCluster.getName();
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
        return weaponCluster.getSpan();
    }

}
