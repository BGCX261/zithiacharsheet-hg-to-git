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
import com.google.gwt.user.client.ui.RootPanel;
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
        final Grid rightSide = new Grid(3,1);
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

    
    // FIXME: Obscelete now; get rid of it.
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
            // FIXME: This whole thing is a hack. Fix later.
            Window.alert("Character not specified: Will create new character.");
            saveCharsheetService.newCharsheet(new AsyncCallback<CharacterMetadata>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("Unable to create character: " + caught);
                }
                @Override
                public void onSuccess(CharacterMetadata result) {
                    characterId = result.getId();
                    System.out.println("Result = " + result + ", result.getId() = " + result.getId()); // FIXME: Remove
                    System.out.println("Character Id = " + characterId); // FIXME: Remove
                }
            });
        } else {
            // -- Load character --
            load(new FailureAction() {
                public void onFailure(Throwable caught) {
                    Window.alert("Load of character failed: " + caught);
                }
            });
        }
        
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
     * When this is called it writes itself to the database as a new, blank
     * entry, and retrieves a new characterId which replaces the ID currently in use.
     * <p>
     * FIXME: Probably more useful if it is NOT blank when written; but that's
     * a change for another time.
     */
    public void saveAsNewCharacter() {
        Window.alert("Character not specified: Will create new character.");
        saveCharsheetService.newCharsheet(new AsyncCallback<CharacterMetadata>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Unable to create character: " + caught);
            }
            @Override
            public void onSuccess(CharacterMetadata result) {
                characterId = result.getId();
                System.out.println("Result = " + result + ", result.getId() = " + result.getId()); // FIXME: Remove
                System.out.println("Character Id = " + characterId); // FIXME: Remove
            }
        });
    }

    
    /** A function that can be passed to load(). */
    public static interface FailureAction {
        public void onFailure(Throwable caught);
    }
    
    /**
     * Use to set the characterId. Will imediately perform a
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
        CharacterStorage storage = new CharacterStorage(characterId, zithiaCharacter);
        saveCharsheetService.saveCharsheet(storage, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Save failed: " + caught);
            }
            @Override
            public void onSuccess(Void result) {
                // Nothing to do
            }
        });
    }

}
