package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
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
            boolean isTrained = hasBasicTraining || wt.getBasicTrainingPurchased();
            int levelsPurchased = wt.getLevelsPurchased();
            int netLevels = levels + levelsPurchased;
            this.addStyleName("weaponSkillRow");
            this.add(new HTML(
                    name + ": " + (isTrained ? "" : "<untrained> ") +
                    netLevels + " levels " +
                    "[" + levelsPurchased + "]"
            ));
            if (wt.getWeaponSkill() instanceof WeaponClusterSkill) {
                final WeaponClusterSkill weaponClusterSkill = (WeaponClusterSkill) wt.getWeaponSkill();
                Button newChildButton = new Button("Add", new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        // FIXME: There's no chooser yet, so we add ALL of them!
                        for (WeaponSkill weaponSkill : WeaponsCatalog.getSingleton().getChildren(weaponClusterSkill)) {
                            WeaponTraining newWT = wt.createChild(weaponSkill);
                            // FIXME: There's no observation yet, so we'll manually re-create it
                            showWT(newWT, false, 0);
                        }
                    }
                });
                this.add(newChildButton);
            }
        }
    }
}
