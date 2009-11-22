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
 * This is a skill with a single weapon, such as Broadsword.
 * Instances are immutable.
 */
public class SingleWeaponSkill extends WeaponSkill {
    private final Weapon weapon;
    private final int basicTrainingCost;
    private final int firstLevelCost;
    
    public SingleWeaponSkill(Weapon weapon, int basicTrainingCost, int firstLevelCost) {
        this.weapon = weapon;
        this.basicTrainingCost = basicTrainingCost;
        this.firstLevelCost = firstLevelCost;
    }

    public Weapon getWeapon() {
        return weapon;
    }
    
    @Override
    public String getId() {
        return weapon.getId();
    }
    
    @Override
    public String getName() {
        return weapon.getName();
    }
    
    @Override
    public int getBasicTrainingCost() {
        return basicTrainingCost;
    }

    @Override
    public int getFirstLevelCost() {
        return firstLevelCost;
    }

    @Override
    public int getSpan() {
        return 1;
    }

}
