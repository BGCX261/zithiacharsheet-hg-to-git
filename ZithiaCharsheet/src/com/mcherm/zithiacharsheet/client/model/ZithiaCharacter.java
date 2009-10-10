package com.mcherm.zithiacharsheet.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mcherm.zithiacharsheet.client.model.weapon.WeaponTraining;


public class ZithiaCharacter {

    private final List<StatValue> stats;
    private final SkillList skills;
    private final WeaponTraining weaponTraining;
    private final ZithiaCosts zithiaCosts;
    
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
        skills = new SkillList();
        skills.addSkillValue(new SkillValue(SkillCatalog.get("climbing"), this));
        skills.addSkillValue(new SkillValue(SkillCatalog.get("stealth"), this));
        
        // -- weapon training --
        weaponTraining = WeaponTraining.createAllCombatTraining();
        
        // -- costs --
        zithiaCosts = new ZithiaCosts(this);
    }
    
    public List<StatValue> getStats() {
        return stats;
    }
    
    public SkillList getSkills() {
        return skills;
    }
    
    /**
     * Return the value of a particular stat.
     */
    public StatValue getStat(ZithiaStat stat) {
        return stats.get(stat.ordinal());
    }
    
    /**
     * Call this to add a new skill to the character.
     */
    public void addNewSkill(ZithiaSkill skill) {
        SkillValue skillValue = new SkillValue(skill, this);
        skills.addSkillValue(skillValue);
    }
    
    /**
     * Obtains the WeaponTraining.
     */
    public WeaponTraining getWeaponTraining() {
        return weaponTraining;
    }
    
    public ZithiaCosts getCosts() {
        return zithiaCosts;
    }

}
