package com.mcherm.zithiacharsheet.client.model;

/**
 * Some utility functions used by the model.
 */
public final class Util {
    
    /** Static functions only; do not create instances. */
    private Util() {}
    
    
    /**
     * Contains the formula for my standard behavior: costs a
     * base amount, and a premium per level, and that premium
     * rises every 2 levels.
     * 
     * @param baseCost
     * @param firstLevelCost
     * @param levels
     * @return the cost of levels levels of a skill which has a
     *   base cost of baseCost and a first level cost of firstLevelCost.
     */
    public final static int skillCost(int baseCost, int firstLevelCost, int levels) {
        if (levels < 0) {
            throw new RuntimeException("Cannot have negative skill levels.");
        } else if (levels == 0) {
            return baseCost;
        } else {
            // FIXME: This math formula can be simplified enormously. Do that sometime.
            int overTwo = (levels - 1) / 2;
            int major = overTwo * (overTwo + 1);
            int extra = ((levels + 1) / 2) - 1;
            int pct2 = levels % 2;
            int stuff = major - extra * pct2;
            return baseCost + levels * firstLevelCost + stuff;
        }
    }

}
