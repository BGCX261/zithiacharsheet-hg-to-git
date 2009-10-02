package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.model.SkillCatalog;

/**
 * This is a dialog for selecting a skill. When shown, the user
 * can select a skill, at which point the indicated callback will
 * be invoked. If the dialog is cancelled, nothing will happen.
 */
public class SelectSkillDialog extends DialogBox {
	private final SkillCatalogDisplay skillCatalogDisplay;
    
    public SelectSkillDialog(SkillCatalogDisplay.SkillSelectCallback actionOnSelect) {
        skillCatalogDisplay = new SkillCatalogDisplay(SkillCatalog.getSingleton());
        skillCatalogDisplay.setSkillSelectCallback(actionOnSelect);
    	VerticalPanel dialogVPanel = new VerticalPanel();
    	dialogVPanel.add(new HTML("<b>Select a skill:</b>"));
    	dialogVPanel.add(skillCatalogDisplay);
        final Button closeButton = new Button("Close");
        final SelectSkillDialog theDialog = this;
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                theDialog.hide();
            }
        });
        dialogVPanel.add(closeButton);
    	this.setWidget(dialogVPanel);
    }
    
}
