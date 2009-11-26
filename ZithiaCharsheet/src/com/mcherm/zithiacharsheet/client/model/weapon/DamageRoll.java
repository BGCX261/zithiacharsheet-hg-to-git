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
package com.mcherm.zithiacharsheet.client.model.weapon;


/**
 * This represents a single roll of some number of dice.
 * <p>
 * Instances are immutable.
 */
public class DamageRoll {
    private String str;

    /** Constructor. */
    public DamageRoll(String str) {
        this.str = str;
    }

    /**
     * Return the string describing the roll. For instance, "2D6-1".
     * @return
     */
    public String getStr() {
        return str;
    }
}
