/*
 * Copyright 2009 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.mcherm.zithiacharsheet.client.modeler;

/**
 * An integer value appearing in a charsheet which is normally calculated, but can
 * be manually tweaked.
 */
public interface TweakableIntValue extends ObservableInt {
    
    /**
     * Returns true if there is either an override or a modifier;
     * returns false if there is neither.
     */
    public boolean isTweaked();
    
    /**
     * Returns the current override value, or null if there is no override
     * value.
     */
    public Integer getOverride();
    
    /**
     * Returns the current modifier value, or null if there is no modifier
     * value.
     */
    public Integer getModifier();
    
    /**
     * Sets the override or modifier. Override is a value which will
     * always be returned regardless of the default calculation.
     * Modifier is a value which will be added to the default
     * calculation. Either may be null to indicate that is not used.
     * It is an error to set both at once: at least one of the arguments
     * must be null;
     * 
     * @param override the value to use as an override, or null to
     *   indicate that there should not be any override.
     * @param modifier the value to use as a modifier, or null to
     *   indicate that there should not be any modifier.
     */
    public void setAdjustments(Integer override, Integer modifier);
    
}
