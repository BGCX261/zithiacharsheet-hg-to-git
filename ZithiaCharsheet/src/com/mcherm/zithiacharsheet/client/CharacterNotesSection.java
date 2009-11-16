package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.SimplePanel;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;


/**
 * The section that shows things related to the character notes.
 * Currently, just the background.
 */
public class CharacterNotesSection extends SimplePanel {

    public CharacterNotesSection(final ZithiaCharacter zithiaCharacter) {
        setWidget(new SettableRichStringField(zithiaCharacter.getCharacterNotes().getBackground()));
    }
}
