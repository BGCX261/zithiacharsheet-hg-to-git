package com.mcherm.zithiacharsheet.client.model;


/**
 * A complete character sheet.
 */
public class ZithiaCharacter {

    private final StatValues statValues;
    private final SkillList skillList;
    private final WeaponTraining weaponTraining;
    private final TalentList talentList;
    private final ZithiaCosts zithiaCosts;
    private final Names names;
    private final CharacterNotes characterNotes;
    
    /**
     * Creates a default blank character sheet.
     */
    public ZithiaCharacter() {
        statValues = new StatValues();
        skillList = new SkillList(statValues);
        addNewSkill(SkillCatalog.get("climbing"));
        addNewSkill(SkillCatalog.get("stealth"));
        weaponTraining = WeaponTraining.createAllCombatTraining();
        talentList = new TalentList();
        zithiaCosts = new ZithiaCosts(statValues, skillList, weaponTraining, talentList);
        names = new Names();
        characterNotes = new CharacterNotes();
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
    
    /**
     * Obtains the talent list.
     */
    public TalentList getTalentList() {
        return talentList;
    }
    
    public ZithiaCosts getCosts() {
        return zithiaCosts;
    }
    
    public Names getNames() {
        return names;
    }

    public CharacterNotes getCharacterNotes() {
        return characterNotes;
    }

}
