package csokicraft.forge.quimby.disarm;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketVacator implements IMessage{
	public static final byte ID=21;
	
	protected BlockPos pos;
	protected EnumFacing side;
	protected EnumHand hand;
	
	public PacketVacator(BlockPos block, EnumFacing facing, EnumHand itemLoc){
		pos=block;
		side=facing;
		hand=itemLoc;
	}
	
	public PacketVacator(){}

	@Override
	public void fromBytes(ByteBuf buf){
		pos=new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		side=EnumFacing.values()[buf.readInt()];
		hand=EnumHand.values()[buf.readInt()];
	}

	@Override
	public void toBytes(ByteBuf buf){
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());

		buf.writeInt(side.ordinal());
		buf.writeInt(hand.ordinal());
	}

}
