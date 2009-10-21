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
    
    protected <T> T notNull(T x) {
        if (x == null) {
            throw new JSONBuildException();
        }
        return x;
    }

    
    public void updateFromField(JSONObject parent, String fieldName, SettableIntValue settableIntValue) {
        JSONValue valueValue = notNull(parent.get(fieldName));
        JSONNumber valueNum = notNull(valueValue.isNumber());
        int valueInt = (int) valueNum.doubleValue();
        settableIntValue.setValue(valueInt);
    }
    
    public void updateFromField(JSONObject parent, String fieldName, TweakableIntValue tweakableIntValue) {
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
    

    public void update(JSONValue input, StatValue statValue) {
        JSONObject inputObj = notNull(input.isObject());
        updateFromField(inputObj, "value", statValue.getValue());
        updateFromField(inputObj, "roll", statValue.getRoll());
        updateFromField(inputObj, "cost", statValue.getCost());
    }
    
    public void update(JSONValue input, StatValues statValues) {
        JSONArray inputArray = notNull(input.isArray());
        if (inputArray.size() != ZithiaStat.getNumStats()) {
            throw new JSONBuildException();
        }
        for (ZithiaStat zithiaStat : ZithiaStat.values()) {
            update(inputArray.get(zithiaStat.ordinal()), statValues.getStat(zithiaStat));
        }
    }
    
    public ZithiaSkill lookupSkill(JSONValue input) {
        JSONObject inputObj = notNull(input.isObject());
        JSONValue idValue = notNull(inputObj.get("id"));
        JSONString idString = notNull(idValue.isString());
        String id = idString.stringValue();
        ZithiaSkill skill = notNull(SkillCatalog.get(id));
        return skill;
    }
    
    
    public void update(JSONValue input, SkillList skillList, StatValues statValues) {
        JSONArray inputArray = notNull(input.isArray());
        skillList.clear();
        for (int i=0; i<inputArray.size(); i++) {
            JSONValue skillDataValue = inputArray.get(i);
            JSONObject skillDataObj = notNull(skillDataValue.isObject());
            JSONValue zithiaSkillValue = notNull(skillDataObj.get("skill"));
            ZithiaSkill zithiaSkill = lookupSkill(zithiaSkillValue);
            SkillValue result = skillList.addNewSkill(zithiaSkill);
            updateFromField(skillDataObj, "levels", result.getLevels());
            updateFromField(skillDataObj, "roll", result.getRoll());
            updateFromField(skillDataObj, "cost", result.getCost());
        }
    }
    
}
