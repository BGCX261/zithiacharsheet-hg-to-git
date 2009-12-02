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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.Widget;
import com.mcherm.zithiacharsheet.client.FancyListSelectionDialog.ItemDisplayCallback;
import com.mcherm.zithiacharsheet.client.FancyListSelectionDialog.ItemSelectCallback;
import com.mcherm.zithiacharsheet.client.model.WeaponTraining;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.weapon.SingleWeaponSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponClusterSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponsCatalog;


/**
 * A TreeGrid table for displaying and editing the spending on weapon
 * skills.
 */
public class WeaponCostTreeGrid extends TreeGrid {

    private final static int NUM_COLUMNS = 5;

    /** Constructor. */
    public WeaponCostTreeGrid(ZithiaCharacter zithiaCharacter) {
        super(new WeaponCostTreeGridItem(zithiaCharacter.getWeaponTraining()),
                NUM_COLUMNS, GWT.<TreeImages> create(TreeImages.class));
    }

    /**
     * Display a header giving the meaning of the different columns.
     */
    protected List<WidgetOrText> getHeader() {
        return Arrays.asList(
                new WidgetOrText(""),
                new WidgetOrText(""),
                new WidgetOrText("Levels"),
                new WidgetOrText("Train"),
                new WidgetOrText("Cost")
        );
    }

    /** Contents to display in each row of this table. */
    private static class WeaponCostTreeGridItem extends WeaponSkillTreeGridItem {

        /** Constructor. */
        public WeaponCostTreeGridItem(WeaponTraining wt) {
            super(wt);
        }

        public WeaponSkillTreeGridItem newInstance(WeaponTraining wt) {
            return new WeaponCostTreeGridItem(wt);
        }

        public List<WidgetOrText> getContents() {
            return Arrays.asList(
                new WidgetOrText(wt.getWeaponSkill().getName()),
                new WidgetOrText(getPlusMinus()),
                new WidgetOrText(disposer.track(new SettableIntField(wt.getLevelsPurchased()))),
                new WidgetOrText(disposer.track(new TrainingEntryField(wt))),
                new WidgetOrText(disposer.track(new TweakableIntField(wt.getThisCost())))
            );
        }


        /** Gets the widget for adding/removing rows. */
        private Widget getPlusMinus() {
            // Since we don't currently have a count of max possible children, the only
            // way to be disqualified from adding a child is if this cannot have children
            boolean canAddChild = !(wt.getWeaponSkill() instanceof SingleWeaponSkill);

            if (wt.canPrune()) {
                if (canAddChild) {
                    HorizontalPanel result = new HorizontalPanel();
                    result.add(getPlus());
                    result.add(new HTML("/"));
                    result.add(getMinus());
                    return result;
                } else {
                    return getMinus();
                }
            } else {
                if (canAddChild) {
                    return getPlus();
                } else {
                    return new HTML("");
                }
            }
        }

        /** Gets the widget for adding rows to the table. */
        private Widget getPlus() {
            Anchor result = new Anchor("+");
            result.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent clickEvent) {
                    addChild();
                }
            });
            return result;
        }

        /** Gets the widget for removing rows from the table. */
        private Widget getMinus() {
            Anchor result = new Anchor("-");
            result.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent clickEvent) {
                    removeWeaponTraining();
                }
            });
            return result;
        }

        /** This is called when the "+" is clicked to allow the user to select and add a child. */
        private void addChild() {
            final WeaponClusterSkill weaponClusterSkill = (WeaponClusterSkill) wt.getWeaponSkill();
            List<WeaponSkill> eligibleSkills = WeaponsCatalog.getSingleton().getChildren(weaponClusterSkill);
            final FancyListSelectionDialog<WeaponSkill> selector = new FancyListSelectionDialog<WeaponSkill>(
                eligibleSkills,
                new ItemDisplayCallback<WeaponSkill>() {
                    public List<Widget> getDisplay(WeaponSkill weaponSkill) {
                        final List<Widget> result = new ArrayList<Widget>(2);
                        String name = weaponSkill.getName();
                        result.add(new Label(name));
                        return result;
                    }
                },
                new ItemSelectCallback<WeaponSkill>() {
                    public void newItemSelected(WeaponSkill weaponSkill) {
                        wt.createChild(weaponSkill);
                    }
                 },
                 true,
                 "Select a skill:"
            );
            selector.show();
        }

        /** This is called when the "-" is clicked; it removes this from the tree. */
        private void removeWeaponTraining() {
            wt.getParent().removeChild(wt);
        }
    }


}
