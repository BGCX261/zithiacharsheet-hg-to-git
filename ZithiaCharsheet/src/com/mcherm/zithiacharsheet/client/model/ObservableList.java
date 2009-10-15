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
public class ObservableList<T extends Observable> extends SimpleObservable implements Iterable<T> {
    
    public static interface Extractor<T> {
        public int extractValue(T item);
    }
    
    private final ArrayList<T> items;
    private final Extractor<T> extractor;
    private final ObservableSum observableSum;
    
    /**
     * A list which doesn't keep a sum. Its value is always 0.
     */
    public ObservableList() {
        // FIXME: Switch to null extractor when not meaningful.
        this(new Extractor<T>() {
            public int extractValue(Observable item) {
                return 0;
            }
        });
    }
    
    // FIXME: Move this elsewhere in the file.
    private class ObservableSum extends SimpleObservable implements ObservableInt, Observer {
        @Override
        public int getValue() {
            int result = 0;
            for (T item : items) {
                result += extractor.extractValue(item);
            }
            return result;
        }

        /** When a value we observe changes, notify our observers. */
        @Override
        public void onChange() {
            alertObservers();
        }
    }
    
    /**
     * A list which which uses the indicated Extractor to get values
     * from items, then sums them.
     */
    public ObservableList(Extractor<T> extractor) {
        items = new ArrayList<T>(4);
        this.extractor = extractor;
        observableSum = new ObservableSum();
    }
    
    
    public Iterator<T> iterator() {
        return items.iterator();
    }
    
    public void add(T item) {
        items.add(item);
        alertObservers();
        item.addObserver(observableSum);
    }

    /**
     * This obtains an ObservableInt which contains the sum of the items
     * in the list and can be monitored to retrieve them. It returns
     * null if there is no meaningful sum defined for this list.
     */
    public ObservableInt getSum() {
        return observableSum;
    }
    
}
