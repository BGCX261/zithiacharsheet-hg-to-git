package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;
import com.mcherm.zithiacharsheet.client.model.ObservableInt;
import com.mcherm.zithiacharsheet.client.model.Observable.Observer;


/**
 * A field that is tied to a value. If the value changes,
 * the field changes. You cannot edit the value (for now).
 */
public class CalculatedIntField extends TextBox {

    protected final ObservableInt field;
    
    public CalculatedIntField(final ObservableInt field_) {
        this.field = field_;
        updateDisplay();
        field.addObserver(new Observer() {
            @Override
            public void onChange() {
                Window.alert("Got a change. Value is " + field.getValue());
                updateDisplay();
            }
        });
        setEnabled(false);
    }

    /**
     * Sets the displayed value to match the field value.
     */
    protected void updateDisplay() {
        setValue(Integer.toString(field.getValue()));
    }
}
