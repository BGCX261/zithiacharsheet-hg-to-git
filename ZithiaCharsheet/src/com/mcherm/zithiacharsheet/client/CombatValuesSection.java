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
package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.model.ArmorType;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;


/**
 * A section in the character sheet that displays the info needed to
 * engage in combat.
 */
public class CombatValuesSection extends VerticalPanel {

    public CombatValuesSection(final ZithiaCharacter zithiaCharacter) {
        addStyleName("combatValuesSection");
        add(new SettableEnumField<ArmorType>(zithiaCharacter.getArmorValue().getArmorType()));
        HorizontalPanel armorEffects = new HorizontalPanel();
        armorEffects.add(new HTML("Blocks&nbsp;"));
        armorEffects.add(new TweakableIntField(zithiaCharacter.getArmorValue().getHpBlock()));
        armorEffects.add(new HTML("/"));
        armorEffects.add(new TweakableIntField(zithiaCharacter.getArmorValue().getStunBlock()));
        armorEffects.add(new HTML("&nbsp;"));
        armorEffects.add(new TweakableIntField(zithiaCharacter.getArmorValue().getDefPenalty()));
        armorEffects.add(new HTML("to dex."));
        add(armorEffects);
        HorizontalPanel offenseDefense = new HorizontalPanel();
        offenseDefense.add(new Label("Off:"));
        offenseDefense.add(new TweakableIntField(zithiaCharacter.getCombatValues().getOffense()));
        offenseDefense.add(new HTML("&nbsp;"));
        offenseDefense.add(new Label("Def:"));
        offenseDefense.add(new TweakableIntField(zithiaCharacter.getCombatValues().getDefense()));
        add(offenseDefense);
        add(new HTML("<span id='toHit'>Hits Def: 3D6 + Off - 10</span>"));
    }
}
