package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.TextBox;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;


/**
 * A field that is tied to a value. If the value changes,
 * the field changes. You cannot edit the value (for now).
 */
public class TweakableIntField extends TextBox {

    protected final TweakableIntValue value;
    
    public TweakableIntField(final TweakableIntValue value) {
        this.value = value;
        updateDisplay();
        value.addObserver(new Observer() {
            public void onChange() {
                updateDisplay();
            }
        });
        setEnabled(false);
    }

    /**
     * Sets the displayed value to match the field value.
     */
    protected void updateDisplay() {
        setValue(Integer.toString(value.getValue()));
    }
}
