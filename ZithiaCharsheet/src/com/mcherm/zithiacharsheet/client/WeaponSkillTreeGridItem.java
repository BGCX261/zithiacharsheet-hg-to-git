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
import java.util.List;

import com.mcherm.zithiacharsheet.client.TreeGrid.RowEditor;
import com.mcherm.zithiacharsheet.client.TreeGrid.TreeGridItem;
import com.mcherm.zithiacharsheet.client.model.WeaponTraining;
import com.mcherm.zithiacharsheet.client.model.weapon.SingleWeaponSkill;
import com.mcherm.zithiacharsheet.client.modeler.Disposable;
import com.mcherm.zithiacharsheet.client.modeler.Disposer;
import com.mcherm.zithiacharsheet.client.modeler.Observable.Observer;


/**
 * Common parent of the TreeGridItems used by various TreeGrid clases that display
 * things about the Weapon Skill hierarchy.
 */
public abstract class WeaponSkillTreeGridItem implements TreeGridItem, Disposable {
    protected final WeaponTraining wt;
    protected final Disposer disposer = new Disposer();
    protected TreeGrid.RowEditor rowEditor;

    /** Constructor. */
    public WeaponSkillTreeGridItem(WeaponTraining wt) {
        this.wt = wt;
    }

    public void setRowEditor(RowEditor rowEditor) {
        this.rowEditor = rowEditor;
    }

    public boolean isLeaf() {
        return wt.getWeaponSkill() instanceof SingleWeaponSkill;
    }
    
    /** Return a new instance of the concrete subtype with the specified WeaponTraining. */
    public abstract WeaponSkillTreeGridItem newInstance(WeaponTraining wt);

    public Iterable<TreeGridItem> getChildren() {
        List<TreeGridItem> result = new ArrayList<TreeGridItem>();
        for (WeaponTraining childWt : wt.getChildren()) {
            result.add(newInstance(childWt));
        }
        disposer.observe(wt.getChildren(), new Observer() {
            public void onChange() {
                rowEditor.resetChildren();
            }
        });
        return result;
    }

    public void dispose() {
        disposer.dispose();
    }
}
