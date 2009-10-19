package com.mcherm.zithiacharsheet.client.model.weapon;

/**
 * This is a weapons skill that a character can learn, such
 * as "hand axe" or "all ranged weapons". A character could
 * learn either basic training OR levels with the skill.
 * <p>
 * Instances are immutable.
 */
public abstract class WeaponSkill {

    public abstract String getId();
    public abstract String getName();
    public abstract int getBasicTrainingCost();
    public abstract int getFirstLevelCost();
    public abstract int getSpan();

}
