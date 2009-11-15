package com.mcherm.zithiacharsheet.client.util;

import java.util.LinkedList;


/**
 * An abstract class that can be extended to form a JSON serializer for some
 * tree-oriented data structure.
 * <p>
 * NOTE: Not threadsafe.
 * // FIXME: Add docs on usage!
 */
public abstract class JSONSerializerBase {

    // ==== Constants ====

    private final static String INDENT_STR = "  ";

    // ==== Instance Variables ====

    private final boolean prettyPrint;
    private final Writer writer;
    private boolean lineHasOpenBracket;

    private enum State {
        IN_DICT, IN_LIST, ITEM
    }
    /**
     * At all times, this will contain a stack of IN_DICT and IN_LIST states in all but the
     * topmost item in the stack. The topmost item will be an ITEM state if we are ready to
     * emit an item, and will NOT be ITEM if we are ready to emit separation (like a comma
     * or whatever).
     */
    private final LinkedList<State> stateStack;


    /** An exception thrown when JSON Serializer is used wrong. */
    public static class JSONSerializerException extends RuntimeException {
    }


    // ==== Inner Classes ====

    public static interface Writer {
        public void write(String s);
    }

    // ==== Construction and Output ====

    protected JSONSerializerBase(Writer writer) {
        this(writer, false); // default to no prettyPrint
    }

    protected JSONSerializerBase(Writer writer, boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        this.writer = writer;
        lineHasOpenBracket = false;
        stateStack = new LinkedList<State>();
        stateStack.addFirst(State.ITEM);
    }

    /**
     * This returns true if the as many brackets have been closed as were
     * opened.
     */
    protected boolean isDone() {
        return stateStack.isEmpty();
    }

    // ==== Raw String Manipulation ====

    /**
     * Returns s with all '"' replaced with '\"', all newlines
     * replaced with "\n", and all "\" replaced with "\\".
     */
    private static String escapeJSONString(String s) {
        boolean hasValuesToEscape = false;
        for (int i=0; i<s.length(); i++) {
            switch(s.charAt(i)) {
                case '\\': case '"': case '\n': hasValuesToEscape = true;
            }
        }

        if (!hasValuesToEscape) {
            return s;
        } else {
            StringBuilder result = new StringBuilder(s.length());
            for (int i=0; i<s.length(); i++) {
                final char c = s.charAt(i);
                switch(c) {
                    case '\\': result.append("\\\\"); break;
                    case '"': result.append("\\\""); break;
                    case '\n': result.append("\\n"); break;
                    default: result.append(c); break;
                }
            }
            return result.toString();
        }
    }

    // ==== Primitive Output ====

    /** The primitive function for generating output. */
    private void write(String s) {
        writer.write(s);
    }

    private void outputNull() {
        write("null");
    }

    private void outputString(String string) {
        if (string == null) {
            outputNull();
        } else {
            write("\"");
            write(escapeJSONString(string));
            write("\"");
        }
    }

    private void outputInt(int i) {
        write(Integer.toString(i));
    }

    private void outputBoolean(boolean b) {
        write(b ? "true" : "false");
    }

    private void indent() {
        if (prettyPrint) {
            final int indentLevel = stateStack.size();
            write("\n");
            for (int i=0; i<indentLevel; i++) {
                write(INDENT_STR);
            }
        }
        lineHasOpenBracket = false;
    }

    private void smartComma() {
        if (!lineHasOpenBracket) {
            write(",");
        }
    }

    /** Call this to assert that the top state on the stack should be State */
    private void expectState(State state) {
        if (stateStack.peek() != state) {
            throw new JSONSerializerException();
        }
    }

    // ==== Emit Routines for Subclasses ====


    protected final void emitStartDict() {
        expectState(State.ITEM);
        stateStack.removeFirst();
        if (lineHasOpenBracket) {
            indent();
        }
        write("{");
        lineHasOpenBracket = true;
        stateStack.addFirst(State.IN_DICT);
    }

    protected final void emitEndDict() {
        expectState(State.IN_DICT);
        stateStack.removeFirst();
        indent();
        write("}");
    }

    protected final void emitStartDictItem(String fieldName) {
        expectState(State.IN_DICT);
        smartComma();
        indent();
        outputString(fieldName);
        write(":");
        stateStack.addFirst(State.ITEM);
    }

    protected final void emitDictItem(String fieldName, String str) {
        emitStartDictItem(fieldName);
        emitItem(str);
    }

    protected final void emitDictItem(String fieldName, int i) {
        emitStartDictItem(fieldName);
        emitItem(i);
    }

    protected final void emitDictItem(String fieldName, boolean b) {
        emitStartDictItem(fieldName);
        emitItem(b);
    }

    protected final void emitStartList() {
        expectState(State.ITEM);
        stateStack.removeFirst();
        if (lineHasOpenBracket) {
            indent();
        }
        write("[");
        lineHasOpenBracket = true;
        stateStack.addFirst(State.IN_LIST);
    }

    protected final void emitEndList() {
        expectState(State.IN_LIST);
        stateStack.removeFirst();
        indent();
        write("]");
    }

    protected final void emitStartListItem() {
        expectState(State.IN_LIST);
        smartComma();
        stateStack.addFirst(State.ITEM);
    }
    
    protected final void emitItem(String s) {
        expectState(State.ITEM);
        outputString(s);
        stateStack.removeFirst();
    }

    protected final void emitItem(int i) {
        expectState(State.ITEM);
        outputInt(i);
        stateStack.removeFirst();
    }

    protected final void emitItem(boolean b) {
        expectState(State.ITEM);
        outputBoolean(b);
        stateStack.removeFirst();
    }

    /**
     * This should only be called in special circumstances. It should be used
     * when an item is expected and it emits raw JSON (which is expected to
     * be balanced).
     *
     * @param s the contents, which should be a complete, balanced entity in
     * JSON.
     */
    protected final void emitRawJSON(String s) {
        expectState(State.ITEM);
        write(s);
        stateStack.removeFirst();
    }
}
