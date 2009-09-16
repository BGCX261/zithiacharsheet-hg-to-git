package com.mcherm.zithiacharsheet.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The master list of well-known skills. It uses the singleton pattern.
 */
public class SkillCatalog {
    
    private static final SkillCatalog singletonInstance = new SkillCatalog();
    
    public static SkillCatalog getSingleton() {
        return singletonInstance;
    }
    
    public static ZithiaSkill get(String id) {
        ZithiaSkill result = getSingleton().getSkill(id);
        if (result == null) {
            throw new RuntimeException("Skill '" + id + "' not found in catelog.");
        } else {
            return result;
        }
    }
    
    private final Map<String,ZithiaSkill> skillMap;
    private final List<SkillCategory> skillCategories;

    
    
    /**
     * Constructor. For now it initializes the data.
     */
    @SuppressWarnings("serial")
    private SkillCatalog() {
        String[][] skillData = getData();
        
        Map<String,ZithiaSkill> skillMap = new HashMap<String,ZithiaSkill>();
        List<SkillCategory> skillCategories = new ArrayList<SkillCategory>();

        final Map<String,ZithiaStat> STAT_MAP = new HashMap<String,ZithiaStat>() { {
            for (ZithiaStat stat : ZithiaStat.values()) {
                put(stat.getName(), stat);
            }
        } };
        
        String currentCategoryName = null;
        List<ZithiaSkill> currentCategorySkills = null;
        int lineNum = 0;
        for (String[] skillDataRow : skillData) {
            lineNum++;
            if (skillDataRow.length == 1) {
                if (currentCategorySkills != null) {
                    skillCategories.add(new SkillCategory(currentCategoryName, currentCategorySkills));
                }
                currentCategoryName = skillDataRow[0];
                currentCategorySkills = new ArrayList<ZithiaSkill>();
            } else if (skillDataRow.length == 5) {
                if (currentCategorySkills == null) {
                    throw new RuntimeException("No category defined on line " + lineNum);
                }
                final String id = skillDataRow[0];
                if (skillMap.containsKey(id)) {
                    throw new RuntimeException("Duplicate skill id on line " + lineNum);
                }
                final String statKey = skillDataRow[1];
                final ZithiaStat stat = STAT_MAP.get(statKey);
                if (stat == null && statKey != "None") {
                    throw new RuntimeException("Syntax error in line " + lineNum);
                }
                final boolean hasRoll = stat != null;
                final int baseCost, firstLevelCost;
                try {
                    baseCost = Integer.parseInt(skillDataRow[2]);
                    firstLevelCost = Integer.parseInt(skillDataRow[3]);
                } catch(NumberFormatException err) {
                    throw new RuntimeException("Syntax error on line "  + lineNum);
                }
                final String name = skillDataRow[4];
                final ZithiaSkill newSkill = new ZithiaSkill(stat, hasRoll, baseCost, firstLevelCost, name);
                skillMap.put(id, newSkill);
                currentCategorySkills.add(newSkill);
            } else {
                throw new RuntimeException("Syntax error on line " + lineNum);
            }
        }
        if (currentCategorySkills != null) {
            skillCategories.add(new SkillCategory(currentCategoryName, currentCategorySkills));
        }
        this.skillMap = Collections.unmodifiableMap(skillMap);
        this.skillCategories = Collections.unmodifiableList(skillCategories);
    }
    
    /**
     * Return a skill by its id (a short, unique, well-formed version of the
     * skill name).
     * 
     * @param id the id of the skill to find
     * @return the skill object or null if not found
     */
    public ZithiaSkill getSkill(String id) {
        return skillMap.get(id);
    }

    
    /**
     * Skills are grouped into categories. The skill category simply has a name and a list
     * of skills.
     */
    public static class SkillCategory {
        private final String name;
        private final List<ZithiaSkill> skills;
        public SkillCategory(String name, List<ZithiaSkill> skills) {
            this.name = name;
            this.skills = Collections.unmodifiableList(skills);
        }
        public String getName() {
            return name;
        }
        public List<ZithiaSkill> getSkills() {
            return skills;
        }
    }
    
    public List<SkillCategory> getSkillCategories() {
        return skillCategories;
    }
    
    private String[][] getData() {
        return new String[][] {
                {"Arts Skills"},
                {"acting", "Pre", "4", "3", "Acting"},
                {"bard", "Int", "6", "3", "Bard"},
                {"calligraphy", "Dex", "3", "1", "Calligraphy"},
                {"dance", "Dex", "3", "2", "Dance"},
                {"choreography", "Int", "3", "2", "Dance Choreography"},
                {"drawing", "Dex", "3", "2", "Drawing"},
                {"flowers", "Obs", "3", "1", "Flower Arranging"},
                {"landscaping", "Obs", "3", "2", "Landscaping"},
                {"musiccomposition", "Int", "3", "3", "Music, Composition"},
                {"musicgeneral", "Pre", "3", "2", "Music, General"},
                {"musicinstrument", "Dex", "3", "2", "Music, Instrument"},
                {"oratory", "Pre", "3", "2", "Oratory"},
                {"origami", "Pre", "3", "2", "Origami"},
                {"painting", "Dex", "3", "2", "Painting"},
                {"epicpoetry", "Int", "3", "2", "Poetry, Epic"},
                {"lyricpoetry", "Int", "3", "2", "Poetry, Lyric"},
                {"sculpting", "Dex", "3", "3", "Sculpting"},
                {"singing", "Pre", "3", "1", "Singing"},
                {"writingfiction", "Int", "3", "2", "Writing, Fiction"},
                {"writinghistorical", "Int", "3", "2", "Writing, Historical"},
                {"writingjokes", "Pre", "2", "2", "Writing, Jokes"},
                {"writingpersuasive", "Pre", "3", "2", "Writing, Persuasive"},
                {"Athletic Skills"},
                {"acrobatics", "Dex", "5", "3", "Acrobatics"},
                {"breakfall", "Dex", "3", "2", "Breakfall"},
                {"breathcontrol", "Con", "6", "2", "Breath Control"},
                {"climbing", "Dex", "0", "2", "Climbing"},
                {"diving", "Dex", "1", "1", "Diving"},
                {"drinking", "Con", "1", "1", "Drinking"},
                {"juggling", "Dex", "3", "2", "Juggling"},
                {"jumping", "Str", "0", "2", "Jumping"},
                {"running", "Con", "2", "1", "Running, Distance"},
                {"sprinting", "Str", "3", "1", "Running, Sprinting"},
                {"skiing", "Dex", "4", "2", "Skiing"},
                {"sleeponthego", "None", "5", "0", "Sleep on the Go"},
                {"sport", "Dex", "1", "2", "Sport, Specific"},
                {"swimming", "None", "4", "0", "Swimming"},
                {"Combat Skills"},
                {"ambidexterity", "None", "6", "0", "Ambidexterity"},
                {"blindfighting", "Obs", "4", "2", "Blind Fighting"},
                {"charioteering", "Dex", "4", "2", "Charioteering"},
                {"disarm", "Str", "3", "3", "Disarm"},
                {"horsebackcombat", "Dex", "4", "2", "Horseback Combat"},
                {"horsebackarchery", "Des", "4", "2", "Horseback Archery"},
                {"pronefighting", "Dex", "3", "1", "Prone Fighting"},
                {"quickdraw", "Dex", "3", "1", "Quick Draw"},
                {"surpriseattack", "Dex", "4", "3", "Suprise Attack"},
                {"targetshooting", "None", "5", "0", "Target Shooting"},
                {"twoweaponcombat", "None", "8", "0", "Two Weapon Combat"},
                {"underwatercombat", "None", "3", "0", "Underwater Combat"},
                {"Thief/Spy Skills"},
                {"stealth", "Dex", "0", "2", "Stealth"},
        };
    }

}
