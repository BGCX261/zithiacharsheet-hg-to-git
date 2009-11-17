package com.mcherm.zithiacharsheet.client.modeler;

import com.mcherm.zithiacharsheet.client.util.ImmutableList;


/**
 * This can be used to implement the Observable interface.
 */
public class SimpleObservable implements Observable {

    private ImmutableList<Observer> observers = new ImmutableList<Observer>();
    
    public void addObserver(Observer observer) {
        observers = observers.add(observer);
    }
    
    public void removeObserver(Observer observer) {
        observers = observers.remove(observer);
    }

    /**
     * Subclasses call this when they're ready to alert observers of a change.
     */
    protected void alertObservers() {
        // FIXME: Possible bug: new observers get added during the iteration. We should probably save the modifications until AFTER the iteration.
        ImmutableList<Observer> currentObservers = observers;
        for (Observer observer : currentObservers) {
            observer.onChange();
        }
    }

}
