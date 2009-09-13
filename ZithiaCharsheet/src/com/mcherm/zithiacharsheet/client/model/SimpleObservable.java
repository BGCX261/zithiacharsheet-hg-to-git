package com.mcherm.zithiacharsheet.client.model;

import java.util.Iterator;
import java.util.LinkedList;


/**
 * This can be used to implement the Observable interface.
 */
public abstract class SimpleObservable implements Observable {

    private final LinkedList<Observer> observers = new LinkedList<Observer>();
    
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    /**
     * Subclasses call this when they're ready to alert observers of a change.
     */
    protected void alertObservers() {
        Iterator<Observer> iter = observers.iterator();
        while (iter.hasNext()) {
            Observer observer = iter.next();
            observer.onChange();
        }
    }

}
