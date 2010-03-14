/*
 * Copyright 2009 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcherm.zithiacharsheet.client;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.ListBox;
import com.mcherm.zithiacharsheet.client.modeler.Disposable;
import com.mcherm.zithiacharsheet.client.modeler.Disposer;
import com.mcherm.zithiacharsheet.client.modeler.EnumWithName;
import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;
import com.mcherm.zithiacharsheet.client.modeler.SettableEnumValue;


/**
 * A UI field for selecting from an enumerated type. It is, of course, represented
 * as a drop-down menu.
 */
public class SettableEnumField<T extends Enum<T>> extends ListBox implements Disposable {

    private final Disposer disposer = new Disposer();
    private final Map<Integer, T> indexToEnumMap;
    private final EnumMap<T, Integer> enumToIndexMap;

    /**
     * Constructor. Must pass in a class object for the specific enum, and must provide the value
     * to which this is tied.
     */
    @SuppressWarnings("unchecked")
    public SettableEnumField(final SettableEnumValue<T> value) {
        final Class<T> enumClass = value.getEnumClass();
        this.addStyleName("settableEnum");
        {
            int index = 0;
            indexToEnumMap = new HashMap<Integer, T>();
            enumToIndexMap = new EnumMap<T, Integer>(enumClass);
            for (T possibleValue : enumClass.getEnumConstants()) {
                indexToEnumMap.put(index, possibleValue);
                enumToIndexMap.put(possibleValue, index);
                String displayName;
                if (possibleValue instanceof EnumWithName) {
                    // If it has display names, use those
                    displayName = ((EnumWithName) possibleValue).getName();
                } else {
                    // Otherwise use the enum's java identifier as a name
                    displayName = possibleValue.name();
                }
                this.addItem(displayName);
                index++;
            }
        }
        setSelectedIndex(enumToIndex(value.getValue()));
        disposer.observe(value, new Observer() {
            public void onChange() {
                SettableEnumField.this.setSelectedIndex(enumToIndex(value.getValue()));
            }
        });
        addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent changeEvent) {
                value.setValue(indexToEnum(getSelectedIndex()));
            }
        });
    }

    private int enumToIndex(T value) {
        return enumToIndexMap.get(value);
    }

    private T indexToEnum(int index) {
        return indexToEnumMap.get(index);
    }


    public void dispose() {
        disposer.dispose();
    }
}
