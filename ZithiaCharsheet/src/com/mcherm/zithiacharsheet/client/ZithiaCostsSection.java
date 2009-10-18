package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;


public class ZithiaCostsSection extends FlexTable {
    
    public ZithiaCostsSection(ZithiaCharacter zithiaCharacter) {
        addStyleName("costs");
        setText(0, 0, "Cost:");
        
        final TweakableIntField totalCostField = new TweakableIntField(zithiaCharacter.getCosts().getTotalCost());
        setWidget(0, 1, totalCostField);
    }
}
