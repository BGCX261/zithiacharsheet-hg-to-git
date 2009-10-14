package com.mcherm.zithiacharsheet.client.model;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * This is a list of Observable objects which can itself be observed
 * for changes to the list. Optionally, a function can be provided
 * to obtain an integer from values in the list, in which case this
 * presents as a sum of the values.
 * <p>
 * This alerts its observers if an item is added to the list, removed
 * from the list, or if an item in the list alerts.
 */
public class ObservableList<T extends Observable> extends SimpleObservable implements ObservableInt, Iterable<T> {
    
    public static interface Extractor<T> {
        public int extractValue(T item);
    }
    
    private final ArrayList<T> items;
    private final Extractor<T> extractor;
    private final Observer childObserver;
    private int cachedSum;
    
    /**
     * A list which doesn't keep a sum. Its value is always 0.
     */
    public ObservableList() {
        this(new Extractor<T>() {
            public int extractValue(Observable item) {
                return 0;
            }
        });
    }
    
    /**
     * A list which which uses the indicated Extractor to get values
     * from items, then sums them.
     */
    public ObservableList(Extractor<T> extractor) {
        items = new ArrayList<T>(4);
        this.extractor = extractor;
        childObserver = new Observer() {
            public void onChange() {
                alertObservers();
            }
        };
        refreshCachedSum();
    }
    
    private void refreshCachedSum() {
        int result = 0;
        for (T item : items) {
            result += extractor.extractValue(item);
        }
        cachedSum = result;
    }
    
    public Iterator<T> iterator() {
        return items.iterator();
    }
    
    public void add(T item) {
        items.add(item);
        refreshCachedSum();
        item.addObserver(childObserver);
        alertObservers();
    }

    /**
     * This retrieves the sum of the values of the items in the list.
     */
    @Override
    public int getValue() {
        return cachedSum;
    }
    
}
