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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mcherm.zithiacharsheet.client.FancyListSelectionDialog.ItemDisplayCallback;
import com.mcherm.zithiacharsheet.client.FancyListSelectionDialog.ItemSelectCallback;
import com.mcherm.zithiacharsheet.client.model.WeaponTraining;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponClusterSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponsCatalog;
import com.mcherm.zithiacharsheet.client.modeler.ObservableList;
import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;


/**
 * A section within a character sheet for displaying the
 * weapon levels and such.
 */
public class ZithiaWeaponSkillsSection extends TabPanel {
    public ZithiaWeaponSkillsSection(final ZithiaCharacter zithiaCharacter) {
        O oldView = new O(zithiaCharacter);
        add(new WeaponUseTreeGrid(zithiaCharacter), "Use");
        add(new W(zithiaCharacter), "Cost");
        add(oldView, "Debug");
        add(new WeaponsUseTree(zithiaCharacter), "Use (old)");
        selectTab(0);
    }
}


// FIXME: This SHOULD be named 'OLD_ZithiaWeaponSkillsSection'. I renamed it due to path length issues
// FIXME: This is a temp hack until I have it working. It's the messy first-draft of weapon skills
class O extends VerticalPanel {

    public O(final ZithiaCharacter zithiaCharacter) {
        this.addStyleName("weaponSkills");
        final WeaponTraining wt = zithiaCharacter.getWeaponTraining();
        showWTInPanel(this, wt);
        this.add(new Button("Prune", new ClickHandler() {
            public void onClick(ClickEvent event) {
                wt.prune();
            }
        }));
        this.add(new Button("Clean", new ClickHandler() {
            public void onClick(ClickEvent event) {
                wt.clean();
            }
        }));
    }

    /**
     * To the panel, add up to 2 items: a row the the WeaponTraining wt and
     * (if appropriate) an inner pane with its children.
     */
    private static void showWTInPanel(VerticalPanel panel, WeaponTraining wt) {
        panel.add(new WeaponTrainingRow(wt));
        final ObservableList<WeaponTraining> children = wt.getChildren();
        final VerticalPanel childPanel = new VerticalPanel();
        if (!children.isEmpty()) {
            childPanel.addStyleName("weaponSkillsChildPanel");
            for (WeaponTraining childWT : children) {
                showWTInPanel(childPanel, childWT);
            }
        }
        panel.add(childPanel);
        children.addObserver(new Observer() {
            public void onChange() {
                childPanel.clear();
                childPanel.addStyleName("weaponSkillsChildPanel");
                for (WeaponTraining childWT : children) {
                    showWTInPanel(childPanel, childWT);
                }
            }
        });
    }
    
    
    /**
     * A single row describing a WeaponTraining.
     */
    private static class WeaponTrainingRow extends HorizontalPanel {
        public WeaponTrainingRow(final WeaponTraining wt) {
            String name = wt.getWeaponSkill().getName();
            this.addStyleName("weaponSkillRow");
            this.add(new HTML(name + ": "));
            this.add(new HTML("levels-"));
            this.add(new TweakableIntField(wt.getLevels()));
            this.add(new HTML("["));
            this.add(new SettableIntField(wt.getLevelsPurchased()));
            this.add(new HTML("]"));
            this.add(new SettableBooleanField("Trained", wt.getBasicTrainingPurchased()));
            this.add(new HTML(" cost-"));
            this.add(new TweakableIntField(wt.getTotalCost()));
            this.add(new HTML("["));
            this.add(new TweakableIntField(wt.getThisCost()));
            this.add(new HTML("]"));
            if (wt.getWeaponSkill() instanceof WeaponClusterSkill) {
                final WeaponClusterSkill weaponClusterSkill = (WeaponClusterSkill) wt.getWeaponSkill();
                Button newChildButton = new Button("Add", new ClickHandler() {
                    public void onClick(ClickEvent event) {
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
                });
                this.add(newChildButton);
            }
        }
    }
    
}
