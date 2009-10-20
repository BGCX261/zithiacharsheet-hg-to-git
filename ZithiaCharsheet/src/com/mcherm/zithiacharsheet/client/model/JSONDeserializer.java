package com.mcherm.zithiacharsheet.client.model;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
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
    
    
}
