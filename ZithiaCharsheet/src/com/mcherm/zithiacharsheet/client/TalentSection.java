package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.model.TalentList;
import com.mcherm.zithiacharsheet.client.model.TalentValue;
import com.mcherm.zithiacharsheet.client.modeler.Observable;


/**
 * The section where talents are displayed and edited.
 */
public class TalentSection extends VerticalPanel {
    private final TalentTable talentTable;

        
    public TalentSection(final TalentList talentList) {
        this.addStyleName("skills");
        talentTable = new TalentTable(talentList);
        this.add(talentTable);
        Button addSkillButton = new Button("Add", new ClickHandler() {
            public void onClick(ClickEvent event) {
                talentList.add(new TalentValue());
            }
        });
        this.add(addSkillButton);
    }


    /**
     * The table of talents
     */
    private static class TalentTable extends FlexTable {
        
        public TalentTable(final TalentList talentList) {
            this.addStyleName("talents");
            int row = 0;
            // -- Draw Header --
            setText(row, 0, "Cost");
            getFlexCellFormatter().addStyleName(row, 0, "costCol");
            setText(row, 1, "Description");
            getFlexCellFormatter().addStyleName(row, 1, "nameCol");
            getRowFormatter().addStyleName(row, "header");
            row++;
            // -- Fill in Talents --
            repopulateTalentTable(talentList);
            // -- Subscribe to future changes to the set of skills --
            talentList.addObserver(new Observable.Observer() {
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
            for (row = getRowCount() - 1; row > 0; row--) {
                removeRow(row);
            }
            row++;
            // -- Re-insert all talents as rows --
            for (final TalentValue talentValue : talents) {
                // -- Cost --
                getFlexCellFormatter().addStyleName(row, 0, "costCol");
                setWidget(row, 0, new SettableIntField(talentValue.getCost()));
                // -- Name --
                getFlexCellFormatter().addStyleName(row, 1, "descriptionCol");
                setWidget(row, 1, new SettableStringField(talentValue.getDescription()));
                // -- Continue loop --
                row++;
            }
        }

    }
}
