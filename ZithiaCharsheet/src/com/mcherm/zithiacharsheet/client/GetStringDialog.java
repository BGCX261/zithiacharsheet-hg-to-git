package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This PURELY for development. It is a dialog where the user enters a string.
 */
public class GetStringDialog extends DialogBox {
    
    public static interface Action {
        public void doAction(String text);
    }
    
    public GetStringDialog(final Action action) {
        final VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.add(new HTML("<b>Enter a string:</b>"));
        final TextArea textEntry = new TextArea();
        dialogVPanel.add(textEntry);
        final Button okButton = new Button("OK");
        final DialogBox theDialog = this;
        okButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                theDialog.hide();
            }
        });
        dialogVPanel.add(okButton);
        this.setWidget(dialogVPanel);
        this.addCloseHandler(new CloseHandler<PopupPanel>() {
            public void onClose(CloseEvent<PopupPanel> event) {
                action.doAction(textEntry.getText());
            }
        });
    }
}
