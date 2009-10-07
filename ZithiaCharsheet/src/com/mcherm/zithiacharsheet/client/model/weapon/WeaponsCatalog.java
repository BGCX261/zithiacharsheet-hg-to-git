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
    private final Map<WeaponClusterSkill,List<WeaponSkill>> skillChildren;
    
    public WeaponsCatalog() {
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
                if ("1:".equals(spanIndicator)) {
                    doSpan1(line);
                } else if ("2:".equals(spanIndicator)) {
                    doSpan2(line);
                } else if ("3:".equals(spanIndicator)) {
                    doSpan3(line);
                } else if ("4:".equals(spanIndicator)) {
                    doSpan4(line);
                } else {
                    throw new RuntimeException("Invalid span indicator.");
                }
            }
        }
        
        private void doSpan4(String[] line) {
            final int span = 4;
            final String name = line[1];
            final int trainingCost = Integer.parseInt(line[2]);
            final int firstLevelCost = Integer.parseInt(line[3]);
            singleSpan4Group = new WeaponCluster(null, name, span);
            WeaponClusterSkill newSkill = new WeaponClusterSkill(singleSpan4Group, trainingCost, firstLevelCost);
            singleSpan4Skill = newSkill;
            currentSpan3Skills = new ArrayList<WeaponSkill>();
            skillChildren.put(newSkill, currentSpan3Skills);
        }
        
        private void doSpan3(String[] line) {
            final int span = 3;
            final String name = line[1];
            final int trainingCost = Integer.parseInt(line[2]);
            final int firstLevelCost = Integer.parseInt(line[3]);
            currentSpan3Group = new WeaponCluster(singleSpan4Group, name, span);
            WeaponClusterSkill newSkill = new WeaponClusterSkill(currentSpan3Group, trainingCost, firstLevelCost);
            currentSpan3Skills.add(newSkill);
            currentSpan2Skills = new ArrayList<WeaponSkill>();
            skillChildren.put(newSkill, currentSpan2Skills);
        }
        
        private void doSpan2(String[] line) {
            final int span = 2;
            final String name = line[1];
            final int groupTrainingCost = 2;
            final int groupLevelCost = 3;
            currentSpan2Group = new WeaponGroup(currentSpan3Group, name, span);
            WeaponClusterSkill newSkill = new WeaponClusterSkill(currentSpan2Group, groupTrainingCost, groupLevelCost);
            currentSpan2Skills.add(newSkill);
            currentSpan1Skills = new ArrayList<WeaponSkill>();
            skillChildren.put(newSkill, currentSpan1Skills);
        }
        
        private void doSpan1(String[] line) {
            String name = line[1];
            int strMin = Integer.parseInt(line[2]);
            int spd = Integer.parseInt(line[3]);
            Weapon weapon = new Weapon(currentSpan2Group, name, strMin, spd);
            weaponsList.add(weapon);
            final int basicTrainingCost = 1;
            final int firstLevelCost = 2;
            SingleWeaponSkill weaponSkill = new SingleWeaponSkill(weapon, basicTrainingCost, firstLevelCost);
            currentSpan1Skills.add(weaponSkill);
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
     * This returns a list of the known child skills for the given
     * parent.
     */
    public List<WeaponSkill> getChildren(WeaponClusterSkill parent) {
        return skillChildren.get(parent);
    }
    
    private String[][] getData() {
        return new String[][] {
            {"4:", "All Combat", "10", "7"},
            {"3:", "Melee Weapons", "6", "5"},
            {"2:", "Daggers"},
            {"1:", "Knife", "3", "3"},
            {"1:", "Dagger", "3", "5"},
            {"1:", "Stiletto", "3", "5"},
            {"2:", "Swords"},
            {"1:", "Short Sword", "4", "8"},
            {"3:", "Ranged Weapons", "5", "5"},
        };
    }
    
}
