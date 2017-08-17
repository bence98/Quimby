package csokicraft.forge.quimby;

import java.util.*;

import csokicraft.forge.quimby.feeder.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler{
	public static final int GUI_ID_FEEDER = 30;
	
	public void registerModels(){}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		switch(ID){
		case GUI_ID_FEEDER:
			return new ContainerFeeder(player.getHeldItem(EnumHand.values()[y]), player.inventory);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID){
		case GUI_ID_FEEDER:
			return new GuiFeeder(player.getHeldItem(EnumHand.values()[y]), player.inventory);
		}
		return null;
	}
}
