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
 * A class where a boolean value can be calculated as depending on certain other
 * observable values.
 */
public class CalculatedBooleanValue extends SimpleObservable implements ObservableBoolean, Disposable {

    private final Disposer disposer = new Disposer();
    private boolean value;
    
    public static interface BooleanValueCalculator {
        public boolean calculateValue();
    }

    /**
     * Constructor.
     * 
     * @param inputs a list of inputs that this depends on. The list
     *   passed must never change (it can be a clone of another list
     *   if necessary).
     * @param valueCalculator a function to calculate the value at
     *   any point in time.
     */
    public CalculatedBooleanValue(
            final Iterable<? extends Observable> inputs,
            final BooleanValueCalculator valueCalculator)
    {
        value = valueCalculator.calculateValue();
        Observable.Observer inputObserver = new Observable.Observer() {
            public void onChange() {
                value = valueCalculator.calculateValue();
                alertObservers();
            }
        };
        for (Observable input : inputs) {
            disposer.observe(input, inputObserver);
        }
    }
    
    
    public boolean getValue() {
        return value;
    }

    public void dispose() {
        disposer.dispose();
    }
}
