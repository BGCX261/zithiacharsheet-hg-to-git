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
    private static SkillSelectCallback skillSelectCallback = null;
    
    public SkillCatalogDisplay(SkillCatalog skillCatalog) {
        final FlexCellFormatter formatter = getFlexCellFormatter();
        this.addStyleName("skillCatalog");
        int row = 0;
        for (final SkillCatalog.SkillCategory skillCategory : skillCatalog.getSkillCategories()) {
            formatter.setColSpan(row, 0, 2); // This spans 2 columns
            formatter.addStyleName(row, 0, "categoryRow");
            Label categoryLabel = new Label(skillCategory.getName());
            final int CATEGORY_ROW = row;
            ClickHandler categoryClickHandler = new ClickHandler() {
                public void onClick(ClickEvent event) {
                    onSkillCategoryClicked(event, skillCategory, CATEGORY_ROW);
                }
            };
            categoryLabel.addClickHandler(categoryClickHandler);
            setWidget(row, 0, categoryLabel);
            row++;
            // FIXME: Should add Category name here
            for (final ZithiaSkill skill : skillCategory.getSkills()) {
                final int SKILL_ROW = row;
                ClickHandler rowClickHandler = new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        onSkillClicked(event, skill, SKILL_ROW);
                    }
                };
                String statsText;
                if (skill.hasRoll()) {
                    statsText = skill.getStat().getName() + "/" + skill.getBaseCost() + "/" + skill.getFirstLevelCost();
                } else {
                    statsText = Integer.toString(skill.getBaseCost());
                }
                formatter.addStyleName(row, 0, "statsCol");
                Label statsLabel = new Label(statsText);
                statsLabel.addClickHandler(rowClickHandler);
                setWidget(row, 0, statsLabel);
                formatter.addStyleName(row, 1, "nameCol");
                Label nameLabel = new Label(skill.getName());
                nameLabel.addClickHandler(rowClickHandler);
                setWidget(row, 1, nameLabel);
                row++;
            }
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
        Window.alert("User clicked in row " + row + " for skill " + skill);
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
