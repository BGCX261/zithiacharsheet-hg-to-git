package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.model.weapon.WeaponClusterSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponSkill;
import com.mcherm.zithiacharsheet.client.model.weapon.WeaponsCatalog;
import com.mcherm.zithiacharsheet.client.modeler.SettableBooleanValue;
import com.mcherm.zithiacharsheet.client.modeler.SettableIntValue;
import com.mcherm.zithiacharsheet.client.modeler.TweakableIntValue;

/**
 * FIXME: Document this once it works.
 */
public class JSONSerializer {
    
    private final boolean prettyPrint;
    private final StringBuilder out = new StringBuilder();
    private int indentLevel;
    private boolean lineHasBracket;
    
    public JSONSerializer(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        indentLevel = 0;
        lineHasBracket = false;
    }
    
    public String output() {
        return out.toString();
    }
    
    protected void indent() {
        if (prettyPrint) {
            out.append("\n");
            for (int i=0; i<indentLevel; i++) {
                out.append("  ");
            }
            lineHasBracket = false;
        }
    }
    
    protected void putAttributeEqInline(String name) {
        out.append("\"");
        out.append(name);
        out.append("\"=");
    }
    protected void putAttributeEq(String name) {
        indent();
        putAttributeEqInline(name);
    }
    
    protected void putComma() {
        out.append(",");
    }
    
    protected void putStartDict() {
        if (lineHasBracket) {
            indent();
        }
        out.append("{");
        indentLevel++;
        lineHasBracket = true;
    }
    
    protected void putEndDict() {
        indentLevel--;
        indent();
        out.append("}");
    }
    
    protected void putStartList() {
        if (lineHasBracket) {
            indent();
        }
        out.append("[");
        indentLevel++;
        lineHasBracket = true;
    }
    
    protected void putEndList() {
        indentLevel--;
        indent();
        out.append("]");
    }
    
    
    protected void serialize(String string) {
        out.append("\"");
        out.append(string); // FIXME: Need escaping of double-quotes and newlines!
        out.append("\"");
    }
    
    protected void serialize(int i) {
        out.append(i);
    }
    
    protected void serialize(TweakableIntValue value) {
        out.append("{}");
    }
    
    protected void serialize(SettableIntValue value) {
        out.append(value.getValue());
    }
    
    protected void serialize(SettableBooleanValue value) {
        out.append(value.getValue() ? "true" : "false");
    }

    
    protected void serialize(StatValue statValue) {
        putStartDict();
        if (prettyPrint) {
            putAttributeEq("stat");
            serialize(statValue.getStat().getName());
            putComma();
        }
        putAttributeEq("value");
        serialize(statValue.getValue());
        putComma();
        putAttributeEq("roll");
        serialize(statValue.getRoll());
        putComma();
        putAttributeEq("cost");
        serialize(statValue.getCost());
        putEndDict();
    }
    
    protected void serialize(StatValues statValues) {
        putStartList();
        boolean firstTimeInLoop = true;
        for (StatValue statValue : statValues) {
            if (firstTimeInLoop) {
                firstTimeInLoop = false;
            } else {
                putComma();
            }
            serialize(statValue);
        }
        putEndList();
    }
    
    protected void serialize(ZithiaSkill skill) {
        putStartDict();
        putAttributeEq("id");
        serialize(skill.getId());
        putEndDict();
    }
    
    protected void serialize(SkillValue skillValue) {
        putStartDict();
        putAttributeEq("skill");
        serialize(skillValue.getSkill());
        putComma();
        putAttributeEq("levels");
        serialize(skillValue.getLevels());
        putComma();
        putAttributeEq("roll");
        serialize(skillValue.getRoll());
        putComma();
        putAttributeEq("cost");
        serialize(skillValue.getCost());
        putEndDict();
    }
    
    
    protected void serialize(SkillList skillList) {
        putStartList();
        boolean firstTimeInLoop = true;
        for (SkillValue skillValue : skillList) {
            if (firstTimeInLoop) {
                firstTimeInLoop = false;
            } else {
                putComma();
            }
            serialize(skillValue);
        }
        putEndList();
    }
    
    protected void serialize(WeaponSkill weaponSkill) {
        putStartDict();
        putAttributeEq("id");
        serialize(weaponSkill.getId());
        putEndDict();
    }
    
    protected void serialize(WeaponTraining wt) {
        putStartDict();
        putAttributeEq("weaponSkill");
        serialize(wt.getWeaponSkill());
        putComma();
        putAttributeEq("basicTrainingPurchased");
        serialize(wt.getBasicTrainingPurchased());
        putComma();
        putAttributeEq("levelsPurchased");
        serialize(wt.getLevelsPurchased());
        putComma();
        putAttributeEq("levels");
        serialize(wt.getLevels());
        putComma();
        putAttributeEq("thisCost");
        serialize(wt.getThisCost());
        putComma();
        putAttributeEq("totalCost");
        serialize(wt.getTotalCost());
        putComma();
        // FIXME: And more fields here
        if (wt.hasChildren()) {
            putAttributeEq("children");
            putStartList();
            boolean firstTimeInLoop = true;
            for (WeaponTraining child : wt.getChildren()) {
                if (firstTimeInLoop) {
                    firstTimeInLoop = false;
                } else {
                    putComma();
                }
                serialize(child);
            }
            putEndList();
        }
        putEndDict();
    }
    
    public void serialize(ZithiaCosts zithiaCosts) {
/*
    private final TweakableIntValue statCost;
    private final TweakableIntValue skillCost;
    private final TweakableIntValue weaponSkillCost;
    private final TweakableIntValue totalCost;
 */
        putStartDict();
        putAttributeEq("statCost");
        serialize(zithiaCosts.getStatCost());
        putComma();
        putAttributeEq("skillCost");
        serialize(zithiaCosts.getSkillCost());
        putComma();
        putAttributeEq("weaponSkillCost");
        serialize(zithiaCosts.getWeaponSkillCost());
        putComma();
        putAttributeEq("totalCost");
        serialize(zithiaCosts.getTotalCost());
        putEndDict();
    }
    
    public void serialize(ZithiaCharacter zithiaCharacter) {
        putStartDict();
        putAttributeEq("statValues");
        serialize(zithiaCharacter.getStatValues());
        putComma();
        putAttributeEq("skillList");
        serialize(zithiaCharacter.getSkillList());
        putComma();
        putAttributeEq("weaponTraining");
        serialize(zithiaCharacter.getWeaponTraining());
        putComma();
        putAttributeEq("costs");
        serialize(zithiaCharacter.getCosts());
        putEndDict();
    }
    
    
    /**
     * Just some tests of JSONSerializer. I can't use tests normally.
     */
    public static void main(String[] args) {
        final SkillCatalog skillCatalog = SkillCatalog.getSingleton();
        final WeaponsCatalog weaponsCatalog = WeaponsCatalog.getSingleton();
        final JSONSerializer jss = new JSONSerializer(true);
        ZithiaCharacter character = new ZithiaCharacter();
        SkillValue skillValue = character.addNewSkill(skillCatalog.getSkill("flowers"));
        skillValue.getLevels().setValue(3);
        final WeaponTraining allCombatTraining = character.getWeaponTraining();
        // Buy 1 level with each tier-2 skill
        for (WeaponSkill weaponSkill : weaponsCatalog.getChildren(((WeaponClusterSkill) allCombatTraining.getWeaponSkill()))) {
            WeaponTraining newWT = allCombatTraining.createChild(weaponSkill);
            newWT.getLevelsPurchased().setValue(1);
        }
        jss.serialize(character);
        String output = jss.output();
        System.out.println(output);
    }
}
