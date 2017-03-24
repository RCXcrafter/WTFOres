package wtfores.config;

import java.util.HashSet;
import net.minecraft.block.Block;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import org.apache.logging.log4j.Logger;
import wtfcore.WTFCore;
import wtfcore.api.AddCustomOre;

public class ParseConfig {
	public ParseConfig() {
	}

	public static void parse(String fullOreString) {
		fullOreString = fullOreString.replaceAll("\\s", "");
		String[] oreStringArray = fullOreString.split(";");

		for (int loop = 0; loop < oreStringArray.length; loop++) {
			String[] currentString = oreStringArray[loop].split(",");

			AddCustomOre customOre = new AddCustomOre();
			String overrideTexture = null;

			WTFCore.log.info("WTF-ores: loading from " + oreStringArray[loop]);

			for (int stringLoop = 0; stringLoop < currentString.length; stringLoop++) {
				if (currentString[stringLoop].startsWith("oreBlock=")) {
					customOre.oreBlock = ((Block) cpw.mods.fml.common.registry.GameData.getBlockRegistry().getObject(currentString[stringLoop].substring(9)));
				} else if (currentString[stringLoop].startsWith("metadata=")) {
					customOre.metadata = Integer.parseInt(currentString[stringLoop].substring(9));
				} else if (currentString[stringLoop].startsWith("useTexture=")) {
					customOre.textureName = currentString[stringLoop].substring(11);
				} else if (currentString[stringLoop].startsWith("genType=")) {
					customOre.genType = Integer.parseInt(currentString[stringLoop].substring(8));
				} else if (currentString[stringLoop].startsWith("maxHeightPercent=")) {
					customOre.setMaxHeightPercent(Integer.parseInt(currentString[stringLoop].substring(17)));
				} else if (currentString[stringLoop].startsWith("minHeightPercent=")) {
					customOre.setMinHeightPercent(Integer.parseInt(currentString[stringLoop].substring(17)));
				} else if (currentString[stringLoop].startsWith("maxPerChunk=")) {
					customOre.setMaxPerChunk(Integer.parseInt(currentString[stringLoop].substring(12)));
				} else if (currentString[stringLoop].startsWith("minPerChunk=")) {
					customOre.setMinPerChunk(Integer.parseInt(currentString[stringLoop].substring(12)));
				} else if (currentString[stringLoop].startsWith("v1=")) {
					customOre.var1 = Integer.parseInt(currentString[stringLoop].substring(3));
				} else if (currentString[stringLoop].startsWith("v2=")) {
					customOre.var2 = Integer.parseInt(currentString[stringLoop].substring(3));
				} else if (currentString[stringLoop].startsWith("overrideTexture=")) {
					overrideTexture = currentString[stringLoop].substring(8);
				} else if (currentString[stringLoop].startsWith("biomeType=")) {
					String[] biometypestring = currentString[stringLoop].split("@");
					String string = biometypestring[0].substring(10);
					BiomeDictionary.Type biome = BiomeDictionary.Type.getType(string, BiomeDictionary.Type.values());
					customOre.biomeModifier.put(biome, Float.valueOf(Float.parseFloat(biometypestring[1])));

				} else if (currentString[stringLoop].startsWith("stone=")) {
					String string = currentString[stringLoop].substring(6);
					customOre.stoneTypes.add(string);
				} else if (currentString[stringLoop].startsWith("dimension=")) {
					customOre.dimension.add(Integer.valueOf(Integer.parseInt(currentString[stringLoop].substring(10))));
				} else {
					WTFCore.log.info("WTFOres CustomOre Config: Cannot parse " + currentString[stringLoop]);
				}
			}

			if ((customOre.oreBlock == null) || (customOre.oreBlock == net.minecraft.init.Blocks.air)) {
				WTFCore.log.info("Adding custom ores, block not found for " + oreStringArray[loop]);
			} else {
				WTFCore.log.info("Adding custom ores, block loaded: " + customOre.oreBlock.getUnlocalizedName());

				if (customOre.dimension.isEmpty()) {
					customOre.dimension.add(Integer.valueOf(0));
				}
				if (customOre.stoneTypes.isEmpty()) {
					customOre.stoneTypes.add("stone");
				}
				WTFOresConfig.customOres.add(customOre);
				if (overrideTexture == null)
					overrideTexture = customOre.textureName;
				texturegeneratorlib.TextureGeneratorLib.registerBlockOverlayOverride(customOre.oreBlock, overrideTexture, "minecraft:stone", customOre.textureName + 0, wtfores.WTFOres.overlayDomain, false);
			}
		}
	}
}
