package csokicraft.forge.quimby.disarm;

import java.util.List;

import csokicraft.forge.quimby.Quimby;
import csokicraft.util.mcforge.UtilMcForge11;
import net.minecraft.client.util.ITooltipFlag;
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand){
		ItemStack itemStackIn=playerIn.getHeldItem(hand);
		if(playerIn.isSneaking()){
			NBTTagCompound tag=itemStackIn.getOrCreateSubCompound("vacator");
			tag.setBoolean("mode", !tag.getBoolean("mode"));
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}
		return super.onItemRightClick(worldIn, playerIn, hand);
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand){
		ItemStack stack=player.getHeldItem(hand);
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
		if(te==null)
			return;
		NBTTagCompound tag=stack.getOrCreateSubCompound("vacator");
		if(te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)){
			transfer(ender, te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side), tag.getBoolean("mode"));
		}else if(te instanceof IInventory){
			transfer(ender, (IInventory)te, tag.getBoolean("mode"));
		}
	}

	private static void transfer(IInventory ender, IInventory te, boolean mode){
		if(mode){//ender->te
			UtilMcForge11.moveAll(new InvWrapper(ender), new InvWrapper(te));
		}else{//te->ender
			UtilMcForge11.moveAll(new InvWrapper(te), new InvWrapper(ender));
		}
	}
	
	private static void transfer(IInventory ender, IItemHandler cap, boolean mode){
		if(mode){//ender->cap
			UtilMcForge11.moveAll(new InvWrapper(ender), cap);
		}else{//cap->ender
			UtilMcForge11.moveAll(cap, new InvWrapper(ender));
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound tag=stack.getOrCreateSubCompound("vacator");
		if(tag.getBoolean("mode"))
			tooltip.add("Dump mode");
		else
			tooltip.add("Vacate mode");
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
