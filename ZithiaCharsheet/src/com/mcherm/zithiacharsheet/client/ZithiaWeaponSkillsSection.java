package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponTraining;


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
        String name = wt.getWeaponSkill().getName();
        boolean isTrained = hasBasicTraining || wt.getBasicTrainingPurchased();
        int levelsPurchased = wt.getLevelsPurchased();
        int netLevels = levels + levelsPurchased;
        this.add(new HTML("<div class='weaponSkillBox'>" +
                name + ": " + (isTrained ? "" : "<untrained> ") +
                netLevels + " levels " +
                "[" + levelsPurchased + "]" +
        		"</div>"));
        for (WeaponTraining child : wt.getChildren()) {
            showWT(child, isTrained, netLevels);
        }
    }
}
