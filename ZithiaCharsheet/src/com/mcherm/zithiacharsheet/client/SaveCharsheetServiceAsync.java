package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface SaveCharsheetServiceAsync {
    void saveCharsheet(String characterId, String saveStr, AsyncCallback<Void> callback);
    void loadCharsheet(String characterId, AsyncCallback<String> callback);
}

