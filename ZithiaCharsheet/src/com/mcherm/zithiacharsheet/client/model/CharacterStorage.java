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
package com.mcherm.zithiacharsheet.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Serializable class for the data that gets persisted when we save a
 * character.
 */
public class CharacterStorage implements IsSerializable {
    public CharacterMetadata metadata;
    public String serializedData;
    
    /**
     * Inner class that stores the metadata.
     */
    public static class CharacterMetadata implements IsSerializable {
        private String id;
        public String playerName;
        public String characterName;
        
        /**
         * No-args constructor for the deserializer.
         */
        @SuppressWarnings("unused")
        private CharacterMetadata() {
        }
        
        /**
         * Constructor that is passed all of the data fields.
         */
        public CharacterMetadata(String id, String playerName, String characterName) {
            this.id = id;
            this.playerName = playerName;
            this.characterName = characterName;
        }
        
        /**
         * Constructor that initializes it from a ZithiaCharacter and id.
         */
        public CharacterMetadata(String id, ZithiaCharacter zithiaCharacter) {
            this.id = id;
            playerName = zithiaCharacter.getNames().getPlayerName().getValue();
            characterName = zithiaCharacter.getNames().getCharacterName().getValue();
        }
        
        /**
         * Returns the id if known, or null if not.
         */
        public String getId() {
            return id;
        }
        
    }

    /**
     * No-args constructor for the deserializer.
     */
    @SuppressWarnings("unused")
    private CharacterStorage() {
    }
    
    /**
     * Constructor which builds a CharacterStorage from a ZithiaCharacter
     * using the specified id.
     */
    public CharacterStorage(String id, ZithiaCharacter zithiaCharacter) {
        metadata = new CharacterMetadata(id, zithiaCharacter);
        JSONSerializer serializer = JSONSerializer.newInstance(false);
        serializer.serialize(zithiaCharacter);
        serializedData = serializer.output();
    }
    
    /**
     * Returns the metadata.
     */
    public CharacterMetadata getMetadata() {
        return metadata;
    }
    
    /**
     * Returns the id.
     */
    public String getId() {
        return metadata.getId();
    }
    
    
}
