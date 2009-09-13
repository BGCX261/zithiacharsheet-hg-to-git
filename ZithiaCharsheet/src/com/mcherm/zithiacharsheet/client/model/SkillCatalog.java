package com.mcherm.zithiacharsheet.client.model;

import java.util.HashMap;
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
        return getSingleton().getSkill(id);
    }
    
    private final Map<String,ZithiaSkill> skills;
    
    /**
     * Return a skill by its id (a short, unique, well-formed version of the
     * skill name).
     * 
     * @param id the id of the skill to find
     * @return the skill object or null if not found
     */
    public ZithiaSkill getSkill(String id) {
        return skills.get(id);
    }
    
    /**
     * Constructor. For now it initializes the data.
     */
    private SkillCatalog() {
        skills = new HashMap<String,ZithiaSkill>();
        skills.put("climbing", new ZithiaSkill(ZithiaStat.DEX, true, 0, 2, "Climbing"));
        skills.put("stealth", new ZithiaSkill(ZithiaStat.DEX, true, 0, 2, "Stealth"));
    }

}
