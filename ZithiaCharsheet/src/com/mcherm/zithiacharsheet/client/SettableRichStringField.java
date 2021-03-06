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

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.mcherm.zithiacharsheet.client.modeler.Disposable;
import com.mcherm.zithiacharsheet.client.modeler.Disposer;
import com.mcherm.zithiacharsheet.client.modeler.Observable;
import com.mcherm.zithiacharsheet.client.modeler.SettableStringValue;


/**
 * This is a text field that is tied to a SettableStringValue, but in this
 * case the string can contain HTML markup, which is displayed.
 */
public class SettableRichStringField extends RichTextPalate implements Disposable {

    protected final SettableStringValue value;
    private final Disposer disposer = new Disposer();
    private boolean ignoreValueUpdates;

    /**
     * Constructor. Must specify the value to which this is tied.
     */
    public SettableRichStringField(final SettableStringValue value) {
        this.value = value;
        this.ignoreValueUpdates = false;
        addStyleName("settableString");
        setHTML(value.getValue());
        disposer.observe(value, new Observable.Observer() {
            public void onChange() {
                if (!ignoreValueUpdates) {
                    SettableRichStringField.this.setHTML(value.getValue());
                }
            }
        });
        addBlurHandler(new BlurHandler() {
            public void onBlur(BlurEvent blurEvent) {
                ignoreValueUpdates = true;
                value.setValue(getHTML());
                ignoreValueUpdates = false;
            }
        });
    }

    public void dispose() {
        disposer.dispose();
    }
}
