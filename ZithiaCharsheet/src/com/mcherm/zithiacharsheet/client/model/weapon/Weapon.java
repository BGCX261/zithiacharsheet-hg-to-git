package com.mcherm.zithiacharsheet.client.model.weapon;

/**
 * A Weapon is a single kind of damage-dealing entity. An example would
 * be a Broadsword. Instances are immutable.
 */
public class Weapon {
    private final WeaponGroup weaponGroup;
    private final String name;
    private final int strMin;
    private final int spd;
    // FIXME: Also has damage, but I have to figure out how that is stored first.
    
    /**
     * Constructor.
     */
    public Weapon(WeaponGroup weaponGroup, String name, int strMin, int spd) {
        this.weaponGroup = weaponGroup;
        this.name = name;
        this.strMin = strMin;
        this.spd = spd;
    }
    
    public WeaponGroup getWeaponGroup() {
        return weaponGroup;
    }

    public String getName() {
        return name;
    }
    
    public int getStrMin() {
        return strMin;
    }
    
    public int getSpd() {
        return spd;
    }
    
}
