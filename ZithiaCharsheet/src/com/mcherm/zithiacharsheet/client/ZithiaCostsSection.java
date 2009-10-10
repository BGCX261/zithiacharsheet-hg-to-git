package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.mcherm.zithiacharsheet.client.model.ObservableInt;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.Observable.Observer;


public class ZithiaCostsSection extends FlexTable {
    
    public ZithiaCostsSection(ZithiaCharacter zithiaCharacter) {
        addStyleName("costs");
        setText(0, 0, "Cost:");
        
        // FIXME: Abstract the creation of a box to display an ObservableInt.
        final TextBox costBox = new TextBox();
        final ObservableInt cost = zithiaCharacter.getCosts().getTotalCost();
        costBox.setValue(Integer.toString(cost.getValue()));
        cost.addObserver(new Observer() {
            public void onChange() {
                costBox.setValue(Integer.toString(cost.getValue()));
            }
        });
        setWidget(0, 1, costBox);
    }
}
