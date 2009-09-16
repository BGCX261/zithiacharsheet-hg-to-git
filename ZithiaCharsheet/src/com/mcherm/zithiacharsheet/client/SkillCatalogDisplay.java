package com.mcherm.zithiacharsheet.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.mcherm.zithiacharsheet.client.model.SkillCatalog;
import com.mcherm.zithiacharsheet.client.model.ZithiaSkill;


/**
 * A graphical element that displays the skill catalog. If a callback is provided, then
 * it will allow a single row at a time to tbe selected and will invoke the callback
 * passing the selected skill.
 */
public class SkillCatalogDisplay extends FlexTable {
    
    /**
     * The callback to invoke when a skill is selected, or null to
     * indicate that selection is not permitted. Defaults to null.
     */
    private SkillSelectCallback skillSelectCallback = null;
    
    public SkillCatalogDisplay(SkillCatalog skillCatalog) {
        final FlexCellFormatter formatter = this.getFlexCellFormatter();
        this.addStyleName("skillCatalog");
        int categoryRow = 0;
        for (final SkillCatalog.SkillCategory skillCategory : skillCatalog.getSkillCategories()) {
            final int CATEGORY_ROW = categoryRow;
            formatter.setColSpan(CATEGORY_ROW, 0, 2); // This spans 2 columns
            formatter.addStyleName(CATEGORY_ROW, 0, "categoryRow");
            Label categoryLabel = new Label(skillCategory.getName());
            ClickHandler categoryClickHandler = new ClickHandler() {
                public void onClick(ClickEvent event) {
                    onSkillCategoryClicked(event, skillCategory, CATEGORY_ROW);
                }
            };
            categoryLabel.addClickHandler(categoryClickHandler);
            this.setWidget(CATEGORY_ROW, 0, categoryLabel);
            categoryRow++;
            
            // FIXME: Make this a subroutine
            FlexTable subTable = new FlexTable();
            final FlexCellFormatter subFormatter = subTable.getFlexCellFormatter();
            int skillRow = 0;
            for (final ZithiaSkill skill : skillCategory.getSkills()) {
                final int SKILL_ROW = skillRow;
                ClickHandler rowClickHandler = new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        onSkillClicked(event, skill, SKILL_ROW);
                    }
                };
                String statsText;
                if (skill.hasRoll()) {
                    statsText = skill.getStat().getName() + "/" + 
                            skill.getBaseCost() + "/" + skill.getFirstLevelCost();
                } else {
                    statsText = Integer.toString(skill.getBaseCost());
                }
                subFormatter.addStyleName(SKILL_ROW, 0, "statsCol");
                Label statsLabel = new Label(statsText);
                statsLabel.addClickHandler(rowClickHandler);
                subTable.setWidget(SKILL_ROW, 0, statsLabel);
                subFormatter.addStyleName(SKILL_ROW, 1, "nameCol");
                Label nameLabel = new Label(skill.getName());
                nameLabel.addClickHandler(rowClickHandler);
                subTable.setWidget(SKILL_ROW, 1, nameLabel);
                skillRow++;
            }
            setWidget(categoryRow, 0, subTable);
            categoryRow++;
        }
    }

    /**
     * This gets called when the user clicks on a row.
     * 
     * @param event the click event
     * @param skill the skill that was selected.
     * @param row the number of the row that got clicked.
     */
    private void onSkillClicked(ClickEvent event, ZithiaSkill skill, int row) {
        if (skillSelectCallback != null) {
            skillSelectCallback.newSkillSelected(skill);
        }
    }
    
    /**
     * This gets called when the user clicks on a category header.
    * 
    * @param event the click event
    * @param skill the skill that was selected.
    * @param row the number of the row that got clicked.
    */
   private void onSkillCategoryClicked(ClickEvent event, SkillCatalog.SkillCategory skillCategory, int row) {
       Window.alert("User clicked in row " + row + " for skillCategory " + skillCategory);
   }
    
    public static interface SkillSelectCallback {
        public void newSkillSelected(ZithiaSkill skill);
    }
    
    public void setSkillSelectCallback(SkillSelectCallback skillSelectCallback) {
        this.skillSelectCallback = skillSelectCallback;
    }
    
}
