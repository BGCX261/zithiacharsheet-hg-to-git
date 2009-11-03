package com.mcherm.zithiacharsheet.client.modeler;


/**
 * A value which can be set and depended on; it stores a single String value.
 */
public class SettableStringValueImpl extends SimpleObservable implements SettableStringValue {

    private String value;
    
    public SettableStringValueImpl(String initialValue) {
        this.value = initialValue;
    }
    
    @Override
    public String getValue() {
        return value;
    }
    
    @Override
    public void setValue(String value) {
        this.value = value;
        alertObservers();
    }

}
