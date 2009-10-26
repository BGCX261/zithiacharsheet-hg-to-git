package com.mcherm.zithiacharsheet.client.model.weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private final WeaponClusterSkill allCombatSkill;
    private final Map<String,WeaponSkill> weaponSkillById;
    private final Map<WeaponClusterSkill,List<WeaponSkill>> skillChildren;
    
    public WeaponsCatalog() {
        weaponSkillById = new HashMap<String,WeaponSkill>();
        LineReader lineReader = new LineReader(Arrays.asList(getData()));
        lineReader.processLines();
        allCombatGroup = lineReader.singleSpan4Group;
        allCombatSkill = lineReader.singleSpan4Skill;
        weapons = Collections.unmodifiableList(lineReader.weaponsList);
        skillChildren = 
            new HashMap<WeaponClusterSkill,List<WeaponSkill>>(lineReader.skillChildren.size());
        for (WeaponClusterSkill key : lineReader.skillChildren.keySet()) {
            skillChildren.put(key, Collections.unmodifiableList(lineReader.skillChildren.get(key)));
        }
    }
    
    /**
     * An inner class that exists just to carry some state around among some recursive
     * routines used when setting up the catalog.
     */
    private class LineReader {
        private final List<String[]> lines;

        public List<Weapon> weaponsList = new ArrayList<Weapon>();
        public Map<WeaponClusterSkill, List<WeaponSkill>> skillChildren = 
            new HashMap<WeaponClusterSkill, List<WeaponSkill>>();
        private WeaponCluster singleSpan4Group;
        private WeaponClusterSkill singleSpan4Skill;
        
        private WeaponCluster currentSpan3Group;
        private WeaponGroup currentSpan2Group;
        private List<WeaponSkill> currentSpan3Skills;
        private List<WeaponSkill> currentSpan2Skills;
        private List<WeaponSkill> currentSpan1Skills;
        
        public LineReader(List<String[]> lines) {
            this.lines = lines;
        }
        
        public void processLines() {
            for (String[] line : lines) {
                String spanIndicator = line[0];
                WeaponSkill weaponSkill;
                if ("1:".equals(spanIndicator)) {
                    weaponSkill = doSpan1(line);
                } else if ("2:".equals(spanIndicator)) {
                    weaponSkill = doSpan2(line);
                } else if ("3:".equals(spanIndicator)) {
                    weaponSkill = doSpan3(line);
                } else if ("4:".equals(spanIndicator)) {
                    weaponSkill = doSpan4(line);
                } else {
                    throw new RuntimeException("Invalid span indicator.");
                }
                if (weaponSkillById.containsKey(weaponSkill.getId())) {
                    throw new RuntimeException("Duplicate WeaponSkill id: " + weaponSkill.getId());
                }
                weaponSkillById.put(weaponSkill.getId(), weaponSkill);
            }
        }
        
        private WeaponClusterSkill doSpan4(String[] line) {
            final int span = 4;
            final String id = line[1];
            final String name = line[2];
            final int trainingCost = Integer.parseInt(line[3]);
            final int firstLevelCost = Integer.parseInt(line[4]);
            singleSpan4Group = new WeaponCluster(null, id, name, span);
            WeaponClusterSkill newSkill = new WeaponClusterSkill(singleSpan4Group, trainingCost, firstLevelCost);
            singleSpan4Skill = newSkill;
            currentSpan3Skills = new ArrayList<WeaponSkill>();
            skillChildren.put(newSkill, currentSpan3Skills);
            return newSkill;
        }
        
        private WeaponClusterSkill doSpan3(String[] line) {
            final int span = 3;
            final String id = line[1];
            final String name = line[2];
            final int trainingCost = Integer.parseInt(line[3]);
            final int firstLevelCost = Integer.parseInt(line[4]);
            currentSpan3Group = new WeaponCluster(singleSpan4Group, id, name, span);
            WeaponClusterSkill newSkill = new WeaponClusterSkill(currentSpan3Group, trainingCost, firstLevelCost);
            currentSpan3Skills.add(newSkill);
            currentSpan2Skills = new ArrayList<WeaponSkill>();
            skillChildren.put(newSkill, currentSpan2Skills);
            return newSkill;
        }
        
        private WeaponClusterSkill doSpan2(String[] line) {
            final int span = 2;
            final String id = line[1];
            final String name = line[2];
            final int groupTrainingCost = 2;
            final int groupLevelCost = 3;
            currentSpan2Group = new WeaponGroup(currentSpan3Group, id, name, span);
            WeaponClusterSkill newSkill = new WeaponClusterSkill(currentSpan2Group, groupTrainingCost, groupLevelCost);
            currentSpan2Skills.add(newSkill);
            currentSpan1Skills = new ArrayList<WeaponSkill>();
            skillChildren.put(newSkill, currentSpan1Skills);
            return newSkill;
        }
        
        private SingleWeaponSkill doSpan1(String[] line) {
            String id = line[1];
            String name = line[2];
            int strMin = Integer.parseInt(line[3]);
            int spd = Integer.parseInt(line[4]);
            Weapon weapon = new Weapon(currentSpan2Group, id, name, strMin, spd);
            weaponsList.add(weapon);
            final int basicTrainingCost = 1;
            final int firstLevelCost = 2;
            SingleWeaponSkill weaponSkill = new SingleWeaponSkill(weapon, basicTrainingCost, firstLevelCost);
            currentSpan1Skills.add(weaponSkill);
            return weaponSkill;
        }
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
    
    /**
     * Attempts to find a WeaponSkill from an Id. Return null if it is
     * not found, or a WeaponSkill if it is.
     */
    public WeaponSkill getWeaponSkillById(String id) {
        return weaponSkillById.get(id);
    }
        
    /**
     * This returns a list of the known child skills for the given
     * parent.
     */
    public List<WeaponSkill> getChildren(WeaponClusterSkill parent) {
        return skillChildren.get(parent);
    }
    
    private String[][] getData() {
        return new String[][] {
            // 1: id, name strMin spd, hpDmg stunDmg
            {"4:", "allcombat", "All Combat", "10", "7"},
            {"3:", "melee", "Melee Weapons", "6", "5"},
            {"2:", "daggers", "Daggers"},
            {"1:", "knife", "Knife", "3", "3", "1D2-1", "1D3-1"},
            {"1:", "dagger", "Dagger", "3", "5", "1D2", "1D3"},
            {"1:", "stiletto", "Stiletto", "3", "5", "1D3-1", "1D2"},
            {"2:", "swords", "Swords"},
            {"1:", "shortsword", "Short Sword", "4", "8", "1D4", "1D6"},
            {"1:", "rapier", "Rapier", "7", "6", "1D4", "1D6"},
            {"1:", "scimitar", "Scimitar", "10", "5", "1D6", "1D8"},
            {"1:", "longsword", "Long Sword", "11", "6", "2D3", "1D8+1"},
            {"1:", "broadsword", "Broadsword", "12", "6", "1D8", "1D10"},
            {"1:", "bastard1h", "Bastard Sword (1-handed)", "15", "7", "2D4+1", "2D6+1"},
            {"2:", "2hswords", "Two-Handed Swords"},
            {"1:", "bastard2h", "Bastard Sword (2-handed)", "13", "7", "2D4+1", "2D6+1"},
            {"1:", "2hsword", "Two-Handed Sword", "17", "10", "2D8+1", "2D12"},
            {"2:", "axes", "Axes"},
            {"1:", "handaxe", "Hand Axe", "8", "5", "1D6", "1D4"},
            {"1:", "francisca", "Francisca", "12", "6", "1D10", "2D4-1"},
            {"1:", "battleaxe", "Battle Axe", "14", "7", "2D6", "2D4"},
            {"1:", "bardiche", "Bardiche", "16", "9", "1D20", "2D6+1"},
            {"1:", "halbard", "Halbard", "18", "9", "2D10+1", "2D8"},
            {"2:", "clubmace", "Club/Mace"},
            {"1:", "hammer", "Hammer", "10", "5", "1D3-1", "1D6+2"},
            {"1:", "mace", "Mace", "13", "7", "1D6", "3D4+1"},
            {"2:", "tridents", "Trident"},
            {"1:", "trident", "Trident", "9", "9", "1D8", "1D8"},
            {"2:", "naturalweap", "Natural Weaponry"},
            {"2:", "unarmed", "Unarmed"},
            {"1:", "fists", "Unarmed", "0", "0", "0-Str", "0"},
            {"3:", "ranged", "Ranged Weapons", "5", "5"},
            {"2:", "bows", "Bow"},
            {"1:", "shortlightbow", "Shortbow, Light Pull", "8", "5", "1D6", "1D6-1"},
            {"1:", "shortmediumbow", "Showtbow, Medium Pull", "11", "5", "1D6", "1D6"},
            {"1:", "shortheavybow", "Showtbow, Heavy Pull", "15", "5", "1D6+1", "1D6"},
            {"1:", "longlightbow", "Longbow, Light Pull", "12", "7", "1D6", "1D6"},
            {"1:", "longmediumbow", "Longbow, Medium Pull", "16", "7", "1D6+1", "1D6"},
            {"1:", "longheavybow", "Longbow, Heavy Pull", "20", "7", "1D6+1", "1D6+1"},
        };
    }
    
}
