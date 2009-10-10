package com.mcherm.zithiacharsheet.client.model;

/**
 * An actual instance of a skill in a particular individual.
 * So, for instance, this might be Rogan Harsha's Stealth.
 */
public class SkillValue implements Observable {

    private final ZithiaSkill skill;
    private final StatValue statValue; // the stat value or null if there is no stat
    private final ObservableIntValue levels;
    
    /**
     * Constructor.
     * 
     * @param skill the skill that will be created
     * @param zithiaCharacter the character it will be created for. This MAY be
     *   only partially initialized, but the stats and race must have been initialized
     *   already.
     */
    public SkillValue(ZithiaSkill skill, ZithiaCharacter zithiaCharacter) {
        this.skill = skill;
        ZithiaStat stat = skill.getStat();
        if (stat == null) {
            statValue = null;
        } else {
            statValue = zithiaCharacter.getStat(stat);
        }
        this.levels = new ObservableIntValue(0);
    }
    
    public int getCost() {
        return skill.getCost(levels.getValue());
    }
    
    public int getRoll() {
        return skill.getRoll(levels.getValue(), statValue.getValue());
    }
    
    public int getLevels() {
        return levels.getValue();
    }
    
    public void setLevels(int newLevels) {
        levels.setValue(newLevels);
    }
    
    public ZithiaSkill getSkill() {
        return skill;
    }

    public void addObserver(Observable.Observer observer) {
        levels.addObserver(observer);
        if (statValue != null) {
            statValue.addObserver(observer);
        }
    }
    
}
