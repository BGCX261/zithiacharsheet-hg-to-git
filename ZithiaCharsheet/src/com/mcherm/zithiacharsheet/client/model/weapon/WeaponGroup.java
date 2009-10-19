package com.mcherm.zithiacharsheet.client.model.weapon;

/**
 * FIXME: If WeaponGroup winds up having no features which
 * distinguish it from WeaponCluster, then get rid of it
 * and rename WeaponCluster as "WeaponGroup".
 */
public class WeaponGroup extends WeaponCluster {

    public WeaponGroup(WeaponCluster parent, String id, String name, int span) {
        super(parent, id, name, span);
    }
}
