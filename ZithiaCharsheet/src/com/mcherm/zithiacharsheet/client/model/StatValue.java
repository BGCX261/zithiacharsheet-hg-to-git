package com.mcherm.zithiacharsheet.client.model;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValueImpl;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation1;


/**
 * An actual instance of a stat in a particular individual.
 * So, for instance, this might be Rogan Harsha's Strength.
 */
public class StatValue {
    private final ZithiaStat stat;
    private final SettableIntValue value;
    private final TweakableIntValue roll;
    private final TweakableIntValue cost;
    
    /**
     * Normal Constructor.
     */
    public StatValue(final ZithiaStat stat) {
        this.stat = stat;
        value = new SettableIntValueImpl(stat.getDefaultValue());
        roll = new EquationIntValue(value, new Equation1() {
            public int getValue(int value) {
                return stat.getRoll(value);
            }
        });
        cost = new EquationIntValue(value, new Equation1() {
            public int getValue(int value) {
                // FIXME: this needs to take race into account. But for now it won't.
                return stat.getCost(value - stat.getDefaultValue());
            }
        });
    }
    
    /**
     * JSON Constructor.
     * <p>
     * FIXME: Massive code duplication here; must fix after I get the API stable.
     */
    public StatValue(JSONValue input, final ZithiaStat stat) {
        this.stat = stat;
        JSONObject inputObj = input.isObject();
        if (inputObj == null) {
            throw new JSONBuildException();
        }
        JSONNumber valueNum = inputObj.get("value").isNumber();
        if (valueNum == null) {
            throw new JSONBuildException();
        }
        int valueInt = (int) valueNum.doubleValue();
        value = new SettableIntValueImpl(valueInt);
        roll = new EquationIntValue(value, new Equation1() {
            public int getValue(int value) {
                return stat.getRoll(value);
            }
        });
        cost = new EquationIntValue(value, new Equation1() {
            public int getValue(int value) {
                // FIXME: this needs to take race into account. But for now it won't.
                return stat.getCost(value - stat.getDefaultValue());
            }
        });
    }
    
    public ZithiaStat getStat() {
        return stat;
    }
    
    public SettableIntValue getValue() {
        return value;
    }
    
    public TweakableIntValue getRoll() {
        return roll;
    }

    public TweakableIntValue getCost() {
        return cost;
    }
    
}
