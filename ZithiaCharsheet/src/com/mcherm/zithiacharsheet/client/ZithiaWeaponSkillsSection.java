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
package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.TabPanel;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;


/**
 * A section within a character sheet for displaying the
 * weapon levels and such.
 */
public class ZithiaWeaponSkillsSection extends TabPanel {
    public ZithiaWeaponSkillsSection(final ZithiaCharacter zithiaCharacter) {
        add(new WeaponUseTreeGrid(zithiaCharacter), "Use");
        add(new WeaponCostTreeGrid(zithiaCharacter), "Cost");
        selectTab(0);
    }
}
