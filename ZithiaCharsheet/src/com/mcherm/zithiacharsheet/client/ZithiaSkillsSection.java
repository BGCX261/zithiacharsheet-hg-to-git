package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.ZithiaSkill;
import com.mcherm.zithiacharsheet.client.model.ZithiaStat;

public class ZithiaSkillsSection extends VerticalPanel {
        
    public ZithiaSkillsSection(final ZithiaCharacter zithiaCharacter) {
        this.addStyleName("skills");
        ZithiaSkillsTable zithiaSkillsTable = new ZithiaSkillsTable(zithiaCharacter);
        this.add(zithiaSkillsTable);
        Button addSkillButton = new Button("Add Skill", new ClickHandler() {
            public void onClick(ClickEvent event) {
                ZithiaSkill skill = new ZithiaSkill(ZithiaStat.STR, true, 2, 2, "Bowling");
                zithiaCharacter.addNewSkill(skill);
            }
        });
        this.add(addSkillButton);
    }

}
