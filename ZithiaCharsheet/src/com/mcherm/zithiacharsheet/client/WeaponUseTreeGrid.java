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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.TreeImages;
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
    /*TreeGridItem rootItem, int numColumns, TreeImages treeImages, boolean showHeader*/

    private final static int NUM_COLUMNS = 5;

    /** Constructor. */
    public WeaponUseTreeGrid(ZithiaCharacter zithiaCharacter) {
        super(new WeaponUseTreeGridItem(zithiaCharacter, zithiaCharacter.getWeaponTraining()),
                NUM_COLUMNS, GWT.<TreeImages> create(TreeImages.class), false);
    }

    private static class WeaponUseTreeGridItem implements TreeGridItem {
        private final ZithiaCharacter zithiaCharacter;
        private final WeaponTraining wt;

        public WeaponUseTreeGridItem(ZithiaCharacter zithiaCharacter, WeaponTraining wt) {
            this.zithiaCharacter = zithiaCharacter;
            this.wt = wt;
        }

        public WidgetOrText getContents(int column) {
            switch(column) {
                case 0: return new WidgetOrText(wt.getWeaponSkill().getName());
                case 1: return new WidgetOrText(new TweakableIntField(wt.getLevels()));
            }

            if (wt.getWeaponSkill() instanceof SingleWeaponSkill) {
                SingleWeaponSkill sws = (SingleWeaponSkill) wt.getWeaponSkill();
                switch(column) {
                    case 2: {
                        final int weaponSpd = sws.getWeapon().getSpd();
                        ObservableInt charSpd = zithiaCharacter.getStat(ZithiaStat.SPD).getValue();
                        TweakableIntValue cycleTime = new EquationIntValue(charSpd, new EquationIntValue.Equation1() {
                            public int getValue(int charSpd) {
                                return charSpd + weaponSpd;
                            }
                        });
                        return new WidgetOrText(new TweakableIntField(cycleTime));
                    }
                    case 3: return new WidgetOrText(sws.getWeapon().getHpDmg().getStr());
                    case 4: return new WidgetOrText(sws.getWeapon().getStunDmg().getStr());
                    default: throw new RuntimeException("Should not reach here.");
                }
            } else {
                switch(column) {
                    case 2: return new WidgetOrText("");
                    case 3: return new WidgetOrText("");
                    case 4: return new WidgetOrText("");
                    default: throw new RuntimeException("Should not reach here.");
                }
            }
        }

        public boolean isLeaf() {
            return false;
        }

        public boolean hasChildren() {
            return false;
        }

        public Iterable<TreeGridItem> getChildren() {
            return null;
        }
    }
}
