package csokicraft.forge.quimby.nature;

import java.util.*;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RakeRecipes{
	public static RakeRecipes inst=new RakeRecipes();
	
	protected List<RakeRecipe> recipes;
	
	protected RakeRecipes(){
		recipes=new LinkedList<>();
	}
	
	public void registerRecipe(RakeRecipe rec){
		if(getRecipe(rec.getInput())!=null){
			System.out.println("Conflicting recipes! "+getRecipe(rec.getInput())+" is replaced by "+rec+"!");
			removeRecipe(rec.getInput());
		}
		recipes.add(rec);
	}
	
	public RakeRecipe getRecipe(IBlockState b){
		for(RakeRecipe rec:recipes){
			if(rec.getInput().equals(b))
				return rec;
		}
		return null;
	}
	
	public void removeRecipe(IBlockState b){
		recipes.remove(getRecipe(b));
	}
	
	public static void addDefaultRecipes(){
		RakeRecipe rec;

		rec=new RakeRecipe(Blocks.DIRT.getStateFromMeta(2), Blocks.DIRT.getDefaultState());
		rec.addDrop(new ItemStack(Blocks.LEAVES), 50);
		rec.addDrop(new ItemStack(Blocks.SAPLING, 1, 0), 10);
		rec.addDrop(new ItemStack(Blocks.SAPLING, 1, 1), 10);
		rec.addDrop(new ItemStack(Blocks.SAPLING, 1, 2), 10);
		rec.addDrop(new ItemStack(Blocks.SAPLING, 1, 3), 8);
		rec.addDrop(new ItemStack(Blocks.SAPLING, 1, 4), 8);
		rec.addDrop(new ItemStack(Blocks.SAPLING, 1, 5), 8);
		rec.addDrop(new ItemStack(Blocks.BROWN_MUSHROOM), 5);
		rec.addDrop(new ItemStack(Blocks.RED_MUSHROOM), 5);
		inst.registerRecipe(rec);

		rec=new RakeRecipe(Blocks.DIRT.getStateFromMeta(1), Blocks.DIRT.getDefaultState());
		rec.addDrop(new ItemStack(Items.FLINT), 20);
		rec.addDrop(new ItemStack(Items.CLAY_BALL), 10);
		rec.addDrop(new ItemStack(Items.GOLD_NUGGET), 1);
		inst.registerRecipe(rec);

		rec=new RakeRecipe(Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState());
		rec.addDrop(new ItemStack(Blocks.TALLGRASS, 1, 1), 40);
		rec.addDrop(new ItemStack(Items.WHEAT_SEEDS), 10);
		rec.addDrop(new ItemStack(Items.MELON_SEEDS), 5);
		rec.addDrop(new ItemStack(Items.PUMPKIN_SEEDS), 5);
		rec.addDrop(new ItemStack(Items.BEETROOT_SEEDS), 5);
		inst.registerRecipe(rec);

		rec=new RakeRecipe(Blocks.GRASS_PATH.getDefaultState(), Blocks.DIRT.getDefaultState());
		rec.addDrop(new ItemStack(Blocks.DEADBUSH), 15);
		rec.addDrop(new ItemStack(Items.WHEAT_SEEDS), 30);
		inst.registerRecipe(rec);

		rec=new RakeRecipe(Blocks.MYCELIUM.getDefaultState(), Blocks.DIRT.getDefaultState());
		rec.addDrop(new ItemStack(Blocks.RED_MUSHROOM), 40);
		rec.addDrop(new ItemStack(Blocks.BROWN_MUSHROOM), 40);
		rec.addDrop(new ItemStack(Items.NETHER_WART), 2);
		inst.registerRecipe(rec);

		rec=new RakeRecipe(Blocks.GRAVEL.getDefaultState(), Blocks.SAND.getDefaultState());
		rec.addDrop(new ItemStack(Items.FLINT), 60);
		rec.addDrop(new ItemStack(Items.GOLD_NUGGET), 10);
		rec.addDrop(new ItemStack(Items.EMERALD), 2);
		inst.registerRecipe(rec);

		rec=new RakeRecipe(Blocks.SAND.getStateFromMeta(1), Blocks.SAND.getDefaultState());
		rec.addDrop(new ItemStack(Items.REDSTONE), 10);
		inst.registerRecipe(rec);

		rec=new RakeRecipe(Blocks.SOUL_SAND.getDefaultState(), Blocks.SAND.getDefaultState());
		rec.addDrop(new ItemStack(Items.QUARTZ), 20);
		rec.addDrop(new ItemStack(Items.NETHER_WART), 10);
		rec.addDrop(new ItemStack(Items.GLOWSTONE_DUST), 10);
		inst.registerRecipe(rec);

		rec=new RakeRecipe(Blocks.MOSSY_COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState());
		rec.addDrop(new ItemStack(Blocks.VINE), 60);
		rec.addDrop(new ItemStack(Items.SLIME_BALL), 10);
		inst.registerRecipe(rec);
	}
	
	static class RakeRecipe{
		protected IBlockState input, output;
		protected Map<ItemStack, Integer> drops;
		
		public RakeRecipe(IBlockState bIn, IBlockState bOut){
			input=bIn;
			output=bOut;
			drops=new HashMap<>();
		}
		
		public IBlockState getInput(){
			return input;
		}
		public IBlockState getOutput(){
			return output;
		}
		
		/** Read-only */
		public Map<ItemStack, Integer> getDrops(){
			return Collections.unmodifiableMap(drops);
		}
		
		public void addDrop(ItemStack is, int percent){
			drops.put(is, percent);
		}
		
		public void removeDrop(ItemStack is){
			drops.remove(is);
		}
		
		@Override
		public String toString(){
			return "["+input+"->"+output+"&"+drops+"]";
		}
	}
}
