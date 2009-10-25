package com.mcherm.zithiacharsheet.server;

import java.io.UnsupportedEncodingException;

import com.mcherm.zithiacharsheet.client.SaveCharsheetService;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


@SuppressWarnings("serial")
public class SaveCharsheetServiceImpl extends RemoteServiceServlet implements SaveCharsheetService {

    @Override
    public void saveCharsheet(String characterId, String saveStr) {
        byte[] saveStrData;
        try {
            saveStrData = saveStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException err) {
            throw new Error("JVM did not support encoding 'UTF-8', which is mandatory.", err);
        }
        Blob blob = new Blob(saveStrData);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey("zithiaCharacterV1", characterId);
        Entity characterEntity = new Entity(key);
        characterEntity.setProperty("jsonData", blob);
        datastore.put(characterEntity);
    }
    
    @Override
    public String loadCharsheet(String characterId) {
        Key key = KeyFactory.createKey("zithiaCharacterV1", characterId);
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
}
