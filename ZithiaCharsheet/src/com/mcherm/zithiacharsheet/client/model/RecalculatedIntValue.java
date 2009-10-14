package com.mcherm.zithiacharsheet.client.model;

import java.util.Arrays;

import com.mcherm.zithiacharsheet.client.model.CalculatedIntValue.ValueCalculator;

/**
 * This is a kind of CalculatedIntValue, except that what it depends on varies.
 * Usually it is used to add up the items in a list.
 * 
 * @deprecated (If I can get ObservableList to take over everywhere.)
 */
public class RecalculatedIntValue extends SimpleObservable implements ObservableInt {
    
    
    public static interface InputResetter {
        public Iterable<? extends Observable> resetInputs();
    }
    
    private final InputResetter inputResetter;
    private final ValueCalculator valueCalculator;
    private CalculatedIntValue currentValue;
    
    public RecalculatedIntValue(
            Observable resetter,
            InputResetter inputResetter,
            ValueCalculator valueCalculator)
    {
        this(Arrays.asList(resetter), inputResetter, valueCalculator);
    }
        

    /**
     * Constructor.
     * 
     * @param resetters observables which, if they are altered, will cause
     *   the RecalculatedIntValue to re-determine the set of inputs.
     * @param inputResetter a function which will return the list of inputs.
     * @param valueCalculator a function to calculate the value from the
     *   current set of inputs.
     */
    public RecalculatedIntValue(
            Iterable<? extends Observable> resetters,
            InputResetter inputResetter,
            ValueCalculator valueCalculator)
    {
        this.inputResetter = inputResetter;
        this.valueCalculator = valueCalculator;
        reset();
        for (Observable resetter : resetters) {
            resetter.addObserver(new Observer() {
                public void onChange() {
                    reset();
                }
            });
        }
    }
    
    private void reset() {
        // FIXME: Need to STOP listening on old set of inputs and destroy old currentValue.
        // FIXME: But cannot implement that while there's no way to unregister from an observable.
        final CalculatedIntValue newValue = new CalculatedIntValue(inputResetter.resetInputs(), valueCalculator);
        newValue.addObserver(new Observer() {
            public void onChange() {
                if (currentValue == newValue) { // This condition needed because we don't unregister old ones
                    alertObservers();
                }
            }
        });
        currentValue = newValue;
        alertObservers();
    }

    @Override
    public int getValue() {
        return currentValue.getValue();
    }

}
