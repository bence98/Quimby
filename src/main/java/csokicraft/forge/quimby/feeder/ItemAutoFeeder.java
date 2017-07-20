package csokicraft.forge.quimby.feeder;

import java.util.*;

import csokicraft.forge.quimby.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ItemAutoFeeder extends Item{
	public ItemAutoFeeder(){
		maxStackSize=1;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected){
		ItemStack inside=getStored(stack);
		if(inside!=null&&inside.getItem() instanceof ItemFood&&entity instanceof EntityPlayer){
			EntityPlayer player=(EntityPlayer) entity;
			ItemFood food=(ItemFood) inside.getItem();
			FoodStats fs=player.getFoodStats();
			if(shouldEat(fs, food, inside)){
				food.onItemUseFinish(inside, world, player);
				if(inside.stackSize<=0) setStored(stack, null);
				else setStored(stack, inside);
			}
			
		}
		super.onUpdate(stack, world, entity, itemSlot, isSelected);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand){
		player.openGui(Quimby.inst, CommonProxy.GUI_ID_FEEDER, world, 0, hand.ordinal(), 0);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		ItemStack inside=getStored(stack);
		if(inside!=null)
			tooltip.add("Contains: "+inside.stackSize+"x "+inside.getDisplayName());
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	public static ItemStack getStored(ItemStack in){
		if(in==null) return null;
		if(in.hasTagCompound()&&in.getTagCompound().hasKey("foodSlot")){
			return ItemStack.loadItemStackFromNBT(in.getTagCompound().getCompoundTag("foodSlot"));
		}
		return null;
	}
	
	public static ItemStack setStored(ItemStack in, ItemStack to){
		if(in==null) return null;
		if(!in.hasTagCompound()) in.setTagCompound(new NBTTagCompound());
		if(to!=null)
			in.getTagCompound().setTag("foodSlot", to.writeToNBT(new NBTTagCompound()));
		else
			in.getTagCompound().removeTag("foodSlot");
		return in;
	}
	
	public static ItemStack newStack(){
		return setStored(new ItemStack(Quimby.autoFeeder), null);
	}
	
	/** @return true if the player can eat this food without wasting
	  * (i.e. their {@link FoodStats} are lower than the {@link ItemFood}'s) */
	protected static boolean shouldEat(FoodStats stats, ItemFood foodItem, ItemStack foodStack){
		return (20-stats.getFoodLevel())>=foodItem.getHealAmount(foodStack)&&(20-stats.getSaturationLevel())>=foodItem.getSaturationModifier(foodStack);
	}
}
