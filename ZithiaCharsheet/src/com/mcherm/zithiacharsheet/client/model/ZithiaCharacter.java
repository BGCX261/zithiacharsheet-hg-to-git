package com.mcherm.zithiacharsheet.client.model;


/**
 * A complete character sheet.
 */
public class ZithiaCharacter {

    private final StatValues statValues;
    private final SkillList skillList;
    private final WeaponTraining weaponTraining;
    private final ZithiaCosts zithiaCosts;
    
    /**
     * Creates a default blank character sheet.
     */
    public ZithiaCharacter() {
        statValues = new StatValues();
        skillList = new SkillList(statValues);
        addNewSkill(SkillCatalog.get("climbing"));
        addNewSkill(SkillCatalog.get("stealth"));
        weaponTraining = WeaponTraining.createAllCombatTraining();
        zithiaCosts = new ZithiaCosts(statValues, skillList, weaponTraining);
    }
    
    public StatValues getStatValues() {
        return statValues;
    }
    
    public SkillList getSkillList() {
        return skillList;
    }
    
    /**
     * Return the value of a particular stat.
     */
    public StatValue getStat(ZithiaStat stat) {
        return statValues.getStat(stat);
    }
    
    /**
     * Call this to add a new skill to the character.
     */
    public SkillValue addNewSkill(ZithiaSkill skill) {
        return skillList.addNewSkill(skill);
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
