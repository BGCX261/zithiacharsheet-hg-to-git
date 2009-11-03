package com.mcherm.zithiacharsheet.client.modeler;

/**
 * A string value appearing in a charsheet which can be freely set
 * to any value desired.
 */
public interface SettableStringValue extends ObservableString {
    public void setValue(String value);
}
