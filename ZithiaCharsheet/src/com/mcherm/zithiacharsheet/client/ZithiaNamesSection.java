package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.Grid;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;


/**
 * Displays the name section of a character sheet.
 */
public class ZithiaNamesSection extends Grid {

    public ZithiaNamesSection(final ZithiaCharacter zithiaCharacter) {
        super(2,2);
        addStyleName("namesSection");
        setText(0, 0, "Name:");
        setWidget(0, 1, new SettableStringField(zithiaCharacter.getNames().getCharacterName()));
        setText(1, 0, "Player:");
        setWidget(1, 1, new SettableStringField(zithiaCharacter.getNames().getPlayerName()));
    }
}
