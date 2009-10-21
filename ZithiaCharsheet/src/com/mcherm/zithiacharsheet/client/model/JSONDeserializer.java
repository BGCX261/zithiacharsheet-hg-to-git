package com.mcherm.zithiacharsheet.client.model;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;


/**
 * This can be used to modify an existing ZithiaCharacter so that it
 * exactly matches the content of a JSON file.
 */
public class JSONDeserializer {

    
    public void updateFromField(JSONObject parent, String fieldName, SettableIntValue settableIntValue) {
        JSONValue valueValue = parent.get(fieldName);
        if (valueValue == null) {
            throw new JSONBuildException();
        }
        JSONNumber valueNum = valueValue.isNumber();
        if (valueNum == null) {
            throw new JSONBuildException();
        }
        int valueInt = (int) valueNum.doubleValue();
        settableIntValue.setValue(valueInt);
    }
    
    public void updateFromField(JSONObject parent, String fieldName, TweakableIntValue tweakableIntValue) {
        JSONValue value = parent.get(fieldName);
        if (value == null) {
            tweakableIntValue.setAdjustments(null, null);
        } else {
            JSONObject obj = value.isObject();
            if (obj == null) {
                throw new JSONBuildException();
            }
            final Integer overrideInt;
            final Integer modifierInt;
            JSONValue overrideValue = obj.get("override");
            if (overrideValue == null) {
                overrideInt = null;
            } else {
                JSONNumber overrideNum = overrideValue.isNumber();
                if (overrideNum == null) {
                    throw new JSONBuildException();
                }
                overrideInt = Integer.valueOf((int) overrideNum.doubleValue());
            }
            JSONValue modifierValue = obj.get("modifier");
            if (modifierValue == null) {
                modifierInt = null;
            } else {
                JSONNumber modifierNum = modifierValue.isNumber();
                if (modifierNum == null) {
                    throw new JSONBuildException();
                }
                modifierInt = Integer.valueOf((int) modifierNum.doubleValue());
            }
            tweakableIntValue.setAdjustments(overrideInt, modifierInt);
        }
    }
    

    public void update(JSONValue input, StatValue statValue) {
        JSONObject inputObj = input.isObject();
        if (inputObj == null) {
            throw new JSONBuildException();
        }
        updateFromField(inputObj, "value", statValue.getValue());
        updateFromField(inputObj, "roll", statValue.getRoll());
        updateFromField(inputObj, "cost", statValue.getCost());
    }
    
    public void update(JSONValue input, StatValues statValues) {
        JSONArray inputArray = input.isArray();
        if (inputArray == null) {
            throw new JSONBuildException();
        }
        if (inputArray.size() != ZithiaStat.getNumStats()) {
            throw new JSONBuildException();
        }
        for (ZithiaStat zithiaStat : ZithiaStat.values()) {
            update(inputArray.get(zithiaStat.ordinal()), statValues.getStat(zithiaStat));
        }
    }
    
    public ZithiaSkill lookupSkill(JSONValue input) {
        if (input == null) {
            throw new JSONBuildException();
        }
        JSONObject inputObj = input.isObject();
        if (inputObj == null) {
            throw new JSONBuildException();
        }
        JSONValue idValue = inputObj.get("id");
        if (idValue == null) {
            throw new JSONBuildException();
        }
        JSONString idString = idValue.isString();
        // FIXME: Make an 'assertNotNull'
        if (idString == null) {
            throw new JSONBuildException();
        }
        String id = idString.stringValue();
        ZithiaSkill skill = SkillCatalog.get(id);
        if (skill == null) {
            throw new JSONBuildException();
        }
        return skill;
    }
    
    
    public void update(JSONValue input, SkillList skillList, StatValues statValues) {
        JSONArray inputArray = input.isArray();
        if (inputArray == null) {
            throw new JSONBuildException();
        }
        skillList.clear();
        for (int i=0; i<inputArray.size(); i++) {
            JSONValue skillDataValue = inputArray.get(i);
            JSONObject skillDataObj = skillDataValue.isObject();
            if (skillDataObj == null) {
                throw new JSONBuildException();
            }
            JSONValue zithiaSkillValue = skillDataObj.get("skill");
            ZithiaSkill zithiaSkill = lookupSkill(zithiaSkillValue);
            SkillValue result = skillList.addNewSkill(zithiaSkill);
            updateFromField(skillDataObj, "levels", result.getLevels());
            updateFromField(skillDataObj, "roll", result.getRoll());
            updateFromField(skillDataObj, "cost", result.getCost());
        }
    }
    
}
