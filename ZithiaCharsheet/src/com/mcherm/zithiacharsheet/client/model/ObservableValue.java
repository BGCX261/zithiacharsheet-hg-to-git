package com.mcherm.zithiacharsheet.client.model;

import java.util.Iterator;
import java.util.LinkedList;


/**
 * Contains a single value, and can be observed. Someday
 * I might enhance this so the value isn't necessarily
 * an int, but for now it is.
 * 
 */
public class ObservableValue implements Observable {
    
    private LinkedList<Observer> observers = new LinkedList<Observer>();
    private int value;
    
    public ObservableValue(int initialValue) {
        this.value = initialValue;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public void setValue(int value) {
        this.value = value;
        Iterator<Observer> iter = observers.iterator();
        while (iter.hasNext()) {
            Observer observer = iter.next();
            observer.onChange();
        }
    }
    
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
}
