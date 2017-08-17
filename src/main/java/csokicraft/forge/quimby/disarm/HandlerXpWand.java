package csokicraft.forge.quimby.disarm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class HandlerXpWand implements IMessageHandler<PacketXpWand, IMessage>{

	@Override
	public IMessage onMessage(PacketXpWand message, MessageContext ctx){
		EntityPlayer player=ctx.getServerHandler().player;
		ItemStack stack=player.getHeldItem(message.hand);
		
		ItemXpWand.shootXp(player, stack, message.lookVec);
		return null;
	}

}
