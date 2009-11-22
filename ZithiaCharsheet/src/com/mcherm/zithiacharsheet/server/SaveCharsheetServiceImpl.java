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
package com.mcherm.zithiacharsheet.server;

import java.util.ArrayList;
import java.util.List;

import com.mcherm.zithiacharsheet.client.SaveCharsheetService;
import com.mcherm.zithiacharsheet.client.model.CharacterStorage;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.CharacterStorage.CharacterMetadata;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * Server-side implementation of services to store and retrieve charsheets.
 */
@SuppressWarnings("serial")
public class SaveCharsheetServiceImpl extends RemoteServiceServlet implements SaveCharsheetService {
    
    
    public final static String CHARSHEET_ENTITY_KIND = "zithiaCharacterV1";
    
    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    
    public CharacterMetadata newCharsheet() {
        Entity entity = new Entity(CHARSHEET_ENTITY_KIND);
        datastore.put(entity);
        String keyStr = KeyFactory.keyToString(entity.getKey());
        CharacterStorage characterStorage = new CharacterStorage(keyStr, new ZithiaCharacter());
        saveCharsheet(characterStorage); // write it out
        return characterStorage.getMetadata();
    }

    public void saveCharsheet(CharacterStorage characterStorage) {
        Key key = KeyFactory.stringToKey(characterStorage.getId());
        Entity entity = new Entity(key);
        entity.setProperty("jsonData", new Text(characterStorage.serializedData));
        entity.setProperty("playerName", characterStorage.metadata.playerName);
        entity.setProperty("characterName", characterStorage.metadata.characterName);
        datastore.put(entity);
    }
    
    public String loadCharsheet(String characterId) {
        Key key = KeyFactory.stringToKey(characterId);
        try {
            Entity entity = datastore.get(key);
            Text jsonData = (Text) entity.getProperty("jsonData");
            return jsonData.getValue();
        } catch (EntityNotFoundException err) {
            throw new RuntimeException("No character found with the id '" + characterId + "'.", err);
        }
    }

    public List<CharacterMetadata> listCharsheets() {
        Query query = new Query(CHARSHEET_ENTITY_KIND);
        PreparedQuery preparedQuery = datastore.prepare(query);
        List<CharacterMetadata> result = new ArrayList<CharacterMetadata>(1000);
        for (Entity entity : preparedQuery.asIterable()) {
            String id = KeyFactory.keyToString(entity.getKey());
            String playerName = (String) entity.getProperty("playerName");
            String characterName = (String) entity.getProperty("characterName");
            result.add(new CharacterMetadata(id, playerName, characterName));
        }
        return result;
    }

}
