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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import com.mcherm.zithiacharsheet.client.util.ImmutableList;


/**
 * A CalculatedIntValue that takes a couple of specific ObservableInts and
 * applies a simple equation to determine the value.
 */
public class EquationIntValue extends CalculatedIntValue<ObservableInt> {

    public static interface Equation0 {
        public int getValue();
    }

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
            final Equation0 equation)
    {
        super(
            new ImmutableList<ObservableInt>(),
            new ValueCalculator<ObservableInt>() {
                public int calculateValue(Iterable<? extends ObservableInt> inputs) {
                    return equation.getValue();
                }
            }
        );
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
