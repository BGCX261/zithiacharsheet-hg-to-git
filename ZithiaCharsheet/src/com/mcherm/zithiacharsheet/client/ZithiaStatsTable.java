package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.mcherm.zithiacharsheet.client.model.Observable;
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
        for (final StatValue statValue : zithiaCharacter.getStats()) {
            // -- Name --
            getFlexCellFormatter().addStyleName(row, 1, "nameCol");
            setText(row, 1, statValue.getStat().getName());
            // -- Cost --
            final TextBox costBox = new TextBox();
            costBox.setValue(Integer.toString(statValue.getCost()));
            costBox.setEnabled(false);
            getFlexCellFormatter().addStyleName(row, 0, "costCol");
            setWidget(row, 0, costBox);
            // -- Roll --
            getFlexCellFormatter().addStyleName(row, 3, "rollCol");
            final TextBox rollBox;
            if (statValue.getStat().hasRoll()) {
                rollBox = new TextBox();
                rollBox.setValue(Integer.toString(statValue.getRoll()));
                rollBox.setEnabled(false);
                setWidget(row, 3, rollBox);
            } else {
                setText(row, 3, "n/a");
                rollBox = null;
            }
            // -- Value --
            final TextBox valueBox = new TextBox();
            getFlexCellFormatter().addStyleName(row, 2, "valueCol");
            valueBox.setValue(Integer.toString(statValue.getValue()));
            valueBox.addValueChangeHandler(new ValueChangeHandler<String>() {
                @Override
                public void onValueChange(ValueChangeEvent<String> event) {
                    int newValue;
                    try {
                        newValue = Integer.parseInt(event.getValue());
                    } catch(NumberFormatException err) {
                        Window.alert("Got number format exception. value was " + event.getValue());
                        return;
                    }
                    statValue.setValue(newValue);
                }
            });
            setWidget(row, 2, valueBox);
            // -- Register to update the values --
            statValue.addObserver(new Observable.Observer() {
                public void onChange() {
                    costBox.setValue(Integer.toString(statValue.getCost()));
                    if (rollBox != null) {
                        rollBox.setValue(Integer.toString(statValue.getRoll()));
                    }
                }
            });
            // reset to trigger initial value calculation // FIXME: Because of this I didn't need to set it above
            statValue.setValue(statValue.getValue());
            // -- Continue loop --
            row++;
        }
    }
    
}

