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

import java.util.Iterator;

import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
import com.mcherm.zithiacharsheet.client.modeler.SummableList;
import com.mcherm.zithiacharsheet.client.modeler.SummableList.Extractor;

/**
 * The full set of StatValues for a character.
 */
public class StatValues implements Iterable<StatValue> {
    
    private final SummableList<StatValue> stats;
    private final StatValue[] statValueArray;
    
    public StatValues(RaceValue raceValue, ArmorValue armorValues) {
        stats = new SummableList<StatValue>(new Extractor<StatValue>() {
            public ObservableInt extractValue(StatValue item) {
                return item.getCost();
            }
        });
        statValueArray = new StatValue[ZithiaStat.getNumStats()];
        for (final ZithiaStat stat : ZithiaStat.values()) {
            StatValue statValue;
            if (stat == ZithiaStat.DEX) {
                statValue = StatValue.newDexStatValue(
                        raceValue, stat, armorValues, statValueArray[ZithiaStat.STR.ordinal()]);
            } else {
                statValue = new StatValue(raceValue, stat);
            }
            stats.add(statValue);
            statValueArray[stat.ordinal()] = statValue;
        }
    }

    /**
     * Iterate through the stats.
     */
    public Iterator<StatValue> iterator() {
        return stats.iterator();
    }
    
    /**
     * Return the value of a particular stat.
     */
    public StatValue getStat(ZithiaStat stat) {
        return statValueArray[stat.ordinal()];
    }
    
    /**
     * A TweakableValue for the cost of the stats.
     */
    public ObservableInt getCost() {
        return stats.getSum();
    }
    
}
