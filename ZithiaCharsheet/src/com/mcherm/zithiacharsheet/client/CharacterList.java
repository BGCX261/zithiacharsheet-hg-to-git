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

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.model.CharacterStorage.CharacterMetadata;


/**
 * Display a list of the available characters and allow the user to
 * either select one (click "Go") or click a "New" button.
 */
public class CharacterList extends VerticalPanel {
    
    List<CharacterMetadata> metadataList;

    /**
     * Create the service object which allows us to save and load the
     * character data.
     */
    private final SaveCharsheetServiceAsync saveCharsheetService =
        GWT.create(SaveCharsheetService.class);
    
    
    public static interface ButtonActions {
        public void onGoButton(CharacterMetadata metadata);
        public void onNewButton();
    }
    
    /**
     * Constructor. This triggers a (delayed) load of the contents of the
     * list.
     */
    public CharacterList(final ButtonActions buttonActions) {
        addStyleName("characterList");
        setHorizontalAlignment(ALIGN_CENTER);
        setVerticalAlignment(ALIGN_TOP);
        final ListBox listBox = new ListBox();
        listBox.setVisibleItemCount(10);
        add(listBox);
        final HorizontalPanel buttonPanel = new HorizontalPanel();
        final Button newButton = new Button("New");
        final Button goButton = new Button("Go");
        buttonPanel.add(newButton);
        buttonPanel.add(goButton);
        add(buttonPanel);
        
        newButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                buttonActions.onNewButton();
            }
        });
        goButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (metadataList != null) {
                    assert metadataList.size() == listBox.getItemCount();
                    int selectedIndex = listBox.getSelectedIndex();
                    if (selectedIndex == -1) {
                        // No selection, so we will simply do nothing.
                    } else {
                        CharacterMetadata metadata = metadataList.get(selectedIndex);
                        buttonActions.onGoButton(metadata);
                    }
                }
            }
        });
        
        saveCharsheetService.listCharsheets(new AsyncCallback<List<CharacterMetadata>>() {

            public void onSuccess(List<CharacterMetadata> result) {
                for (CharacterMetadata metadata : result) {
                    String textOfItem = metadata.playerName + " :: " + metadata.characterName;
                    listBox.addItem(textOfItem);
                }
                metadataList = result;
            }
            
            public void onFailure(Throwable caught) {
                Window.alert("Could not load the list of characters: " + caught);
            }
        });
    }

}
