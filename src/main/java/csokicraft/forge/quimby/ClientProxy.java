package csokicraft.forge.quimby;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy{
	@Override
	public void registerModels(Item[] items){
		ItemModelMesher imm=Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		for(Item it:items){
			String name=it.getRegistryName().toString();
			name=name.substring(name.indexOf(':')+1);
			
			ModelResourceLocation mdl_loc = new ModelResourceLocation(Quimby.MODID+":"+name, "inventory");
			imm.register(it, 0, mdl_loc);
			ModelBakery.registerItemVariants(it, mdl_loc);
		}
		ModelResourceLocation mdl_loc = new ModelResourceLocation(Quimby.MODID+":itemWarpingMagnet_on");
		imm.register(Quimby.warpingMagnet, 1, mdl_loc);
		ModelBakery.registerItemVariants(Quimby.warpingMagnet, mdl_loc);
	}
}
