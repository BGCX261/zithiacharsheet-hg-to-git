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

import com.mcherm.zithiacharsheet.client.model.weapon.WeaponClusterSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponsCatalog;
import com.mcherm.zithiacharsheet.client.modeler.SettableBooleanValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableEnumValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableStringValue;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;
import com.mcherm.zithiacharsheet.client.util.JSONSerializerBase;


/**
 * This serializes a ZithiaCharsheet into a String in JSON format. To use, create
 * an instance using the newInstance() factory method (optionally specifying whether
 * to use "prettyPrinting"). Then call the serialize() method with a ZithiaCharacter.
 * Finally, call the output() method to obtain the results as a String.
 */
public class JSONSerializer extends JSONSerializerBase {

    private final InMemoryWriter inMemoryWriter;


    private static class InMemoryWriter implements Writer {
        private final StringBuilder stringBuilder = new StringBuilder();
        public void write(String s) {
            stringBuilder.append(s);
        }
        public String getOutput() {
            return stringBuilder.toString();
        }
    }

    /** Constructor is private: use factory function. */
    private JSONSerializer(InMemoryWriter inMemoryWriter, boolean prettyPrint) {
        super(inMemoryWriter, prettyPrint);
        this.inMemoryWriter = inMemoryWriter;
    }



    /**
     * Factory function to obtain instances.
     */
    public static JSONSerializer newInstance() {
        return newInstance(false); // default prettyPrint to false.
    }

    /**
     * Factory function to obtain instances.
     */
    public static JSONSerializer newInstance(boolean prettyPrint) {
        return new JSONSerializer(new InMemoryWriter(), prettyPrint);
    }

    /**
     * Extracts the output after the whole thing is serialized. If this is called
     * while the tree is unbalanced (opened more things than were closed) then an
     * exception will be thrown.
     */
    public String output() {
        if (!isDone()) {
            throw new RuntimeException("JSON output obtained before the tree was completed.");
        } else {
            return inMemoryWriter.getOutput();
        }
    }

    protected void serialize(String fieldName, TweakableIntValue value) {
        if (value.isTweaked()) {
            emitStartDictItem(fieldName);
            emitStartDict();
            if (value.getOverride() != null) {
                emitDictItem("override", value.getOverride());
            }
            if (value.getModifier() != null) {
                emitDictItem("modifier", value.getModifier());
            }
            emitEndDict();
        }
    }
    
    protected void serialize(String fieldName, SettableIntValue value) {
        emitDictItem(fieldName, value.getValue());
    }
    
    protected void serialize(String fieldName, SettableBooleanValue value) {
        emitDictItem(fieldName, value.getValue());
    }
    
    protected void serialize(String fieldName, SettableStringValue value) {
        if (!"".equals(value.getValue())) {
            emitDictItem(fieldName, value.getValue());
        }
    }

    protected void serialize(String fieldName, SettableEnumValue<?> settableEnumValue) {
        emitDictItem(fieldName, settableEnumValue.getValue().name());
    }

    protected void serialize(StatValue statValue) {
        emitStartDict();
        emitDictItem("stat", statValue.getStat().getName()); // NOTE: this is ONLY for readability
        serialize("value", statValue.getValue());
        serialize("roll", statValue.getRoll());
        serialize("cost", statValue.getCost());
        emitEndDict();
    }
    
    protected void serialize(String fieldName, StatValues statValues) {
        emitStartDictItem(fieldName);
        emitStartList();
        for (StatValue statValue : statValues) {
            emitStartListItem();
            serialize(statValue);
        }
        emitEndList();
    }
    
    protected void serialize(String fieldName, ZithiaSkill skill) {
        emitStartDictItem(fieldName);
        emitStartDict();
        emitDictItem("id", skill.getId());
        emitEndDict();
    }
    
    protected void serialize(SkillValue skillValue) {
        emitStartDict();
        serialize("skill", skillValue.getSkill());
        serialize("levels", skillValue.getLevels());
        if (skillValue.getSkill().hasRoll()) {
            serialize("roll", skillValue.getRoll());
        }
        serialize("cost", skillValue.getCost());
        emitEndDict();
    }
    
    
    protected void serialize(String fieldName, SkillList skillList) {
        emitStartDictItem(fieldName);
        emitStartList();
        for (SkillValue skillValue : skillList) {
            emitStartListItem();
            serialize(skillValue);
        }
        emitEndList();
    }
    
    protected void serialize(String fieldName, WeaponSkill weaponSkill) {
        emitStartDictItem(fieldName);
        emitStartDict();
        emitDictItem("id", weaponSkill.getId());
        emitEndDict();
    }
    
    protected void serialize(String fieldName, WeaponTraining wt) {
        emitStartDictItem(fieldName);
        serialize(wt);
    }
    
    protected void serialize(WeaponTraining wt) {
        emitStartDict();
        serialize("weaponSkill", wt.getWeaponSkill());
        serialize("basicTrainingDesired", wt.getBasicTrainingDesired());
        serialize("levelsPurchased", wt.getLevelsPurchased());
        serialize("levels", wt.getLevels());
        serialize("thisCost", wt.getThisCost());
        serialize("totalCost", wt.getTotalCost());
        if (wt.hasChildren()) {
            emitStartDictItem("children");
            emitStartList();
            for (WeaponTraining child : wt.getChildren()) {
                emitStartListItem();
                serialize(child);
            }
            emitEndList();
        }
        emitEndDict();
    }
    
    protected void serialize(TalentValue talentValue) {
        emitStartDict();
        serialize("description", talentValue.getDescription());
        serialize("cost", talentValue.getCost());
        emitEndDict();
    }
    
    protected void serialize(String fieldName, TalentList talentList) {
        if (!talentList.isEmpty()) {
            emitStartDictItem(fieldName);
            emitStartList();
            for (TalentValue talentValue : talentList) {
                emitStartListItem();
                serialize(talentValue);
            }
            emitEndList();
        }
    }
    
    protected void serialize(String fieldName, ZithiaCosts zithiaCosts) {
        emitStartDictItem(fieldName);
        emitStartDict();
        serialize("raceCost", zithiaCosts.getRaceCost());
        serialize("statCost", zithiaCosts.getStatCost());
        serialize("skillCost", zithiaCosts.getSkillCost());
        serialize("weaponSkillCost", zithiaCosts.getWeaponSkillCost());
        serialize("totalCost", zithiaCosts.getTotalCost());
        serialize("basePts", zithiaCosts.getBasePts());
        serialize("loanPts", zithiaCosts.getLoanPts());
        serialize("expSpent", zithiaCosts.getExpSpent());
        serialize("expEarned", zithiaCosts.getExpEarned());
        serialize("paidForLoan", zithiaCosts.getPaidForLoan());
        serialize("expUnspent", zithiaCosts.getExpUnspent());
        emitEndDict();
    }

    protected void serialize(String fieldName, RaceValue raceValue) {
        if (raceValue.getRace().getValue() != Race.Human) {
            emitStartDictItem(fieldName);
            emitStartDict();
            serialize("race", raceValue.getRace());
            emitEndDict();
        }
    }

    protected void serialize(String fieldName, Names names) {
        emitStartDictItem(fieldName);
        emitStartDict();
        serialize("name", names.getCharacterName());
        serialize("player", names.getPlayerName());
        emitEndDict();
    }

    protected void serialize(String fieldName, CombatValues combatValues) {
        emitStartDictItem(fieldName);
        emitStartDict();
        serialize("offense", combatValues.getOffense());
        serialize("defense", combatValues.getDefense());
        emitEndDict();
    }

    protected void serialize(String fieldName, ArmorValue armorValue) {
        if (! armorValue.hasDefaultSettings()) {
            emitStartDictItem(fieldName);
            emitStartDict();
            serialize("armorType", armorValue.getArmorType());
            serialize("hpBlock", armorValue.getHpBlock());
            serialize("stunBlock", armorValue.getStunBlock());
            serialize("defPenalty", armorValue.getDefPenalty());
            emitEndDict();
        }
    }
    
    protected void serialize(String fieldName, CharacterNotes notes) {
        if (notes.getBackground().getValue().length() != 0) {
            emitStartDictItem(fieldName);
            emitStartDict();
            serialize("background", notes.getBackground());
            emitEndDict();
        }
    }

    public void serialize(ZithiaCharacter zithiaCharacter) {
        emitStartDict();
        serialize("race", zithiaCharacter.getRaceValue());
        serialize("names", zithiaCharacter.getNames());
        serialize("statValues", zithiaCharacter.getStatValues());
        serialize("skillList", zithiaCharacter.getSkillList());
        serialize("weaponTraining", zithiaCharacter.getWeaponTraining());
        serialize("talentList", zithiaCharacter.getTalentList());
        serialize("costs", zithiaCharacter.getCosts());
        serialize("combatValues", zithiaCharacter.getCombatValues());
        serialize("armorValue", zithiaCharacter.getArmorValue());
        serialize("notes", zithiaCharacter.getCharacterNotes());
        emitEndDict();
    }
    
    
    /**
     * Just some tests of JSONSerializer. I can't use tests normally.
     */
    public static void main(String[] args) {
        final SkillCatalog skillCatalog = SkillCatalog.getSingleton();
        final WeaponsCatalog weaponsCatalog = WeaponsCatalog.getSingleton();
        final JSONSerializer jss = JSONSerializer.newInstance(true);
        ZithiaCharacter character = new ZithiaCharacter();
        character.getRaceValue().getRace().setValue(Race.Elf);
        character.getNames().getCharacterName().setValue("Entarm");
        character.getStat(ZithiaStat.OBS).getValue().setValue(16);
        SkillValue skillValue = character.addNewSkill(skillCatalog.getSkill("flowers"));
        skillValue.getLevels().setValue(3);
        final WeaponTraining allCombatTraining = character.getWeaponTraining();
        // Buy 1 level with each tier-2 skill
        for (WeaponSkill weaponSkill : weaponsCatalog.getChildren(((WeaponClusterSkill) allCombatTraining.getWeaponSkill()))) {
            WeaponTraining newWT = allCombatTraining.createChild(weaponSkill);
            newWT.getLevelsPurchased().setValue(2);
            if (newWT.getWeaponSkill().getName().equals("melee")) {
                newWT.createChild(weaponsCatalog.getWeaponSkillById("swords")).getLevelsPurchased().setValue(4);
            }
        }
        TalentValue talent = new TalentValue();
        talent.getDescription().setValue("Limps");
        talent.getCost().setValue(-4);
        character.getTalentList().add(talent);
        character.getCosts().getWeaponSkillCost().setAdjustments(0, null);
        character.getCosts().getTotalCost().setAdjustments(null, -10);
        jss.serialize(character);
        String output = jss.output();
        System.out.println(output);
    }
}
