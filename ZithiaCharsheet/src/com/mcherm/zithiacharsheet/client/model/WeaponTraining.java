package com.mcherm.zithiacharsheet.client.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mcherm.zithiacharsheet.client.model.Util;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponsCatalog;
import com.mcherm.zithiacharsheet.client.modeler.CalculatedIntValue;
import com.mcherm.zithiacharsheet.client.modeler.Observable;
import com.mcherm.zithiacharsheet.client.modeler.SummableList.Extractor;
import com.mcherm.zithiacharsheet.client.modeler.CalculatedBooleanValue;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue;
import com.mcherm.zithiacharsheet.client.modeler.ObservableBoolean;
import com.mcherm.zithiacharsheet.client.modeler.ObservableList;
import com.mcherm.zithiacharsheet.client.modeler.SettableBooleanValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableBooleanValueImpl;
import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValueImpl;
import com.mcherm.zithiacharsheet.client.modeler.SummableList;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.CalculatedBooleanValue.BooleanValueCalculator;
import com.mcherm.zithiacharsheet.client.modeler.CalculatedIntValue.ValueCalculator;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation1;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation2;


/**
 * This is an a weapon skill that a character actually possesses.
 * They may have basic training in it, levels in it, or even neither.
 * The WeaponTraining instances for a character form a tree, where
 * each level points to the contained elements of the span lower
 * than it and the character itself contains the span-4 WeaponTraining.
 */
public class WeaponTraining {
    private final WeaponTraining parent;
    private final WeaponSkill weaponSkill;
    private final SettableBooleanValue basicTrainingPurchased;
    private final SettableIntValue levelsPurchased;
    private final EquationIntValue levels;
    private final ObservableBoolean trained;
    private final CalculatedIntValue<Observable> thisCost;
    private final SummableList<WeaponTraining> children;
    private final EquationIntValue totalCost;
    
    private WeaponTraining(final WeaponTraining parent, final WeaponSkill weaponSkill) {
        this.parent = parent;
        this.weaponSkill = weaponSkill;
        final SettableBooleanValue basicTrainingPurchased = new SettableBooleanValueImpl(false);
        this.basicTrainingPurchased = basicTrainingPurchased;
        levelsPurchased = new SettableIntValueImpl(0);
        if (parent == null) {
            levels = new EquationIntValue(levelsPurchased, new Equation1() {
                public int getValue(int levelsPurchased) {
                    return levelsPurchased;
                }
            });
            trained = new CalculatedBooleanValue(
                Arrays.asList(basicTrainingPurchased),
                new BooleanValueCalculator() {
                    public boolean calculateValue() {
                        return basicTrainingPurchased.getValue();
                    }
                }
            );
        } else {
            levels = new EquationIntValue(levelsPurchased, parent.getLevels(), new Equation2() {
                public int getValue(int levelsPurchased, int parentLevels) {
                    return levelsPurchased + parentLevels;
                }
            });
            trained = new CalculatedBooleanValue(
                Arrays.asList(basicTrainingPurchased, parent.isTrained()),
                new BooleanValueCalculator() {
                    public boolean calculateValue() {
                        return basicTrainingPurchased.getValue() || parent.isTrained().getValue();
                    }
                }
            );
        }
        thisCost = new CalculatedIntValue<Observable>(
            Arrays.asList(basicTrainingPurchased, levelsPurchased),
            new ValueCalculator<Observable>() {
                public int calculateValue(Iterable<? extends Observable> inputs) {
                    int basicTrainingCost = basicTrainingPurchased.getValue() ? weaponSkill.getBasicTrainingCost() : 0;
                    int firstLevelCost = weaponSkill.getFirstLevelCost();
                    int levels = levelsPurchased.getValue();
                    return Util.skillCost(basicTrainingCost, firstLevelCost, levels);
                }
            }
        );
        children = new SummableList<WeaponTraining>(new Extractor<WeaponTraining>() {
            public ObservableInt extractValue(WeaponTraining item) {
                return item.getTotalCost();
            }
        });
        totalCost = new EquationIntValue(thisCost, children.getSum(), new Equation2() {
            public int getValue(int thisCost, int childrenCost) {
                return thisCost + childrenCost;
            }
        });
    }
    
    public WeaponTraining getParent() {
        return parent;
    }
    
    public boolean hasChildren() {
        return !children.isEmpty();
    }
    
    public ObservableList<WeaponTraining> getChildren() {
        return children;
    }
    
    public WeaponSkill getWeaponSkill() {
        return weaponSkill;
    }
    
    /**
     * Returns true if the character has paid for training with
     * this particular WeaponSkill.
     */
    public SettableBooleanValue getBasicTrainingPurchased() {
        return basicTrainingPurchased;
    }
    
    /**
     * Returns true if the character is trained in the use of
     * this WeaponSkill or some more broad category that includes
     * it.
     */
    public ObservableBoolean isTrained() {
        return trained;
    }
    
    /**
     * Returns the number of levels of this weapon skill that the
     * character has paid for.
     */
    public SettableIntValue getLevelsPurchased() {
        return levelsPurchased;
    }
    
    /**
     * Returns the number of levels that the character can use with
     * this weapon or weapons in this weapon group.
     */
    public TweakableIntValue getLevels() {
        return levels;
    }
    
    /**
     * Returns the cost for just this particular WeaponTraining, not
     * including any parent or child WeaponTrainings.
     */
    public TweakableIntValue getThisCost() {
        return thisCost;
    }
    
    /**
     * Returns the cumulative cost for this particular WeaponTraining and all
     * child WeaponTrainings.
     */
    public TweakableIntValue getTotalCost() {
        return totalCost;
    }
    
    /**
     * This creates a new WeaponTraining which is a child of this one.
     * 
     * @param weaponSkill the WeaponSkill of this child. Must have a
     *   span which is one less than the span of this WeaponTraining's skill.
     * @return the newly created WeaponTraining.
     */
    public WeaponTraining createChild(WeaponSkill weaponSkill) {
        WeaponTraining result = new WeaponTraining(this, weaponSkill);
        children.add(result);
        return result;
    }
    
    /**
     * This walks this WeaponTraining and all child WeaponTrainings, keeping
     * every item that has basicTrainingPurchased, levelsPurchased, any tweaked
     * values, or has child we keep, and removing all other items. This node itself
     * will still exist, even if empty, but the method returns true if this
     * WeaponTraining can itself be pruned and false if it cannot.
     */
    public boolean prune() {
        boolean hasChild;
        if (children.isEmpty()) {
            hasChild = false;
        } else {
            List<WeaponTraining> childrenToRemove = new ArrayList<WeaponTraining>();
            for (WeaponTraining child : children) {
                if (child.prune()) {
                    childrenToRemove.add(child);
                }
            }
            for (WeaponTraining child : childrenToRemove) {
                children.remove(child);
            }
            hasChild = !children.isEmpty();
        }
        boolean hasBTP = getBasicTrainingPurchased().getValue();
        boolean hasLevels = getLevelsPurchased().getValue() != 0;
        boolean hasTweaks = getLevels().isTweaked() ||  getThisCost().isTweaked() || getTotalCost().isTweaked();
        return !(hasChild || hasBTP || hasLevels || hasTweaks);
    }
    
    /**
     * This removes all children, all tweaks, and sets the levelsPurchased to 0 and
     * basicTrainingPurchased to false. Essentially, it wipes clean this and all children.
     */
    public void clean() {
        for (WeaponTraining child : children) {
            child.clobber();
        }
        children.clear();
        getLevels().setAdjustments(null, null);
        getThisCost().setAdjustments(null, null);
        getTotalCost().setAdjustments(null, null);
        getBasicTrainingPurchased().setValue(false);
        getLevelsPurchased().setValue(0);
    }
    
    /**
     * A subroutine of clean() so that we don't have to carefully reset values
     * on items which will be deleted in a moment.
     * @param resetValues
     */
    private void clobber() {
        for (WeaponTraining child : children) {
            child.clobber();
        }
        children.clear();
    }
    
    /**
     * This creates the top-level WeaponTraining for a character.
     */
    public static WeaponTraining createAllCombatTraining() {
        return new WeaponTraining(null, WeaponsCatalog.getSingleton().getAllCombatSkill());
    }
    
}
