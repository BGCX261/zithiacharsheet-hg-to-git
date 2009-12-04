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
package com.mcherm.zithiacharsheet.client.modeler;


/**
 * This is a generic form of a TweakableIntValue. The value is computed
 * according to some formula and depends on certain fixed other values.
 */
public class CalculatedIntValue<T extends Observable> extends SimpleObservable implements TweakableIntValue {
    
    public static interface ValueCalculator<T> {
        public int calculateValue(Iterable<? extends T> inputs);
    }
    
        
    private final Disposer disposer = new Disposer();
    private int value;
    private Integer override;
    private Integer modifier;

    /**
     * Constructor.
     * 
     * @param inputs a list of inputs that this depends on. The list
     *   passed will be iterated once; after that any changes made
     *   to it would be ignored.
     * @param valueCalculator a function to calculate the value at
     *   any point in time.
     */
    public CalculatedIntValue(
            final Iterable<? extends T> inputs,
            final ValueCalculator<T> valueCalculator)
    {
        override = null;
        modifier = null;
        value = valueCalculator.calculateValue(inputs);
        Observable.Observer inputObserver = new Observable.Observer() {
            public void onChange() {
                value = valueCalculator.calculateValue(inputs);
                alertObservers();
            }
        };
        for (Observable input : inputs) {
            disposer.observe(input, inputObserver);
        }
    }

    public int getValue() {
        if (override != null) {
            return override.intValue();
        } else if (modifier != null) {
            return value + modifier.intValue();
        } else {
            return value;
        }
    }

    public boolean isTweaked() {
        return override != null || modifier != null;
    }

    public Integer getOverride() {
        return override;
    }

    public Integer getModifier() {
        return modifier;
    }

    public void setAdjustments(Integer override, Integer modifier) {
        if (override != null && modifier != null) {
            throw new IllegalArgumentException("Either override or modifier must be null.");
        }
        this.override = override;
        this.modifier = modifier;
        alertObservers();
    }
    
    public void dispose() {
        disposer.dispose();
    }
}
