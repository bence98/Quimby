package csokicraft.forge.quimby.disarm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class HandlerVacator implements IMessageHandler<PacketVacator, IMessage>{

	@Override
	public IMessage onMessage(PacketVacator message, MessageContext ctx){
		EntityPlayer player=ctx.getServerHandler().playerEntity;
		ItemStack vacatorStack=player.getHeldItem(message.hand);
		
		ItemChestVacator.processBlockClick(vacatorStack, player, player.worldObj, message.pos, message.side);
		return null;
	}

}
