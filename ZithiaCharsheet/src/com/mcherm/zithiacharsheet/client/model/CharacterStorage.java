package com.mcherm.zithiacharsheet.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Serializable class for the data that gets persisted when we save a
 * character.
 */
public class CharacterStorage implements IsSerializable {
    public String id;
    public CharacterMetadata metadata;
    public String serializedData;
    
    /**
     * Inner class that stores the metadata.
     */
    public static class CharacterMetadata implements IsSerializable {
        public String playerName;
        public String characterName;
        
        /**
         * No-args constructor for the deserializer.
         */
        CharacterMetadata() {
        }
        
        /**
         * Constructor that initializes it from a ZithiaCharacter.
         */
        public CharacterMetadata(ZithiaCharacter zithiaCharacter) {
            playerName = zithiaCharacter.getNames().getPlayerName().getValue();
            characterName = zithiaCharacter.getNames().getCharacterName().getValue();
        }
    }

    /**
     * No-args constructor for the deserializer.
     */
    CharacterStorage() {
    }
    
    /**
     * Constructor which builds a CharacterStorage from a ZithiaCharacter
     * using the specified id.
     */
    public CharacterStorage(String id, ZithiaCharacter zithiaCharacter) {
        this.id = id;
        metadata = new CharacterMetadata(zithiaCharacter);
        JSONSerializer serializer = new JSONSerializer(false);
        serializer.serialize(zithiaCharacter);
        serializedData = serializer.output();
    }
    
    
    /**
     * Constructor which builds a CharacterStorage from a ZithiaCharacter
     * using a default id.
     */
    public CharacterStorage(ZithiaCharacter zithiaCharacter) {
        this(getDefaultId(zithiaCharacter), zithiaCharacter);
    }
    
    
    /**
     * Subroutine to obtain a default id from a character. This uses the
     * character name replacing everything but letters with underscores.
     */
    private static String getDefaultId(ZithiaCharacter zithiaCharacter) {
        String baseName = zithiaCharacter.getNames().getCharacterName().getValue();
        char[] outputChars = new char[baseName.length()];
        for (int i=0; i<baseName.length(); i++) {
            char c = baseName.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                outputChars[i] = c;
            } else {
                outputChars[i] = '_';
            }
        }
        return new String(outputChars);
    }

}
