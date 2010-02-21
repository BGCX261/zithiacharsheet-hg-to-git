/*
 * Copyright 2010 Michael Chermside
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

import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation1;
import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;


/**
 * The section of a character that determines values to be used
 * during combat.
 */
public class CombatValues {

    private final EquationIntValue offense;
    private final EquationIntValue defense;

    public CombatValues(StatValues statValues) {
        ObservableInt dexValue = statValues.getStat(ZithiaStat.DEX).getValue();
        offense = new EquationIntValue(dexValue, new Equation1() {
            public int getValue(int dexValue) {
                return (dexValue + 1) / 3;
            }
        });
        defense = new EquationIntValue(dexValue, new Equation1() {
            public int getValue(int dexValue) {
                return (dexValue - 1) / 3;
            }
        });
    }

    public TweakableIntValue getOffense() {
        return offense;
    }

    public TweakableIntValue getDefense() {
        return defense;
    }
}
