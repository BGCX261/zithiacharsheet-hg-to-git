package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
import com.mcherm.zithiacharsheet.client.modeler.SummableList;

/**
 * This is the set of skills that a character.
 */
public class SkillList extends SummableList<SkillValue> {
    
    private final StatValues statValues;

    /**
     * Constructor. Creates an empty list of skills.
     */
    public SkillList(StatValues statValues) {
        super(new Extractor<SkillValue>() {
            public ObservableInt extractValue(SkillValue item) {
                return item.getCost();
            }
        });
        this.statValues = statValues;
    }
    
    /**
     * Returns the cost of the skills.
     */
    public ObservableInt getCost() {
        return getSum();
    }

    /**
     * Call this to add a new skill to the list. Returns the new SkillValue.
     */
    public SkillValue addNewSkill(ZithiaSkill skill) {
        SkillValue skillValue = new SkillValue(skill, this.statValues);
        add(skillValue);
        return skillValue;
    }
    
}
