package com.mcherm.zithiacharsheet.client.modeler;


/**
 * A class where a boolean value can be calculated as depending on certain other
 * observable values.
 */
public class CalculatedBooleanValue extends SimpleObservable implements ObservableBoolean {

    private boolean value;
    
    public static interface BooleanValueCalculator {
        public boolean calculateValue();
    }

    /**
     * Constructor.
     * 
     * @param inputs a list of inputs that this depends on. The list
     *   passed must never change (it can be a clone of another list
     *   if necessary).
     * @param valueCalculator a function to calculate the value at
     *   any point in time.
     */
    public CalculatedBooleanValue(
            final Iterable<? extends Observable> inputs,
            final BooleanValueCalculator valueCalculator)
    {
        value = valueCalculator.calculateValue();
        Observable.Observer inputObserver = new Observable.Observer() {
            public void onChange() {
                value = valueCalculator.calculateValue();
                alertObservers();
            }
        };
        for (Observable input : inputs) {
            input.addObserver(inputObserver);
        }
    }
    
    
    @Override
    public boolean getValue() {
        return value;
    }

}
