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
            public void onChange() {
                checkBox.setValue(value.getValue());
            }
        });
        checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                value.setValue(event.getValue());
            }
        });
        this.add(checkBox);
    }
}
