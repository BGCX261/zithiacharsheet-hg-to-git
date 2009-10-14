package com.mcherm.zithiacharsheet.client.model;



/**
 * This is an ObservableIntValue for which the value is (normally) computed
 * according to some formula and depends on other values.
 */
public class CalculatedIntValue extends ObservableIntValue {
    
    public static interface ValueCalculator {
        public int calculateValue(Iterable<? extends Observable> inputs);
    }
        
    private final Iterable<? extends Observable> inputs;
    private final ValueCalculator valueCalculator;

    /**
     * Constructor.
     * 
     * @param inputs a list of inputs that this depends on. The list
     *   passed must never change (it can be a clone of another list
     *   if necessary).
     * @param valueCalculator a function to calculate the value at
     *   any point in time.
     */
    public CalculatedIntValue(
            Iterable<? extends Observable> inputs,
            ValueCalculator valueCalculator)
    {
        super(valueCalculator.calculateValue(inputs));
        this.inputs = inputs;
        this.valueCalculator = valueCalculator;
        Observable.Observer inputObserver = new Observable.Observer() {
            public void onChange() {
                recalculateCost();
            }
        };
        for (Observable input : inputs) {
            input.addObserver(inputObserver);
        }
    }

    private void recalculateCost() {
        setValue(valueCalculator.calculateValue(inputs));
    }
    
}
