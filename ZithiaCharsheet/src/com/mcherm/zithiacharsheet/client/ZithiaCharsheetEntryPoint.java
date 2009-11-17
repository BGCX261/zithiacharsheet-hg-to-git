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
