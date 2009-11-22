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
 * This is a weapons skill that a character can learn, such
 * as "hand axe" or "all ranged weapons". A character could
 * learn either basic training OR levels with the skill.
 * <p>
 * Instances are immutable.
 */
public abstract class WeaponSkill {

    public abstract String getId();
    public abstract String getName();
    public abstract int getBasicTrainingCost();
    public abstract int getFirstLevelCost();
    public abstract int getSpan();

}
