package csokicraft.forge.quimby.disarm;

import java.util.List;

import csokicraft.forge.quimby.Quimby;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemXpWand extends Item{
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack is, World w, EntityPlayer p, EnumHand hand){
		if(p.isSneaking()){
			setMode(is, XpWandMode.next(getMode(is)));
			if(!w.isRemote)
				p.addChatComponentMessage(new TextComponentString(I18n.format("chat.xpwand.set", getMode(is).localisedName())));
		}else if(w.isRemote){
			Quimby.net.sendToServer(new PacketXpWand(p, hand));
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, is);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		tooltip.add(I18n.format("chat.xpwand.get", getMode(stack).localisedName()));
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	public static void shootXp(EntityPlayer p, ItemStack is, Vec3d lookVec){
		World w=p.worldObj;
		int qty=Math.min(p.experienceTotal, getMode(is).getXpAmount());
		removeXp(p, qty);
		double  newX=p.posX+lookVec.xCoord,
				newY=p.posY+lookVec.yCoord+p.eyeHeight,
				newZ=p.posZ+lookVec.zCoord;
		EntityXPOrb ent=new EntityXPOrb(w, newX, newY, newZ, qty);
		ent.setVelocity(lookVec.xCoord, lookVec.yCoord, lookVec.zCoord);
		w.spawnEntityInWorld(ent);
	}
	
	protected static void removeXp(EntityPlayer p, int qty){
		if(p.experience>0){
			//old xp=max xp * fill percentage
			//new xp=old xp - removed xp
			float newXp=p.xpBarCap()*p.experience-qty;
			p.experience=newXp/p.xpBarCap();
		}else{
			//new xp=old max xp - removed xp
			float newXp=p.xpBarCap()-qty;
			p.removeExperienceLevel(1);
			p.experience=newXp/p.xpBarCap();
		}
		p.experienceTotal-=qty;
		p.addScore(-qty);
	}
	
	protected static XpWandMode getMode(ItemStack is){
		NBTTagCompound nbt=is.getSubCompound("xpwand", true);
		return XpWandMode.values()[nbt.getInteger("mode")];
	}
	
	protected static void setMode(ItemStack is, XpWandMode mode){
		NBTTagCompound nbt=is.getSubCompound("xpwand", true);
		nbt.setInteger("mode", mode.ordinal());
	}
	
	protected enum XpWandMode{
		SMALL, MEDIUM, LARGE, HUGE;
		
		public static XpWandMode next(XpWandMode current){
			switch(current){
			case SMALL:
				return MEDIUM;
			case MEDIUM:
				return LARGE;
			case LARGE:
				return HUGE;
			default:
				return SMALL;
			}
		}
		
		public Object localisedName(){
			return I18n.format("chat.xpwand.mode."+toString());
		}

		public int getXpAmount(){
			switch(this){
			case SMALL:
				return 1;
			case MEDIUM:
				return 5;
			case LARGE:
				return 20;
			default:
				return 1_000_000;
			}
		}
	}
}
