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
