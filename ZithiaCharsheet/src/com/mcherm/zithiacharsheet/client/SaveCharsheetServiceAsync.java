package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mcherm.zithiacharsheet.client.model.CharacterStorage;


public interface SaveCharsheetServiceAsync {
    void saveCharsheet(String characterId, String saveStr, AsyncCallback<Void> callback);
    void saveCharsheet2(CharacterStorage data, AsyncCallback<Void> callback);
    void loadCharsheet(String characterId, AsyncCallback<String> callback);
}

