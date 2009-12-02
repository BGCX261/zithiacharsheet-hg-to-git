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
package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.mcherm.zithiacharsheet.client.model.WeaponTraining;
import com.mcherm.zithiacharsheet.client.modeler.Disposable;
import com.mcherm.zithiacharsheet.client.modeler.Disposer;
import com.mcherm.zithiacharsheet.client.modeler.Observable;
import com.mcherm.zithiacharsheet.client.modeler.ObservableBoolean;
import com.mcherm.zithiacharsheet.client.modeler.SettableBooleanValue;


/**
 * A field for viewing the training status. Has three possible values:
 * unchecked (is not trained), checked (paid to train in this weapon skill),
 * and checked-and-disabled (inherited training from a parent weapon skill).
 */
public class TrainingEntryField extends CheckBox implements Disposable {
    private final ObservableBoolean trained;
    private final SettableBooleanValue trainDesired;
    private final ObservableBoolean trainPaidHere;
    private final Disposer disposer = new Disposer();


    public TrainingEntryField(WeaponTraining wt) {
        trained = wt.isTrained();
        trainDesired = wt.getBasicTrainingDesired();
        trainPaidHere = wt.getBasicTrainingPaidHere();
        updateCheckboxState();
        final Observable.Observer observer = new Observable.Observer() {
            public void onChange() {
                updateCheckboxState();
            }
        };
        disposer.observe(trained, observer);
        disposer.observe(trainPaidHere, observer);
        addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                trainDesired.setValue(event.getValue());
            }
        });
    }

    /**
     * Updates whether the checkbox is checked and/or enabled.
     */
    private void updateCheckboxState() {
        boolean isTrained = trained.getValue();
        this.setValue(isTrained); // FIXME: Should I disable events during this?
        boolean disableCheckbox = isTrained && ! trainPaidHere.getValue();
        this.setEnabled(!disableCheckbox);
    }

    public void dispose() {
        disposer.dispose();
    }
}
