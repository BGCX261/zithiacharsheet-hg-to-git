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

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TreeImages;
import com.mcherm.zithiacharsheet.client.model.WeaponTraining;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.modeler.Disposable;
import com.mcherm.zithiacharsheet.client.modeler.Disposer;
import com.mcherm.zithiacharsheet.client.modeler.Observable;
import com.mcherm.zithiacharsheet.client.modeler.ObservableBoolean;
import com.mcherm.zithiacharsheet.client.modeler.SettableBooleanValue;


/**
 * A TreeGrid table for displaying and editing the spending on weapon
 * skills.
 */
public class WeaponCostTreeGrid extends TreeGrid {

    private final static int NUM_COLUMNS = 4;

    /** Constructor. */
    public WeaponCostTreeGrid(ZithiaCharacter zithiaCharacter) {
        super(new WeaponCostTreeGridItem(zithiaCharacter.getWeaponTraining()),
                NUM_COLUMNS, GWT.<TreeImages> create(TreeImages.class));
    }

    /**
     * Display a header giving the meaning of the different columns.
     */
    protected List<WidgetOrText> getHeader() {
        return Arrays.asList(
                new WidgetOrText(""),
                new WidgetOrText("Levels"),
                new WidgetOrText("Train"),
                new WidgetOrText("Cost")
        );
    }

    /** Contents to display in each row of this table. */
    private static class WeaponCostTreeGridItem extends WeaponSkillTreeGridItem {

        /** Constructor. */
        public WeaponCostTreeGridItem(WeaponTraining wt) {
            super(wt);
        }

        public WeaponSkillTreeGridItem newInstance(WeaponTraining wt) {
            return new WeaponCostTreeGridItem(wt);
        }

        public List<WidgetOrText> getContents() {
            return Arrays.asList(
                new WidgetOrText(wt.getWeaponSkill().getName()),
                new WidgetOrText(disposer.track(new SettableIntField(wt.getLevelsPurchased()))),
                new WidgetOrText(disposer.track(new TrainingEntryField(wt))),
                new WidgetOrText(disposer.track(new TweakableIntField(wt.getThisCost())))
            );
        }

    }


    /**
     * A field for viewing the training status. Has three possible values:
     * unchecked (is not trained), checked (paid to train in this weapon skill),
     * and checked-and-disabled (inherited training from a parent weapon skill).
     */
    private static class TrainingEntryField extends CheckBox implements Disposable {
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

}
