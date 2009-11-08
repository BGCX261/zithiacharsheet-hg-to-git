package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
import com.mcherm.zithiacharsheet.client.modeler.SummableList;


/**
 * The collection of talents in a character.
 */
public class TalentList extends SummableList<TalentValue> {
    
    public TalentList() {
        super(new Extractor<TalentValue>() {
            public ObservableInt extractValue(TalentValue item) {
                return item.getCost();
            }
        });
    }


    /**
     * Returns the total cost of the talents.
     */
    public ObservableInt getCost() {
        return getSum();
    }

}
