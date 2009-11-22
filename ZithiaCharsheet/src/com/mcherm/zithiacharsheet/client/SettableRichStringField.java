package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.mcherm.zithiacharsheet.client.modeler.Observable;
import com.mcherm.zithiacharsheet.client.modeler.SettableStringValue;


/**
 * This is a text field that is tied to a SettableStringValue, but in this
 * case the string can contain HTML markup, which is displayed.
 */
public class SettableRichStringField extends RichTextPalate {

    protected final SettableStringValue value;
    private boolean ignoreValueUpdates;

    /**
     * Constructor. Must specify the value to which this is tied.
     */
    public SettableRichStringField(final SettableStringValue value) {
        this.value = value;
        this.ignoreValueUpdates = false;
        addStyleName("settableString");
        setHTML(value.getValue());
        value.addObserver(new Observable.Observer() {
            public void onChange() {
                if (!ignoreValueUpdates) {
                    SettableRichStringField.this.setHTML(value.getValue());
                }
            }
        });
        addBlurHandler(new BlurHandler() {
            public void onBlur(BlurEvent blurEvent) {
                ignoreValueUpdates = true;
                value.setValue(getHTML());
                ignoreValueUpdates = false;
            }
        });
    }

}
