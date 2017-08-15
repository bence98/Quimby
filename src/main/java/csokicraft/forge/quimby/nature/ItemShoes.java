package csokicraft.forge.quimby.nature;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemShoes extends Item{
	/** Borrowed UUID from vanilla. I plan on giving it back, I promise! */
	private static final UUID feetAttribID=UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150");
	
	@Override
	public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity){
		return armorType.equals(EntityEquipmentSlot.FEET);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack is){
		Multimap<String, AttributeModifier> map=super.getAttributeModifiers(equipmentSlot, is);
		ShoeAttribs attribs=getAttributes(is);
		if(equipmentSlot.equals(EntityEquipmentSlot.FEET)){
			double boost=0.05;
			if(attribs.getWetness()>-25) boost-=(attribs.getWetness()*0.001);
			map.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(feetAttribID, "Walking speed", boost, 0));
			double armor=2-(attribs.getBurnt()*0.015);
			map.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(feetAttribID, "Armor", armor, 0));
		}
		return map;
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack){
		if(!world.isRemote&&world.getTotalWorldTime()%100==0){
			ShoeAttribs attribs=getAttributes(itemStack);
			Block inBlock=world.getBlockState(player.getPosition()).getBlock(),
				  onBlock=world.getBlockState(player.getPosition().add(0, -1, 0)).getBlock();
			
			int wetness=0;
			if(player.isWet()){
				wetness+=3;
				if(attribs.getBurnt()>0){
					wetness+=attribs.getBurnt();
					attribs.addBurnt(-1);
				}else if(attribs.getDirty()>10){
					attribs.addDirty(-10);
					wetness-=2;
				}else if(attribs.getDirty()>0){
					attribs.setDirty(0);
					wetness-=1;
				}
			}
			if(wetness>0) attribs.addWetness(wetness);
			
			if(player.isBurning()){
				attribs.addBurnt(1);
				attribs.addWetness(-5);
			}
			
			int dirt=0;
			if(onBlock.equals(Blocks.DIRT)){
				dirt=5;
			}else if(onBlock.equals(Blocks.SAND)){
				dirt=3;
			}else if(onBlock.equals(Blocks.GRASS)){
				dirt=2;
			}else if(onBlock.equals(Blocks.GRAVEL)){
				dirt=1;
			}else if(inBlock.equals(Blocks.CARPET)){
				dirt-=5;
				if(attribs.getWetness()>1){
					attribs.addWetness(-2);
				}else if(attribs.getWetness()==1){
					attribs.setWetness(0);
				}
			}
			
			if(dirt>0){
				if(attribs.getWetness()>1){
					dirt*=2;
					attribs.addWetness(-1);
				}
			}
			if(dirt!=0){
				attribs.addDirty(dirt);
			}
			
			if(attribs.isChanged()) setAttributes(itemStack, attribs);
		}
		super.onArmorTick(world, player, itemStack);
	}
	
	public static ShoeAttribs getAttributes(ItemStack is){
		ShoeAttribs ret=new ShoeAttribs();
		ret.readFromNBT(is.getOrCreateSubCompound("attribs"));
		return ret;
	}
	
	public static void setAttributes(ItemStack is, ShoeAttribs attribs){
		attribs.writeToNBT(is.getOrCreateSubCompound("attribs"));
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		ShoeAttribs attribs=getAttributes(stack);
		
		if(attribs.getWetness()>75){
			tooltip.add(ChatFormatting.DARK_BLUE+"W: Soggy ("+attribs.getWetness()+")");
		}else if(attribs.getWetness()>50){
			tooltip.add(ChatFormatting.BLUE+"W: Soaked ("+attribs.getWetness()+")");
		}else if(attribs.getWetness()>25){
			tooltip.add(ChatFormatting.DARK_AQUA+"W: Wet ("+attribs.getWetness()+")");
		}else if(attribs.getWetness()>0){
			tooltip.add(ChatFormatting.AQUA+"W: Moist ("+attribs.getWetness()+")");
		}else if(attribs.getWetness()<-75){
			tooltip.add(ChatFormatting.DARK_RED+"W: Parched ("+attribs.getWetness()+")");
		}else if(attribs.getWetness()<-50){
			tooltip.add(ChatFormatting.RED+"W: Arid ("+attribs.getWetness()+")");
		}else if(attribs.getWetness()<0){
			tooltip.add(ChatFormatting.GOLD+"W: Dry ("+attribs.getWetness()+")");
		}
		
		if(attribs.getDirty()>75){
			tooltip.add(ChatFormatting.DARK_GRAY+"D: Grungy ("+attribs.getDirty()+")");
		}else if(attribs.getDirty()>50){
			tooltip.add(ChatFormatting.GRAY+"D: Filthy ("+attribs.getDirty()+")");
		}else if(attribs.getDirty()>25){
			tooltip.add(ChatFormatting.DARK_PURPLE+"D: Dirty ("+attribs.getDirty()+")");
		}else if(attribs.getDirty()>0){
			tooltip.add(ChatFormatting.LIGHT_PURPLE+"D: Dusty ("+attribs.getDirty()+")");
		}else if(attribs.getDirty()<-75){
			tooltip.add(ChatFormatting.WHITE+"D: Clean ("+attribs.getDirty()+")");
		}else if(attribs.getDirty()<-50){
			tooltip.add(ChatFormatting.YELLOW+"D: Neat ("+attribs.getDirty()+")");
		}else if(attribs.getDirty()<0){
			tooltip.add(ChatFormatting.GREEN+"D: Well-kept ("+attribs.getDirty()+")");
		}
		
		if(attribs.getBurnt()>75){
			tooltip.add(ChatFormatting.DARK_GRAY+"B: Ashes ("+attribs.getBurnt()+")");
		}else if(attribs.getBurnt()>50){
			tooltip.add(ChatFormatting.GRAY+"B: Charred ("+attribs.getBurnt()+")");
		}else if(attribs.getBurnt()>25){
			tooltip.add(ChatFormatting.DARK_PURPLE+"B: Burnt ("+attribs.getBurnt()+")");
		}else if(attribs.getBurnt()>0){
			tooltip.add(ChatFormatting.LIGHT_PURPLE+"B: Crusted ("+attribs.getBurnt()+")");
		}else if(attribs.getBurnt()<-75){
			tooltip.add(ChatFormatting.WHITE+"B: New ("+attribs.getBurnt()+")");
		}else if(attribs.getBurnt()<-50){
			tooltip.add(ChatFormatting.YELLOW+"B: Intact ("+attribs.getBurnt()+")");
		}else if(attribs.getBurnt()<0){
			tooltip.add(ChatFormatting.GREEN+"B: Preserved ("+attribs.getBurnt()+")");
		}
		
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	public static class ShoeAttribs{
		/** Strictly in range [-100;100]! */
		byte wetness, dirty, burnt;
		boolean changed;
		
		public ShoeAttribs(){
			changed=false;
		}
		
		void writeToNBT(NBTTagCompound c){
			c.setByte("wetness", wetness);
			c.setByte("dirty", dirty);
			c.setByte("burnt", burnt);
		}
		
		void readFromNBT(NBTTagCompound c){
			wetness=c.getByte("wetness");
			dirty=c.getByte("dirty");
			burnt=c.getByte("burnt");
		}
		
		public boolean isChanged(){
			return changed;
		}
		
		public byte getWetness(){
			return wetness;
		}
		
		public byte getDirty(){
			return dirty;
		}
		
		public byte getBurnt(){
			return burnt;
		}
		
		public void setWetness(int b){
			if(b<-100) wetness=-100;
			else if(b>100) wetness=100;
			else wetness=(byte) b;
			changed=true;
		}
		
		public void setDirty(int b){
			if(b<-100) dirty=-100;
			else if(b>100) dirty=100;
			else dirty=(byte) b;
			changed=true;
		}
		
		public void setBurnt(int b){
			if(b<-100) dirty=-100;
			else if(b>100) dirty=100;
			else dirty=(byte) b;
			changed=true;
		}
		
		public void addWetness(int b){
			int sum=wetness+b;
			if(sum<-100) wetness=-100;
			else if(sum>100) wetness=100;
			else wetness=(byte) sum;
			changed=true;
		}
		
		public void addDirty(int b){
			int sum=dirty+b;
			if(sum<-100) dirty=-100;
			else if(sum>100) dirty=100;
			else dirty=(byte) sum;
			changed=true;
		}
		
		public void addBurnt(int b){
			int sum=burnt+b;
			if(sum<-100) burnt=-100;
			else if(sum>100) burnt=100;
			else burnt=(byte) sum;
			changed=true;
		}
	}
}
