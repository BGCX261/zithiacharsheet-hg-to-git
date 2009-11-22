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
package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValueImpl;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation1;


/**
 * An actual instance of a stat in a particular individual.
 * So, for instance, this might be Rogan Harsha's Strength.
 */
public class StatValue {
    private final ZithiaStat stat;
    private final SettableIntValue value;
    private final TweakableIntValue roll;
    private final TweakableIntValue cost;
    
    public StatValue(final ZithiaStat stat) {
        this.stat = stat;
        value = new SettableIntValueImpl(stat.getDefaultValue());
        roll = new EquationIntValue(value, new Equation1() {
            public int getValue(int value) {
                return stat.getRoll(value);
            }
        });
        cost = new EquationIntValue(value, new Equation1() {
            public int getValue(int value) {
                // FIXME: this needs to take race into account. But for now it won't.
                return stat.getCost(value - stat.getDefaultValue());
            }
        });
    }
    
    public ZithiaStat getStat() {
        return stat;
    }
    
    public SettableIntValue getValue() {
        return value;
    }
    
    public TweakableIntValue getRoll() {
        return roll;
    }

    public TweakableIntValue getCost() {
        return cost;
    }
    
}
