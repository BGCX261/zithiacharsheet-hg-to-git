package com.mcherm.zithiacharsheet.client.model.weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is the master catalog of all weapons, weapon groups, and weapon skills.
 * It is a singleton. It is also immutable after the constructor runs.
 */
public class WeaponsCatalog {
    private final static WeaponsCatalog singletonInstance = new WeaponsCatalog();
    
    public static WeaponsCatalog getSingleton() {
        return singletonInstance;
    }

    
    private final List<Weapon> weapons;
    private final WeaponCluster allCombatGroup;
    private final WeaponCluster meleeGroup;
    private final WeaponCluster rangedGroup;
    private final WeaponSkill allCombatSkill;
    private final WeaponSkill meleeSkill;
    private final WeaponSkill rangedSkill;
    
    public WeaponsCatalog() {
        List<Weapon> weapons = new ArrayList<Weapon>();
        
        allCombatGroup = new WeaponCluster(null, "All Combat", 4);
        allCombatSkill = new WeaponClusterSkill(allCombatGroup, 10, 7);
        meleeGroup = new WeaponCluster(allCombatGroup, "Melee Weapons", 3);
        meleeSkill = new WeaponClusterSkill(meleeGroup, 6, 5);
        WeaponGroup daggers = new WeaponGroup(meleeGroup, "Daggers", 2);
        weapons.add(new Weapon(daggers, "Knife", 3, 3));
        weapons.add(new Weapon(daggers, "Dagger", 3, 5));
        weapons.add(new Weapon(daggers, "Stiletto", 3, 5));
        WeaponGroup swords = new WeaponGroup(meleeGroup, "Swords", 2);
        weapons.add(new Weapon(swords, "Short Sword", 4, 8));
        rangedGroup = new WeaponCluster(allCombatGroup, "Ranged Weapons", 3);
        rangedSkill = new WeaponClusterSkill(rangedGroup, 5, 5);
        
        this.weapons = Collections.unmodifiableList(weapons);
    }
    
    /**
     * Returns a list of all weapons, in order by weapon group.
     */
    public List<Weapon> getWeapons() {
        return weapons;
    }
    
    /**
     * Returns the top-level WeaponCluster. This is a tree
     * and can be walked to find all WeaponClusters.
     */
    public WeaponCluster getAllCombatGroup() {
        return allCombatGroup;
    }
    
    public WeaponSkill getAllCombatSkill() {
        return allCombatSkill;
    }
    
}
