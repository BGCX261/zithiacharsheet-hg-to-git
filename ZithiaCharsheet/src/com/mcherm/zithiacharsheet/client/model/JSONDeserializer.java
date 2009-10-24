package com.mcherm.zithiacharsheet.client.model;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableBooleanValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;
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

    // FIXME: Make the publics private instead, right?
    
    
    public void updateFromField(JSONObject parent, String fieldName, SettableIntValue settableIntValue) {
        JSONValue valueValue = notNull(parent.get(fieldName));
        JSONNumber valueNum = notNull(valueValue.isNumber());
        int valueInt = (int) valueNum.doubleValue();
        settableIntValue.setValue(valueInt);
    }
    
    public void updateFromField(JSONObject parent, String fieldName, SettableBooleanValue settableBooleanValue) {
        JSONValue valueValue = notNull(parent.get(fieldName));
        JSONBoolean valueBool = notNull(valueValue.isBoolean());
        settableBooleanValue.setValue(valueBool.booleanValue());
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
        return notNull(SkillCatalog.get(id));
    }
    
    
    public void update(JSONValue input, SkillList skillList) {
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
    
    public WeaponSkill lookupWeaponSkill(JSONValue input) {
        JSONObject inputObj = notNull(input.isObject());
        JSONValue idValue = notNull(inputObj.get("id"));
        JSONString idString = notNull(idValue.isString());
        String id = idString.stringValue();
        return notNull(WeaponsCatalog.getSingleton().getWeaponSkillById(id));
    }
    
    public WeaponTraining newWeaponTraining(JSONValue input, WeaponTraining parent) {
        JSONObject inputObject = notNull(input.isObject());
        WeaponSkill weaponSkill = lookupWeaponSkill(notNull(inputObject.get("weaponSkill")));
        WeaponTraining result = parent.createChild(weaponSkill);
        updateFromField(inputObject, "basicTrainingPurchased", result.getBasicTrainingPurchased());
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
    public void update(JSONValue input, WeaponTraining wt) {
        JSONObject inputObject = notNull(input.isObject());
        WeaponSkill weaponSkillFound = lookupWeaponSkill(notNull(inputObject.get("weaponSkill")));
        if (wt.getWeaponSkill() != weaponSkillFound) {
            throw new JSONBuildException();
        }
        updateFromField(inputObject, "basicTrainingPurchased", wt.getBasicTrainingPurchased());
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
    
    public void updateFromField(JSONObject inputObject, String fieldName, ZithiaCosts zithiaCosts) {
        JSONValue fieldValue = inputObject.get(fieldName);
        if (fieldValue != null) {
            JSONObject fieldObject = notNull(fieldValue.isObject());
            updateFromField(fieldObject, "statCost", zithiaCosts.getStatCost());
            updateFromField(fieldObject, "skillCost", zithiaCosts.getSkillCost());
            updateFromField(fieldObject, "weaponSkillCost", zithiaCosts.getWeaponSkillCost());
            updateFromField(fieldObject, "totalCost", zithiaCosts.getTotalCost());
        }
    }
    
    public void update(JSONValue inputValue, ZithiaCharacter zithiaCharacter) {
        // FIXME: refactor so most of these are updateFromField?
        JSONObject inputObject = notNull(inputValue.isObject());
        update(notNull(inputObject.get("statValues")), zithiaCharacter.getStatValues());
        update(notNull(inputObject.get("skillList")), zithiaCharacter.getSkillList());
        WeaponTraining wt = zithiaCharacter.getWeaponTraining();
        wt.clean();
        update(notNull(inputObject.get("weaponTraining")), wt);
        updateFromField(inputObject, "costs", zithiaCharacter.getCosts());
    }

}
