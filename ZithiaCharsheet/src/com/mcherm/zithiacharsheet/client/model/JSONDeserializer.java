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

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableBooleanValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableEnumValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableStringValue;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponsCatalog;


/**
 * This can be used to modify an existing ZithiaCharacter so that it
 * exactly matches the content of a JSON file.
 */
public class JSONDeserializer {
    
    protected <T> T notNull(T x) {
        if (x == null) {
            throw new JSONBuildException();
        }
        return x;
    }
    
    
    protected void updateFromField(JSONObject parent, String fieldName, SettableIntValue settableIntValue) {
        JSONValue valueValue = notNull(parent.get(fieldName));
        JSONNumber valueNum = notNull(valueValue.isNumber());
        int valueInt = (int) valueNum.doubleValue();
        settableIntValue.setValue(valueInt);
    }
    
    protected void updateFromField(JSONObject parent, String fieldName, SettableBooleanValue settableBooleanValue) {
        JSONValue valueValue = notNull(parent.get(fieldName));
        JSONBoolean valueBool = notNull(valueValue.isBoolean());
        settableBooleanValue.setValue(valueBool.booleanValue());
    }
    
    protected void updateFromField(JSONObject parent, String fieldName, SettableStringValue settableStringValue) {
        JSONValue valueValue = parent.get(fieldName);
        String actualValue;
        if (valueValue == null) {
            actualValue = "";
        } else {
            JSONString valueString = notNull(valueValue.isString());
            actualValue = valueString.stringValue();
        }
        settableStringValue.setValue(actualValue);
    }
    
    protected void updateFromFieldRace(JSONObject parent, String fieldName, SettableEnumValue<Race> settableRaceValue) {
        JSONValue valueValue = notNull(parent.get(fieldName));
        JSONString valueString = notNull(valueValue.isString());
        String raceName = valueString.stringValue();
        settableRaceValue.setValue(Race.valueOf(raceName));
    }
    
    protected void updateFromFieldArmorType(JSONObject parent, String fieldName, SettableEnumValue<ArmorType> settableArmorTypeValue) {
        JSONValue valueValue = notNull(parent.get(fieldName));
        JSONString valueString = notNull(valueValue.isString());
        String armorTypeName = valueString.stringValue();
        settableArmorTypeValue.setValue(ArmorType.valueOf(armorTypeName));
    }

    protected void updateFromField(JSONObject parent, String fieldName, TweakableIntValue tweakableIntValue) {
        JSONValue value = parent.get(fieldName);
        if (value == null) {
            tweakableIntValue.setAdjustments(null, null);
        } else {
            JSONObject obj = notNull(value.isObject());
            final Integer overrideInt;
            final Integer modifierInt;
            JSONValue overrideValue = obj.get("override");
            if (overrideValue == null) {
                overrideInt = null;
            } else {
                JSONNumber overrideNum = notNull(overrideValue.isNumber());
                overrideInt = Integer.valueOf((int) overrideNum.doubleValue());
            }
            JSONValue modifierValue = obj.get("modifier");
            if (modifierValue == null) {
                modifierInt = null;
            } else {
                JSONNumber modifierNum = notNull(modifierValue.isNumber());
                modifierInt = Integer.valueOf((int) modifierNum.doubleValue());
            }
            tweakableIntValue.setAdjustments(overrideInt, modifierInt);
        }
    }
    

    protected void update(JSONValue input, StatValue statValue) {
        JSONObject inputObj = notNull(input.isObject());
        updateFromField(inputObj, "value", statValue.getValue());
        updateFromField(inputObj, "roll", statValue.getRoll());
        updateFromField(inputObj, "cost", statValue.getCost());
    }
    
    protected void updateFromField(JSONObject inputObject, String fieldName, StatValues statValues) {
        JSONValue fieldValue = notNull(inputObject.get(fieldName));
        JSONArray fieldArray = notNull(fieldValue.isArray());
        if (fieldArray.size() != ZithiaStat.getNumStats()) {
            throw new JSONBuildException();
        }
        for (ZithiaStat zithiaStat : ZithiaStat.values()) {
            update(fieldArray.get(zithiaStat.ordinal()), statValues.getStat(zithiaStat));
        }
    }
    
    protected ZithiaSkill lookupSkill(JSONValue input) {
        JSONObject inputObj = notNull(input.isObject());
        JSONValue idValue = notNull(inputObj.get("id"));
        JSONString idString = notNull(idValue.isString());
        String id = idString.stringValue();
        return notNull(SkillCatalog.get(id));
    }
    
    
    protected void updateFromField(JSONObject inputObject, String fieldName, SkillList skillList) {
        JSONValue fieldValue = notNull(inputObject.get(fieldName));
        JSONArray fieldArray = notNull(fieldValue.isArray());
        skillList.clear();
        for (int i=0; i<fieldArray.size(); i++) {
            JSONValue skillDataValue = fieldArray.get(i);
            JSONObject skillDataObj = notNull(skillDataValue.isObject());
            JSONValue zithiaSkillValue = notNull(skillDataObj.get("skill"));
            ZithiaSkill zithiaSkill = lookupSkill(zithiaSkillValue);
            SkillValue result = skillList.addNewSkill(zithiaSkill);
            updateFromField(skillDataObj, "levels", result.getLevels());
            if (zithiaSkill.hasRoll()) {
                updateFromField(skillDataObj, "roll", result.getRoll());
            }
            updateFromField(skillDataObj, "cost", result.getCost());
        }
    }
    
    protected WeaponSkill lookupWeaponSkill(JSONValue input) {
        JSONObject inputObj = notNull(input.isObject());
        JSONValue idValue = notNull(inputObj.get("id"));
        JSONString idString = notNull(idValue.isString());
        String id = idString.stringValue();
        return notNull(WeaponsCatalog.getSingleton().getWeaponSkillById(id));
    }
    
    protected WeaponTraining newWeaponTraining(JSONValue input, WeaponTraining parent) {
        JSONObject inputObject = notNull(input.isObject());
        WeaponSkill weaponSkill = lookupWeaponSkill(notNull(inputObject.get("weaponSkill")));
        WeaponTraining result = parent.createChild(weaponSkill);
        updateFromField(inputObject, "basicTrainingDesired", result.getBasicTrainingDesired());
        updateFromField(inputObject, "levelsPurchased", result.getLevelsPurchased());
        updateFromField(inputObject, "levels", result.getLevels());
        updateFromField(inputObject, "thisCost", result.getThisCost());
        updateFromField(inputObject, "totalCost", result.getTotalCost());
        return result;
    }
    
    /**
     * This is used for reading the top-level WeaponTraining. The input must
     * specify a weaponSkill that matches the skill in wt. The WeaponTraining
     * passed must be newly created or have had .clean() called on it before
     * calling this.
     */
    protected void update(JSONValue input, WeaponTraining wt) {
        JSONObject inputObject = notNull(input.isObject());
        WeaponSkill weaponSkillFound = lookupWeaponSkill(notNull(inputObject.get("weaponSkill")));
        if (wt.getWeaponSkill() != weaponSkillFound) {
            throw new JSONBuildException();
        }
        updateFromField(inputObject, "basicTrainingDesired", wt.getBasicTrainingDesired());
        updateFromField(inputObject, "levelsPurchased", wt.getLevelsPurchased());
        updateFromField(inputObject, "levels", wt.getLevels());
        updateFromField(inputObject, "thisCost", wt.getThisCost());
        updateFromField(inputObject, "totalCost", wt.getTotalCost());
        JSONValue childrenValue = inputObject.get("children");
        if (childrenValue != null) {
            JSONArray childrenArray = notNull(childrenValue.isArray());
            for (int i=0; i<childrenArray.size(); i++) {
                JSONValue childValue = childrenArray.get(i);
                JSONObject childObject = notNull(childValue.isObject());
                JSONValue childWeaponSkillValue = notNull(childObject.get("weaponSkill"));
                WeaponSkill childWeaponSkill = lookupWeaponSkill(childWeaponSkillValue);
                WeaponTraining newChild = wt.createChild(childWeaponSkill);
                update(childValue, newChild);
            }
        }
    }
    
    protected void updateFromField(JSONObject inputObject, String fieldName, WeaponTraining wt) {
        wt.clean();
        JSONValue fieldValue = notNull(inputObject.get(fieldName));
        update(fieldValue, wt);
    }
    
    protected void updateFromField(JSONObject inputObject, String fieldName, TalentList talentList) {
        JSONValue fieldValue = inputObject.get(fieldName);
        talentList.clear();
        if (fieldValue != null) {
            JSONArray fieldArray = notNull(fieldValue.isArray());
            for (int i=0; i<fieldArray.size(); i++) {
                JSONObject talentDataObj = notNull(fieldArray.get(i).isObject());
                TalentValue talentValue = new TalentValue();
                updateFromField(talentDataObj, "description", talentValue.getDescription());
                updateFromField(talentDataObj, "cost", talentValue.getCost());
                talentList.add(talentValue);
            }
        }
    }
    
    protected void updateFromField(JSONObject inputObject, String fieldName, ZithiaCosts zithiaCosts) {
        JSONValue fieldValue = inputObject.get(fieldName);
        if (fieldValue != null) {
            JSONObject fieldObject = notNull(fieldValue.isObject());
            updateFromField(fieldObject, "raceCost", zithiaCosts.getRaceCost());
            updateFromField(fieldObject, "statCost", zithiaCosts.getStatCost());
            updateFromField(fieldObject, "skillCost", zithiaCosts.getSkillCost());
            updateFromField(fieldObject, "weaponSkillCost", zithiaCosts.getWeaponSkillCost());
            updateFromField(fieldObject, "totalCost", zithiaCosts.getTotalCost());
            updateFromField(fieldObject, "basePts", zithiaCosts.getBasePts());
            updateFromField(fieldObject, "loanPts", zithiaCosts.getLoanPts());
            updateFromField(fieldObject, "expSpent", zithiaCosts.getExpSpent());
            updateFromField(fieldObject, "expEarned", zithiaCosts.getExpEarned());
            updateFromField(fieldObject, "paidForLoan", zithiaCosts.getPaidForLoan());
            updateFromField(fieldObject, "expUnspent", zithiaCosts.getExpUnspent());
        }
    }
    
    protected void updateFromField(JSONObject inputObject, String fieldName, RaceValue raceValue) {
        JSONValue fieldValue = inputObject.get(fieldName);
        if (fieldValue == null) {
            // If omitted, default to Human.
            raceValue.getRace().setValue(Race.Human);
        } else {
            JSONObject fieldObject = notNull(fieldValue.isObject());
            updateFromFieldRace(fieldObject, "race", raceValue.getRace());
        }
    }

    protected void updateFromField(JSONObject inputObject, String fieldName, CombatValues combatValues) {
        JSONValue fieldValue = inputObject.get(fieldName);
        if (fieldValue == null) {
            // If omitted, then all fields take default values
            combatValues.getOffense().setAdjustments(null, null);
            combatValues.getDefense().setAdjustments(null, null);
        } else {
            JSONObject fieldObject = notNull(fieldValue.isObject());
            updateFromField(fieldObject, "offense", combatValues.getOffense());
            updateFromField(fieldObject, "defense", combatValues.getDefense());
        }
    }

    protected void updateFromField(JSONObject inputObject, String fieldName, ArmorValue armorValue) {
        JSONValue fieldValue = inputObject.get(fieldName);
        if (fieldValue == null) {
            // If omitted, then all fields take default values
            armorValue.setDefaultSettings();
        } else {
            JSONObject fieldObject = notNull(fieldValue.isObject());
            updateFromFieldArmorType(fieldObject, "armorType", armorValue.getArmorType());
            updateFromField(fieldObject, "hpBlock", armorValue.getHpBlock());
            updateFromField(fieldObject, "stunBlock", armorValue.getStunBlock());
            updateFromField(fieldObject, "defPenalty", armorValue.getDefPenalty());
        }
    }



    protected void updateFromField(JSONObject inputObject, String fieldName, Names names) {
        JSONValue fieldValue = notNull(inputObject.get(fieldName));
        JSONObject fieldObject = notNull(fieldValue.isObject());
        updateFromField(fieldObject, "name", names.getCharacterName());
        updateFromField(fieldObject, "player", names.getPlayerName());
    }
    
    protected void updateFromField(JSONObject inputObject, String fieldName, CharacterNotes notes) {
        JSONValue fieldValue = inputObject.get(fieldName);
        if (fieldValue == null) {
            notes.getBackground().setValue("");
        } else {
            JSONObject fieldObject = notNull(fieldValue.isObject());
            updateFromField(fieldObject, "background", notes.getBackground());
        }
    }

    public void update(JSONValue inputValue, ZithiaCharacter zithiaCharacter) {
        zithiaCharacter.changeStatsOnRaceUpdate(false);
        JSONObject inputObject = notNull(inputValue.isObject());
        updateFromField(inputObject, "race", zithiaCharacter.getRaceValue());
        updateFromField(inputObject, "names", zithiaCharacter.getNames());
        updateFromField(inputObject, "statValues", zithiaCharacter.getStatValues());
        updateFromField(inputObject, "skillList", zithiaCharacter.getSkillList());
        updateFromField(inputObject, "weaponTraining", zithiaCharacter.getWeaponTraining());
        updateFromField(inputObject, "talentList", zithiaCharacter.getTalentList());
        updateFromField(inputObject, "costs", zithiaCharacter.getCosts());
        updateFromField(inputObject, "combatValues", zithiaCharacter.getCombatValues());
        updateFromField(inputObject, "armorValue", zithiaCharacter.getArmorValue());
        updateFromField(inputObject, "notes", zithiaCharacter.getCharacterNotes());
        zithiaCharacter.changeStatsOnRaceUpdate(true);
    }

}
