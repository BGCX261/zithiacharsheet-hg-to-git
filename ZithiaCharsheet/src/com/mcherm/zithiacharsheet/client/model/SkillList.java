package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
import com.mcherm.zithiacharsheet.client.modeler.SummableList;

/**
 * This is the set of skills that a character.
 */
public class SkillList extends SummableList<SkillValue> {

    /**
     * Constructor. Creates an empty list of skills.
     */
    public SkillList() {
        super(new Extractor<SkillValue>() {
            public ObservableInt extractValue(SkillValue item) {
                return item.getCost();
            }
        });
    }
    
    /**
     * Returns the cost of the skills.
     */
    public ObservableInt getCost() {
        return getSum();
    }

}
