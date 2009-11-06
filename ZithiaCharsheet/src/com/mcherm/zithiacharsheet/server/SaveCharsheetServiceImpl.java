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
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * Server-side implementation of services to store and retrieve charsheets.
 */
@SuppressWarnings("serial")
public class SaveCharsheetServiceImpl extends RemoteServiceServlet implements SaveCharsheetService {
    
    
    final static String CHARSHEET_ENTITY_KIND = "zithiaCharacterV1";
    
    
    @Override
    public CharacterMetadata newCharsheet() {
        Entity characterEntity = new Entity(CHARSHEET_ENTITY_KIND);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(characterEntity);
        String keyStr = KeyFactory.keyToString(characterEntity.getKey());
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
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.stringToKey(characterStorage.getId());
        Entity characterEntity = new Entity(key);
        characterEntity.setProperty("jsonData", blob);
        characterEntity.setProperty("playerName", characterStorage.metadata.playerName);
        characterEntity.setProperty("characterName", characterStorage.metadata.characterName);
        datastore.put(characterEntity);
    }
    
    @Override
    public String loadCharsheet(String characterId) {
        Key key = KeyFactory.stringToKey(characterId);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity characterEntity = datastore.get(key);
            Blob blob = (Blob) characterEntity.getProperty("jsonData");
            return new String(blob.getBytes(), "UTF-8");
        } catch (EntityNotFoundException err) {
            throw new RuntimeException("No character found with the id '" + characterId + "'.", err);
        } catch (UnsupportedEncodingException err) {
            throw new Error("JVM did not support encoding 'UTF-8', which is mandatory.", err);
        }
    }

    @Override
    public List<CharacterMetadata> listCharsheets() {
        return new ArrayList<CharacterMetadata>(); // FIXME: Return real results.
    }

}
