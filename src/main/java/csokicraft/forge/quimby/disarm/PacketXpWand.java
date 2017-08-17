package csokicraft.forge.quimby.disarm;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketXpWand implements IMessage {
	public static final byte ID=22;
	
	protected Vec3d lookVec;
	protected EnumHand hand;

	public PacketXpWand(EntityPlayer p, EnumHand itemLoc){
		lookVec=p.getLookVec();
		hand=itemLoc;
	}
	
	public PacketXpWand(){}
	
	@Override
	public void fromBytes(ByteBuf buf){
		hand=EnumHand.values()[buf.readInt()];
		lookVec=new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	@Override
	public void toBytes(ByteBuf buf){
		buf.writeInt(hand.ordinal());
		
		buf.writeDouble(lookVec.x);
		buf.writeDouble(lookVec.y);
		buf.writeDouble(lookVec.z);
	}

}
