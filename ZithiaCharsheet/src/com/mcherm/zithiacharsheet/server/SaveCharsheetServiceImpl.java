package com.mcherm.zithiacharsheet.server;

import com.mcherm.zithiacharsheet.client.SaveCharsheetService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


@SuppressWarnings("serial")
public class SaveCharsheetServiceImpl extends RemoteServiceServlet implements SaveCharsheetService {

    @Override
    public void saveCharsheet(String characterId, String saveStr) {
        System.out.println("saveCharsheet(" + saveStr + ")");
    }
    
    @Override
    public String loadCharsheet(String characterId) {
        return "{\"statValues\":[{\"value\":10},{\"value\":10},{\"value\":10},{\"value\":10},{\"value\":16},{\"value\":10},{\"value\":10},{\"value\":10},{\"value\":10},{\"value\":10},{\"value\":6}],\"skillList\":[{\"skill\":{\"id\":\"climbing\"},\"levels\":0},{\"skill\":{\"id\":\"stealth\"},\"levels\":0}],\"weaponTraining\":{\"weaponSkill\":{\"id\":\"allcombat\"},\"basicTrainingPurchased\":false,\"levelsPurchased\":0},\"costs\":{}}";
    }
}
