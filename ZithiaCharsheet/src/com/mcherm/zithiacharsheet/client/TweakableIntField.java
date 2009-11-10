package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;



/**
 * A field that is tied to a value. If the value changes,
 * the field changes. You cannot edit the value (for now).
 */
public class TweakableIntField extends HorizontalPanel implements HasClickHandlers {
    
    private final TweakableIntValue value;
    private final TextBox fieldValue;
    private final AsterixThing asterixThing;
    
    
    private class TweakPopup extends PopupPanel {
        final TweakableIntValue value;
        final RadioButton radioNormal;
        final RadioButton radioModifier;
        final RadioButton radioOverride;
        final TextBox modifierText;
        final TextBox overrideText;
        
        public TweakPopup(final TweakableIntValue value) {
            this.value = value;
            final Panel content = new VerticalPanel();
            content.add(new HTML("Tweak Calculation"));
            final Panel row1 = new HorizontalPanel();
            radioNormal = new RadioButton("tweak_type", "Calculate normally.");
            row1.add(radioNormal);
            content.add(row1);
            final Panel row2 = new HorizontalPanel();
            radioModifier = new RadioButton("tweak_type", "Calculate, then add&nbsp;", true);
            row2.add(radioModifier);
            modifierText = new TextBox();
            modifierText.addStyleName("settableInt");
            row2.add(modifierText);
            content.add(row2);
            final Panel row3 = new HorizontalPanel();
            radioOverride = new RadioButton("tweak_type", "Just use&nbsp;", true);
            row3.add(radioOverride);
            overrideText = new TextBox();
            overrideText.addStyleName("settableInt");
            row3.add(overrideText);
            content.add(row3);
            final Button exitButton = new Button("Done");
            content.add(exitButton);
            add(content);
            exitButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    TweakPopup.this.hide();
                }
            });
            addCloseHandler(new CloseHandler<PopupPanel>() {
                public void onClose(CloseEvent<PopupPanel> event) {
                    Integer override, modifier;
                    if (radioModifier.getValue()) {
                        override = null;
                        try {
                           modifier = new Integer(modifierText.getText());
                        } catch(NumberFormatException err) {
                            Window.alert("Invalid integer.");
                            modifier = null;
                        }
                    } else if (radioOverride.getValue()) {
                        modifier = null;
                        try {
                            override = new Integer(overrideText.getText());
                        } catch(NumberFormatException err) {
                            Window.alert("Invalid integer.");
                            override = null;
                        }
                    } else {
                        modifier = null;
                        override = null;
                    }
                    value.setAdjustments(override, modifier);
                }
            });
        }
        
        /**
         * Call this to launch the popup.
         */
        public void launch() {
            if (!value.isTweaked()) {
                radioNormal.setValue(true);
            } else if (value.getModifier() != null) {
                radioModifier.setValue(true);
            } else if (value.getOverride() != null) {
                radioOverride.setValue(true);
            } else {
                throw new RuntimeException("Unexpected state of value.");
            }
            modifierText.setValue(intToStr(value.getModifier()));
            overrideText.setValue(intToStr(value.getOverride()));
            center();
        }
        
        private String intToStr(Integer i) {
            return i == null ? "" : i.toString();
        }
    }
    
    private class AsterixThing extends SimplePanel implements HasClickHandlers {
        private String currentState;
        private TweakPopup tweakPopup;
        
        public AsterixThing() {
            currentState = ".";
            tweakPopup = new TweakPopup(value);
            this.add(new HTML(currentState));
            this.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    tweakPopup.launch();
                }
            });
        }
        
        public void setTweaked(boolean isTweaked) {
            if (isTweaked && currentState != "*") {
                currentState = "*";
                clear();
                this.add(new HTML(currentState));
            } else if (!isTweaked && currentState != ".") {
                currentState = ".";
                clear();
                this.add(new HTML(currentState));
            }
        }

        /**
         * HasClickHandlers - Code to add handlers to the panel
         */  
        public HandlerRegistration addClickHandler(ClickHandler handler) {
             return addDomHandler(handler, ClickEvent.getType());
        }
    }
    
    
    /**
     * Constructor.
     * 
     * @param value the TweakableIntValue this is bound to.
     */
    public TweakableIntField(final TweakableIntValue value) {
        this.value = value;
        this.addStyleName("tweakableInt");
        fieldValue = new TextBox();
        fieldValue.setEnabled(false);
        asterixThing = new AsterixThing();
        add(fieldValue);
        add(asterixThing);
        updateDisplay();
        value.addObserver(new Observer() {
            public void onChange() {
                updateDisplay();
            }
        });
    }
    

    /**
     * Sets the displayed value to match the field value.
     */
    protected void updateDisplay() {
        String valueStr = Integer.toString(value.getValue());
        fieldValue.setValue(valueStr);
        asterixThing.setTweaked(value.isTweaked());
    }


    /**
     * HasClickHandlers - Code to add handlers to the panel
     */  
    public HandlerRegistration addClickHandler(ClickHandler handler) {
         return addDomHandler(handler, ClickEvent.getType());
    }

}
