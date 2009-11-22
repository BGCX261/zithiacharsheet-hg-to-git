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

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mcherm.zithiacharsheet.client.model.CharacterStorage;


@RemoteServiceRelativePath("saveCharsheet")
public interface SaveCharsheetService extends RemoteService {
    
    /**
     * Creates a new charsheet and returns the (nearly empty) character metadata for it.
     */
    CharacterStorage.CharacterMetadata newCharsheet();
    
    /**
     * This updates the existing character with the specified id.
     */
    void saveCharsheet(CharacterStorage characterStorage);
    
    /**
     * This retrieves the serialized form of a character.
     */
    String loadCharsheet(String characterId);

    /**
     * Returns a bunch of characters. This doesn't support paging, so the design
     * will break once we exceed AppEngine's normal limits on queries.
     * @return
     */
    List<CharacterStorage.CharacterMetadata> listCharsheets();
}
