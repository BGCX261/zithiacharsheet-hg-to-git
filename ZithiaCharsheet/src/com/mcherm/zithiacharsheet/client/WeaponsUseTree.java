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
public class WeaponsUseTree extends Tree {

    final ZithiaCharacter zithiaCharacter;

    public WeaponsUseTree(ZithiaCharacter zithiaCharacter) {
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

    private class WeaponUseTreeItem extends TreeItem {
        public WeaponUseTreeItem() {
            super();
        }
    }


    // FIXME: Doc this
    private class WeaponsUseRow extends Grid {
        /** Number of pixels each level of the tree is indented, relative to the last. */
        private final int INDENT_PIXELS_PER_LEVEL = 16;
        private final int NAME_COL_WIDTH = 240;
        private final int LEVELS_COL_WIDTH = 40;
        private final int DMG_COL_WIDTH = 65;

        /** Constructor. */
        public WeaponsUseRow(WeaponTraining wt) {
            super(1, 3);
            final CellFormatter cellFormatter = getCellFormatter();
            final int indent = INDENT_PIXELS_PER_LEVEL * wt.getWeaponSkill().getSpan();
            setWidth((indent + NAME_COL_WIDTH + LEVELS_COL_WIDTH + DMG_COL_WIDTH) + "px");
            cellFormatter.setWidth(0, 0, (indent + NAME_COL_WIDTH) + "px");
            cellFormatter.setWidth(0, 1, LEVELS_COL_WIDTH + "px");
            cellFormatter.setWidth(0, 2, DMG_COL_WIDTH + "px");
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
            }
        }

        /** Call this to release all observers before deleting it. */
        public void destroy() {
            // FIXME: Not working, because I can't release the EquationIntValue!
        }
    }

}
