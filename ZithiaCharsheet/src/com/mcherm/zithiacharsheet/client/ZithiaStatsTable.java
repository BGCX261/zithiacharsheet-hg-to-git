package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.mcherm.zithiacharsheet.client.model.StatValue;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;


public class ZithiaStatsTable extends FlexTable {
    
    public ZithiaStatsTable(ZithiaCharacter zithiaCharacter) {
        this.addStyleName("stats");
        int row = 0;
        // -- Header --
        setText(row, 0, "Cost");
        getFlexCellFormatter().addStyleName(row, 0, "costCol");
        setText(row, 1, "Stat");
        getFlexCellFormatter().addStyleName(row, 1, "nameCol");
        setText(row, 2, "Value");
        getFlexCellFormatter().addStyleName(row, 2, "valueCol");
        setText(row, 3, "Roll");
        getFlexCellFormatter().addStyleName(row, 3, "rollCol");
        getRowFormatter().addStyleName(row, "header");
        row++;
        
        
        // -- Rows --
        for (final StatValue statValue : zithiaCharacter.getStatValues()) {
            // -- Name --
            getFlexCellFormatter().addStyleName(row, 1, "nameCol");
            setText(row, 1, statValue.getStat().getName());
            // -- Cost --
            getFlexCellFormatter().addStyleName(row, 0, "costCol");
            final Widget costField = new TweakableIntField(statValue.getCost());
            setWidget(row, 0, costField);
            // -- Roll --
            getFlexCellFormatter().addStyleName(row, 3, "rollCol");
            final Widget rollField;
            if (statValue.getStat().hasRoll()) {
                rollField = new TweakableIntField(statValue.getRoll());
                setWidget(row, 3, rollField);
            } else {
                setText(row, 3, "n/a");
            }
            // -- Value --
            getFlexCellFormatter().addStyleName(row, 2, "valueCol");
            final SettableIntField valueField = new SettableIntField(statValue.getValue());
            setWidget(row, 2, valueField);
            // -- Continue loop --
            row++;
        }
    }
    
}

