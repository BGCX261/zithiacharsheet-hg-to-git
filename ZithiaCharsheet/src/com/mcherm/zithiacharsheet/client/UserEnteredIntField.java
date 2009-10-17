package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.mcherm.zithiacharsheet.client.model.ObservableIntValue;


/**
 * A field that is tied to a value. If the value changes,
 * the field changes and you can edit the field to set
 * the value.
 */
public class UserEnteredIntField extends CalculatedIntField {
    
    /**
     * Constructor. Must specify the value to which this is tied.
     */
    public UserEnteredIntField(final ObservableIntValue field) {
        super(field);
        setEnabled(true);
        addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                try {
                    int newValue = Integer.parseInt(event.getValue());
                    Window.alert("Going to set it to " + newValue);
                    field.setValue(newValue);
                } catch(NumberFormatException err) {
                    Window.alert("Got number format exception. value was " + event.getValue());
                    updateDisplay();
                }
            }
        });
    }
    
}
