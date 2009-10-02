package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.ZithiaSkill;


/**
 * The section of the character sheet where skills will be shown.
 */
public class ZithiaSkillsSection extends VerticalPanel {
    private final ZithiaSkillsTable zithiaSkillsTable;
    private final SelectSkillDialog testDialog;

        
    public ZithiaSkillsSection(final ZithiaCharacter zithiaCharacter) {
        this.addStyleName("skills");
        zithiaSkillsTable = new ZithiaSkillsTable(zithiaCharacter);
        this.add(zithiaSkillsTable);
        testDialog = new SelectSkillDialog(new SkillCatalogDisplay.SkillSelectCallback() {
            public void newSkillSelected(ZithiaSkill skill) {
                zithiaCharacter.addNewSkill(skill);
                testDialog.hide();
            }
        });
        Button addSkillButton = new Button("Add Skill", new ClickHandler() {
            public void onClick(ClickEvent event) {
                testDialog.show();
            }
        });
        this.add(addSkillButton);
    }

}
