/*
 * Copyright 2009 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcherm.zithiacharsheet.client.modeler;

import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;
import com.mcherm.zithiacharsheet.client.util.ImmutableList;


/**
 * Because observers keep a (hard) reference to their observers, the
 * observers cannot be freed by the garbage collector. This is a class
 * that can be extended by things which will need to register
 * observers and want to be sure that they will get unregistered
 * when the object is no longer in use. Users must call the dispose()
 * method before the object is ready to be garbage collected.
 * <p>
 * The class can be used in two ways. A class can extend from it, or
 * can create an instance of it. If they create an instance, it is a
 * good practice to call the dispose() method from a dispose() method
 * in the owner object.
 * <p>
 * FIXME: Need to update the docs and rename it to "Disposer" if I keep
 *   the change that adds a list of disposables to be cleaned up.
 */
public class Disposer implements Disposable {

    private boolean hasBeenDisposed = false;
    private ImmutableList<Disposable> disposables = new ImmutableList<Disposable>();

    /**
     * Call this to add an observer to an observable; it will be removed when the
     * dispose() method is called.
     *
     * @param observable the observable to observe
     * @param observer the observer that will be doing the observing
     */
    public void observe(Observable observable, Observer observer) {
        if (hasBeenDisposed) {
            throw new RuntimeException("Must not use an object that has been disposed.");
        }
        addDisposable(new Observation(observable, observer));
    }

    // FIXME: Don't keep BOTH this AND track() ... figure out which is best
    public void addDisposable(Disposable disposable) {
        disposables = disposables.add(disposable);
    }

    // FIXME: Doc this, but only if it works!
    public <T extends Disposable> T track(T disposable) {
        addDisposable(disposable);
        return disposable;
    }

    /**
     * Call this before freeing the last reference to the object. It will then
     * unregister its listeners from all observers.
     */
    public void dispose() {
        if (hasBeenDisposed) {
            throw new RuntimeException("Cannot dispose object more than once.");
        }
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        hasBeenDisposed = true;
        disposables = null;
    }

    /** On creation it adds an observer; when disposed it removes it. */
    private static class Observation implements Disposable {
        private final Observable observable;
        private final Observer observer;
        public Observation(Observable observable, Observer observer) {
            this.observable = observable;
            this.observer = observer;
            observable.addObserver(observer);
        }

        public void dispose() {
            observable.removeObserver(observer);
        }
    }

}
