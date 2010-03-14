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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.model.SkillList;
import com.mcherm.zithiacharsheet.client.model.SkillValue;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.ZithiaSkill;

import java.util.ArrayList;


/**
 * The section of the character sheet where skills will be shown.
 */
public class ZithiaSkillsSection extends VerticalPanel {
    private final ZithiaSkillsTable zithiaSkillsTable;
    private final SelectSkillDialog addSkillDialog;


    public ZithiaSkillsSection(final ZithiaCharacter zithiaCharacter) {
        this.addStyleName("skills");
        zithiaSkillsTable = new ZithiaSkillsTable(zithiaCharacter);
        this.add(zithiaSkillsTable);
        addSkillDialog = new SelectSkillDialog(new SkillCatalogDisplay.SkillSelectCallback() {
            public void newSkillSelected(ZithiaSkill skill) {
                addSkillDialog.hide();
                zithiaCharacter.addNewSkill(skill);

            }
        });
        Button addSkillButton = new Button("Add Skill", new ClickHandler() {
            public void onClick(ClickEvent event) {
                addSkillDialog.center();
            }
        });
        Button deleteSkillButton = new Button("Delete Skill", new ClickHandler() {
            public void onClick(ClickEvent event) {
                int row = 1;
                CheckBox checkBox;
                SkillList skills = zithiaCharacter.getSkillList();
                ArrayList<SkillValue> deleteSkills = new ArrayList<SkillValue>();

                for (final SkillValue skillValue : skills) {
                    checkBox = (CheckBox) zithiaSkillsTable.getWidget (row, 0);
                    if (checkBox.getValue()){
                        deleteSkills.add(skillValue);
                    }
                    row++;
                }
                for (final SkillValue deleteSkill: deleteSkills){
                    skills.remove(deleteSkill);
                }
            }
        });

        this.add(addSkillButton);
        this.add(deleteSkillButton);
    }

}
