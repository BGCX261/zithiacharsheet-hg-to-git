package com.mcherm.zithiacharsheet.client.model;

/**
 * This is an arbitrary skill, such as would be found in the skill
 * catalog.
 */
public class ZithiaSkill {
    
    private final ZithiaStat stat; // contains stat, or 0 if no stat applies
    private final boolean hasRoll;
    private final int baseCost;
    private final int firstLevelCost;
    private final String name;
    private final String id;
    
    
    /**
     * Constructor.
     * 
     * @param id the unique ID of the skill in the SkillCatalog
     * @param stat the stat it is based on, or null if there is no roll
     *   and no stat
     * @param baseCost the base cost of purchasing this skill
     * @param firstLevelCost the cost for the first level with the skill,
     *   or 0 if the skill has no meaningful levels or roll.
     * @param name the name of the skill
     */
    public ZithiaSkill(String id, ZithiaStat stat, int baseCost, int firstLevelCost, String name) {
        this.stat = stat;
        this.hasRoll = stat != null;
        this.baseCost = baseCost;
        this.firstLevelCost = firstLevelCost;
        this.name = name;
        this.id = id;
    }
    
    
    /**
     * Returns the stat that is used to calculate this skill, or returns null
     * if no stat affects this skill.
     */
    public ZithiaStat getStat() {
        return stat;
    }
    
    public boolean hasRoll() {
        return hasRoll;
    }
    
    public int getBaseCost() {
        return baseCost;
    }
    
    /**
     * Returns the first level cost IF there is one, and returns 0 if not.
     */
    public int getFirstLevelCost() {
        return firstLevelCost;
    }
    
    /**
     * Returns the total cost of the indicated number of levels of this skill.
     */
    public int getCost(int levels) {
        return Util.skillCost(baseCost, firstLevelCost, levels);
    }
    
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
}
