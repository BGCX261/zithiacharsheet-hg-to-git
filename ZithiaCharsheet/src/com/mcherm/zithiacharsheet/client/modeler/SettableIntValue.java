package com.mcherm.zithiacharsheet.client.modeler;

/**
 * An integer value appearing in a charsheet which can be freely set
 * to any value desired.
 */
public interface SettableIntValue extends ObservableInt {
    public void setValue(int value);
}
