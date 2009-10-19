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
     * @param stat the stat it is based on, or null.
     * @param hasRoll true if this is a normal skill with a roll, false
     *   if there is no roll associated with it.
     * @param baseCost the base cost of purchasing this skill
     * @param firstLevelCost the cost for the first level with the skill,
     *   or 0 if the skill has no meaningful levels or roll.
     * @param name the name of the skill
     * @param id the unique ID of the skill in the SkillCatalog
     */
    public ZithiaSkill(ZithiaStat stat, boolean hasRoll,
                       int baseCost, int firstLevelCost, String name, String id)
    {
        if (stat == null && hasRoll == true) {
            throw new RuntimeException("A skill with a roll must have an associated stat.");
        }
        this.stat = stat;
        this.hasRoll = hasRoll;
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
