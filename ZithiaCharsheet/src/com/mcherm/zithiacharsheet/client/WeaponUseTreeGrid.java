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
package com.mcherm.zithiacharsheet.client;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.model.WeaponTraining;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.ZithiaStat;
import com.mcherm.zithiacharsheet.client.model.weapon.SingleWeaponSkill;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue;
import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;


/**
 * A Tree view for viewing the weapons as they are used. Built using my
 * TreeGrid class.
 */
public class WeaponUseTreeGrid extends TreeGrid {

    private final static int NUM_COLUMNS = 6;

    /** Constructor. */
    public WeaponUseTreeGrid(ZithiaCharacter zithiaCharacter) {
        super(new WeaponUseTreeGridItem(zithiaCharacter, zithiaCharacter.getWeaponTraining()),
                NUM_COLUMNS, GWT.<TreeImages> create(TreeImages.class));
    }


    /**
     * Display a header giving the meaning of the different columns.
     */
    protected List<WidgetOrText> getHeader() {
        VerticalPanel totalLevels = new VerticalPanel();
        totalLevels.add(new HTML("Total"));
        totalLevels.add(new HTML("Levels"));
        return Arrays.asList(
                new WidgetOrText(""),
                new WidgetOrText("Trained"),
                new WidgetOrText(totalLevels),
                new WidgetOrText("Speed"),
                new WidgetOrText("Hp"),
                new WidgetOrText("Stun")
        );
    }

    /** Contents to display in each row of this table. */
    private static class WeaponUseTreeGridItem extends WeaponSkillTreeGridItem {
        private final ZithiaCharacter zithiaCharacter;

        public WeaponUseTreeGridItem(ZithiaCharacter zithiaCharacter, WeaponTraining wt) {
            super(wt);
            this.zithiaCharacter = zithiaCharacter;
        }

        public WeaponSkillTreeGridItem newInstance(WeaponTraining wt) {
            return new WeaponUseTreeGridItem(zithiaCharacter, wt);
        }

        public List<WidgetOrText> getContents() {
            WidgetOrText col_0 = new WidgetOrText(wt.getWeaponSkill().getName());
            WidgetOrText col_1 = new WidgetOrText(new TrainingEntryField(wt));
            WidgetOrText col_2 = new WidgetOrText(new TweakableIntField(wt.getLevels()));
            WidgetOrText col_3, col_4, col_5;

            if (wt.getWeaponSkill() instanceof SingleWeaponSkill) {
                SingleWeaponSkill sws = (SingleWeaponSkill) wt.getWeaponSkill();
                final int weaponSpd = sws.getWeapon().getSpd();
                ObservableInt charSpd = zithiaCharacter.getStat(ZithiaStat.SPD).getValue();
                TweakableIntValue cycleTime = new EquationIntValue(charSpd, new EquationIntValue.Equation1() {
                    public int getValue(int charSpd) {
                        return charSpd + weaponSpd;
                    }
                });
                col_3 = new WidgetOrText(new TweakableIntField(cycleTime));
                col_4 = new WidgetOrText(sws.getWeapon().getHpDmg().getStr());
                col_5 = new WidgetOrText(sws.getWeapon().getStunDmg().getStr());
            } else {
                col_3 = col_4 = col_5 = new WidgetOrText("");
            }
            return Arrays.asList(
                    col_0,
                    col_1,
                    col_2,
                    col_3,
                    col_4,
                    col_5
            );
        }

    }
}
