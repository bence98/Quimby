package csokicraft.forge.quimby.disarm;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;

public class ItemDisarmWand extends Item{
	public ItemDisarmWand(){
		maxStackSize=1;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand){
		if(!target.getEntityWorld().isRemote){
			for(EntityEquipmentSlot slot:EntityEquipmentSlot.values()){
				ItemStack is=target.getItemStackFromSlot(slot);
				if(is!=null){
					target.entityDropItem(is, 0);
					target.setItemStackToSlot(slot, null);
				}
			}
		}
		return true;
	}
}
