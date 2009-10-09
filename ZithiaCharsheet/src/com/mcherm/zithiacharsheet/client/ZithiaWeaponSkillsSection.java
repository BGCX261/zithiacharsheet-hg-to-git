package com.mcherm.zithiacharsheet.client;

import java.util.Arrays;
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
                        List<String> values = Arrays.asList("abc", "def", "ghi");
                        StringSelectionDialog ssd = new StringSelectionDialog(values, 
                            new ItemSelectCallback<String>() {
                                public void newItemSelected(String item) {
                                    Window.alert("Selected the string '" + item + "'.");
                                }
                             }
                        );
                        ssd.show();
                    }
                });
                this.add(newChildButton);
            }
        }
    }
    
    
    private static class StringSelectionDialog extends FancyListSelectionDialog<String> {

        public StringSelectionDialog(List<String> items,
                ItemSelectCallback<String> itemSelectCallback)
        {
            super(
                items,
                new ItemDisplayCallback<String>() {

                    @Override
                    public FancyListSelectionDialog.ItemDisplay<String> getDisplay(final String item) {
                        return new FancyListSelectionDialog.ItemDisplay<String>() {
                            public Widget contents(int column) {
                                switch(column) {
                                case 0: return new Label("X");
                                case 1: return new Label(item);
                                default: throw new ArrayIndexOutOfBoundsException();
                                }
                            }
                        };
                    }
                    public int getNumColumns() {
                        return 2;
                    }
                },
                itemSelectCallback
            );
        }
        
    }
}
