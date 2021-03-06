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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * This is a class for a pop-up dialog that allows the user to select an
 * item from a list. The objects to be selected are of type T.
 */
public class FancyListSelectionDialog<T> extends DialogBox {
    
    public static interface ItemDisplayCallback<T> {
        public List<Widget> getDisplay(T item);
    }
    
    public static interface ItemSelectCallback<T> {
        public void newItemSelected(T item);
    }
    
    private final List<T> items;
    private final ItemDisplayCallback<T> itemDisplayCallback;
    private final ItemSelectCallback<T> itemSelectCallback;
    private final boolean destroyOnClose;
    private final String title;
    private final String tableStyle;
    
    /**
     * Constructor.
     */
    public FancyListSelectionDialog(
            List<T> items,
            ItemDisplayCallback<T> itemDisplayCallback, 
            ItemSelectCallback<T> itemSelectCallback,
            boolean destroyOnClose,
            String title)
    {
        this.items = items;
        this.itemDisplayCallback = itemDisplayCallback;
        this.itemSelectCallback = itemSelectCallback;
        this.destroyOnClose = destroyOnClose;
        this.title = title;
        tableStyle = "skillCatalog";
        setupDialogContents();
        setModal(true);
        center();
    }
    
    /**
     * Private subroutine of the constructor that puts contents into
     * the dialog.
     */
    private void setupDialogContents() {
        final VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.add(new HTML("<b>" + title + "</b>"));
        final FlexTable table = new FlexTable();
        table.addStyleName(tableStyle);
        
        int row = 0;
        for (final T item : items) {
            ClickHandler rowClickHandler = new ClickHandler() {
                public void onClick(ClickEvent event) {
                    itemSelectCallback.newItemSelected(item);
                    dialogCompleted();
                }
            };
            final List<Widget> widgets = itemDisplayCallback.getDisplay(item);
            int col = 0;
            for (final Widget widget : widgets) {
                if (widget instanceof HasClickHandlers) {
                    final HasClickHandlers hch = (HasClickHandlers)  widget;
                    hch.addClickHandler(rowClickHandler);
                }
                table.setWidget(row, col, widget);
                col++;
            }
            row++;
        }
        
        dialogVPanel.add(table);
        final Button closeButton = new Button("Cancel");
        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialogCompleted();
            }
        });
        dialogVPanel.add(closeButton);
        this.setWidget(dialogVPanel);
    }
    
    /**
     * This is called after an item is selected or the dialog is canceled.
     */
    private void dialogCompleted() {
        hide(); // FIXME: Do we hide on close, or delete? Perhaps a constructor flag to control this?
        if (destroyOnClose) {
            this.removeFromParent();
        }
    }

}
