package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValueImpl;
import com.mcherm.zithiacharsheet.client.modeler.SettableStringValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableStringValueImpl;


/**
 * Any property of a character, it's got a description and a cost.
 */
public class TalentValue {
    private final SettableIntValue cost;
    private final SettableStringValue description;
    
    public TalentValue() {
        cost = new SettableIntValueImpl(0);
        description = new SettableStringValueImpl("");
    }
    
    /**
     * Accessor.
     */
    public SettableIntValue getCost() {
        return cost;
    }

    /**
     * Accessor.
     */
    public SettableStringValue getDescription() {
        return description;
    }

}
