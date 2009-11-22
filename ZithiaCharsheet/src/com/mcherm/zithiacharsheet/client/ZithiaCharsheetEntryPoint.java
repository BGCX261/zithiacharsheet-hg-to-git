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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.mcherm.zithiacharsheet.client.ZithiaCharsheet.FailureAction;
import com.mcherm.zithiacharsheet.client.model.CharacterStorage.CharacterMetadata;


/**
 * This is a rather bare class that exists simply to be the "entry point" to
 * the ZithiaCharsheet application.
 */
public class ZithiaCharsheetEntryPoint implements EntryPoint {
    
    private final ZithiaCharsheet zithiaCharsheet;

    public ZithiaCharsheetEntryPoint() {
        zithiaCharsheet = new ZithiaCharsheet();
    }
    

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        // -- Create widgets --
        final DeckPanel mainPanel = new DeckPanel();
        RootPanel.get("charsheet").add(mainPanel);
        
        // -- Set up the card in the deck for showing the list --
        mainPanel.add(new CharacterList(new CharacterList.ButtonActions() {

            public void onGoButton(CharacterMetadata metadata) {
                mainPanel.showWidget(1); // Show the character sheet
                zithiaCharsheet.setCharacterId(metadata.getId(), new FailureAction() {
                    public void onFailure(Throwable caught) {
                        Window.alert("Load of character failed: " + caught);
                        mainPanel.showWidget(0);
                    }
                });
            }

            public void onNewButton() {
                mainPanel.showWidget(1); // Show the character sheet
                // FIXME: Maybe we need to empty it out; for now we won't.
                zithiaCharsheet.saveAsNewCharacter();
            }
        }));
        
        mainPanel.add(zithiaCharsheet);
        mainPanel.showWidget(0); // Show the select list
    }

}
