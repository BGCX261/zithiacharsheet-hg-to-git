package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.richtexttoolbar.RichTextToolbar;


/**
 * This is a RichTextArea combined with a formatting toolbar.
 * <p>
 * FIXME: I would really like a way to enter (and edit) the raw HTML also.
 * This is simply a RichTextArea combined with an editing toolbar.
 */
public class RichTextPalate extends VerticalPanel {

    private final RichTextArea richTextArea;

    public RichTextPalate() {
        richTextArea = new RichTextArea();

        // Style
        addStyleName("rich-text-palate");

        // Add the RichTextToolbar
        RichTextToolbar toolbar = new RichTextToolbar(richTextArea);
        add(toolbar);
        setCellWidth(toolbar, "100%");
        toolbar.setWidth("100%");

        // Add the RichTextArea
        add(richTextArea);
        setCellWidth(richTextArea, "100%");
        richTextArea.setWidth("100%");
    }

    /**
     * Retrieves the content of the rich text control as an HTML string.
     */
    public String getHTML() {
        return richTextArea.getHTML();
    }

    /**
     * Sets the content of the rich text control from an HTML string.
     */
    public void setHTML(String html) {
        richTextArea.setHTML(html);
    }

    /**
     * Provides a way to access the RichTextArea itself.
     */
    public RichTextArea getRichTextArea() {
        return richTextArea;
    }
}
