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

/**
 * An instance of this represents a type of armor that can be purchased.
 */
public enum ArmorType {
    NONE("Normal Clothing", 0, 0, 0),
    LEATHER_CLOTHS("Leather Clothing", 0, 1, 1),
    QUILTED("Quilted", 0, 3, 1),
    LEATHER_ARMOR("Leather Armor", 1, 1, 1),
    BAMBOO("Bamboo", 3, 1, 2),
    CUIR_BOULLI("Cuir Boulli", 1, 3, 2),
    STUDDED("Studded Leather", 2, 2, 2),
    BRIGANDINE("Brigandine", 3, 3, 3),
    CHAIN_MAIL("Chain Mail", 4, 2, 3),
    SCALE_MAIL("Scale Mail", 3, 4, 4),
    PLATE_AND_CHAIN("Plate & Chain", 4, 4, 4),
    PLATE_MAIL("Plate Mail", 5, 4, 5),
    FIELD_PLATE("Field Plate", 5, 5, 5),
    FULL_PLATE("Full Plate", 5, 6, 4),
    ;


    private final String name;
    private final int hpBlock;
    private final int stunBlock;
    private final int defPenalty;

    private ArmorType(String name, int hpBlock, int stunBlock, int defPenalty) {
        this.name = name;
        this.hpBlock = hpBlock;
        this.stunBlock = stunBlock;
        this.defPenalty = defPenalty;
    }

    /** The name of the armor, for display. */
    public String getName() {
        return name;
    }

    /** Number of hit points blocked on each blow. */
    public int getHpBlock() {
        return hpBlock;
    }

    /** Number of stun points blocked on each blow. */
    public int getStunBlock() {
        return stunBlock;
    }

    /** Penalty to def and dex rolls, before mitigation by high Str. */
    public int getDefPenalty() {
        return defPenalty;
    }
}
