package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.mcherm.zithiacharsheet.client.modeler.SettableBooleanValue;
import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;

/**
 * A boolean that can be set and is tied to a value; displayed as a checkbox.
 */
public class SettableBooleanField extends HorizontalPanel {

    public SettableBooleanField(String text, final SettableBooleanValue value) {
        final CheckBox checkBox = new CheckBox(text);
        checkBox.setValue(value.getValue());
        value.addObserver(new Observer() {
            @Override
            public void onChange() {
                checkBox.setValue(value.getValue());
            }
        });
        checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                value.setValue(event.getValue());
            }
        });
        this.add(checkBox);
    }
}
