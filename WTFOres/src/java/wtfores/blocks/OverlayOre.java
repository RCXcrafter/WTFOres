package wtfores.blocks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.Logger;
import wtfcore.WTFCore;
import wtfcore.api.AddCustomOre;
import wtfcore.api.BlockSets;
import wtfcore.api.IStoneRegister;
import wtfcore.api.OreBlockInfo;
import wtfcore.blocks.OreChildBlock;
import wtfcore.items.ItemMetadataSubblock;
import wtfcore.utilities.LoadBlockSets;
import wtfcore.utilities.UBCblocks;
import wtfores.WTFOres;
import wtfores.config.WTFOresConfig;
import wtfores.gencores.genreplacers.OreRemover;
import wtfores.gencores.genreplacers.OreUbifier;

public class OverlayOre extends OreChildBlock {
	private IIcon[] textures;
	public static String[] vanillaStone = { "stone" };

	public OverlayOre(Block oreBlock, int parentMeta, int oreLevel, Block stoneBlock, String oreType, String[] stoneNames, String domain) {
		super(oreBlock, parentMeta, stoneBlock);

		loadTextureStrings(oreType, stoneNames, domain);

		this.oreLevel = oreLevel;
		if ((stoneBlock != Blocks.lit_redstone_ore) && (oreLevel == 0)) {
			setCreativeTab(WTFOres.oreTab);
		}
	}

	public void loadTextureStrings(String oreType, String[] stoneNames, String domain) {
		textureNames = new String[stoneNames.length];
		parentLocations = new String[stoneNames.length];
		localizedNames = new String[stoneNames.length];

		for (int loop = 0; loop < stoneNames.length; loop++) {
			textureNames[loop] = (oreType + "_" + stoneNames[loop]);
			parentLocations[loop] = (domain + ":" + stoneNames[loop]);
			String localizedStone = null;
			if (stoneNames[loop] == "redGranite") {
				localizedStone = "Red Granite";
			} else if (stoneNames[loop] == "blackGranite") {
				localizedStone = "Black Granite";
			} else if (stoneNames[loop] == "greenschist") {
				localizedStone = "Green Schist";
			} else if (stoneNames[loop] == "blueschist") {
				localizedStone = "Blue Schist";
			} else if (stoneNames[loop] == "ligniteblock")
				localizedStone = "Lignite";
			else
				localizedStone = stoneNames[loop];
			localizedNames[loop] = (WordUtils.capitalize(localizedStone) + " " + WordUtils.capitalize(oreType.substring(0, oreType.length() - 1).replace("_", " ")));
		}
		this.oreType = oreType;
	}

	public Block setBlockName(String p_149663_1_) {
		Block block = super.setBlockName(p_149663_1_);
		for (int loop = 0; loop < textureNames.length; loop++) {
			String string = this.getUnlocalizedName() + "." + loop + ".name=" + localizedNames[loop];
			WTFOres.orenames.add(string);
		}
		return block;
	}

	public static Block[] registerOverlaidOre(Block oreBlock, int parentMeta, String oreType, Block stoneBlock, String unlocalisedName, String[] stoneNames, String domain, boolean ubify) {
		Block[] blockArray = new Block[3];
		Block blockToRegister = null;

		if (oreBlock == Blocks.redstone_ore) {
			RedstoneOverlayOre.registerOverlaidOre(Blocks.lit_redstone_ore, parentMeta, "lit_redstone_ore", stoneBlock, unlocalisedName, stoneNames, domain);
			return RedstoneOverlayOre.registerOverlaidOre(oreBlock, parentMeta, oreType, stoneBlock, unlocalisedName, stoneNames, domain);
		}

		for (int loop = 2; loop > -1; loop--) {
			String[] oreDomain = GameData.getBlockRegistry().getNameForObject(oreBlock).split(":");
			String name = "ore" + oreDomain[0] + "_" + oreType + loop + "_" + unlocalisedName;
			WTFCore.log.info("Ore Domain = " + oreDomain[0]);

			blockToRegister = new OverlayOre(oreBlock, parentMeta, loop, stoneBlock, oreType + loop, stoneNames, domain).setBlockName(name);
			cpw.mods.fml.common.registry.GameRegistry.registerBlock(blockToRegister, ItemMetadataSubblock.class, name);

			if (ubify) {
			}

			BlockSets.oreUbifier.put(new OreBlockInfo(oreBlock, parentMeta, stoneBlock, loop), blockToRegister);
			LoadBlockSets.addOreBlock(blockToRegister);

			blockArray[loop] = blockToRegister;
		}

		BlockSets.oreUbifier.put(new OreBlockInfo(oreBlock, parentMeta, stoneBlock, -1), blockToRegister);
		BlockSets.oreUbifier.put(new OreBlockInfo(oreBlock, parentMeta, stoneBlock, -2), blockToRegister);
		BlockSets.oreUbifier.put(new OreBlockInfo(oreBlock, parentMeta, stoneBlock, -3), blockToRegister);

		if (ubify) {
			new OreUbifier(oreBlock);
		} else {
			new OreRemover(oreBlock);
		}

		return blockArray;
	}

	public static void register() {
		Iterator<AddCustomOre> iterator = WTFOresConfig.customOres.iterator();
		while (iterator.hasNext()) {
			AddCustomOre newOre = (AddCustomOre) iterator.next();
			boolean ubify = false;
			if (newOre.genType == -1) {
				ubify = true;
			}

			Iterator<String> stoneTypeIterator = newOre.stoneTypes.iterator();
			while (stoneTypeIterator.hasNext()) {
				String stoneTypeString = (String) stoneTypeIterator.next();

				if (stoneTypeString.equals("stone")) {
					registerStoneOreSet(newOre.oreBlock, newOre.textureName, newOre.metadata, ubify);
				} else if (stoneTypeString.equals("sand")) {

					String[] nameArray = { "sand" };
					String[] nameArray2 = { "sandstone" };
					registerOverlaidOre(newOre.oreBlock, newOre.metadata, newOre.textureName, Blocks.sand, "sand", nameArray, "minecraft", ubify);

				} else if (stoneTypeString.equals("gravel")) {
					String[] nameArray = { "gravel" };
					registerOverlaidOre(newOre.oreBlock, newOre.metadata, newOre.textureName, Blocks.gravel, "gravel", nameArray, "minecraft", ubify);
				} else if (stoneTypeString.equals("obsidian")) {
					String[] nameArray = { "obsidian" };
					registerOverlaidOre(newOre.oreBlock, newOre.metadata, newOre.textureName, Blocks.obsidian, "obsidian", nameArray, "minecraft", ubify);
				} else if (stoneTypeString.equals("netherrack")) {
					String[] nameArray = { "netherrack" };
					registerOverlaidOre(newOre.oreBlock, newOre.metadata, newOre.textureName, Blocks.netherrack, "netherrack", nameArray, "minecraft", ubify);
				} else if (stoneTypeString.equals("dirt")) {
					String[] nameArray = { "dirt" };
					registerOverlaidOre(newOre.oreBlock, newOre.metadata, newOre.textureName, Blocks.dirt, "dirt", nameArray, "minecraft", ubify);
				} else if (BlockSets.stoneRegisters.containsKey(stoneTypeString)) {
					IStoneRegister stoneregister = (IStoneRegister) BlockSets.stoneRegisters.get(stoneTypeString);
					registerOverlaidOre(newOre.oreBlock, newOre.metadata, newOre.textureName, stoneregister.stone, stoneregister.unlocalisedName, stoneregister.stoneTextureNames, stoneregister.domain, ubify);
				} else {
					WTFCore.log.info("WTFOres: Unable to recognise stone type:" + stoneTypeString + ".");
				}
			}
		}
	}

	public static void registerStoneOreSet(Block oreBlock, String oreType, int parentMeta, boolean ubify) {
		registerOverlaidOre(oreBlock, parentMeta, oreType, Blocks.stone, "stone", vanillaStone, "minecraft", ubify);
		if (Loader.isModLoaded("UndergroundBiomes")) {
			registerOverlaidOre(oreBlock, parentMeta, oreType, UBCblocks.IgneousStone, "igneous", UBCblocks.IgneousStoneList, "undergroundbiomes", ubify);
			registerOverlaidOre(oreBlock, parentMeta, oreType, UBCblocks.MetamorphicStone, "metamorphic", UBCblocks.MetamorphicStoneList, "undergroundbiomes", ubify);
			registerOverlaidOre(oreBlock, parentMeta, oreType, UBCblocks.SedimentaryStone, "sedimentary", UBCblocks.SedimentaryStoneList, "undergroundbiomes", ubify);
		}
	}

	public void registerBlockIcons(IIconRegister iconRegister) {
		textures = new IIcon[16];
		for (int loop = 0; loop < textureNames.length; loop++) {
			textures[loop] = iconRegister.registerIcon("WTFOres:" + textureNames[loop]);
			texturegeneratorlib.TextureGeneratorLib.registerBlockOverlay(this, textureNames[loop], parentLocations[loop], oreType, WTFOres.overlayDomain, false);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return textures[meta];
	}

	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
		if ((WTFOresConfig.enableDenseOres) && (oreLevel < 2)) {
			Block blockToSet = (Block) BlockSets.oreUbifier.get(new OreBlockInfo(oreBlock, oreMeta, stoneBlock, oreLevel + 1));
			if (blockToSet != null) {
				world.setBlock(x, y, z, blockToSet, meta, 0);
			}
		}
	}
}
