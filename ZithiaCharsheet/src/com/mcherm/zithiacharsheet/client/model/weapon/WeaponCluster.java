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
 * A WeaponCluster is any grouping of weapons, such as a WeaponGroup
 * or all melee weapons, etc. Instances are immutable. All WeaponCluster
 * instances have a parent instance EXCEPT for the AllCombatWeaponCluster
 * which has null as a parent. They also all have a span, which is an
 * integer:
 * <dl>
 *   <dt>4</dt>
 *   <dd>All Combat</dd>
 *   <dt>3</dt>
 *   <dd>Melee or Ranged</dd>
 *   <dt>2</dt>
 *   <dd>Weapon Group</dd>
 *   <dt>1</dt>
 *   <dd>Single Weapon</dd>
 * </dl>
 */
public class WeaponCluster {
    private final WeaponCluster parent;
    private final String id;
    private final String name;
    private final int span;
    
    public WeaponCluster(WeaponCluster parent, String id, String name, int span) {
        assert parent != null || span == 4;
        assert parent == null || span + 1 == parent.getSpan();
        this.parent = parent;
        this.id = id;
        this.name = name;
        this.span = span;
    }
    
    public WeaponCluster getParent() {
        return parent;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getSpan() {
        return span;
    }
    
}
