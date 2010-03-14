/*
 * Copyright 2010 Michael Chermside
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
package com.mcherm.zithiacharsheet.client.model;

import java.util.Arrays;

import com.mcherm.zithiacharsheet.client.modeler.CalculatedIntValue;
import com.mcherm.zithiacharsheet.client.modeler.CalculatedIntValue.ValueCalculator;
import com.mcherm.zithiacharsheet.client.modeler.SettableEnumValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableEnumValueImpl;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;


/**
 * The value within a ZithiaCharacter of the armor they are wearing.
 * <p>
 * DESIGN NOTES: This is rather low in the dependency order because
 * it impacts dex rolls.
 */
public class ArmorValue {
    private final SettableEnumValue<ArmorType> armorType;
    private final TweakableIntValue hpBlock;
    private final TweakableIntValue stunBlock;
    private final TweakableIntValue defPenalty;


    public ArmorValue() {
        armorType = new SettableEnumValueImpl<ArmorType>(ArmorType.class, ArmorType.NONE);
        hpBlock = new CalculatedIntValue<SettableEnumValue<ArmorType>>(
                Arrays.asList(armorType),
                new ValueCalculator<SettableEnumValue<ArmorType>>() {
                    public int calculateValue(Iterable<? extends SettableEnumValue<ArmorType>> inputs) {
                        return armorType.getValue().getHpBlock();
                    }
                }
        );
        stunBlock = new CalculatedIntValue<SettableEnumValue<ArmorType>>(
                Arrays.asList(armorType),
                new ValueCalculator<SettableEnumValue<ArmorType>>() {
                    public int calculateValue(Iterable<? extends SettableEnumValue<ArmorType>> inputs) {
                        return armorType.getValue().getStunBlock();
                    }
                }
        );
        defPenalty = new CalculatedIntValue<SettableEnumValue<ArmorType>>(
                Arrays.asList(armorType),
                new ValueCalculator<SettableEnumValue<ArmorType>>() {
                    public int calculateValue(Iterable<? extends SettableEnumValue<ArmorType>> inputs) {
                        return armorType.getValue().getDefPenalty();
                    }
                }
        );
    }

    public SettableEnumValue<ArmorType> getArmorType() {
        return armorType;
    }

    public TweakableIntValue getHpBlock() {
        return hpBlock;
    }

    public TweakableIntValue getStunBlock() {
        return stunBlock;
    }


    /**
     * This obtains the def penalty of the armor. This is the
     * amount of the penalty IF there were no str modification.
     */
    public TweakableIntValue getDefPenalty() {
        return defPenalty;
    }

    /**
     * Returns true if any value is tweaked or if the armor is anything other
     * than NONE.
     */
    public boolean hasDefaultSettings() {
        return armorType.getValue() == ArmorType.NONE &&
                !hpBlock.isTweaked() &&
                !stunBlock.isTweaked() &&
                !defPenalty.isTweaked();
    }

    /**
     * Sets armor to NONE and removes any tweaks. After calling this hasSettings()
     * will be false.
     */
    public void setDefaultSettings() {
        hpBlock.setAdjustments(0, 0);
        stunBlock.setAdjustments(0, 0);
        defPenalty.setAdjustments(0, 0);
        armorType.setValue(ArmorType.NONE);
    }

}
