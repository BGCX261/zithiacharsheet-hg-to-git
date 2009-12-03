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

import com.mcherm.zithiacharsheet.client.model.StatCost.DexStatCost;
import com.mcherm.zithiacharsheet.client.model.StatCost.HpStunStatCost;
import com.mcherm.zithiacharsheet.client.model.StatCost.MoveStatCost;
import com.mcherm.zithiacharsheet.client.model.StatCost.NormalStatCost;
import com.mcherm.zithiacharsheet.client.model.StatCost.SpdStatCost;


public enum ZithiaStat {
    STR("Str", new NormalStatCost(), true, 10),
    DEX("Dex", new DexStatCost(), true, 10),
    CON("Con", new NormalStatCost(), true, 10),
    INT("Int", new NormalStatCost(), true, 10),
    WILL("Will", new NormalStatCost(), true, 10),
    OBS("Obs", new NormalStatCost(), true, 10),
    PRE("Pre", new NormalStatCost(), true, 10),
    HP("Hp", new HpStunStatCost(), false, 10),
    STUN("Stun", new HpStunStatCost(), false, 10),
    MOVE("Move", new MoveStatCost(), false, 10),
    SPD("Spd", new SpdStatCost(), false, 6),
    ;
    
    public static int getNumStats() {
        return ZithiaStat.values().length;
    }
    
    private final String name;
    private final StatCost statCost;
    private final boolean hasRoll;
    private final int defaultValue;
    
    private ZithiaStat(String name, StatCost statCost, boolean hasRoll, int defaultValue) {
        this.name = name;
        this.statCost = statCost;
        this.hasRoll = hasRoll;
        this.defaultValue = defaultValue;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * This returns the cost to raise this stat by amountRaised points.
     * amountRaised must be greater than or equal to -9 for stats other
     * than Spd.
     * 
     * @param pointsRaised the number of points it was raised.
     * @return the cost
     */
    public int getCost(int pointsRaised) {
        return statCost.getCost(pointsRaised);
    }
    
    /**
     * Returns true if this kind of stat has a roll, false if it doesn't.
     */
    public boolean hasRoll() {
        return hasRoll;
    }
    
    /**
     * This returns the roll.
     * 
     * @param statValue the actual value of the stat
     * @return the roll (this or less on 3D6)
     */
    public int getRoll(int statValue) {
        return 7 + (statValue / 3);
    }
    
    /**
     * This returns the default starting value of the stat for humans.
     * @return
     */
    public int getDefaultValue() {
        return defaultValue;
    }
}

