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
    
    public interface ItemDisplay<T> {
        public Widget contents(int column);
    }
    
    public static interface ItemDisplayCallback<T> {
        public int getNumColumns();
        public ItemDisplay<T> getDisplay(T item);
    }
    
    public static interface ItemSelectCallback<T> {
        public void newItemSelected(T item);
    }
    
    private final List<T> items;
    private final ItemDisplayCallback<T> itemDisplayCallback;
    private final ItemSelectCallback<T> itemSelectCallback;
    private final String title;
    private final String tableStyle;
    
    /**
     * Constructor.
     */
    public FancyListSelectionDialog(
            List<T> items,
            ItemDisplayCallback<T> itemDisplayCallback, 
            ItemSelectCallback<T> itemSelectCallback)
    {
        this.items = items;
        this.itemDisplayCallback = itemDisplayCallback;
        this.itemSelectCallback = itemSelectCallback;
        title = "Select one:"; // FIXME: Don't hardcode
        tableStyle = "skillCatalog";
        setupDialogContents();
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
        
        final int numColumns = itemDisplayCallback.getNumColumns();
        int row = 0;
        for (final T item : items) {
            ClickHandler rowClickHandler = new ClickHandler() {
                public void onClick(ClickEvent event) {
                    itemSelectCallback.newItemSelected(item);
                    dialogCompleted();
                }
            };
            final ItemDisplay<T> itemDisplay = itemDisplayCallback.getDisplay(item);
            for (int col = 0; col < numColumns; col++) {
                final Widget widget = itemDisplay.contents(col);
                if (widget instanceof HasClickHandlers) {
                    final HasClickHandlers hch = (HasClickHandlers)  widget;
                    hch.addClickHandler(rowClickHandler);
                }
                table.setWidget(row, col, widget);
            }
            row++;
        }
        
        dialogVPanel.add(table);
        final Button closeButton = new Button("Cancel");
        closeButton.addClickHandler(new ClickHandler() {
            @Override
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
    }

}
