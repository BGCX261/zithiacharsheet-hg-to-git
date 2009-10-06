package com.mcherm.zithiacharsheet.client.model.weapon;

import java.util.ArrayList;
import java.util.List;


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
    
    public boolean getBasicTrainingPurchased() {
        return basicTraingPurchased;
    }
    
    public int getLevelsPurchased() {
        return levelsPurchased;
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
