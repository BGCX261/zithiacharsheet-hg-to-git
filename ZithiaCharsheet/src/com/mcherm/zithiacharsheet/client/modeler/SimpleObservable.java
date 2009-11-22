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

import com.mcherm.zithiacharsheet.client.util.ImmutableList;


/**
 * This can be used to implement the Observable interface.
 */
public class SimpleObservable implements Observable {

    private ImmutableList<Observer> observers = new ImmutableList<Observer>();
    
    public void addObserver(Observer observer) {
        observers = observers.add(observer);
    }
    
    public void removeObserver(Observer observer) {
        observers = observers.remove(observer);
    }

    /**
     * Subclasses call this when they're ready to alert observers of a change.
     */
    protected void alertObservers() {
        // FIXME: Possible bug: new observers get added during the iteration. We should probably save the modifications until AFTER the iteration.
        ImmutableList<Observer> currentObservers = observers;
        for (Observer observer : currentObservers) {
            observer.onChange();
        }
    }

}
