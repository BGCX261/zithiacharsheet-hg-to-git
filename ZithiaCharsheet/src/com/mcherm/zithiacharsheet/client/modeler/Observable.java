package com.mcherm.zithiacharsheet.client.modeler;


/**
 * Creates an object which can be observed.
 */
public interface Observable {
    public static interface Observer {
        public void onChange();
    }

    public void addObserver(Observer observer);
    
    public void removeObserver(Observer observer);
    
    // FIXME: Need the ability to mark an observable as deleted, then it will get released for GC.
}
