/*
 * Copyright 2009 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.mcherm.zithiacharsheet.client.model;

import com.mcherm.zithiacharsheet.client.model.SkillCatalog.SkillCategory;


/**
 * This consists of some specialized Java (an embedded DSL would be the proper
 * term) for defining certain data in the game such as the content for the
 * SkillCatalog and WeaponsCatalog.
 */
public class GameData {
    
    public static SkillCategory[] getSkillData() {
        return new SkillCategory[] {
            new SkillCategory("Arts Skills",
                new ZithiaSkill("acting", ZithiaStat.PRE, 4, 3, "Acting"),
                new ZithiaSkill("bard", ZithiaStat.INT, 6, 3, "Bard"),
                new ZithiaSkill("calligraphy", ZithiaStat.DEX, 3, 1, "Calligraphy"),
                new ZithiaSkill("dance", ZithiaStat.DEX, 3, 2, "Dance"),
                new ZithiaSkill("choreography", ZithiaStat.INT, 3, 2, "Dance Choreography"),
                new ZithiaSkill("drawing", ZithiaStat.DEX, 3, 2, "Drawing"),
                new ZithiaSkill("flowers", ZithiaStat.OBS, 3, 1, "Flower Arranging"),
                new ZithiaSkill("landscaping", ZithiaStat.OBS, 3, 2, "Landscaping"),
                new ZithiaSkill("musiccomposition", ZithiaStat.INT, 3, 3, "Music, Composition"),
                new ZithiaSkill("musicgeneral", ZithiaStat.PRE, 3, 2, "Music, General"),
                new ZithiaSkill("musicinstrument", ZithiaStat.DEX, 3, 2, "Music, Instrument"),
                new ZithiaSkill("oratory", ZithiaStat.PRE, 3, 2, "Oratory"),
                new ZithiaSkill("origami", ZithiaStat.PRE, 3, 2, "Origami"),
                new ZithiaSkill("painting", ZithiaStat.DEX, 3, 2, "Painting"),
                new ZithiaSkill("epicpoetry", ZithiaStat.INT, 3, 2, "Poetry, Epic"),
                new ZithiaSkill("lyricpoetry", ZithiaStat.INT, 3, 2, "Poetry, Lyric"),
                new ZithiaSkill("sculpting", ZithiaStat.DEX, 3, 3, "Sculpting"),
                new ZithiaSkill("singing", ZithiaStat.PRE, 3, 1, "Singing"),
                new ZithiaSkill("writingfiction", ZithiaStat.INT, 3, 2, "Writing, Fiction"),
                new ZithiaSkill("writinghistorical", ZithiaStat.INT, 3, 2, "Writing, Historical"),
                new ZithiaSkill("writingjokes", ZithiaStat.PRE, 2, 2, "Writing, Jokes"),
                new ZithiaSkill("writingpersuasive", ZithiaStat.PRE, 3, 2, "Writing, Persuasive")
            ),
            new SkillCategory("Athletic Skills",
                new ZithiaSkill("acrobatics", ZithiaStat.DEX, 5, 3, "Acrobatics"),
                new ZithiaSkill("breakfall", ZithiaStat.DEX, 3, 2, "Breakfall"),
                new ZithiaSkill("breathcontrol", ZithiaStat.CON, 6, 2, "Breath Control"),
                new ZithiaSkill("climbing", ZithiaStat.DEX, 0, 2, "Climbing"),
                new ZithiaSkill("diving", ZithiaStat.DEX, 1, 1, "Diving"),
                new ZithiaSkill("drinking", ZithiaStat.CON, 1, 1, "Drinking"),
                new ZithiaSkill("juggling", ZithiaStat.DEX, 3, 2, "Juggling"),
                new ZithiaSkill("jumping", ZithiaStat.STR, 0, 2, "Jumping"),
                new ZithiaSkill("running", ZithiaStat.CON, 2, 1, "Running, Distance"),
                new ZithiaSkill("sprinting", ZithiaStat.STR, 3, 1, "Running, Sprinting"),
                new ZithiaSkill("skiing", ZithiaStat.DEX, 4, 2, "Skiing"),
                new ZithiaSkill("sleeponthego", null, 5, 0, "Sleep on the Go"),
                new ZithiaSkill("sport", ZithiaStat.DEX, 1, 2, "Sport, Specific"),
                new ZithiaSkill("swimming", null, 4, 0, "Swimming")
            ),
            new SkillCategory("Combat Skills",
                new ZithiaSkill("ambidexterity", null, 6, 0, "Ambidexterity"),
                new ZithiaSkill("blindfighting", ZithiaStat.OBS, 4, 2, "Blind Fighting"),
                new ZithiaSkill("charioteering", ZithiaStat.DEX, 4, 2, "Charioteering"),
                new ZithiaSkill("disarm", ZithiaStat.STR, 3, 3, "Disarm"),
                new ZithiaSkill("horsebackcombat", ZithiaStat.DEX, 4, 2, "Horseback Combat"),
                new ZithiaSkill("horsebackarchery", ZithiaStat.DEX, 4, 2, "Horseback Archery"),
                new ZithiaSkill("pronefighting", ZithiaStat.DEX, 3, 1, "Prone Fighting"),
                new ZithiaSkill("quickdraw", ZithiaStat.DEX, 3, 1, "Quick Draw"),
                new ZithiaSkill("surpriseattack", ZithiaStat.DEX, 4, 3, "Suprise Attack"),
                new ZithiaSkill("targetshooting", null, 5, 0, "Target Shooting"),
                new ZithiaSkill("twoweaponcombat", null, 8, 0, "Two Weapon Combat"),
                new ZithiaSkill("underwatercombat", null, 3, 0, "Underwater Combat")
            ),
            new SkillCategory("Craftsman Skills",
                new ZithiaSkill("animaltrainer", ZithiaStat.PRE, 5, 3, "Animal Trainer"),
                new ZithiaSkill("specificanimaltrainer", ZithiaStat.PRE, 2, 2, "Animal Trainer, Specific"),
                new ZithiaSkill("armorer", ZithiaStat.STR, 5, 2, "Armorer"),
                new ZithiaSkill("blacksmith", ZithiaStat.STR, 3, 2, "Blacksmith"),
                new ZithiaSkill("boatmaker", ZithiaStat.INT, 3, 2, "Boatwright"),
                new ZithiaSkill("bower", ZithiaStat.DEX, 5, 2, "Bowyer/Fletcher"),
                new ZithiaSkill("brewer", ZithiaStat.INT, 3, 2, "Brewer"),
                new ZithiaSkill("carpentry", ZithiaStat.STR, 3, 2, "Carpentry"),
                new ZithiaSkill("cleaning", ZithiaStat.WILL, 1, 1, "Cleaning"),
                new ZithiaSkill("clockmaker", ZithiaStat.INT, 5, 3, "Clockmaker"),
                new ZithiaSkill("cobbler", ZithiaStat.DEX, 3, 1, "Cobbler"),
                new ZithiaSkill("cooking", ZithiaStat.INT, 2, 1, "Cooking"),
                new ZithiaSkill("falconry", ZithiaStat.PRE, 2, 2, "Falconry"),
                new ZithiaSkill("gemcutter", ZithiaStat.DEX, 3, 3, "Gem Cutter"),
                new ZithiaSkill("jeweler", ZithiaStat.DEX, 3, 2, "Jeweler"),
                new ZithiaSkill("leatherworker", ZithiaStat.INT, 2, 1, "Leather Worker"),
                new ZithiaSkill("locksmith", ZithiaStat.DEX, 3, 2, "Locksmith"),
                new ZithiaSkill("papermaker", ZithiaStat.INT, 3, 2, "Paper Maker"),
                new ZithiaSkill("potter", ZithiaStat.DEX, 3, 2, "Potter"),
                new ZithiaSkill("scribe", ZithiaStat.INT, 2, 2, "Scribe"),
                new ZithiaSkill("shipbuilding", ZithiaStat.INT, 4, 3, "Shipbuilding"),
                new ZithiaSkill("silkmaker", ZithiaStat.INT, 5, 2, "Silk Maker"),
                new ZithiaSkill("smelter", ZithiaStat.INT, 3, 2, "Smelter"),
                new ZithiaSkill("stonemason", ZithiaStat.STR, 3, 2, "Stonemason"),
                new ZithiaSkill("tailor", ZithiaStat.OBS, 3, 2, "Tailor"),
                new ZithiaSkill("weaponsmith", ZithiaStat.STR, 5, 2, "Weaponsmith"),
                new ZithiaSkill("weaver", ZithiaStat.INT, 4, 2, "Weaver"),
                new ZithiaSkill("woodworker", ZithiaStat.DEX, 3, 2, "Woodworker")
            ),
            new SkillCategory("Thief/Spy Skills",
                new ZithiaSkill("stealth", ZithiaStat.DEX, 0, 2, "Stealth")
            ),
        };
    }

}
