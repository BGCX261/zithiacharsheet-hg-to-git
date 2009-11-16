package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.modeler.SettableStringValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableStringValueImpl;


/**
 * The section that contains notes and background for a character.
 */
public class CharacterNotes {
    private final SettableStringValue background;

    public CharacterNotes() {
        background = new SettableStringValueImpl("");
    }
    
    public SettableStringValue getBackground() {
        return background;
    }
}
