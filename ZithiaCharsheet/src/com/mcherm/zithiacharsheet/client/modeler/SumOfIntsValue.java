package com.mcherm.zithiacharsheet.client.modeler;


/**
 * A CalculatedIntValue which just adds up some ObservableInt values. The set
 * of values to add must not change.
 */
public class SumOfIntsValue extends CalculatedIntValue<ObservableInt> {
    
    public static class SumValueCalculator implements ValueCalculator<ObservableInt> {
        public int calculateValue(Iterable<? extends ObservableInt> inputs) {
            int result = 0;
            for (ObservableInt input : inputs) {
                result += input.getValue();
            }
            return result;
        }
    }
    
    public static SumValueCalculator sumValueCalculatorInstance = new SumValueCalculator();

    public SumOfIntsValue(Iterable<? extends ObservableInt> inputs) {
        super(inputs, sumValueCalculatorInstance);
    }

}
