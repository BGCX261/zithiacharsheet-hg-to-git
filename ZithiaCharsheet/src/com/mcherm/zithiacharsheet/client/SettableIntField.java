/*
 * Copyright 2009 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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
