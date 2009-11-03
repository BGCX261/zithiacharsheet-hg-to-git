package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.modeler.SettableStringValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableStringValueImpl;


/**
 * This is the names section in a character.
 */
public class Names {
    private final SettableStringValue characterName;
    private final SettableStringValue playerName;

    public Names() {
        characterName = new SettableStringValueImpl("");
        playerName = new SettableStringValueImpl("");
    }
    
    public SettableStringValue getCharacterName() {
        return characterName;
    }

    public SettableStringValue getPlayerName() {
        return playerName;
    }

}
