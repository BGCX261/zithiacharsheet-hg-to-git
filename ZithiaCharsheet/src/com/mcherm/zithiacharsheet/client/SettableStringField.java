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
import com.google.gwt.user.client.ui.TextBox;
import com.mcherm.zithiacharsheet.client.modeler.Disposable;
import com.mcherm.zithiacharsheet.client.modeler.Disposer;
import com.mcherm.zithiacharsheet.client.modeler.SettableStringValue;
import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;


/**
 * A field that is tied to a value. If the value changes,
 * the field changes and you can edit the field to set
 * the value.
 */
public class SettableStringField extends TextBox implements Disposable {
    
    protected final SettableStringValue value;
    private final Disposer disposer = new Disposer();

    /**
     * Constructor. Must specify the value to which this is tied.
     */
    public SettableStringField(final SettableStringValue value) {
        this.value = value;
        this.addStyleName("settableString");
        setValue(value.getValue());
        disposer.observe(value, new Observer() {
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
    
    public void dispose() {
        disposer.dispose();
    }
}
