package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.mcherm.zithiacharsheet.client.modeler.SettableStringValue;
import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;


/**
 * A field that is tied to a value. If the value changes,
 * the field changes and you can edit the field to set
 * the value.
 */
public class SettableStringField extends TextBox {
    
    protected final SettableStringValue value;
    
    /**
     * Constructor. Must specify the value to which this is tied.
     */
    public SettableStringField(final SettableStringValue value) {
        this.value = value;
        this.addStyleName("settableInt");
        setValue(value.getValue());
        value.addObserver(new Observer() {
            public void onChange() {
                SettableStringField.this.setValue(value.getValue());
            }
        });
        addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> event) {
                value.setValue(event.getValue());
            }
        });
    }
    
}
