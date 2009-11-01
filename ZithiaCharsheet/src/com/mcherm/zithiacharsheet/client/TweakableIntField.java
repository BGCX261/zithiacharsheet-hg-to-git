package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;


/**
 * A field that is tied to a value. If the value changes,
 * the field changes. You cannot edit the value (for now).
 */
public class TweakableIntField extends DeckPanel implements HasClickHandlers {
    
    private final TweakableIntValue value;
    private final TextBox fieldValue1;
    private final TextBox fieldValue2;
    private final TextBox modifierValue;
    private final TextBox overrideValue;
    private int currentDeckItem;
    
    /**
     * Constructor.
     * 
     * @param value the TweakableIntValue this is bound to.
     */
    public TweakableIntField(final TweakableIntValue value) {
        this.value = value;
        this.addStyleName("tweakableInt");
        
        // -- First panel in deck --
        fieldValue1 = new TextBox();
        fieldValue1.setEnabled(false);
        
        // -- Second panel in deck --
        ComplexPanel modifierPanel = new FlowPanel();
        modifierValue = new TextBox();
        modifierValue.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> event) {
                try {
                    Integer modifier = new Integer(event.getValue());
                    value.setAdjustments(null, modifier);
                } catch(NumberFormatException err) {
                    value.setAdjustments(null, null);
                }
            }
        });
        fieldValue2 = new TextBox();
        fieldValue2.setEnabled(false);
        modifierPanel.add(new HTML("+"));
        modifierPanel.add(modifierValue);
        modifierPanel.add(new HTML("="));
        modifierPanel.add(fieldValue2);
        
        // -- Third panel in deck --
        ComplexPanel overridePanel = new FlowPanel();
        overrideValue = new TextBox();
        overrideValue.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> event) {
                try {
                    Integer override = new Integer(event.getValue());
                    value.setAdjustments(override, null);
                } catch(NumberFormatException err) {
                    value.setAdjustments(null, null);
                }
            }
        });
        overridePanel.add(new HTML("Set to"));
        overridePanel.add(overrideValue);
        
        // -- Assemble the deck --
        add(fieldValue1);
        add(modifierPanel);
        add(overridePanel);
        currentDeckItem = 0;
        updateDisplay();
        showWidget(currentDeckItem);
        value.addObserver(new Observer() {
            public void onChange() {
                updateDisplay();
            }
        });
        this.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                currentDeckItem = (currentDeckItem + 1) % 3;
                TweakableIntField.this.showWidget(currentDeckItem);
            }
        });
    }
    

    /**
     * Sets the displayed value to match the field value.
     */
    protected void updateDisplay() {
        String valueStr = Integer.toString(value.getValue());
        fieldValue1.setValue(valueStr);
        fieldValue2.setValue(valueStr);
        Integer modifier = value.getModifier();
        modifierValue.setValue(modifier == null ? "" : modifier.toString());
        Integer override = value.getOverride();
        overrideValue.setValue(override == null ? "" : override.toString());
    }

    /**
     * HasClickHandlers - Code to add handlers to the panel
     */  
    public HandlerRegistration addClickHandler(ClickHandler handler) {
         return addDomHandler(handler, ClickEvent.getType());
    }

}
