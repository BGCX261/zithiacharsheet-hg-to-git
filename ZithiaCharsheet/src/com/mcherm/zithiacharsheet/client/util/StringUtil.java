package com.mcherm.zithiacharsheet.client.util;

/**
 * The ever-present collection of handy string manipulation functions.
 */
public class StringUtil {

    /**
     * Replaces all instances of "\\" with "\", of "\n" with a
     * newline and of "\"" with """. A backslash followed by any
     * other character or a backslash that ends the string is
     * invalid and will result in an exception.
     */
    public static String unescapeJSONString(String s) {
        if (s.indexOf('\\') == -1) {
            return s;
        } else {
            StringBuilder result = new StringBuilder(s.length());
            for (int i=0; i<s.length(); i++) {
                char c = s.charAt(i);
                if (c == '\\') {
                    i++;
                    if (i >= s.length()) {
                        throw new InvalidJSONString();
                    } else {
                        char c2 = s.charAt(i);
                        switch(c2) {
                            case '\\': result.append('\\'); break;
                            case '"': result.append('"'); break;
                            case 'n': result.append('\n'); break;
                            default: throw new InvalidJSONString();
                        }
                    }
                } else {
                    result.append(c);
                }
            }
            return result.toString();
        }
    }

    public static class InvalidJSONString extends RuntimeException {
    }
}
