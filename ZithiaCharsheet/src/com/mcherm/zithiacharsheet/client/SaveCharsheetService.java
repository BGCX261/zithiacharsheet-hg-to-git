package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mcherm.zithiacharsheet.client.model.CharacterStorage;


@RemoteServiceRelativePath("saveCharsheet")
public interface SaveCharsheetService extends RemoteService {
    void saveCharsheet(String characterId, String saveStr);
    void saveCharsheet2(CharacterStorage metadata);
    String loadCharsheet(String characterId);
}
