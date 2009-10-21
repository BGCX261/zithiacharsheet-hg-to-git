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
    private boolean lineHasOpenBracket;
    
    public JSONSerializer(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        indentLevel = 0;
        lineHasOpenBracket = false;
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
            lineHasOpenBracket = false;
        }
    }
    
    protected void putAttributeEqInline(String name) {
        out.append("\"");
        out.append(name);
        out.append("\":");
    }
    protected void putAttributeEq(String name) {
        indent();
        putAttributeEqInline(name);
    }
    
    protected void putComma() {
        if (!lineHasOpenBracket) {
            out.append(",");
        }
    }
    
    protected void putStartField(String fieldName) {
        putComma();
        putAttributeEq(fieldName);
    }
    
    protected void putStartDict() {
        if (lineHasOpenBracket) {
            indent();
        }
        out.append("{");
        indentLevel++;
        lineHasOpenBracket = true;
    }
    
    protected void putEndDict() {
        indentLevel--;
        indent();
        out.append("}");
    }
    
    protected void putStartList() {
        if (lineHasOpenBracket) {
            indent();
        }
        out.append("[");
        indentLevel++;
        lineHasOpenBracket = true;
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
    
    protected void serialize(String fieldName, TweakableIntValue value) {
        if (value.isTweaked()) {
            putStartField(fieldName);
            Integer override = value.getOverride();
            Integer modifier = value.getModifier();
            out.append("{");
            if (override != null) {
                putAttributeEqInline("override");
                serialize(override.intValue());
            }
            if (modifier != null) {
                putAttributeEqInline("modifier");
                serialize(modifier.intValue());
            }
            out.append("}");
        }
    }
    
    protected void serialize(String fieldName, SettableIntValue value) {
        putStartField(fieldName);
        out.append(value.getValue());
    }
    
    protected void serialize(String fieldName, SettableBooleanValue value) {
        putStartField(fieldName);
        out.append(value.getValue() ? "true" : "false");
    }

    
    protected void serialize(StatValue statValue) {
        putStartDict();
        if (prettyPrint) {
            putAttributeEq("stat");
            serialize(statValue.getStat().getName());
        }
        serialize("value", statValue.getValue());
        serialize("roll", statValue.getRoll());
        serialize("cost", statValue.getCost());
        putEndDict();
    }
    
    protected void serialize(String fieldName, StatValues statValues) {
        putStartField(fieldName);
        putStartList();
        for (StatValue statValue : statValues) {
            putComma();
            serialize(statValue);
        }
        putEndList();
    }
    
    protected void serialize(String fieldName, ZithiaSkill skill) {
        putStartField(fieldName);
        putStartDict();
        putAttributeEq("id");
        serialize(skill.getId());
        putEndDict();
    }
    
    protected void serialize(SkillValue skillValue) {
        putStartDict();
        serialize("skill", skillValue.getSkill());
        serialize("levels", skillValue.getLevels());
        serialize("roll", skillValue.getRoll());
        serialize("cost", skillValue.getCost());
        putEndDict();
    }
    
    
    protected void serialize(String fieldName, SkillList skillList) {
        putStartField(fieldName);
        putStartList();
        for (SkillValue skillValue : skillList) {
            putComma();
            serialize(skillValue);
        }
        putEndList();
    }
    
    protected void serialize(String fieldName, WeaponSkill weaponSkill) {
        putStartField(fieldName);
        putStartDict();
        putAttributeEq("id");
        serialize(weaponSkill.getId());
        putEndDict();
    }
    
    protected void serialize(String fieldName, WeaponTraining wt) {
        putStartField(fieldName);
        serialize(wt);
    }
    
    protected void serialize(WeaponTraining wt) {
        putStartDict();
        serialize("weaponSkill", wt.getWeaponSkill());
        serialize("basicTrainingPurchased", wt.getBasicTrainingPurchased());
        serialize("levelsPurchased", wt.getLevelsPurchased());
        serialize("levels", wt.getLevels());
        serialize("thisCost", wt.getThisCost());
        serialize("totalCost", wt.getTotalCost());
        if (wt.hasChildren()) {
            putStartField("children");
            putStartList();
            for (WeaponTraining child : wt.getChildren()) {
                putComma();
                serialize(child);
            }
            putEndList();
        }
        putEndDict();
    }
    
    protected void serialize(String fieldName, ZithiaCosts zithiaCosts) {
        putStartField(fieldName);
        putStartDict();
        serialize("statCost", zithiaCosts.getStatCost());
        serialize("skillCost", zithiaCosts.getSkillCost());
        serialize("weaponSkillCost", zithiaCosts.getWeaponSkillCost());
        serialize("totalCost", zithiaCosts.getTotalCost());
        putEndDict();
    }
    
    public void serialize(ZithiaCharacter zithiaCharacter) {
        putStartDict();
        serialize("statValues", zithiaCharacter.getStatValues());
        serialize("skillList", zithiaCharacter.getSkillList());
        serialize("weaponTraining", zithiaCharacter.getWeaponTraining());
        serialize("costs", zithiaCharacter.getCosts());
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
        character.getStat(ZithiaStat.OBS).getValue().setValue(16);
        SkillValue skillValue = character.addNewSkill(skillCatalog.getSkill("flowers"));
        skillValue.getLevels().setValue(3);
        final WeaponTraining allCombatTraining = character.getWeaponTraining();
        // Buy 1 level with each tier-2 skill
        for (WeaponSkill weaponSkill : weaponsCatalog.getChildren(((WeaponClusterSkill) allCombatTraining.getWeaponSkill()))) {
            WeaponTraining newWT = allCombatTraining.createChild(weaponSkill);
            newWT.getLevelsPurchased().setValue(1);
        }
        character.getCosts().getWeaponSkillCost().setAdjustments(0, null);
        character.getCosts().getTotalCost().setAdjustments(null, -10);
        jss.serialize(character);
        String output = jss.output();
        System.out.println(output);
    }
}
