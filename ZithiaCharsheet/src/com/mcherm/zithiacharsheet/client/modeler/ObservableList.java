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
package com.mcherm.zithiacharsheet.client.modeler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * This is a list of objects which can itself be observed
 * for changes to the list.
 * <p>
 * This alerts its observers if an item is added to the list or removed
 * from the list.
 */
public class ObservableList<T> extends SimpleObservable implements Iterable<T> {
    
    private final List<T> items;
    
    /**
     * Constructor.
     */
    public ObservableList() {
        items = new ArrayList<T>(4);
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(items).iterator();
    }
    
    public void add(T item) {
        items.add(item);
        alertObservers();
    }
    
    /**
     * Removes the first occurrence of item from the list if it was in the list;
     * does nothing if it was not in the list. equals() is used to test for
     * equality.
     */
    public void remove(T item) {
        items.remove(item);
        alertObservers();
    }
    
    /**
     * Removes <i>all</i> items from the list in a single call.
     */
    public void clear() {
        items.clear();
        alertObservers();
    }
}
