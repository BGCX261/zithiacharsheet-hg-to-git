package com.mcherm.zithiacharsheet.client.model;

import java.util.Iterator;

import com.mcherm.zithiacharsheet.client.modeler.ObservableInt;
import com.mcherm.zithiacharsheet.client.modeler.SummableList;
import com.mcherm.zithiacharsheet.client.modeler.SummableList.Extractor;

/**
 * The full set of StatValues for a character.
 */
public class StatValues implements Iterable<StatValue> {
    
    private final SummableList<StatValue> stats;
    private final StatValue[] statValueArray;
    
    public StatValues() {
        stats = new SummableList<StatValue>(new Extractor<StatValue>() {
            public ObservableInt extractValue(StatValue item) {
                return item.getCost();
            }
        });
        statValueArray = new StatValue[ZithiaStat.getNumStats()];
        for (final ZithiaStat stat : ZithiaStat.values()) {
            StatValue statValue = new StatValue(stat);
            stats.add(statValue);
            statValueArray[stat.ordinal()] = statValue;
        }
    }

    /**
     * Iterate through the stats.
     */
    public Iterator<StatValue> iterator() {
        return stats.iterator();
    }
    
    /**
     * Return the value of a particular stat.
     */
    public StatValue getStat(ZithiaStat stat) {
        return statValueArray[stat.ordinal()];
    }
    
    /**
     * A TweakableValue for the cost of the stats.
     */
    public ObservableInt getCost() {
        return stats.getSum();
    }
    
}
