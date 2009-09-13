package com.mcherm.zithiacharsheet.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The master list of well-known skills. It uses the singleton pattern.
 */
public class SkillCatalog {
    
    private static final SkillCatalog singletonInstance = new SkillCatalog();
    
    public static SkillCatalog getSingleton() {
        return singletonInstance;
    }
    
    public static ZithiaSkill get(String id) {
        ZithiaSkill result = getSingleton().getSkill(id);
        if (result == null) {
            throw new RuntimeException("Skill '" + id + "' not found in catelog.");
        } else {
            return result;
        }
    }
    
    private final Map<String,ZithiaSkill> skillMap;
    private final List<SkillCategory> skillCategories;

    
    
    /**
     * Constructor. For now it initializes the data.
     */
    private SkillCatalog() {
        String[][] skillData = getData();
        
        Map<String,ZithiaSkill> skillMap = new HashMap<String,ZithiaSkill>();
        List<SkillCategory> skillCategories = new ArrayList<SkillCategory>();

        final Map<String,ZithiaStat> STAT_MAP = new HashMap<String,ZithiaStat>() { {
            for (ZithiaStat stat : ZithiaStat.values()) {
                put(stat.getName(), stat);
            }
        } };
        
        String currentCategoryName = null;
        List<ZithiaSkill> currentCategorySkills = null;
        int lineNum = 0;
        for (String[] skillDataRow : skillData) {
            lineNum++;
            if (skillDataRow.length == 1) {
                if (currentCategorySkills != null) {
                    skillCategories.add(new SkillCategory(currentCategoryName, currentCategorySkills));
                }
                currentCategoryName = skillDataRow[0];
                currentCategorySkills = new ArrayList<ZithiaSkill>();
            } else if (skillDataRow.length == 5) {
                if (currentCategorySkills == null) {
                    throw new RuntimeException("No category defined on line " + lineNum);
                }
                final String id = skillDataRow[0];
                if (skillMap.containsKey(id)) {
                    throw new RuntimeException("Duplicate skill id on line " + lineNum);
                }
                final String statKey = skillDataRow[1];
                final ZithiaStat stat = STAT_MAP.get(statKey);
                if (stat == null && statKey != "n/a") {
                    throw new RuntimeException("Syntax error in line " + lineNum);
                }
                final boolean hasRoll = stat != null;
                final int baseCost, firstLevelCost;
                try {
                    baseCost = Integer.parseInt(skillDataRow[2]);
                    firstLevelCost = Integer.parseInt(skillDataRow[3]);
                } catch(NumberFormatException err) {
                    throw new RuntimeException("Syntax error on line "  + lineNum);
                }
                final String name = skillDataRow[4];
                final ZithiaSkill newSkill = new ZithiaSkill(stat, hasRoll, baseCost, firstLevelCost, name);
                skillMap.put(id, newSkill);
                currentCategorySkills.add(newSkill);
            } else {
                throw new RuntimeException("Syntax error on line " + lineNum);
            }
        }
        if (currentCategorySkills != null) {
            skillCategories.add(new SkillCategory(currentCategoryName, currentCategorySkills));
        }
        this.skillMap = Collections.unmodifiableMap(skillMap);
        this.skillCategories = Collections.unmodifiableList(skillCategories);
    }
    
    /**
     * Return a skill by its id (a short, unique, well-formed version of the
     * skill name).
     * 
     * @param id the id of the skill to find
     * @return the skill object or null if not found
     */
    public ZithiaSkill getSkill(String id) {
        return skillMap.get(id);
    }

    
    /**
     * Skills are grouped into categories. The skill category simply has a name and a list
     * of skills.
     */
    public static class SkillCategory {
        private final String name;
        private final List<ZithiaSkill> skills;
        public SkillCategory(String name, List<ZithiaSkill> skills) {
            this.name = name;
            this.skills = Collections.unmodifiableList(skills);
        }
        public String getName() {
            return name;
        }
        public List<ZithiaSkill> getSkills() {
            return skills;
        }
    }
    
    public List<SkillCategory> getSkillCategories() {
        return skillCategories;
    }
    
    private String[][] getData() {
        return new String[][] {
                {"Everyman Skills"},
                {"climbing", "Dex", "0", "2", "Climbing"},
                {"stealth", "Dex", "0", "2", "Stealth"},
                {"Game Skills"},
                {"bowling", "Str", "1", "2", "Bowling"},
                {"Spy Skills"},
                {"spymaster", "Int", "4", "3", "Spymaster"},
        };
    }

}
