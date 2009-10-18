package com.mcherm.zithiacharsheet.client.modeler;

import java.util.Arrays;
import java.util.Iterator;


/**
 * A CalculatedIntValue that takes a couple of specific ObservableInts and
 * applies a simple equation to determine the value.
 */
public class EquationIntValue extends CalculatedIntValue<ObservableInt> {
    
    public static interface Equation1 {
        public int getValue(int x1);
    }
    public static interface Equation2 {
        public int getValue(int x1, int x2);
    }
    public static interface Equation3 {
        public int getValue(int x1, int x2, int x3);
    }
    public static interface Equation4 {
        public int getValue(int x1, int x2, int x3, int x4);
    }
    public static interface Equation5 {
        public int getValue(int x1, int x2, int x3, int x4, int x5);
    }
    
    
    public EquationIntValue(
            ObservableInt in1,
            final Equation1 equation)
    {
        super(
            Arrays.asList(in1),
            new ValueCalculator<ObservableInt>() {
                public int calculateValue(Iterable<? extends ObservableInt> inputs) {
                    Iterator<? extends ObservableInt> iterator = inputs.iterator();
                    int x1 = iterator.next().getValue();
                    return equation.getValue(x1);
                }
            }
        );
    }

    public EquationIntValue(
            ObservableInt in1,
            ObservableInt in2,
            final Equation2 equation)
    {
        super(
            Arrays.asList(in1, in2),
            new ValueCalculator<ObservableInt>() {
                public int calculateValue(Iterable<? extends ObservableInt> inputs) {
                    Iterator<? extends ObservableInt> iterator = inputs.iterator();
                    int x1 = iterator.next().getValue();
                    int x2 = iterator.next().getValue();
                    return equation.getValue(x1, x2);
                }
            }
        );
    }

    public EquationIntValue(
            ObservableInt in1,
            ObservableInt in2,
            ObservableInt in3,
            final Equation3 equation)
    {
        super(
            Arrays.asList(in1, in2, in3),
            new ValueCalculator<ObservableInt>() {
                public int calculateValue(Iterable<? extends ObservableInt> inputs) {
                    Iterator<? extends ObservableInt> iterator = inputs.iterator();
                    int x1 = iterator.next().getValue();
                    int x2 = iterator.next().getValue();
                    int x3 = iterator.next().getValue();
                    return equation.getValue(x1, x2, x3);
                }
            }
        );
    }

    public EquationIntValue(
            ObservableInt in1,
            ObservableInt in2,
            ObservableInt in3,
            ObservableInt in4,
            final Equation4 equation)
    {
        super(
            Arrays.asList(in1, in2, in3, in4),
            new ValueCalculator<ObservableInt>() {
                public int calculateValue(Iterable<? extends ObservableInt> inputs) {
                    Iterator<? extends ObservableInt> iterator = inputs.iterator();
                    int x1 = iterator.next().getValue();
                    int x2 = iterator.next().getValue();
                    int x3 = iterator.next().getValue();
                    int x4 = iterator.next().getValue();
                    return equation.getValue(x1, x2, x3, x4);
                }
            }
        );
    }

    public EquationIntValue(
            ObservableInt in1,
            ObservableInt in2,
            ObservableInt in3,
            ObservableInt in4,
            ObservableInt in5,
            final Equation5 equation)
    {
        super(
            Arrays.asList(in1, in2, in3, in4, in5),
            new ValueCalculator<ObservableInt>() {
                public int calculateValue(Iterable<? extends ObservableInt> inputs) {
                    Iterator<? extends ObservableInt> iterator = inputs.iterator();
                    int x1 = iterator.next().getValue();
                    int x2 = iterator.next().getValue();
                    int x3 = iterator.next().getValue();
                    int x4 = iterator.next().getValue();
                    int x5 = iterator.next().getValue();
                    return equation.getValue(x1, x2, x3, x4, x5);
                }
            }
        );
    }

}
