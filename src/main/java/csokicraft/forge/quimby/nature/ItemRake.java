package csokicraft.forge.quimby.nature;

import java.util.Map;

import csokicraft.forge.quimby.nature.RakeRecipes.RakeRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRake extends Item{
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		
		IBlockState b=worldIn.getBlockState(pos);
		RakeRecipe rec=RakeRecipes.inst.getRecipe(b);
		if(rec!=null){
			if(!worldIn.isRemote){
				for(Map.Entry<ItemStack, Integer>e:rec.getDrops().entrySet()){
					int n=worldIn.rand.nextInt(100);
					if(e.getValue()>n){
						worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY()+1, pos.getZ(), e.getKey()));
					}
				}
				worldIn.setBlockState(pos, rec.getOutput());
			}
			return EnumActionResult.SUCCESS;
		}
		return super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
