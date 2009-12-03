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
package com.mcherm.zithiacharsheet.client.modeler;

/**
 * An implementation of SettableEnumValue.
 */
public class SettableEnumValueImpl<T extends Enum<T>> extends SimpleObservable implements SettableEnumValue<T> {
    private Class<T> enumClass;
    private T value;

    public SettableEnumValueImpl(Class<T> enumClass, T initialValue) {
        this.enumClass = enumClass;
        this.value = initialValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        alertObservers();
    }

    public Class<T> getEnumClass() {
        return enumClass;
    }
}
