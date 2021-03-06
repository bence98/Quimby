package csokicraft.forge.quimby.magnet;

import java.util.*;

import csokicraft.forge.quimby.Quimby;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemWarpingMagnet extends Item{
	public ItemWarpingMagnet(){
		maxStackSize=1;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected){
		if(stack.getItemDamage()==1){
			AxisAlignedBB aabb=new AxisAlignedBB(
					entityIn.posX+Quimby.magnetRadius, entityIn.posY+Quimby.magnetRadius, entityIn.posZ+Quimby.magnetRadius,
					entityIn.posX-Quimby.magnetRadius, entityIn.posY-Quimby.magnetRadius, entityIn.posZ-Quimby.magnetRadius
				);
			List<Entity> l=worldIn.getEntitiesWithinAABBExcludingEntity(entityIn, aabb);
			for(Entity e:l){
				if(e instanceof EntityItem||e instanceof EntityXPOrb){
					e.setPosition(entityIn.posX, entityIn.posY, entityIn.posZ);
				}
			}
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
		if(itemStackIn.getItemDamage()==0)
			itemStackIn.setItemDamage(1);
		else
			itemStackIn.setItemDamage(0);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		if(stack.getItemDamage()==0)
			tooltip.add("Inactive. Right-click to activate!");
		else
			tooltip.add("Active. Right-click to deactivate!");
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
