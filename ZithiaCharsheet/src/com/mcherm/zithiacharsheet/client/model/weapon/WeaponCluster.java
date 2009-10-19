package com.mcherm.zithiacharsheet.client.model.weapon;

/**
 * A WeaponCluster is any grouping of weapons, such as a WeaponGroup
 * or all melee weapons, etc. Instances are immutable. All WeaponCluster
 * instances have a parent instance EXCEPT for the AllCombatWeaponCluster
 * which has null as a parent. They also all have a span, which is an
 * integer:
 * <dl>
 *   <dt>4</dt>
 *   <dd>All Combat</dd>
 *   <dt>3</dt>
 *   <dd>Melee or Ranged</dd>
 *   <dt>2</dt>
 *   <dd>Weapon Group</dd>
 *   <dt>1</dt>
 *   <dd>Single Weapon</dd>
 * </dl>
 */
public class WeaponCluster {
    private final WeaponCluster parent;
    private final String id;
    private final String name;
    private final int span;
    
    public WeaponCluster(WeaponCluster parent, String id, String name, int span) {
        assert parent != null || span == 4;
        assert parent == null || span + 1 == parent.getSpan();
        this.parent = parent;
        this.id = id;
        this.name = name;
        this.span = span;
    }
    
    public WeaponCluster getParent() {
        return parent;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getSpan() {
        return span;
    }
    
}
