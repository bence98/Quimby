package csokicraft.forge.quimby.feeder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.util.text.*;

public class InventoryFeeder implements IInventory{
	protected ItemStack stack, slot;

	public InventoryFeeder(ItemStack feeder){
		stack=feeder;
	}

	@Override
	public String getName(){
		return stack.getDisplayName();
	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public ITextComponent getDisplayName(){
		return new TextComponentString(getName());
	}

	@Override
	public int getSizeInventory(){
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int index){
		slot=ItemAutoFeeder.getStored(stack);
		return slot;
	}

	@Override
	public ItemStack decrStackSize(int index, int count){
		ItemStack is=getStackInSlot(0);
		ItemStack ret=is.copy();
		int trans=Math.min(is.stackSize, count);
		ret.stackSize=trans;
		
		is.stackSize-=trans;
		if(is.stackSize==0) is=null;
		setInventorySlotContents(0, is);
		
		return ret;
	}

	@Override
	public ItemStack removeStackFromSlot(int index){
		ItemStack is=getStackInSlot(0).copy();
		setInventorySlotContents(0, null);
		return is;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack is){
		slot=is;
		markDirty();
	}

	@Override
	public int getInventoryStackLimit(){
		return 64;
	}

	@Override
	public void markDirty(){
		ItemAutoFeeder.setStored(stack, slot);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player){
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player){}

	@Override
	public void closeInventory(EntityPlayer player){}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack is){
		return index==0&&is.getItem() instanceof ItemFood;
	}

	@Override
	public int getField(int id){
		return 0;
	}

	@Override
	public void setField(int id, int value){

	}

	@Override
	public int getFieldCount(){
		return 0;
	}

	@Override
	public void clear(){
		setInventorySlotContents(0, null);
	}

}
