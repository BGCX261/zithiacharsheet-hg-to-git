package com.mcherm.zithiacharsheet.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is the set of skills that a character.
 */
public class SkillList extends ObservableList<SkillValue> {

    public SkillList() {
        super(new Extractor<SkillValue>() {
            @Override
            public int extractValue(SkillValue item) {
                return item.getCost();
            }
        });
    }
}


// FIXME: Remove old code once new code is tested.
class OldSkillList extends SimpleObservable {
    
    private final List<SkillValue> skillValues = new ArrayList<SkillValue>();

    public List<SkillValue> getSkillValues() {
        return Collections.unmodifiableList(skillValues);
    }

    public void addSkillValue(SkillValue skillValue) {
        skillValues.add(skillValue);
        alertObservers();
    }
}
