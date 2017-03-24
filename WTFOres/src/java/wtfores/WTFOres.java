package wtfores;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashSet;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import wtfcore.utilities.LangWriter;
import wtfores.blocks.OverlayOre;
import wtfores.config.WTFOresConfig;

@Mod(modid = "WTFOres", name = "WhiskyTangoFox's Ores", version = "1.5", dependencies = "after:UndergroundBiomes;after:TConstructs;required-after:WTFCore@[1.7,);after:WTFTweaks;required-after:TextureGeneratorLib")
public class WTFOres {
	public static final String modid = "WTFOres";
	@Mod.Instance("WTFOres")
	public static WTFOres instance;
	public static String alphaMaskDomain = "wtfores:textures/blocks/alphamasks/";
	public static String overlayDomain = "wtfores:textures/blocks/overlays/";

	public static HashSet<String> orenames = new HashSet();

	public static CreativeTabs oreTab = new CreativeTabs("WTFOres") {

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.gold_ore);
		}
	};

	public WTFOres() {
	}

	@Mod.EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent) {
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {
		MinecraftForge.ORE_GEN_BUS.register(new VanillOreGenCatcher());
	}

	@Mod.EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent) {
		WTFOresConfig.customConfig();
		OverlayOre.register();
		if (WTFOresConfig.genLangFile) {
			LangWriter.genLangFile(orenames, "WTFOres_en_US.lang");
		}
		wtfcore.worldgen.WorldGenListener.generator = new OreGenTweaked();
	}
}
