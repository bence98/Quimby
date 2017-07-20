package csokicraft.forge.quimby.disarm;

import java.util.List;

import csokicraft.forge.quimby.Quimby;
import csokicraft.util.mcforge.UtilMcForge10;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ItemChestVacator extends Item{
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
		if(playerIn.isSneaking()){
			NBTTagCompound tag=itemStackIn.getSubCompound("vacator", true);
			tag.setBoolean("mode", !tag.getBoolean("mode"));
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}
		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}
	
	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand){
		if(!world.isRemote){
			processBlockClick(stack, player, world, pos, side);
		}else{
			Quimby.net.sendToServer(new PacketVacator(pos, side, hand));
		}
		return EnumActionResult.SUCCESS;
	}
	
	public static void processBlockClick(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side){
		if(player.isSneaking()) return;
		InventoryEnderChest ender = player.getInventoryEnderChest();
		TileEntity te=world.getTileEntity(pos);
		NBTTagCompound tag=stack.getSubCompound("vacator", true);
		if(te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)){
			transfer(ender, te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side), tag.getBoolean("mode"));
		}else if(te instanceof IInventory){
			transfer(ender, (IInventory)te, tag.getBoolean("mode"));
		}
	}

	private static void transfer(IInventory ender, IInventory te, boolean mode){
		if(mode){//ender->te
			UtilMcForge10.moveAll(new InvWrapper(ender), new InvWrapper(te));
		}else{//te->ender
			UtilMcForge10.moveAll(new InvWrapper(te), new InvWrapper(ender));
		}
	}
	
	private static void transfer(IInventory ender, IItemHandler cap, boolean mode){
		if(mode){//ender->cap
			UtilMcForge10.moveAll(new InvWrapper(ender), cap);
		}else{//cap->ender
			UtilMcForge10.moveAll(cap, new InvWrapper(ender));
		}
	}
	
	/*
	private static void transfer(IInventory ender, IInventory te, boolean mode){
		if(mode){//ender->te
			outer:for(int i=0;i<ender.getSizeInventory();i++){
				ItemStack toTrans=ender.getStackInSlot(i);
				if(toTrans==null) continue outer;
				for(int j=0;j<te.getSizeInventory();j++){
					if(te.isItemValidForSlot(j, toTrans)){
						if(te.getStackInSlot(j)==null){
							te.setInventorySlotContents(j, toTrans.copy());
							ender.setInventorySlotContents(i, null);
							break;
						}else if(te.getStackInSlot(j).isItemEqual(toTrans)){
							int count=toTrans.stackSize+te.getStackInSlot(j).stackSize;
							
							if(count>te.getInventoryStackLimit())
								count=te.getInventoryStackLimit();
							if(count>toTrans.getMaxStackSize())
								count=toTrans.getMaxStackSize();
							
							int trans=count-te.getStackInSlot(j).stackSize;
							if(trans<toTrans.stackSize){
								toTrans.stackSize-=trans;
								ender.setInventorySlotContents(i, toTrans);
							}else{
								ender.setInventorySlotContents(i, null);
								break;
							}
							te.getStackInSlot(j).stackSize+=trans;
						}
					}
				}
			}
		}else{//te->ender
			outer:for(int i=0;i<te.getSizeInventory();i++){
				ItemStack toTrans=te.getStackInSlot(i);
				if(toTrans==null) continue outer;
				inner:for(int j=0;j<ender.getSizeInventory();j++){
					if(ender.isItemValidForSlot(j, toTrans)){
						if(ender.getStackInSlot(j)==null){
							ender.setInventorySlotContents(j, toTrans.copy());
							te.setInventorySlotContents(i, null);
							break inner;
						}else if(ender.getStackInSlot(j).isItemEqual(toTrans)){
							int count=toTrans.stackSize+ender.getStackInSlot(j).stackSize;

							if(count>ender.getInventoryStackLimit())
								count=ender.getInventoryStackLimit();
							if(count>toTrans.getMaxStackSize())
								count=toTrans.getMaxStackSize();
							
							int trans=count-ender.getStackInSlot(j).stackSize;
							if(trans<toTrans.stackSize){
								toTrans.stackSize-=trans;
								te.setInventorySlotContents(i, toTrans);
							}else{
								te.setInventorySlotContents(i, null);
								break inner;
							}
						}
					}
				}
			}
		}
	}

	private static void transfer(IInventory ender, IItemHandler cap, boolean mode){
		if(mode){//ender->cap
			outer:for(int i=0;i<ender.getSizeInventory();i++){
				ItemStack toTrans=ender.getStackInSlot(i);
				if(toTrans==null) continue outer;
				inner:for(int j=0;j<cap.getSlots();j++){
					toTrans=cap.insertItem(j, toTrans, false);
					ender.setInventorySlotContents(i, toTrans);
					if(toTrans==null) break inner;
				}
			}
		}else{//cap->ender
			outer:for(int i=0;i<cap.getSlots();i++){
				ItemStack transType=cap.getStackInSlot(i);
				if(transType==null) continue outer;
				inner:for(int j=0;j<ender.getSizeInventory();j++){
					if(ender.isItemValidForSlot(j, transType)){
						if(ender.getStackInSlot(j)==null){
							ItemStack toTrans=cap.extractItem(i, transType.stackSize, false);
							ender.setInventorySlotContents(j, toTrans);
							break inner;
						}else if(ender.getStackInSlot(j).isItemEqual(transType)){
							int count=ender.getStackInSlot(j).stackSize+transType.stackSize;
							
							if(count>ender.getInventoryStackLimit())
								count=ender.getInventoryStackLimit();
							if(count>transType.getMaxStackSize())
								count=transType.getMaxStackSize();
							
							int trans=count-ender.getStackInSlot(j).stackSize;
							if(trans>0){
								ItemStack toTrans=cap.extractItem(i, trans, true);
								ender.getStackInSlot(j).stackSize+=toTrans.stackSize;
								cap.extractItem(i, toTrans.stackSize, false);
							}
						}
					}
				}
			}
		}
	}
	*/
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		NBTTagCompound tag=stack.getSubCompound("vacator", true);
		if(tag.getBoolean("mode"))
			tooltip.add("Dump mode");
		else
			tooltip.add("Vacate mode");
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
