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
