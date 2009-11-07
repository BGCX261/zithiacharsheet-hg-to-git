package com.mcherm.zithiacharsheet.server;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.mcherm.zithiacharsheet.client.SaveCharsheetService;
import com.mcherm.zithiacharsheet.client.model.CharacterStorage;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.CharacterStorage.CharacterMetadata;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * Server-side implementation of services to store and retrieve charsheets.
 */
@SuppressWarnings("serial")
public class SaveCharsheetServiceImpl extends RemoteServiceServlet implements SaveCharsheetService {
    
    
    final static String CHARSHEET_ENTITY_KIND = "zithiaCharacterV1";
    
    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    
    @Override
    public CharacterMetadata newCharsheet() {
        Entity entity = new Entity(CHARSHEET_ENTITY_KIND);
        datastore.put(entity);
        String keyStr = KeyFactory.keyToString(entity.getKey());
        CharacterStorage characterStorage = new CharacterStorage(keyStr, new ZithiaCharacter());
        saveCharsheet(characterStorage); // write it out
        return characterStorage.getMetadata();
    }

    @Override
    public void saveCharsheet(CharacterStorage characterStorage) {
        byte[] saveStrData;
        try {
            saveStrData = characterStorage.serializedData.getBytes("UTF-8");
        } catch (UnsupportedEncodingException err) {
            throw new Error("JVM did not support encoding 'UTF-8', which is mandatory.", err);
        }
        Blob blob = new Blob(saveStrData);
        Key key = KeyFactory.stringToKey(characterStorage.getId());
        Entity entity = new Entity(key);
        entity.setProperty("jsonData", blob);
        entity.setProperty("playerName", characterStorage.metadata.playerName);
        entity.setProperty("characterName", characterStorage.metadata.characterName);
        datastore.put(entity);
    }
    
    @Override
    public String loadCharsheet(String characterId) {
        Key key = KeyFactory.stringToKey(characterId);
        try {
            Entity entity = datastore.get(key);
            Blob blob = (Blob) entity.getProperty("jsonData");
            return new String(blob.getBytes(), "UTF-8");
        } catch (EntityNotFoundException err) {
            throw new RuntimeException("No character found with the id '" + characterId + "'.", err);
        } catch (UnsupportedEncodingException err) {
            throw new Error("JVM did not support encoding 'UTF-8', which is mandatory.", err);
        }
    }

    @Override
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
