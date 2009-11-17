package com.mcherm.zithiacharsheet.client.modeler;

/**
 * A value which can be set and depended on; it stores a single boolean value.
 */
public class SettableBooleanValueImpl extends SimpleObservable implements SettableBooleanValue {

    private boolean value;
    
    public SettableBooleanValueImpl(boolean initialValue) {
        this.value = initialValue;
    }
    
    public boolean getValue() {
        return value;
    }
    
    public void setValue(boolean value) {
        this.value = value;
        alertObservers();
    }
}
