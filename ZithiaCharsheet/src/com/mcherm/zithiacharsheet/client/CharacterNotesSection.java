/*
 * Copyright 2009 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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