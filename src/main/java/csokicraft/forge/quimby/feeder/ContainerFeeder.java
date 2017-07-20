package csokicraft.forge.quimby.feeder;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerFeeder extends Container{

	public ContainerFeeder(ItemStack stack, InventoryPlayer inv){
		addSlotToContainer(new SlotFeeder(stack, 78, 32));
		
		/*Borrowed code. This should totally be a public static function in vanilla/Forge */
		for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(inv, k, 8 + k * 18, 142));
        }
		/* End of borrowed code */
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn){
		return true;
	}

	/** Stop crashing already! This bug has been in the game since at least ModLoader 1.4.7 (possibly 1.3, can't remember) WTF??? */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index){
		return null;
	}
}
