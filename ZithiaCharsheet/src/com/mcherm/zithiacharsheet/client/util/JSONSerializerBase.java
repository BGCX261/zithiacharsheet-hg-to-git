package com.mcherm.zithiacharsheet.client.util;


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
    private int indentLevel;
    private boolean lineHasOpenBracket;
    // FIXME: Should maintain inDict and inList variables and use them for validation that the call sequence is correct

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
        indentLevel = 0;
        lineHasOpenBracket = false;
    }

    /**
     * This returns true if the as many brackets have been closed as were
     * opened.
     */
    protected boolean isDone() {
        return indentLevel == 0;
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

    // ==== Emit Routines for Subclasses ====


    protected final void emitStartDict() {
        if (lineHasOpenBracket) {
            indent();
        }
        write("{");
        indentLevel++;
        lineHasOpenBracket = true;
    }

    protected final void emitEndDict() {
        indentLevel--;
        indent();
        write("}");
    }

    protected final void emitStartDictItem(String fieldName) {
        smartComma();
        indent();
        outputString(fieldName);
        write(":");
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
        if (lineHasOpenBracket) {
            indent();
        }
        write("[");
        indentLevel++;
        lineHasOpenBracket = true;
    }

    protected final void emitEndList() {
        indentLevel--;
        indent();
        write("]");
    }

    protected final void emitStartListItem() {
        smartComma();
    }
    
    protected final void emitItem(String s) {
        outputString(s);
    }

    protected final void emitItem(int i) {
        outputInt(i);
    }

    protected final void emitItem(boolean b) {
        outputBoolean(b);
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
        write(s);
    }
}
