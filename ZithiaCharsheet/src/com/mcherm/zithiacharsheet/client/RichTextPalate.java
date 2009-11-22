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

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.richtexttoolbar.RichTextToolbar;


/**
 * This is a RichTextArea combined with a formatting toolbar.
 * <p>
 * FIXME: I should use the class Composite to hide the implementation of this.
 * FIXME: I should use the fancy editing widget for the HTML source also,
 *   although there it would insert tags in the text.
 */
public class RichTextPalate extends DeckPanel {

    private final RichTextArea richTextArea;
    private final TextArea htmlSourceArea;

    public RichTextPalate() {
        // --- Overall Setup ---
        addStyleName("rich-text-palate");

        // --- The rich-editor slide in the deck ---
        richTextArea = new RichTextArea();
        VerticalPanel richEditorContent = new VerticalPanel();
        RichTextToolbar richToolbar = new RichTextToolbar(this);
        richEditorContent.add(richToolbar);
        richEditorContent.add(richTextArea);
        richEditorContent.setCellWidth(richToolbar, "100%");
        richToolbar.setWidth("100%");
        richEditorContent.setCellWidth(richTextArea, "100%");
        richTextArea.setWidth("100%");

        // --- The html-source slide in the deck ---
        htmlSourceArea = new TextArea();
        VerticalPanel htmlSourceContent = new VerticalPanel();
        HorizontalPanel htmlToolbar = new HorizontalPanel();
        htmlToolbar.add(new Button("Rich Edit", new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                switchToRichEditMode();
            }
        }));
        htmlSourceContent.add(htmlToolbar);
        htmlSourceContent.add(htmlSourceArea);
        htmlSourceContent.setCellWidth(htmlToolbar, "100%");
        htmlToolbar.setWidth("100%");
        htmlSourceContent.setCellWidth(htmlSourceArea, "100%");
        htmlSourceArea.setWidth("100%");
        htmlSourceArea.setVisibleLines(10);

        // --- Add contents to the DeckPanel ---
        this.add(richEditorContent);
        this.add(htmlSourceContent);
        this.showWidget(0);
    }

    /**
     * Returns true if the WYSIWYG editor is currently active, and false if the
     * HTML-source editor is currently active.
     */
    public boolean richEditMode() {
        return getVisibleWidget() == 0;
    }

    public void switchToRichEditMode() {
        richTextArea.setHTML(htmlSourceArea.getText());
        this.showWidget(0);
    }

    public void switchToHTMLSourceMode() {
        htmlSourceArea.setText(richTextArea.getHTML());
        this.showWidget(1);
    }

    /**
     * Retrieves the content of the rich text control as an HTML string.
     */
    public String getHTML() {
        if (richEditMode()) {
            return richTextArea.getHTML();
        } else {
            return htmlSourceArea.getText();
        }
    }

    /**
     * Sets the content of the rich text control from an HTML string.
     */
    public void setHTML(String html) {
        if (richEditMode()) {
            richTextArea.setHTML(html);
        } else {
            htmlSourceArea.setText(html);
        }
    }

    public RichTextArea getRichTextArea() {
        return richTextArea;
    }


    /**
     * This can be used to create a blurHandler which will be invoked
     * whenver
     * @param blurHandler
     */
    public void addBlurHandler(BlurHandler blurHandler) {
        richTextArea.addBlurHandler(blurHandler);
        htmlSourceArea.addBlurHandler(blurHandler);
    }
}
