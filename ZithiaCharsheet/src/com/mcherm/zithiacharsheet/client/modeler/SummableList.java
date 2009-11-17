package com.mcherm.zithiacharsheet.client.modeler;


/**
 * This is a list of objects which can itself be observed
 * for changes to the list and which sums up the items in the list.
 * A function must be provided to obtain an ObservableInt from each
 * item in the list.
 * <p>
 * This alerts its observers if an item is added to the list or removed
 * from the list. The sum field alerts ITS observers if the sum changes
 * and also when the list itself alerts.
 */
public class SummableList<T> extends ObservableList<T> {
    
    public static interface Extractor<T> {
        public ObservableInt extractValue(T item);
    }
    
    private class ObservableSum extends SimpleObservable implements ObservableInt, Observer {
        public int getValue() {
            int result = 0;
            for (T item : SummableList.this) {
                result += extractor.extractValue(item).getValue();
            }
            return result;
        }

        public void onChange() {
            alertObservers(); // When a value we observe changes, notify our observers.
        }
    }
    
    private final Extractor<T> extractor;
    private final ObservableSum observableSum;
    
    /**
     * A list which which uses the indicated Extractor to get values
     * from items, then sums them.
     */
    public SummableList(Extractor<T> extractor) {
        super();
        this.extractor = extractor;
        observableSum = new ObservableSum();
        this.addObserver(observableSum); // when size of list changes, alert the sum
    }
    

    @Override
    public void add(T item) {
        super.add(item);
        extractor.extractValue(item).addObserver(observableSum); // the sum observes each item's value
    }
    
    @Override
    public void remove(T item) {
        super.remove(item);
        extractor.extractValue(item).removeObserver(observableSum);
    }
    
    @Override
    public void clear() {
        for (T item : this) {
            extractor.extractValue(item).removeObserver(observableSum);
        }
        super.clear();
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
