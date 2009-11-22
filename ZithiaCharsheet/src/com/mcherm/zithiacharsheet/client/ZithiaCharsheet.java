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
package com.mcherm.zithiacharsheet.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.mcherm.zithiacharsheet.client.model.CharacterStorage;
import com.mcherm.zithiacharsheet.client.model.JSONDeserializer;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.CharacterStorage.CharacterMetadata;


/**
 * Displays a character sheet, along with save and load buttons. This object
 * is rather slow to create, so it is designed to be reused. When initialized,
 * the character sheet is blank (in the default state) and it can be updated
 * by calling XXXXXX. // FIXME: Fill that in.
 * <p>
 * DESIGN NOTE: Much of the setup of this object can be a bit slow, so
 * it has carefully been wrapped in deferred execution. Creating an instance
 * triggers the load of the data, but does not do so instantly.
 * <p>
 * DESIGN NOTE: Perhaps I should separate out the save/load buttons.
 */
public class ZithiaCharsheet extends Grid {

    private final ZithiaCharacter zithiaCharacter;
    private String characterId;
    
    /**
     * Create the service object which allows us to save and load the
     * character data.
     */
    private final SaveCharsheetServiceAsync saveCharsheetService =
        GWT.create(SaveCharsheetService.class);
    
    
    public ZithiaCharsheet() {
        // -- Set up 2 columns, with specific numbers of items in each --
        super(1,2); // initialize to a 2-column, 1 row grid
        final Grid leftSide = new Grid(4,1);
        setWidget(0, 0, leftSide);
        final Grid rightSide = new Grid(4,1);
        setWidget(0, 1, rightSide);
        
        // -- Create the character --
        characterId = null;
        zithiaCharacter = new ZithiaCharacter();
        
        // -- When there's time, create the contents of each section --
        DeferredCommand.addCommand(new Command() { public void execute() {
            leftSide.setWidget(0, 0, new ZithiaNamesSection(zithiaCharacter));
        } });
        DeferredCommand.addCommand(new Command() { public void execute() {
            leftSide.setWidget(1, 0, new ZithiaStatsTable(zithiaCharacter));
        } });
        DeferredCommand.addCommand(new Command() { public void execute() {
            leftSide.setWidget(2, 0, new ZithiaCostsSection(zithiaCharacter));
        } });
        DeferredCommand.addCommand(new Command() { public void execute() {
            rightSide.setWidget(0, 0, new ZithiaSkillsSection(zithiaCharacter));
        } });
        DeferredCommand.addCommand(new Command() { public void execute() {
            rightSide.setWidget(1, 0, new ZithiaWeaponSkillsSection(zithiaCharacter));
        } });
        DeferredCommand.addCommand(new Command() { public void execute() {
            rightSide.setWidget(2, 0, new TalentSection(zithiaCharacter.getTalentList()));
        } });
        DeferredCommand.addCommand(new Command() { public void execute() {
            rightSide.setWidget(3, 0, new CharacterNotesSection(zithiaCharacter));
        } });


        // -- Show save/load buttons --
        DeferredCommand.addCommand(new Command() { public void execute() {
            final Button saveButton = new Button("Save");
            saveButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    save();
                }
            });
            leftSide.setWidget(3, 0, saveButton);
        } });
    }

    
    /**
     * When this is called it writes itself to the database as a new, blank
     * entry, and retrieves a new characterId which replaces the ID currently in use.
     */
    public void saveAsNewCharacter() {
        Window.alert("Character not specified: Will create new character.");
        saveCharsheetService.newCharsheet(new AsyncCallback<CharacterMetadata>() {
            public void onFailure(Throwable caught) {
                Window.alert("Unable to create character: " + caught);
            }
            public void onSuccess(CharacterMetadata result) {
                characterId = result.getId();
            }
        });
    }

    
    /** A function that can be passed to load(). */
    public static interface FailureAction {
        public void onFailure(Throwable caught);
    }
    
    /**
     * Use to set the characterId. Will immediately perform a
     * load().
     */
    public void setCharacterId(String characterId, FailureAction failureAction) {
        this.characterId = characterId;
        load(failureAction);
    }

    /**
     * A version of load() where you can specify what to do if it fails.
     */
    public void load(final FailureAction failureAction) {
        saveCharsheetService.loadCharsheet(characterId, new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                failureAction.onFailure(caught);
            }
            public void onSuccess(String result) {
                JSONValue jsonValue = JSONParser.parse(result);
                JSONDeserializer deserializer = new JSONDeserializer();
                deserializer.update(jsonValue, zithiaCharacter);
            }
        });
    }
    
    
    /**
     * Saves the character data, overwriting whatever is currently stored
     * for this character.
     */
    private void save() {
        CharacterStorage storage = new CharacterStorage(characterId, zithiaCharacter);
        saveCharsheetService.saveCharsheet(storage, new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                Window.alert("Save failed: " + caught);
            }
            public void onSuccess(Void result) {
                // Nothing to do
            }
        });
    }

}
