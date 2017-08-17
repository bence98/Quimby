package csokicraft.forge.quimby;

import csokicraft.forge.quimby.disarm.*;
import csokicraft.forge.quimby.feeder.*;
import csokicraft.forge.quimby.magnet.*;
import csokicraft.forge.quimby.nature.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(modid = Quimby.MODID, version = Quimby.VERSION, dependencies="required-after:csokicraftutil@1.3+")
@EventBusSubscriber
public class Quimby
{
    public static final String MODID = "quimby";
    public static final String VERSION = "1.3.5";
    @Instance
    public static Quimby inst;
    @SidedProxy(clientSide="csokicraft.forge.quimby.ClientProxy", serverSide="csokicraft.forge.quimby.CommonProxy")
    public static CommonProxy proxy;
    
    public static SimpleNetworkWrapper net=new SimpleNetworkWrapper(MODID);
    
    public static Item  autoFeeder = new ItemAutoFeeder().setCreativeTab(CreativeTabs.FOOD).setUnlocalizedName("itemAutoFeeder").setRegistryName("itemAutoFeeder"),
    					warpingMagnet = new ItemWarpingMagnet().setCreativeTab(CreativeTabs.TOOLS).setUnlocalizedName("itemWarpingMagnet").setRegistryName("itemWarpingMagnet"),
    					disarmWand = new ItemDisarmWand().setCreativeTab(CreativeTabs.TOOLS).setUnlocalizedName("itemDisarmWand").setRegistryName("itemDisarmWand"),
    					chestVacator = new ItemChestVacator().setCreativeTab(CreativeTabs.TOOLS).setUnlocalizedName("itemChestVacator").setRegistryName("itemChestVacator"),
    					rake=new ItemRake().setCreativeTab(CreativeTabs.TOOLS).setUnlocalizedName("itemRake").setRegistryName("itemRake"),
    					shoes=new ItemShoes().setCreativeTab(CreativeTabs.COMBAT).setUnlocalizedName("itemShoes").setRegistryName("itemShoes"),
    					xpWand=new ItemXpWand().setCreativeTab(CreativeTabs.TOOLS).setUnlocalizedName("itemXpWand").setRegistryName("itemXpWand");
	
    public static int magnetRadius=7;
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent evt){
    	Configuration cfg=new Configuration(evt.getSuggestedConfigurationFile());
    	magnetRadius=cfg.getInt("Warping magnet radius", "misc", 7, 1, 20, "How many blocks the magnet should search in each direction. The total AoE will be a (2r+1)^3 cube where 'r' is this value");
    	
    }
    
    private static final Item[] toReg=new Item[]{autoFeeder, warpingMagnet, disarmWand, chestVacator, rake, shoes, xpWand};
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	net.registerMessage(HandlerVacator.class, PacketVacator.class, PacketVacator.ID, Side.SERVER);
    	net.registerMessage(HandlerXpWand.class, PacketXpWand.class, PacketXpWand.ID, Side.SERVER);
    	NetworkRegistry.INSTANCE.registerGuiHandler(inst, proxy);
        
    	RakeRecipes.addDefaultRecipes();
        
        proxy.registerModels(toReg);
    }
    
    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event){
    	IForgeRegistry<Item> registry=event.getRegistry();
    	
        for(Item reg:toReg)
        	registry.register(reg);
    }
}
