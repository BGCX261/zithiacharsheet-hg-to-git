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

import java.util.HashMap;
import java.util.Map;


/**
 * The enumerated type for supported races.
 * <p>
 * Design note: Perhaps the values for this can eventually be data driven. But
 * for now, I think it will be easier for me to hard-code the set of values in
 * the code.
 */
public enum Race {
    // --- Instances ---
    Human(0, new HashMap<ZithiaStat, Integer>() {}),
    Elf(5, new HashMap<ZithiaStat, Integer>() {{
        put(ZithiaStat.STR,  -3);
        put(ZithiaStat.CON,  -4);
        put(ZithiaStat.DEX,  +5);
        put(ZithiaStat.OBS,  +3);
    }}),
    Dwarf(8, new HashMap<ZithiaStat, Integer>() {{
        put(ZithiaStat.STR,  +3);
        put(ZithiaStat.CON,  +8);
        put(ZithiaStat.PRE,  -3);
        put(ZithiaStat.OBS,  -3);
        put(ZithiaStat.MOVE, -4);
    }}),
    Hob(3, new HashMap<ZithiaStat, Integer>() {{
        put(ZithiaStat.STR,  -3);
        put(ZithiaStat.DEX,  +2);
        put(ZithiaStat.WILL, +2);
        put(ZithiaStat.PRE,  -4);
        put(ZithiaStat.MOVE, -2);
        put(ZithiaStat.SPD,  -1);
    }}),
    ;

    // --- Instance Fields ---
    private int cost;
    private Map<ZithiaStat, Integer> statModifiers;

    /**
     * Private constructor. The Map passed may contain mulls for all stats
     * that are NOT modified.
     */
    private Race(int cost, Map<ZithiaStat, Integer> statModifiers) {
        this.cost = cost;
        this.statModifiers = statModifiers;
    }

    /** Returns the base cost to be a member of this race. */
    public int getCost() {
        return cost;
    }

    /**
     * Returns the stat modifier for this race for the specified stat.
     */
    public int getModifier(ZithiaStat stat) {
        Integer result = statModifiers.get(stat);
        return result == null ? 0 : result;
    }
}
