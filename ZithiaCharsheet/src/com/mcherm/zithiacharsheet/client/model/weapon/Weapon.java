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
package com.mcherm.zithiacharsheet.client.model.weapon;

/**
 * A Weapon is a single kind of damage-dealing entity. An example would
 * be a Broadsword. Instances are immutable.
 */
public class Weapon {
    private final WeaponGroup weaponGroup;
    private final String id;
    private final String name;
    private final int strMin;
    private final int spd;
    // FIXME: Also has damage, but I have to figure out how that is stored first.
    
    /**
     * Constructor.
     */
    public Weapon(WeaponGroup weaponGroup, String id, String name, int strMin, int spd) {
        this.weaponGroup = weaponGroup;
        this.id = id;
        this.name = name;
        this.strMin = strMin;
        this.spd = spd;
    }
    
    public WeaponGroup getWeaponGroup() {
        return weaponGroup;
    }
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public int getStrMin() {
        return strMin;
    }
    
    public int getSpd() {
        return spd;
    }
    
}
