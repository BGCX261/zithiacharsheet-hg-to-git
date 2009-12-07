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

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.mcherm.zithiacharsheet.client.model.SkillList;
import com.mcherm.zithiacharsheet.client.model.SkillValue;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.modeler.Disposable;
import com.mcherm.zithiacharsheet.client.modeler.Disposer;
import com.mcherm.zithiacharsheet.client.modeler.Observable;


// FIXME: Can this share code with stats table? I'm thinking probably not, but right now it's nearly a perfect duplicate
public class ZithiaSkillsTable extends FlexTable implements Disposable {
    private final Disposer disposer = new Disposer();
    private Disposer rowsDisposer;

    public ZithiaSkillsTable(final ZithiaCharacter zithiaCharacter) {
        this.addStyleName("skills");
        int row = 0;
        // -- Draw Header --
        setText(row, 0, "Checkbox");
        getFlexCellFormatter().addStyleName(row, 0, "checkBox");
        setText(row, 1, "Cost");
        getFlexCellFormatter().addStyleName(row, 1, "costCol");
        setText(row, 2, "Skill");
        getFlexCellFormatter().addStyleName(row, 2, "nameCol");
        setText(row, 3, "Levels");
        getFlexCellFormatter().addStyleName(row, 3, "levelsCol");
        setText(row, 4, "Roll");
        getFlexCellFormatter().addStyleName(row, 4, "rollCol");
        getRowFormatter().addStyleName(row, "header");
        // -- Fill in Skills --
        final SkillList skillList = zithiaCharacter.getSkillList();
        repopulateSkillTable(skillList);
        // -- Subscribe to future changes to the set of skills --
        disposer.observe(skillList, new Observable.Observer() {
            public void onChange() {
                repopulateSkillTable(skillList);
            }
        });
    }
    
    
    /**
     * Called to wipe out the full table and repopulate it.
     */
    private void repopulateSkillTable(final SkillList skills) {
        int row;
        // -- Remove existing rows --
        for (row = getRowCount() - 1; row > 0; row--) {
            removeRow(row);
        }
        if (rowsDisposer != null) {
            rowsDisposer.dispose();
        }
        row++;

        // -- Re-insert all skills as rows --
        rowsDisposer = new Disposer();
        for (final SkillValue skillValue : skills) {
            //---Checkbox for row selection--//
            getFlexCellFormatter().addStyleName(row, 0, "checkBox");
            setWidget(row, 0, new CheckBox());
            // -- Name --
            getFlexCellFormatter().addStyleName(row, 2, "nameCol");
            setText(row, 2, skillValue.getSkill().getName());
            // -- Cost --
            getFlexCellFormatter().addStyleName(row, 1, "costCol");
            setWidget(row, 1, rowsDisposer.track(new TweakableIntField(skillValue.getCost())));
            // -- Roll --
            getFlexCellFormatter().addStyleName(row, 4, "rollCol");
            if (skillValue.getSkill().hasRoll()) {
                setWidget(row, 4, rowsDisposer.track(new TweakableIntField(skillValue.getRoll())));
            } else {
                setText(row, 4, "n/a");
            }
            // -- Value --
            getFlexCellFormatter().addStyleName(row, 3, "levelsCol");
            setWidget(row, 3, rowsDisposer.track(new SettableIntField(skillValue.getLevels())));
            // -- Continue loop --
            row++;
        }
    }

    public void dispose() {
        rowsDisposer.dispose();
        disposer.dispose();
    }
}
