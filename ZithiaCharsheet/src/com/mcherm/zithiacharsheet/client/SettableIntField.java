package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;


/**
 * A field that is tied to a value. If the value changes,
 * the field changes and you can edit the field to set
 * the value.
 */
public class SettableIntField extends TextBox {
    
    protected final SettableIntValue value;
    
    /**
     * Constructor. Must specify the value to which this is tied.
     */
    public SettableIntField(final SettableIntValue value) {
        this.value = value;
        this.addStyleName("settableInt");
        updateDisplay();
        value.addObserver(new Observer() {
            public void onChange() {
                updateDisplay();
            }
        });
        addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> event) {
                try {
                    int newValue = Integer.parseInt(event.getValue());
                    value.setValue(newValue);
                } catch(NumberFormatException err) {
                    Window.alert("Got number format exception. value was " + event.getValue());
                    updateDisplay();
                }
            }
        });
    }
    
    /**
     * Sets the displayed value to match the field value.
     */
    protected void updateDisplay() {
        setValue(Integer.toString(value.getValue()));
    }
}
