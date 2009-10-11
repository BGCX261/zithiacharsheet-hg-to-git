package com.mcherm.zithiacharsheet.client.model.weapon;

import java.util.ArrayList;
import java.util.List;
import com.mcherm.zithiacharsheet.client.model.Util;


/**
 * This is an a weapon skill that a character actually possesses.
 * They may have basic training in it, levels in it, or even neither.
 * The WeaponTraining instances for a character form a tree, where
 * each level points to the contained elements of the span lower
 * than it and the character itself contains the span-4 WeaponTraining.
 * <p>
 * DESIGN NOTE: The problem of notifications has NOT been solved. One
 * possibility is to make these immutable and just clobber the whole
 * thing whenever it changes.
 */
public class WeaponTraining {
    private final WeaponTraining parent;
    private final List<WeaponTraining> children;
    private final WeaponSkill weaponSkill;
    private boolean basicTraingPurchased;
    private int levelsPurchased;
    
    private WeaponTraining(WeaponTraining parent, WeaponSkill weaponSkill) {
        this.parent = parent;
        this.children = new ArrayList<WeaponTraining>();
        this.weaponSkill = weaponSkill;
        this.basicTraingPurchased = false;
        this.levelsPurchased = 0;
    }
    
    public WeaponTraining getParent() {
        return parent;
    }
    
    /**
     * Callers must not modify the list that gets returned.
     */
    public List<WeaponTraining> getChildren() {
        return children;
    }
    
    public WeaponSkill getWeaponSkill() {
        return weaponSkill;
    }
    
    /**
     * Returns true if the character has paid for training with
     * this particular WeaponSkill.
     */
    public boolean getBasicTrainingPurchased() {
        return basicTraingPurchased;
    }
    
    /**
     * Returns true if the character is trained in the use of
     * this WeaponSkill or some more broad category that includes
     * it.
     */
    public boolean isTrained() {
        return basicTraingPurchased || (parent != null && parent.isTrained());
    }
    
    /**
     * Used to set whether basic training is purchased for this.
     */
    public void setBasicTrainingPurchased(boolean b) {
        basicTraingPurchased = b;
    }
    
    /**
     * Returns the number of levels of this weapon skill that the
     * character has paid for.
     */
    public int getLevelsPurchased() {
        return levelsPurchased;
    }
    
    /**
     * Returns the number of levels that the character can use with
     * this weapon or weapons in this weapon group.
     */
    public int getLevels() {
        return levelsPurchased + (parent == null ? 0 : parent.getLevels());
    }
    
    /**
     * Used to set the number of levels purchased.
     */
    public void setLevelsPurchased(int levels) {
        levelsPurchased = levels;
    }
    
    /**
     * Returns the cost for just this particular WeaponTraining, not
     * including any parent or child WeaponTrainings.
     */
    public int getThisCost() {
        int basicTrainingCost = getBasicTrainingPurchased() ? weaponSkill.getBasicTrainingCost() : 0;
        int firstLevelCost = weaponSkill.getFirstLevelCost();
        int levels = getLevelsPurchased();
        return Util.skillCost(basicTrainingCost, firstLevelCost, levels);
    }
    
    /**
     * Returns the cumulative cost for this particular WeaponTraining and all
     * child WeaponTrainings.
     */
    public int getTotalCost() {
        int result = getThisCost();
        for (WeaponTraining child : children) {
            result += child.getTotalCost();
        }
        return result;
    }
    
    /**
     * This creates a new WeaponTraining which is a child of this one.
     * 
     * @param weaponSkill the WeaponSkill of this child. Must have a
     *   span which is one less than the span of this WeaponTraining's skill.
     * @return the newly created WeaponTraining.
     */
    public WeaponTraining createChild(WeaponSkill weaponSkill) {
        return new WeaponTraining(this, weaponSkill);
    }
    
    /**
     * This creates the top-level WeaponTraining for a character.
     */
    public static WeaponTraining createAllCombatTraining() {
        return new WeaponTraining(null, WeaponsCatalog.getSingleton().getAllCombatSkill());
    }
    
}
