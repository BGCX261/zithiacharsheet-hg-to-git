package com.mcherm.zithiacharsheet.client.model;

import java.util.Arrays;
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
            throw new RuntimeException("Skill '" + id + "' not found in catalog.");
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
        Map<String,ZithiaSkill> skillMap = new HashMap<String,ZithiaSkill>();
        
        SkillCategory[] skillCategories = GameData.getSkillData();
        for (SkillCategory skillCategory : skillCategories) {
            for (ZithiaSkill skill : skillCategory.getSkills()) {
                final String id = skill.getId();
                if (skillMap.containsKey(id)) {
                    throw new RuntimeException("Duplicate skill id '" + id + "'.");
                } else {
                    skillMap.put(id, skill);
                }
            }
        }
        this.skillMap = Collections.unmodifiableMap(skillMap);
        this.skillCategories = Collections.unmodifiableList(Arrays.asList(skillCategories));
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
        public SkillCategory(String name, ZithiaSkill... skills) {
            this.name = name;
            this.skills = Collections.unmodifiableList(Arrays.asList(skills));
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
    
}
