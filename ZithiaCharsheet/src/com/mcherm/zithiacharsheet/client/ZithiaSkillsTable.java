package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.mcherm.zithiacharsheet.client.model.Observable;
import com.mcherm.zithiacharsheet.client.model.SkillList;
import com.mcherm.zithiacharsheet.client.model.SkillValue;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;

// FIXME: Can this share code with stats table? I'm thinking probably not, but right now it's nearly a perfect duplicate
public class ZithiaSkillsTable extends FlexTable {
    
    public ZithiaSkillsTable(final ZithiaCharacter zithiaCharacter) {
        this.addStyleName("skills");
        int row = 0;
        // -- Draw Header --
        setText(row, 0, "Cost");
        getFlexCellFormatter().addStyleName(row, 0, "costCol");
        setText(row, 1, "Skill");
        getFlexCellFormatter().addStyleName(row, 1, "nameCol");
        setText(row, 2, "Levels");
        getFlexCellFormatter().addStyleName(row, 2, "levelsCol");
        setText(row, 3, "Roll");
        getFlexCellFormatter().addStyleName(row, 3, "rollCol");
        getRowFormatter().addStyleName(row, "header");
        row++;
        // -- Fill in Skills --
        final SkillList skillList = zithiaCharacter.getSkills();
        repopulateSkillTable(skillList);
        // -- Subscribe to future changes to the set of skills --
        skillList.addObserver(new Observable.Observer() {
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
        row++;
        // -- Re-insert all skills as rows --
        for (final SkillValue skillValue : skills.getSkillValues()) {
            // -- Name --
            getFlexCellFormatter().addStyleName(row, 1, "nameCol");
            setText(row, 1, skillValue.getSkill().getName());
            // -- Cost --
            final TextBox costBox = new TextBox();
            costBox.setValue(Integer.toString(skillValue.getCost()));
            costBox.setEnabled(false);
            getFlexCellFormatter().addStyleName(row, 0, "costCol");
            setWidget(row, 0, costBox);
            // -- Roll --
            getFlexCellFormatter().addStyleName(row, 3, "rollCol");
            final TextBox rollBox;
            if (skillValue.getSkill().hasRoll()) {
                rollBox = new TextBox();
                rollBox.setValue(Integer.toString(skillValue.getRoll()));
                rollBox.setEnabled(false);
                setWidget(row, 3, rollBox);
            } else {
                setText(row, 3, "n/a");
                rollBox = null;
            }
            // -- Value --
            final TextBox levelsBox = new TextBox();
            getFlexCellFormatter().addStyleName(row, 2, "levelsCol");
            levelsBox.setValue(Integer.toString(skillValue.getLevels()));
            levelsBox.addValueChangeHandler(new ValueChangeHandler<String>() {
                @Override
                public void onValueChange(ValueChangeEvent<String> event) {
                    int newLevels;
                    try {
                        newLevels = Integer.parseInt(event.getValue());
                    } catch(NumberFormatException err) {
                        Window.alert("Got number format exception. value was " + event.getValue());
                        return;
                    }
                    skillValue.setLevels(newLevels);
                }
            });
            setWidget(row, 2, levelsBox);
            // -- Register to update the values --
            skillValue.addObserver(new Observable.Observer() {
                public void onChange() {
                    costBox.setValue(Integer.toString(skillValue.getCost()));
                    if (rollBox != null) {
                        rollBox.setValue(Integer.toString(skillValue.getRoll()));
                    }
                }
            });
            // -- Continue loop --
            row++;
        }
    }

}
