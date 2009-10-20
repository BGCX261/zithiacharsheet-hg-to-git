package com.mcherm.zithiacharsheet.client.modeler;


/**
 * This is a generic form of a TweakableIntValue. The value is computed
 * according to some formula and depends on certain fixed other values.
 */
public class CalculatedIntValue<T extends Observable> extends SimpleObservable implements TweakableIntValue {
    
    public static interface ValueCalculator<T> {
        public int calculateValue(Iterable<? extends T> inputs);
    }
    
        
    private int value;
    private Integer override;
    private Integer modifier;

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
            final Iterable<? extends T> inputs,
            final ValueCalculator<T> valueCalculator)
    {
        override = null;
        modifier = null;
        value = valueCalculator.calculateValue(inputs);
        Observable.Observer inputObserver = new Observable.Observer() {
            public void onChange() {
                value = valueCalculator.calculateValue(inputs);
                alertObservers();
            }
        };
        for (Observable input : inputs) {
            input.addObserver(inputObserver);
        }
    }

    @Override
    public int getValue() {
        if (override != null) {
            return override.intValue();
        } else if (modifier != null) {
            return value + modifier.intValue();
        } else {
            return value;
        }
    }

    @Override
    public boolean isEdited() {
        return override != null || modifier != null;
    }

    @Override
    public Integer getOverride() {
        return override;
    }

    @Override
    public Integer getModifier() {
        return modifier;
    }

    @Override
    public void setAdjustments(Integer override, Integer modifier) {
        if (override != null && modifier != null) {
            throw new IllegalArgumentException("Either override or modifier must be null.");
        }
        this.override = override;
        this.modifier = modifier;
        alertObservers();
    }
    
}
