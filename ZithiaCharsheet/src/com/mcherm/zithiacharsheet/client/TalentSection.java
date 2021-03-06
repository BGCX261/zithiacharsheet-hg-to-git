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
import com.google.gwt.user.client.ui.*;
import com.mcherm.zithiacharsheet.client.model.TalentList;
import com.mcherm.zithiacharsheet.client.model.TalentValue;
import com.mcherm.zithiacharsheet.client.modeler.Disposable;
import com.mcherm.zithiacharsheet.client.modeler.Disposer;
import com.mcherm.zithiacharsheet.client.modeler.Observable;

import java.util.ArrayList;


/**
 * The section where talents are displayed and edited.
 */
public class TalentSection extends VerticalPanel implements Disposable {
    private final Disposer disposer = new Disposer();

        
    public TalentSection(final TalentList talentList) {
        this.addStyleName("talents");
        final TalentTable talentTable = disposer.track(new TalentTable(talentList));
        this.add(talentTable);
        Button addSkillButton = new Button("Add", new ClickHandler() {
            public void onClick(ClickEvent event) {
                talentList.add(new TalentValue());
            }
        });
        Button deleteSkillButton = new Button ("Delete", new ClickHandler(){
            public void onClick (ClickEvent event){
                int row = 1;
                CheckBox checkBox;

                ArrayList<TalentValue> deleteSkills = new ArrayList<TalentValue>();

                for (final TalentValue talentValue : talentList) {
                    checkBox = (CheckBox) talentTable.getWidget (row, 0);
                    if (checkBox.getValue()){
                        deleteSkills.add(talentValue);
                    }
                    row++;
                }
                for (final TalentValue deleteSkill: deleteSkills){
                    talentList.remove(deleteSkill);
                }


            }
        });
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(addSkillButton);
        horizontalPanel.add(deleteSkillButton);
        this.add(horizontalPanel);
        
        this.add(horizontalPanel);
    }


    /**
     * The table of talents
     */
    private static class TalentTable extends FlexTable implements Disposable {
        private final Disposer disposer = new Disposer();
        private Disposer contentDisposer = new Disposer();

        public TalentTable(final TalentList talentList) {
            this.addStyleName("talents");
            int row = 0;
            // -- Draw Header --
            setText(row, 0, "");
            getFlexCellFormatter().addStyleName(row, 0, "checkBoxCol");

            setText(row, 1, "Cost");
            getFlexCellFormatter().addStyleName(row, 1, "costCol");
            setText(row, 2, "Description");
            getFlexCellFormatter().addStyleName(row, 2, "nameCol");
            getRowFormatter().addStyleName(row, "header");
            // -- Fill in Talents --
            repopulateTalentTable(talentList);
            // -- Subscribe to future changes to the set of skills --
            disposer.observe(talentList, new Observable.Observer() {
                public void onChange() {
                    repopulateTalentTable(talentList);
                }
            });
        }
        
        
        /**
         * Called to wipe out the full table and repopulate it.
         */
        private void repopulateTalentTable(final TalentList talents) {
            int row;
            // -- Remove existing rows --
            contentDisposer.dispose();
            for (row = getRowCount() - 1; row > 0; row--) {
                removeRow(row);
            }
            row++;
            // -- Re-insert all talents as rows --
            contentDisposer = new Disposer();
            for (final TalentValue talentValue : talents) {
                //- Checkbox --
                getFlexCellFormatter().addStyleName(row, 0, "checkBoxCol");
                setWidget(row, 0, new CheckBox());
                // -- Cost --
                getFlexCellFormatter().addStyleName(row, 1, "costCol");
                setWidget(row, 1, contentDisposer.track(new SettableIntField(talentValue.getCost())));
                // -- Name --
                getFlexCellFormatter().addStyleName(row, 2, "descriptionCol");
                setWidget(row, 2, contentDisposer.track(new SettableStringField(talentValue.getDescription())));
                // -- Continue loop --
                row++;
            }
        }

        public void dispose() {
            contentDisposer.dispose();
            disposer.dispose();
        }
    }

    public void dispose() {
        disposer.dispose();
    }
}
