package com.mcherm.zithiacharsheet.client;

import com.google.gwt.core.client.EntryPoint;
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
import com.google.gwt.user.client.ui.RootPanel;
import com.mcherm.zithiacharsheet.client.model.JSONDeserializer;
import com.mcherm.zithiacharsheet.client.model.JSONSerializer;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ZithiaCharsheet implements EntryPoint {

    private final ZithiaCharacter zithiaCharacter;
    private String characterId;
    
    // FIXME: Move or doc this or something after it works.
    private final SaveCharsheetServiceAsync saveCharsheetService = GWT.create(SaveCharsheetService.class);
    
    
    public ZithiaCharsheet() {
        zithiaCharacter = new ZithiaCharacter();
    }

    
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        // -- Create the pieces of the charsheet --
        final Grid mainPanel = new Grid(1,2);
        RootPanel.get("charsheet").add(mainPanel);
        final Grid leftSide = new Grid(4,1);
        mainPanel.setWidget(0, 0, leftSide);
        final Grid rightSide = new Grid(2,1);
        mainPanel.setWidget(0, 1, rightSide);
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
        
        // -- Determine character id --
        characterId = Window.Location.getParameter("character");
        if (characterId == null) {
            characterId = "onlyCharacter"; // FIXME: Does this stay?
        }
        
        // -- Load character --
        load(new FailureAction() {
            public void onFailure(Throwable caught) {
                save(); // if the character doesn't exist, populate it with the default values
            }
        });
        
        // -- Show save/load buttons --
        final Button saveButton = new Button("Save");
        saveButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                save();
            }
        });
        DeferredCommand.addCommand(new Command() { public void execute() {
            leftSide.setWidget(3, 0, saveButton);
        } });
    }

    
    /**
     * Loads the character data, overwriting what is currently displayed.
     * <p>
     * FIXME: In the long run, is this method needed?
     */
    private void load() {
        load(new FailureAction() {
            public void onFailure(Throwable caught) {
                Window.alert("Could not load the data: " + caught);
            }
        });
    }

    /** A function that can be passed to load(). */
    private interface FailureAction {
        public void onFailure(Throwable caught);
    }
    
    /**
     * A version of load() where you can specify what to do if it fails.
     */
    private void load(final FailureAction failureAction) {
        saveCharsheetService.loadCharsheet(characterId, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                failureAction.onFailure(caught);
            }
            @Override
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
        JSONSerializer serializer = new JSONSerializer(false);
        serializer.serialize(zithiaCharacter);
        String output = serializer.output();
        saveCharsheetService.saveCharsheet(characterId, output, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("onFailure " + caught);
            }
            @Override
            public void onSuccess(Void result) {
                // Nothing to do
            }
        });
    }
}
