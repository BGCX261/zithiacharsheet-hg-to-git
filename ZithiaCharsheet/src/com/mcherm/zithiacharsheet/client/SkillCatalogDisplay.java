package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.mcherm.zithiacharsheet.client.model.SkillCatalog;
import com.mcherm.zithiacharsheet.client.model.ZithiaSkill;


/**
 * A graphical element that displays the skill catalog.
 */
public class SkillCatalogDisplay extends FlexTable {
    
    public SkillCatalogDisplay(SkillCatalog skillCatalog) {
        final FlexCellFormatter formatter = getFlexCellFormatter();
        this.addStyleName("skillCatalog");
        int row = 0;
        for (SkillCatalog.SkillCategory skillCategory : skillCatalog.getSkillCategories()) {
            formatter.setColSpan(row, 0, 2); // This spans 2 columns
            formatter.addStyleName(row, 0, "categoryRow");
            setText(row, 0, skillCategory.getName());
            row++;
            // FIXME: Should add Category name here
            for (ZithiaSkill skill : skillCategory.getSkills()) {
                String costLabel;
                if (skill.hasRoll()) {
                    costLabel = skill.getStat().getName() + "/" + skill.getBaseCost() + "/" + skill.getFirstLevelCost();
                } else {
                    costLabel = Integer.toString(skill.getBaseCost());
                }
                formatter.addStyleName(row, 0, "statsCol");
                setText(row, 0, costLabel);
                formatter.addStyleName(row, 1, "nameCol");
                setText(row, 1, skill.getName());
                row++;
            }
        }
    }

}
