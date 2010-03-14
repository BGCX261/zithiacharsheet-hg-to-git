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

import com.mcherm.zithiacharsheet.client.modeler.Disposable;
import com.mcherm.zithiacharsheet.client.modeler.Disposer;
import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;


/**
 * A complete character sheet.
 */
public class ZithiaCharacter implements Disposable {

    private final Disposer disposer = new Disposer();
    private final RaceValue raceValue;
    private final ArmorValue armorValue;
    private final StatValues statValues;
    private final SkillList skillList;
    private final WeaponTraining weaponTraining;
    private final TalentList talentList;
    private final ZithiaCosts zithiaCosts;
    private final CombatValues combatValues;
    private final Names names;
    private final CharacterNotes characterNotes;
    private Race previousRace; // Used by RaceChangeObserver. null means don't update stats
    
    /**
     * Creates a default blank character sheet.
     */
    public ZithiaCharacter() {
        raceValue = new RaceValue();
        armorValue = new ArmorValue();
        statValues = new StatValues(raceValue, armorValue);
        skillList = new SkillList(statValues);
        addNewSkill(SkillCatalog.get("climbing"));
        addNewSkill(SkillCatalog.get("stealth"));
        weaponTraining = WeaponTraining.createAllCombatTraining();
        talentList = new TalentList();
        zithiaCosts = new ZithiaCosts(raceValue, statValues, skillList, weaponTraining, talentList);
        combatValues = new CombatValues(statValues);
        names = new Names();
        characterNotes = new CharacterNotes();

        // FIXME: Probably some other fields need disposing also. The disposing isn't universal yet.
        disposer.addDisposable(raceValue);
        disposer.addDisposable(skillList);
        disposer.addDisposable(talentList);
        disposer.observe(raceValue.getRace(), new RaceChangeObserver());
        changeStatsOnRaceUpdate(true);
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

    public CombatValues getCombatValues() {
        return combatValues;
    }

    public ArmorValue getArmorValue() {
        return armorValue;
    }
    
    public Names getNames() {
        return names;
    }

    public CharacterNotes getCharacterNotes() {
        return characterNotes;
    }

    public void dispose() {
        disposer.dispose();
    }

    /**
     * We want to make sure that when a user switches the race of a
     * character that we adjust the stat values. But we want the stat
     * values themselves to be directly editable also. And we can't be
     * editing the stat values when loading a saved character.
     * <p>
     * The solution is that we set up this listener to observe all race
     * changes. It keeps a 'memory' of what the old race was and modifies
     * the stats based on the difference in racial modifiers. The VERY
     * FIRST time it is called the 'memory' will be empty and there will
     * be no updates (that should be when the character is loaded).
     * <p>
     * I am not confident that this is a good solution; it may need to be
     * revised later.
     */
    private class RaceChangeObserver implements Observer {
        public void onChange() {
            if (previousRace != null) {
                Race newRace = getRaceValue().getRace().getValue();
                for (ZithiaStat stat : ZithiaStat.values()) {
                    int changeInModifier = newRace.getModifier(stat) - previousRace.getModifier(stat);
                    SettableIntValue statValue = getStat(stat).getValue();
                    statValue.setValue(statValue.getValue() + changeInModifier);
                }
                previousRace = newRace;
            }
        }
    }

    /**
     * Use this to set whether the stats are updated when the race is changed. true
     * means that they will be updated (this is the default) and false means that
     * they won't be (useful when loading a character, for example).
     */
    public void changeStatsOnRaceUpdate(boolean change) {
        if (change) {
            previousRace = getRaceValue().getRace().getValue();
        } else {
            previousRace = null;
        }
    }
}
