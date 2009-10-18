package com.mcherm.zithiacharsheet.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mcherm.zithiacharsheet.client.FancyListSelectionDialog.ItemDisplayCallback;
import com.mcherm.zithiacharsheet.client.FancyListSelectionDialog.ItemSelectCallback;
import com.mcherm.zithiacharsheet.client.model.WeaponTraining;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponClusterSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponsCatalog;


/**
 * A section within a character sheet for displaying the
 * weapon levels and such.
 * <p>
 * FIXME: Probably needs to use SettableIntField and TweakableIntField.
 */
public class ZithiaWeaponSkillsSection extends VerticalPanel {

    public ZithiaWeaponSkillsSection(final ZithiaCharacter zithiaCharacter) {
        this.addStyleName("weaponSkills");
        WeaponTraining weaponTraining = zithiaCharacter.getWeaponTraining();
        showWT(weaponTraining);
    }
    
    /**
     * A subroutine of the constructor, used just to get recursion.
     */
    private void showWT(WeaponTraining wt) {
        this.add(new WeaponTrainingRow(wt));
        for (WeaponTraining child : wt.getChildren()) {
            showWT(child);
        }
    }
    
    // FIXME: Could probably be static
    private class WeaponTrainingRow extends HorizontalPanel {
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
                                    showWT(newWT);
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
