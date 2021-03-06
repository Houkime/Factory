package com.sinius15.factory.crafting;

import com.sinius15.factory.item.Inventory;
import com.sinius15.factory.item.InventorySlot;
import com.sinius15.factory.item.ItemProvider;

/**
 * Created by Sinius on 1-9-2015.
 */
public class CraftingRecipe {

    private final int resultItemId;
    private final int resultAmount;
    private final CraftingRecipeSpot[] recipe;

    public CraftingRecipe(int resultItemId, int resultAmount, CraftingRecipeSpot... recipe) {
        this.resultItemId = resultItemId;
        this.resultAmount = resultAmount;
        this.recipe = recipe;
    }

    public boolean canCraft(Inventory inventory){
        pieces:
        for(CraftingRecipeSpot peice : recipe){
            for(InventorySlot slot : inventory.getSlots()){
                if(slot.getItemId() == peice.getItemId() && slot.getAmount() >= peice.getAmount())
                    continue pieces;
            }
            return false;
        }
        return true;
    }

    public boolean canBeStackedToSlot(InventorySlot slot){
        if(slot.isEmpty())
            return true;
        if(slot.getItemId() == getResultItemId() && slot.spacesFree() >= getResultAmount())
            return true;
        return false;
    }

    public boolean tryToCraft(Inventory inventory){
        //not enough resources
        if(!canCraft(inventory))
            return false;
        if(!canBeStackedToSlot(inventory.getSelectedSlot()))
            return false;

        pieces:
        for(CraftingRecipeSpot piece : recipe){
            for(InventorySlot slot : inventory.getSlots()){
                if(slot.getItemId() == piece.getItemId() && slot.getAmount() >= piece.getAmount()) {
                    slot.subtractAmount(piece.getAmount());
                    continue pieces;
                }
            }
        }
        inventory.getSelectedSlot().setItemId(resultItemId);
        inventory.getSelectedSlot().addAmount(resultAmount);
        return true;
    }

    public int getResultItemId() {
        return resultItemId;
    }

    public int getResultAmount() {
        return resultAmount;
    }
}
