package com.mcherm.zithiacharsheet.client.model;


/**
 * Contains a single value, and can be observed.
 */
public class ObservableIntValue extends SimpleObservable implements ObservableInt {
    
    private int value;
    
    public ObservableIntValue(int initialValue) {
        this.value = initialValue;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public void setValue(int value) {
        this.value = value;
        alertObservers();
    }
    
}
