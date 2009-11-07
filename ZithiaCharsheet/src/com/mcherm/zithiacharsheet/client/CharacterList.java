package com.mcherm.zithiacharsheet.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.mcherm.zithiacharsheet.client.model.CharacterStorage.CharacterMetadata;


/**
 * This widget is a list of the characters. Selecting one performs an action.
 */
public class CharacterList extends FlexTable {
    
    private SelectAction selectAction;

    /**
     * Create the service object which allows us to save and load the
     * character data.
     */
    private final SaveCharsheetServiceAsync saveCharsheetService =
        GWT.create(SaveCharsheetService.class);
    
    
    public static interface SelectAction {
        public void onSelect(CharacterMetadata metadata);
    }
    
    /**
     * Constructor. This triggers a (delayed) load of the contents of the
     * list.
     */
    public CharacterList(SelectAction selectAction) {
        this.selectAction = selectAction;
        addStyleName("characterList");
        saveCharsheetService.listCharsheets(new AsyncCallback<List<CharacterMetadata>>() {
            
            @Override
            public void onSuccess(List<CharacterMetadata> result) {
                for (CharacterMetadata metadata : result) {
                    addRow(metadata);
                }
            }
            
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Could not load the list of characters: " + caught);
            }
        });
    }
    
    
    /**
     * Called for each row (in order) to add them to the table.
     * @param metadata the CharacterMetadata for this row.
     */
    private void addRow(final CharacterMetadata metadata) {
        int row = getRowCount();
        
        ClickHandler rowClickHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                selectAction.onSelect(metadata);
            }
        };
        
        Label number = new Label(Integer.toString(row));
        number.addClickHandler(rowClickHandler);
        setWidget(row, 0, number);
        
        Label playerName = new Label(metadata.playerName);
        playerName.addClickHandler(rowClickHandler);
        setWidget(row, 1, playerName);

        Label characterName = new Label(metadata.characterName);
        characterName.addClickHandler(rowClickHandler);
        setWidget(row, 2, characterName);
    }

}
