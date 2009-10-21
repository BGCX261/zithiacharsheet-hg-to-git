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
