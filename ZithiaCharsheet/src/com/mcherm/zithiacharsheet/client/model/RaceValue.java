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
package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.modeler.Disposable;
import com.mcherm.zithiacharsheet.client.modeler.Disposer;
import com.mcherm.zithiacharsheet.client.modeler.ObservableEnum;
import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
import com.mcherm.zithiacharsheet.client.modeler.SettableEnumValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableEnumValueImpl;
import com.mcherm.zithiacharsheet.client.modeler.SimpleObservable;


/**
 * The value within a ZithiaCharacter of the race, and any related fields.
 */
public class RaceValue implements Disposable {
    private final Disposer disposer = new Disposer();
    private final SettableEnumValue<Race> race;
    private final ObservableInt cost;


    public RaceValue() {
        race = new SettableEnumValueImpl<Race>(Race.class, Race.Human);
        cost = new ObservableInt() {
            public void addObserver(Observer observer) {
                disposer.observe(race, observer);
            }
            public void removeObserver(Observer observer) {
                throw new RuntimeException("Does not support removing observers.");
            }
            public int getValue() {
                return race.getValue().getCost();
            }
        };

    }

    public SettableEnumValue<Race> getRace() {
        return race;
    }

    public ObservableInt getCost() {
        return cost;
    }

    public void dispose() {
        disposer.dispose();
    }
}
