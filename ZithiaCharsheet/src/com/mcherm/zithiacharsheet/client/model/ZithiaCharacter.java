package com.mcherm.zithiacharsheet.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ZithiaCharacter {

    private final List<StatValue> stats;
    private final List<SkillValue> skills;
    
    /**
     * Creates a default blank character sheet.
     */
    public ZithiaCharacter() {
        // -- starting stats --
        List<StatValue> statList = new ArrayList<StatValue>();
        for (final ZithiaStat stat : ZithiaStat.values()) {
            statList.add(new StatValue(stat));
        }
        stats = Collections.unmodifiableList(statList);
        
        // -- starting skills --
        skills = new ArrayList<SkillValue>();
        skills.add(new SkillValue(SkillCatalog.getSkillX("climbing"), this));
        skills.add(new SkillValue(SkillCatalog.getSkillX("stealth"), this));
    }
    
    public List<StatValue> getStats() {
        return stats;
    }
    
    public List<SkillValue> getSkills() {
        return skills;
    }
    
    /**
     * Return the value of a particular stat.
     */
    public StatValue getStat(ZithiaStat stat) {
        return stats.get(stat.ordinal());
    }

}
