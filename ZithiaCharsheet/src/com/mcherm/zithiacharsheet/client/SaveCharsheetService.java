package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("saveCharsheet")
public interface SaveCharsheetService extends RemoteService {
    void saveCharsheet(String characterId, String saveStr);
    String loadCharsheet(String characterId);
}
