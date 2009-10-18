package com.mcherm.zithiacharsheet.client.modeler;


/**
 * Contains a single value, and can be observed.
 */
public class SettableIntValueImpl extends SimpleObservable implements SettableIntValue {
    
    private int value;
    
    public SettableIntValueImpl(int initialValue) {
        this.value = initialValue;
    }
    
    @Override
    public int getValue() {
        return this.value;
    }
    
    @Override
    public void setValue(int value) {
        this.value = value;
        alertObservers();
    }
    
}
