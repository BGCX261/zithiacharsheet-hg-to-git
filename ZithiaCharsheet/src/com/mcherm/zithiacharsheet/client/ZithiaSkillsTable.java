package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.mcherm.zithiacharsheet.client.model.SkillList;
import com.mcherm.zithiacharsheet.client.model.SkillValue;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.modeler.Observable;


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
        final SkillList skillList = zithiaCharacter.getSkillList();
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
        for (final SkillValue skillValue : skills) {
            // -- Name --
            getFlexCellFormatter().addStyleName(row, 1, "nameCol");
            setText(row, 1, skillValue.getSkill().getName());
            // -- Cost --
            getFlexCellFormatter().addStyleName(row, 0, "costCol");
            final TweakableIntField costField = new TweakableIntField(skillValue.getCost());
            setWidget(row, 0, costField);
            // -- Roll --
            getFlexCellFormatter().addStyleName(row, 3, "rollCol");
            final TweakableIntField rollField;
            if (skillValue.getSkill().hasRoll()) {
                rollField = new TweakableIntField(skillValue.getRoll());
                setWidget(row, 3, rollField);
            } else {
                setText(row, 3, "n/a");
                rollField = null;
            }
            // -- Value --
            getFlexCellFormatter().addStyleName(row, 2, "levelsCol");
            final SettableIntField levelsField = new SettableIntField(skillValue.getLevels());
            setWidget(row, 2, levelsField);
            // -- Continue loop --
            row++;
        }
    }

}
