package csokicraft.forge.quimby.feeder;

import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class SlotFeeder extends Slot{

	public SlotFeeder(ItemStack feeder, int xPosition, int yPosition) {
		super(new InventoryFeeder(feeder), 0, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack){
		return inventory.isItemValidForSlot(getSlotIndex(), stack);
	}
}
