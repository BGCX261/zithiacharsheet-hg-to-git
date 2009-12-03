/*
 * Copyright 2009 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.mcherm.zithiacharsheet.client.model;


/**
 * A complete character sheet.
 */
public class ZithiaCharacter {

    private final RaceValue raceValue;
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
        raceValue = new RaceValue();
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

    public RaceValue getRaceValue() {
        return raceValue;
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
