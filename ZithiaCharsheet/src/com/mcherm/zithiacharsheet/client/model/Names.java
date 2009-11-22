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
