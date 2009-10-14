package com.mcherm.zithiacharsheet.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mcherm.zithiacharsheet.client.FancyListSelectionDialog.ItemDisplayCallback;
import com.mcherm.zithiacharsheet.client.FancyListSelectionDialog.ItemSelectCallback;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponClusterSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponTraining;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponsCatalog;


/**
 * A section within a character sheet for displaying the
 * weapon levels and such.
 */
public class ZithiaWeaponSkillsSection extends VerticalPanel {

    public ZithiaWeaponSkillsSection(final ZithiaCharacter zithiaCharacter) {
        this.addStyleName("weaponSkills");
        WeaponTraining weaponTraining = zithiaCharacter.getWeaponTraining();
        showWT(weaponTraining, false, 0);
    }
    
    /**
     * A subroutine of the constructor, used just to get recursion.
     */
    private void showWT(WeaponTraining wt, boolean hasBasicTraining, int levels) {
        this.add(new WeaponTrainingRow(wt, hasBasicTraining, levels));
        boolean isTrained = hasBasicTraining || wt.getBasicTrainingPurchased();
        int netLevels = levels + wt.getLevelsPurchased();
        for (WeaponTraining child : wt.getChildren()) {
            showWT(child, isTrained, netLevels);
        }
    }
    
    // FIXME: Could probably be static
    private class WeaponTrainingRow extends HorizontalPanel {
        public WeaponTrainingRow(final WeaponTraining wt, boolean hasBasicTraining, int levels) {
            String name = wt.getWeaponSkill().getName();
            boolean isTrained = wt.isTrained();
            int levelsPurchased = wt.getLevelsPurchased();
            int netLevels = wt.getLevels();
            this.addStyleName("weaponSkillRow");
            this.add(new HTML(
                    name + ": " + (isTrained ? "" : "<untrained> ") +
                    netLevels + " levels " +
                    "[" + levelsPurchased + "]"
            ));
            if (wt.getWeaponSkill() instanceof WeaponClusterSkill) {
                final WeaponClusterSkill weaponClusterSkill = (WeaponClusterSkill) wt.getWeaponSkill();
                Button trainButton = new Button("Train", new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        if (wt.isTrained()) {
                            wt.setLevelsPurchased(wt.getLevelsPurchased() + 1);
                        } else {
                            wt.setBasicTrainingPurchased(true);
                        }
                        Window.alert("Probably just trained."); // FIXME: Remove when display works.
                    }
                });
                this.add(trainButton);
                Button newChildButton = new Button("Add", new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        List<WeaponSkill> eligibleSkills = WeaponsCatalog.getSingleton().getChildren(weaponClusterSkill);
                        final FancyListSelectionDialog<WeaponSkill> selector = new FancyListSelectionDialog<WeaponSkill>(
                            eligibleSkills,
                            new ItemDisplayCallback<WeaponSkill>() {
                                @Override
                                public List<Widget> getDisplay(WeaponSkill weaponSkill) {
                                    final List<Widget> result = new ArrayList<Widget>(2);
                                    String name = weaponSkill.getName();
                                    result.add(new Label(name));
                                    return result;
                                }
                            },
                            new ItemSelectCallback<WeaponSkill>() {
                                @Override
                                public void newItemSelected(WeaponSkill weaponSkill) {
                                    WeaponTraining newWT = wt.createChild(weaponSkill);
                                    // FIXME: There's no observation yet, so we'll manually re-create it
                                    showWT(newWT, false, 0);
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
