package csokicraft.forge.quimby;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid=Quimby.MODID, value=Side.CLIENT)
public class ClientProxy extends CommonProxy{
	@Override
	public void registerModels(){
		for(Item it:Quimby.toReg){
			String name=it.getRegistryName().toString();
			name=name.substring(name.indexOf(':')+1);
			
			ModelResourceLocation mdl_loc = new ModelResourceLocation(Quimby.MODID+":"+name, "inventory");
			ModelLoader.setCustomModelResourceLocation(it, 0, mdl_loc);
		}
		ModelResourceLocation mdl_loc = new ModelResourceLocation(Quimby.MODID+":itemwarpingmagnet_on");
		ModelLoader.setCustomModelResourceLocation(Quimby.warpingMagnet, 1, mdl_loc);
	}
	
	@SubscribeEvent
	public static void modelRegister(ModelRegistryEvent evt){
		Quimby.proxy.registerModels();
	}
}
