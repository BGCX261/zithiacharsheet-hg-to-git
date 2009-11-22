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
