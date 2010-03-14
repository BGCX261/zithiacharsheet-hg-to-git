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

import java.util.Arrays;
import java.util.Iterator;

import com.mcherm.zithiacharsheet.client.modeler.CalculatedIntValue;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue.Equation3;
import com.mcherm.zithiacharsheet.client.modeler.Observable;
import com.mcherm.zithiacharsheet.client.modeler.ObservableEnum;
import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
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

    /** This constructor exists ONLY for use by DexStatValue. */
    private StatValue(ZithiaStat stat, SettableIntValue value, TweakableIntValue roll, TweakableIntValue cost) {
        this.stat = stat;
        this.value = value;
        this.roll = roll;
        this.cost = cost;
    }

    public StatValue(final RaceValue raceValue, final ZithiaStat stat) {
        this.stat = stat;
        value = new SettableIntValueImpl(stat.getDefaultValue());
        roll = new EquationIntValue(value, new Equation1() {
            public int getValue(int value) {
                return stat.getRoll(value);
            }
        });
        cost = new StatCost(stat, raceValue.getRace(), value);
    }

    /**
     * Factory function to use for making DexStatValue.
     */
    public static StatValue newDexStatValue(final RaceValue raceValue,
                                            final ZithiaStat stat,
                                            final ArmorValue armorValues,
                                            final StatValue strValue)
    {
        if (stat != ZithiaStat.DEX) {
            throw new RuntimeException("Invalid: DexStatValue is only for dex.");
        }
        if (strValue.getStat() != ZithiaStat.STR) {
            throw new RuntimeException("Invalid: DexStatValue must be based on str.");
        }
        SettableIntValue value = new SettableIntValueImpl(stat.getDefaultValue());
        TweakableIntValue roll = new EquationIntValue(
                value, armorValues.getDefPenalty(), strValue.getValue(),
                new Equation3() {
                    public int getValue(int dex, int defPenalty, int str) {
                        int normalRoll = stat.getRoll(dex);
                        return normalRoll - DexStatValue.strAdjustedArmorPenalty(str, defPenalty);
                    }
                }
        );
        TweakableIntValue cost = new StatCost(stat, raceValue.getRace(), value);
        return new DexStatValue(stat, value, roll, cost);
    }

    /**
     * A subclass of StatValue which is specialized for dex because the roll for
     * dex is affected by armor and str and so the constructor needs different
     * arguments.
     */
    private static class DexStatValue extends StatValue {

        /** Constructor that passes the values on to the special superclass constructor. */
        private DexStatValue(ZithiaStat stat, SettableIntValue value, TweakableIntValue roll, TweakableIntValue cost) {
            super(stat, value, roll, cost);
        }

        public static int strAdjustedArmorPenalty(int str, int armorPenalty) {
            int adjustedPenalty;
            if (str <= 4) {
                adjustedPenalty = armorPenalty * 2;
            } else if (str <= 10) {
                adjustedPenalty = armorPenalty;
            } else if (str <= 15) {
                adjustedPenalty = armorPenalty - 1;
            } else if (str <= 20) {
                adjustedPenalty = armorPenalty - 2;
            } else if (str <= 25) {
                adjustedPenalty = armorPenalty -3;
            } else {
                adjustedPenalty = armorPenalty - 4;
            }
            return Math.max(0, adjustedPenalty);
        }
    }


    /** A class to store and calculate stat costs. */
    private static class StatCost extends CalculatedIntValue<Observable> {
        public StatCost(final ZithiaStat stat, ObservableEnum<Race> obsRace, ObservableInt obsValue) {
            super(Arrays.asList(obsRace, obsValue), new ValueCalculator<Observable>() {
                @SuppressWarnings("unchecked")
                public int calculateValue(Iterable<? extends Observable> inputs) {
                    Iterator<? extends Observable> iter = inputs.iterator();
                    ObservableEnum<Race> obsRace = (ObservableEnum<Race>) iter.next();
                    ObservableInt obsValue = (ObservableInt) iter.next();
                    assert !iter.hasNext();
                    int value = obsValue.getValue();
                    Race race = obsRace.getValue();
                    int pointsRaised = value - stat.getDefaultValue() - race.getModifier(stat);
                    return stat.getCost(pointsRaised);
                }
            });
        }

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
