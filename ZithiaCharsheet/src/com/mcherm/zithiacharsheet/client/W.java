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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TreeImages;
import com.mcherm.zithiacharsheet.client.model.WeaponTraining;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.weapon.SingleWeaponSkill;
import com.mcherm.zithiacharsheet.client.modeler.Observable;
import com.mcherm.zithiacharsheet.client.modeler.ObservableBoolean;
import com.mcherm.zithiacharsheet.client.modeler.SettableBooleanValue;


/**
 * A TreeGrid table for displaying and editing the spending on weapon
 * skills.
 */
// FIXME: Really named 'WeaponCostTreeGrid' -- PATHNAME ISSUES!
public class W extends TreeGrid {

    private final static int NUM_COLUMNS = 4;

    /** Constructor. */
    public W(ZithiaCharacter zithiaCharacter) {
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
    private static class WeaponCostTreeGridItem implements TreeGridItem {
        private final WeaponTraining wt;

        /** Constructor. */
        public WeaponCostTreeGridItem(WeaponTraining wt) {
            this.wt = wt;
        }

        public List<WidgetOrText> getContents() {
            return Arrays.asList(
                new WidgetOrText(wt.getWeaponSkill().getName()),
                new WidgetOrText(new SettableIntField(wt.getLevelsPurchased())),
                new WidgetOrText(new TrainingEntryField(wt)),
                new WidgetOrText(new TweakableIntField(wt.getThisCost()))
            );
        }

        public boolean isLeaf() {
            return wt.getWeaponSkill() instanceof SingleWeaponSkill;
        }

        public Iterable<TreeGridItem> getChildren() {
            List<TreeGridItem> result = new ArrayList<TreeGridItem>();
            for (WeaponTraining childWt : wt.getChildren()) {
                result.add(new WeaponCostTreeGridItem(childWt));
            }
            return result;
        }
    }

    private static class TrainingEntryField extends CheckBox {
        private final ObservableBoolean trained;
        private final SettableBooleanValue trainDesired;
        private final ObservableBoolean trainPaidHere;

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
            trained.addObserver(observer);
            trainPaidHere.addObserver(observer);
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

    }

}
