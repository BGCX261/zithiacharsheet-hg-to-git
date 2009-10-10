package com.mcherm.zithiacharsheet.client.model;

/**
 * An actual instance of a stat in a particular individual.
 * So, for instance, this might be Rogan Harsha's Strength.
 */
public class StatValue implements ObservableInt {
    private final ObservableIntValue value;
    private final ZithiaStat stat;
    
    public StatValue(ZithiaStat stat) {
        this.stat = stat;
        this.value = new ObservableIntValue(stat.getDefaultValue());
    }
    
    public int getValue() {
        return value.getValue();
    }

    public void setValue(int newValue) {
        value.setValue(newValue);
    }
    
    public ZithiaStat getStat() {
        return stat;
    }
    
    public void addObserver(Observable.Observer observer) {
        value.addObserver(observer);
    }
    
    // FIXME: this needs to take race into account. But for now it won't.
    public int getCost() {
        return stat.getCost(value.getValue() - stat.getDefaultValue());
    }
    
    public int getRoll() {
        return stat.getRoll(value.getValue());
    }
}
