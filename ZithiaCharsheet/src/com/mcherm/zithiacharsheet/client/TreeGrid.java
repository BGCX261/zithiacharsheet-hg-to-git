/*
 * Copyright 2009 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcherm.zithiacharsheet.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.Widget;


/**
 * This class is general-purpose widget which shares some of the features
 * of a FlexGrid and some of the features of a tree. Basically, it's a tree
 * where there are columns and rows (the indentation happens in the first
 * column) or, viewed differently, it's a grid where certain rows can be
 * "opened" and "closed" to show "child" rows.
 */
public class TreeGrid extends Composite {

    private final int numColumns;
    private final TreeImages treeImages;
    private final FlexTable table;

    public TreeGrid(TreeGridItem rootItem, int numColumns, final TreeImages treeImages) {
        this.numColumns = numColumns;
        this.treeImages = treeImages;
        table = new FlexTable();
        int row = 0;
        List<WidgetOrText> header = getHeader();
        if (header != null) {
            createHeader(header);
            row += 1;
        }
        new TreeGridBranchLiveRoot(rootItem, row);
        initWidget(table);
    }


    /**
     * An item in the TreeGrid. Items can either be a leaf (cannot possibly
     * have children; shown without an "open/close" image) or branches.
     * Branches, when shown, will have an open/close image. They will be
     * displayed with a state which can be toggled between open and closed.
     * They start out closed. The children of a branch will be created
     * lazily the first time that the branch is opened.
     */
    public static interface TreeGridItem {
        /**
         * This will be called on the TreeGridItem when it is first created. It
         * provides an object which the TreeGridItem can use to control the
         * display of the row.
         */
        public void setRowEditor(RowEditor rowEditor);
        /**
         * Returns a list of the items that go in the row. The list returned
         * must be exactly numColumns long.
         */
        public List<WidgetOrText> getContents();
        /** Return true if this is a leaf. Implies no children. */
        public boolean isLeaf();
        /** Returns the list of children of this TreeGridItem or null to indicate no children. */
        public Iterable<TreeGridItem> getChildren();
        /** Called when the row for this TreeGridItem is going to be deleted permanently. */
        public void dispose();
    }

    /**
     * A TreeGrid.RowEditor is a class which is used to control a particular row
     * in the table. It provides methods for doing things like marking the row
     * a particular color, reseting a row's children or deleting a row. It may
     * grow more abilities in the future. An instance will be provided to each
     * TreeGridItem so it can control the display of its row.
     */
    public static class RowEditor {
        private final TreeGridItemLive treeGridItemLive;

        /**
         * Constructor is private: they are created only within TreeGrid.
         */
        private RowEditor(TreeGridItemLive treeGridItemLive) {
            this.treeGridItemLive = treeGridItemLive;
        }

        /**
         * Calling this will delete all decedents of this row, and reset its list of
         * children (which may later get re-populated with a call to getChildren()).
         */
        public void resetChildren() {
            treeGridItemLive.removeChildren();
        }
    }

    /**
     * Wraps a TreeGridItem and also keep track of its current state. Has concrete
     * subclasses for the root node and for all other nodes.
     */
    private abstract class TreeGridItemLive {
        // -- final fields --
        protected final TreeGridItem treeGridItem;
        protected final int indentLevel;
        protected final Image treeControlsImage;
        // -- mutable fields --
        protected boolean currentlyOpen;
        /** List of children, or null if children need to be initialized. */
        protected List<TreeGridItemLiveBranch> children;

        /** Constructor. */
        public TreeGridItemLive(TreeGridItem treeGridItem, int indentLevel, int row) {
            this.indentLevel = indentLevel;
            this.treeGridItem = treeGridItem;
            this.currentlyOpen = false;
            if (treeGridItem.isLeaf()) {
                treeControlsImage = treeImages.treeLeaf().createImage();
            } else {
                treeControlsImage = treeImages.treeClosed().createImage();
            }
            children = null;
            drawRow(row);
            treeGridItem.setRowEditor(new RowEditor(this));
        }

        /** Returns the indent level of this item. The root has level 0. */
        public int getIndentLevel() {
            return indentLevel;
        }

        /**
         * Obtains the current row number for this item within the table.
         * <p>
         * NOTE: This may need some caching for performance. We would have
         * to clear the cache whenever any row had new children added
         * (but NOT on open and close).
         */
        abstract int getCurrentRow();

        /** Toggles between the open and closed state. */
        public void toggle() {
            if (currentlyOpen) {
                closeChildren();
            } else {
                openChildren();
            }
        }

        /** Returns true if this particular node is a branch which is currently open. */
        public boolean isOpen() {
            return currentlyOpen;
        }

        /** Displays the correct tree image. */
        private void updateTreeImage() {
            AbstractImagePrototype desiredImage;
            if (currentlyOpen) {
                desiredImage = treeImages.treeOpen();
            } else {
                desiredImage = treeImages.treeClosed();
            }
            desiredImage.applyTo(treeControlsImage);
        }

        /** Call this to hide all the children. Called only when they HAD BEEN open. */
        public void closeChildren() {
            if (children == null) {
                throw new RuntimeException("Should be impossible to try closing a node without children.");
            }
            currentlyOpen = false;
            for (TreeGridItemLiveBranch child : children) {
                child.hide();
            }
            updateTreeImage();
        }
        
        /**
         * Finds the row number where a particular child begins.
         *
         * @param specificChild this must be a TreeGridBranchLive which is a
         *   child of the one on which the method is being called.
         * @return the row number where that child currently resides.
         */
        int getRowWhereAChildBegins(TreeGridItemLiveBranch specificChild) {
            if (children == null) {
                throw new RuntimeException("getRowWhereAChildBegins() called on node without children.");
            }
            int result = getCurrentRow();
            for (TreeGridItemLiveBranch child : children) {
                if (child.equals(specificChild)) {
                    result += 1; // That's what was used; add one more for the child...
                    return result;
                } else {
                    result += child.getRowsUsedByThisAndAllDescendants();
                }
            }
            throw new RuntimeException("getRowWhereAChildBegins() called with node not found in children.");
        }

        /** Like the method name says. */
        int getRowsUsedByThisAndAllDescendants() {
            int result = 1;
            if (children != null) {
                for (TreeGridItemLiveBranch child : children) {
                    result += child.getRowsUsedByThisAndAllDescendants();
                }
            }
            return result;
        }

        /** Displays the previously-hidden children (creating them if needed). */
        public void openChildren() {
            currentlyOpen = true;
            if (children == null) {
                children = new ArrayList<TreeGridItemLiveBranch>();
                int row = this.getCurrentRow();
                for (TreeGridItem childItem : treeGridItem.getChildren()) {
                    row++;
                    TreeGridItemLiveBranch child = new TreeGridItemLiveBranch(childItem, this, row);
                    children.add(child);
                }
            } else {
                for (TreeGridItemLiveBranch child : children) {
                    child.show();
                }
            }
            updateTreeImage();
        }

        /** Returns false if any parent is collapsed; true if all are open. */
        public abstract boolean isVisible();

        /**
         * This creates a new row at position 'row'.
         * @param row the number of the new row
         */
        private void insertRow(int row) {
            if (row < table.getRowCount()) {
                table.insertRow(row);
            } else if (row == table.getRowCount()) {
                // Nothing to do... appending happens automatically in a FlexTable.
            } else {
                throw new RuntimeException("Attempt to add beyond the end of the table.");
            }
        }

        /** This uses a particular TreeGridItem and renders it to a given row of the table. */
        private void drawRow(int row) {
            insertRow(row);
            List<WidgetOrText> contents = treeGridItem.getContents();
            // --- Column 0 has tree indent ---
            HorizontalPanel colZeroPanel;
            {
                Widget normalColZeroContent;
                {
                    WidgetOrText widgetOrText = contents.get(0);
                    if (widgetOrText.isWidget()) {
                        normalColZeroContent = widgetOrText.getWidget();
                    } else {
                        normalColZeroContent = new Label(widgetOrText.getText());
                    }
                }
                // FIXME: Better to share a common click handler.
                if (! treeGridItem.isLeaf()) {
                    treeControlsImage.addClickHandler(new ClickHandler() {
                        public void onClick(ClickEvent clickEvent) {
                            toggle();
                        }
                    });
                }
                int indentPixels = 16 * getIndentLevel(); // FIXME: Don't hardcode 16
                Widget indentSpacer = new HTML("<div style=\"width: " + indentPixels + "px\"></div>");
                colZeroPanel = new HorizontalPanel();
                colZeroPanel.add(indentSpacer);
                colZeroPanel.add(treeControlsImage);
                colZeroPanel.add(normalColZeroContent);
            }
            table.setWidget(row, 0, colZeroPanel);

            // --- All other columns ---
            populateRow(contents, row, 1);
        }

        /**
         * When this is called, all child rows in the table are removed, and the TreeGrid
         * is set back to a state of not knowing what children it has.
         */
        public void removeChildren() {
            if (children == null) {
                return;
            }
            if (currentlyOpen) {
                closeChildren();
            }
            List<TreeGridItemLiveBranch> oldChildren = children;
            for (int i=oldChildren.size() - 1; i>= 0; i--) {
                TreeGridItemLiveBranch child = oldChildren.get(i);
                int childRow = child.getCurrentRow();
                child.removeChildren();
                table.removeRow(childRow);
                child.treeGridItem.dispose();
            }
            children = null;
        }

    }

    /**
     * This is a subclass of TreeGridBranchLive which is used for the top
     * one in the tree.
     */
    private class TreeGridBranchLiveRoot extends TreeGridItemLive {

        private final int row;

        /**
         * Constructor.
         *
         * @param treeGridItem the item this is associated with.
         * @param row the row were this is shown. 0 without header; 1 with.
         */
        public TreeGridBranchLiveRoot(TreeGridItem treeGridItem, int row) {
            super(treeGridItem, 0, row);
            this.row = row;
        }

        @Override
        int getCurrentRow() {
            return row;
        }

        @Override
        public boolean isVisible() {
            return true;
        }

    }


    /**
     * This is a subclass of TreeGridBranchLive which is used for everything
     * EXCEPT the top one in the tree.
     */
    private class TreeGridItemLiveBranch extends TreeGridItemLive {
        // -- final fields --
        private final TreeGridItemLive parent;

        /**
         * Constructor.
         *
         * @param treeGridItem the TreeGridItem to display
         * @param parent the TreeGridBranchLive that is this one's parent, or null for the root.
         * @param row the row of the table this should be displayed in (which may not
         *    exist at the moment).
         */
        public TreeGridItemLiveBranch(TreeGridItem treeGridItem, TreeGridItemLive parent, int row) {
            super(treeGridItem, parent.getIndentLevel() + 1, row);
            this.parent = parent;
        }

        @Override
        int getCurrentRow() {
            return parent.getRowWhereAChildBegins(this);
        }

        /** Returns false if any parent is collapsed; true if all are open. */
        public boolean isVisible() {
            return parent.isOpen() && parent.isVisible();
        }

        /** Calling this will hide this item (and its children). Called only when it HAD BEEN visible. */
        public void hide() {
            Element tdElem = table.getFlexCellFormatter().getElement(getCurrentRow(), 0);
            Element trElem = tdElem.getParentElement();
            trElem.getStyle().setProperty("display", "none");
            if (isOpen()) {
                for (TreeGridItemLiveBranch child : children) {
                    child.hide();
                }
            }
        }

        /** Calling this will show the item (and descendants). Called only when it HAD BEEN hidden. */
        public void show() {
            Element tdElem = table.getFlexCellFormatter().getElement(getCurrentRow(), 0);
            Element trElem = tdElem.getParentElement();
            trElem.getStyle().setProperty("display", "table-row");
            if (isOpen()) {
                for (TreeGridItemLiveBranch child : children) {
                    child.show();
                }
            }
        }

    }


    /**
     * A convenience class which wraps EITHER a string of html OR a Widget
     * (basically, something that can go in a cell).
     */
    public static class WidgetOrText {
        private final Widget widget;
        private final String text;

        /** Constructs an instance that contains a widget. */
        public WidgetOrText(Widget widget) {
            if (widget == null) {
                throw new RuntimeException("Widget must not be null.");
            }
            this.widget = widget;
            this.text = null;
        }

        /** Constructs an instance that contains text. */
        public WidgetOrText(String text) {
            if (text == null) {
                throw new RuntimeException("Text must not be null.");
            }
            this.text = text;
            this.widget = null;
        }

        /** Returns true if it's a widget; false if it's text. */
        public boolean isWidget() {
            return this.widget != null;
        }

        /** Returns the widget if it's a widget, null if it's text. */
        public Widget getWidget() {
            return this.widget;
        }

        /** Returns the text if it's text, null if it's a widget. */
        public String getText() {
            return this.text;
        }
    }


    /**
     * Subclasses that wish to display a header can override this to
     * return a non-null TreeGridRowValues. If so, then it will be
     * shown as a header.
     *
     * @return null to display no header, or a List of exactly numColumns
     *   WidgetOrText objects.
     */
    protected List<WidgetOrText> getHeader() {
        return null; // default
    }

    /** Creates the header row. */
    private void createHeader(List<WidgetOrText> headerContents) {
        populateRow(headerContents, 0, 0);
    }

    /**
     * Insert values from a TreeGridRowValues into the table. Displays to
     * row <code>row</code> starting from column <code>firstCol</code> and
     * continuing to the end of the row.
     *
     * @param contents the items to display; this must be exactly numColumns long.
     * @param row the row to draw them in
     * @param firstCol the column to start with; this and all later cols are done
     */
    private void populateRow(List<WidgetOrText> contents, int row, int firstCol) {
        for (int col=firstCol; col<numColumns; col++) {
            WidgetOrText widgetOrText = contents.get(col);
            if (widgetOrText.isWidget()) {
                table.setWidget(row, col, widgetOrText.getWidget());
            } else {
                table.setText(row, col, widgetOrText.getText());
            }
        }
    }

}
