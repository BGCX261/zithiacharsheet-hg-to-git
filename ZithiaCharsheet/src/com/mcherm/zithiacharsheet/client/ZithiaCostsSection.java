package com.mcherm.zithiacharsheet.client;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mcherm.zithiacharsheet.client.model.ZithiaCharacter;
import com.mcherm.zithiacharsheet.client.model.ZithiaCosts;


public class ZithiaCostsSection extends VerticalPanel {
    
    public ZithiaCostsSection(ZithiaCharacter zithiaCharacter) {
        addStyleName("costs");
        final ZithiaCosts costs = zithiaCharacter.getCosts();

        final Grid expGrid = new Grid(4,2);
        expGrid.setText(0, 0, "Exp Earned:");
        expGrid.setWidget(0, 1, new SettableIntField(costs.getExpEarned()));
        expGrid.setText(1, 0, "Exp Spent:");
        expGrid.setWidget(1, 1, new TweakableIntField(costs.getExpSpent()));
        expGrid.setText(2, 0, "Paid on Loan:");
        expGrid.setWidget(2, 1, new TweakableIntField(costs.getPaidForLoan()));
        expGrid.setText(3, 0, "Exp Unspent:");
        expGrid.setWidget(3, 1, new TweakableIntField(costs.getExpUnspent()));
        add(expGrid);

        final Grid costGrid = new Grid(6, 4);
        costGrid.setText(0, 0, "Race:");
        costGrid.setWidget(0, 1, new TweakableIntField(costs.getRaceCost()));
        costGrid.setText(1, 0, "Stats:");
        costGrid.setWidget(1, 1, new TweakableIntField(costs.getStatCost()));
        costGrid.setText(2, 0, "Skills:");
        costGrid.setWidget(2, 1, new TweakableIntField(costs.getSkillCost()));
        costGrid.setText(3, 0, "Weapons:");
        costGrid.setWidget(3, 1, new TweakableIntField(costs.getWeaponSkillCost()));
        costGrid.setText(4, 0, "Other:");
        costGrid.setWidget(4, 1, new TweakableIntField(costs.getTalentCost()));
        costGrid.setText(5, 0, "Cost:");
        costGrid.setWidget(5, 1, new TweakableIntField(costs.getTotalCost()));
        costGrid.setText(2, 2, "Base:");
        costGrid.setWidget(2, 3, new SettableIntField(costs.getBasePts()));
        costGrid.setText(3, 2, "Loan:");
        costGrid.setWidget(3, 3, new SettableIntField(costs.getLoanPts()));
        costGrid.setText(4, 2, "Exp Spent:");
        costGrid.setWidget(4, 3, new TweakableIntField(costs.getExpSpent()));
        costGrid.setText(5, 2, "Cost:");
        costGrid.setWidget(5, 3, new TweakableIntField(costs.getTotalCost()));
        add(costGrid);
    }
}
