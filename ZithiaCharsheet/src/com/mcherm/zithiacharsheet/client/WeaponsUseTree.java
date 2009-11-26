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

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.model.WeaponTraining;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.ZithiaStat;
import com.mcherm.zithiacharsheet.client.model.weapon.SingleWeaponSkill;
import com.mcherm.zithiacharsheet.client.modeler.EquationIntValue;
import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;
import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
import com.mcherm.zithiacharsheet.client.modeler.ObservableList;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;


/**
 * A Tree view for viewing the weapons as they are used.
 */
public class WeaponsUseTree extends VerticalPanel {
    public WeaponsUseTree(ZithiaCharacter zithiaCharacter) {
        add(new WeaponsUseHeader());
        add(new WeaponsUseTreeTree(zithiaCharacter));
    }

    /** Number of pixels each level of the tree is indented, relative to the last. */
    public static final int INDENT_PIXELS_PER_LEVEL = 16;
    public static final int NAME_COL_WIDTH = 210;
    public static final int LEVELS_COL_WIDTH = 50;
    public static final int SPD_COL_WIDTH = 50;
    public static final int DMG_COL_WIDTH = 50;

    private static class WeaponsUseHeader extends Grid {
        // FIXME: Code duplication here, with WeaponsUseRow. Fix it.
        public WeaponsUseHeader() {
            super(1, 5);
            final CellFormatter cellFormatter = getCellFormatter();
            final int indent = INDENT_PIXELS_PER_LEVEL * 4;
            final int TOTAL_WIDTH = indent + NAME_COL_WIDTH + LEVELS_COL_WIDTH + SPD_COL_WIDTH + (DMG_COL_WIDTH * 2);
            setWidth(TOTAL_WIDTH + "px");
            cellFormatter.setWidth(0, 0, (indent + NAME_COL_WIDTH) + "px");
            cellFormatter.setWidth(0, 1, LEVELS_COL_WIDTH + "px");
            cellFormatter.setWidth(0, 2, SPD_COL_WIDTH + "px");
            cellFormatter.setWidth(0, 3, DMG_COL_WIDTH + "px");
            cellFormatter.setWidth(0, 4, DMG_COL_WIDTH + "px");

            setText(0, 0, "Name");
            setText(0, 1, "Levels");
            setText(0, 2, "Spd");
            setText(0, 3, "Hp");
            setText(0, 4, "Stun");
        }
    }

    private static class WeaponsUseTreeTree extends Tree {

        final ZithiaCharacter zithiaCharacter;

        public WeaponsUseTreeTree(ZithiaCharacter zithiaCharacter) {
            this.zithiaCharacter = zithiaCharacter;
            WeaponTraining wt = zithiaCharacter.getWeaponTraining();
            TreeItem treeItem = this.addItem(new WeaponsUseRow(wt));
            addChildren(treeItem, wt);
        }

        // FIXME: Probably leaves stray observers lying around. Won't matter if I implement differently
        private void addChildren(final TreeItem parentTreeItem, final WeaponTraining parentWt) {
            final ObservableList<WeaponTraining> childWts = parentWt.getChildren();
            innerAddChildren(parentTreeItem, childWts);
            childWts.addObserver(new Observer() {
                public void onChange() {
                    parentTreeItem.removeItems();
                    innerAddChildren(parentTreeItem, parentWt.getChildren());
                }
            });
        }

        private void innerAddChildren(TreeItem parentTreeItem, Iterable<WeaponTraining> childWts) {
            for (final WeaponTraining childWt : childWts) {
                TreeItem childTreeItem = parentTreeItem.addItem(new WeaponsUseRow(childWt));
                addChildren(childTreeItem, childWt);
            }
        }

        // FIXME: Doc this
        private class WeaponsUseRow extends Grid {

            /** Constructor. */
            public WeaponsUseRow(WeaponTraining wt) {
                super(1, 5);
                final CellFormatter cellFormatter = getCellFormatter();
                final int indent = INDENT_PIXELS_PER_LEVEL * wt.getWeaponSkill().getSpan();
                final int TOTAL_WIDTH = indent + NAME_COL_WIDTH + LEVELS_COL_WIDTH + SPD_COL_WIDTH + (DMG_COL_WIDTH * 2);
                setWidth(TOTAL_WIDTH + "px");
                cellFormatter.setWidth(0, 0, (indent + NAME_COL_WIDTH) + "px");
                cellFormatter.setWidth(0, 1, LEVELS_COL_WIDTH + "px");
                cellFormatter.setWidth(0, 2, SPD_COL_WIDTH + "px");
                cellFormatter.setWidth(0, 3, DMG_COL_WIDTH + "px");
                cellFormatter.setWidth(0, 4, DMG_COL_WIDTH + "px");
                setText(0, 0, wt.getWeaponSkill().getName());
                setWidget(0, 1, new TweakableIntField(wt.getLevels()));
                if (wt.getWeaponSkill() instanceof SingleWeaponSkill) {
                    SingleWeaponSkill sws = (SingleWeaponSkill) wt.getWeaponSkill();
                    final int weaponSpd = sws.getWeapon().getSpd();
                    ObservableInt charSpd = zithiaCharacter.getStat(ZithiaStat.SPD).getValue();
                    TweakableIntValue cycleTime = new EquationIntValue(charSpd, new EquationIntValue.Equation1() {
                        public int getValue(int charSpd) {
                            return charSpd + weaponSpd;
                        }
                    });
                    setWidget(0, 2, new TweakableIntField(cycleTime));
                    setText(0, 3, sws.getWeapon().getHpDmg().getStr());
                    setText(0, 4, sws.getWeapon().getStunDmg().getStr());
                }
            }

            /** Call this to release all observers before deleting it. */
            public void destroy() {
                // FIXME: Not working, because I can't release the EquationIntValue!
            }
        }

    }

}
