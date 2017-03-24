package wtfores;

import cpw.mods.fml.common.Loader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import wtfcore.InterModBlocks;
import wtfcore.api.AddCustomOre;
import wtfcore.api.BlockInfo;
import wtfcore.api.BlockSets;
import wtfcore.api.OreBlockInfo;
import wtfcore.utilities.CavePosition;
import wtfores.config.WTFOresConfig;
import wtfores.gencores.VOreGen;

public class OreGenTweaked implements wtfcore.worldgen.IWTFGenerator {
	private World world;
	private int surface;
	private int chunkX;
	private int chunkZ;
	private Random random;
	private float surfaceMod;
	private VOreGen gen;
	private BiomeDictionary.Type[] biomeTypes;
	private ArrayList<CavePosition> cavepositions;

	public OreGenTweaked() {
	}

	public void generate(World world, int surface, int x, int z, Random random, ArrayList<CavePosition> cavepositions) {
		this.world = world;
		this.surface = surface;
		chunkX = x;
		chunkZ = z;
		biomeTypes = BiomeDictionary.getTypesForBiome(world.getBiomeGenForCoords(chunkX, chunkZ));
		this.random = random;
		surfaceMod = (surface / 64.0F);
		if (gen == null) {
			gen = wtfores.gencores.GenOreProvider.getGenCore();
		}
		this.cavepositions = cavepositions;

		Iterator<AddCustomOre> iterator = WTFOresConfig.customOres.iterator();
		while (iterator.hasNext()) {
			AddCustomOre newOre = (AddCustomOre) iterator.next();

			if (newOre.dimension.contains(Integer.valueOf(world.provider.dimensionId))) {
				switch (newOre.genType) {
				case 0:
					genDefault(newOre);
					break;
				case 1:
					genSheet(newOre);
					break;
				case 2:
					genthickVein(newOre);
					break;
				case 3:
					genDisperseVein(newOre);
					break;
				case 4:
					genSingle(newOre);
					break;
				case 5:
					genVertical(newOre);
					break;
				case 6:
					genStar(newOre);
					break;
				case 7:
					genCaveFloor(newOre);
				}

			}
		}
	}

	public void genDefault(AddCustomOre newOre) {
		Block oreBlock = newOre.oreBlock;

		int counter = MathHelper.floor_float(newOre.getPerChunk(biomeTypes) * surfaceMod);
		int numberOfBlocks = newOre.var1;

		for (int fail = 0; (counter > 0) && (fail < 1000); fail++) {
			float f = random.nextFloat() * 3.1415927F;
			double d0 = chunkX + 8 + MathHelper.sin(f) * numberOfBlocks / 8.0F;
			double d1 = chunkX + 8 - MathHelper.sin(f) * numberOfBlocks / 8.0F;
			double d2 = chunkZ + 8 + MathHelper.cos(f) * numberOfBlocks / 8.0F;
			double d3 = chunkZ + 8 - MathHelper.cos(f) * numberOfBlocks / 8.0F;
			int height = newOre.getHeight(surface);
			double d4 = height + random.nextInt(3) - 2;
			double d5 = height + random.nextInt(3) - 2;

			for (int l = 0; l <= numberOfBlocks; l++) {
				double d6 = d0 + (d1 - d0) * l / numberOfBlocks;
				double d7 = d4 + (d5 - d4) * l / numberOfBlocks;
				double d8 = d2 + (d3 - d2) * l / numberOfBlocks;
				double d9 = random.nextDouble() * numberOfBlocks / 16.0D;
				double d10 = (MathHelper.sin(l * 3.1415927F / numberOfBlocks) + 1.0F) * d9 + 1.0D;
				double d11 = (MathHelper.sin(l * 3.1415927F / numberOfBlocks) + 1.0F) * d9 + 1.0D;
				int i1 = MathHelper.floor_double(d6 - d10 / 2.0D);
				int j1 = MathHelper.floor_double(d7 - d11 / 2.0D);
				int k1 = MathHelper.floor_double(d8 - d10 / 2.0D);
				int l1 = MathHelper.floor_double(d6 + d10 / 2.0D);
				int i2 = MathHelper.floor_double(d7 + d11 / 2.0D);
				int j2 = MathHelper.floor_double(d8 + d10 / 2.0D);

				for (int k2 = i1; k2 <= l1; k2++) {
					double d12 = (k2 + 0.5D - d6) / (d10 / 2.0D);
					if (d12 * d12 < 1.0D) {
						for (int l2 = j1; l2 <= i2; l2++) {
							double d13 = (l2 + 0.5D - d7) / (d11 / 2.0D);
							if (d12 * d12 + d13 * d13 < 1.0D) {
								for (int i3 = k1; i3 <= j2; i3++) {
									int densityToSet = random.nextInt(3);
									if (!WTFOresConfig.enableDenseOres) {
										densityToSet = 0;
									}
									if (genOre(world, oreBlock, newOre.metadata, k2, l2, i3, densityToSet)) {
										counter--;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void genSheet(AddCustomOre newOre) {
		Block oreBlock = newOre.oreBlock;

		int counter = MathHelper.floor_float(newOre.getPerChunk(biomeTypes) * surfaceMod);

		int widthX = newOre.var1;
		int widthZ = newOre.var2;

		for (int fail = 0; (counter > 0) && (fail < 1000); fail++) {
			int height = newOre.getHeight(surface);
			float slopeX = random.nextFloat() / 2.0F;
			float slopeZ = random.nextFloat() / 2.0F;
			int startX = random.nextInt(8);
			int startZ = random.nextInt(8);
			widthX = widthX + random.nextInt(2) + random.nextInt(2);
			widthZ = widthZ + random.nextInt(2) + random.nextInt(2);

			for (int loopX = 0; loopX < widthX; loopX++) {
				int x = chunkX + startX + loopX;
				for (int loopZ = 0; loopZ < widthZ; loopZ++) {
					int z = chunkZ + startZ + loopZ;
					int y = height + MathHelper.floor_double(loopX * slopeX + loopZ * slopeZ);
					int densityToSet = 2 - 2 * (height / (surface + 10)) + random.nextInt(3) - 1;
					if (!WTFOresConfig.enableDenseOres) {
						densityToSet = 0;
					}
					if (genOre(world, oreBlock, newOre.metadata, x, y, z, densityToSet)) {
						if (WTFOresConfig.enableDenseOres) {
							counter -= 3 - densityToSet;
						} else {
							counter--;
						}
					}
				}
			}
		}
	}

	private void genthickVein(AddCustomOre newOre) {
		Block oreBlock = newOre.oreBlock;

		int counter = MathHelper.floor_float(newOre.getPerChunk(biomeTypes) * surfaceMod);
		int baseLength = newOre.var1;

		for (int fail = 0; (counter > 0) && (fail < 1000); fail++) {
			float slopeXZ = random.nextFloat() - 0.5F;
			float slopeXY = random.nextFloat() - 0.5F;
			int startX = random.nextInt(8) + 8;
			int startZ = random.nextInt(8) + 8;
			int height = newOre.getHeight(surface);
			int length = baseLength - 3 + random.nextInt(3) + random.nextInt(3) + random.nextInt(3);

			for (int loopX = 0; loopX < length; loopX++) {
				int x = MathHelper.floor_double(chunkX + startX + loopX);
				int z = MathHelper.floor_double(chunkZ + startZ + slopeXZ * loopX);
				int y = height + MathHelper.floor_double(loopX * slopeXY);
				if ((y > 0) && (y < 256)) {
					int zmod = 0;
					int ymod = 0;

					for (int loop = 0; loop < 4; loop++) {
						if (loop == 1) {
							ymod = 0;
							zmod = 1;
						}
						if (loop == 2) {
							ymod = 1;
							zmod = 0;
						}
						if (loop == 3) {
							ymod = 1;
							zmod = 1;
						}
						if (random.nextBoolean()) {
							int densityToSet = 2 - 2 * (height / (surface + 10)) + random.nextInt(3) - 1;
							if (!WTFOresConfig.enableDenseOres) {
								densityToSet = 0;
							}
							if (genOre(world, oreBlock, newOre.metadata, x, y + ymod, z + zmod, densityToSet)) {
								if (WTFOresConfig.enableDenseOres) {
									counter -= 3 - densityToSet;
								} else {
									counter--;
								}
							}
						}
					}
				}
			}
		}
	}

	private void genDisperseVein(AddCustomOre newOre) {
		Block oreBlock = newOre.oreBlock;

		int counter = MathHelper.floor_float(newOre.getPerChunk(biomeTypes) * surfaceMod);
		int baseLength = newOre.var1;

		for (int fail = 0; (counter > 0) && (fail < 1000); fail++) {
			int height = newOre.getHeight(surface);
			float slopeXZ = random.nextFloat() - 0.5F;
			float slopeXY = random.nextFloat() - 0.5F;
			int startX = random.nextInt(8);
			int startZ = random.nextInt(4) + 8;

			int length = baseLength + random.nextInt(5) - 2;

			for (int loopX = 0; loopX < length; loopX++) {
				int x = MathHelper.floor_double(chunkX + startX + loopX);
				int z = MathHelper.floor_double(chunkZ + startZ + slopeXZ * loopX);
				int y = height + MathHelper.floor_double(loopX * slopeXY);

				int rand = random.nextInt(6);
				if (rand == 0) {
					z += 1;
				} else if (rand == 2) {
					z -= 1;
				} else if (rand == 3) {
					x += 1;
				} else if (rand == 4) {
					x -= 1;
				}
				int densityToSet = 2 - 2 * (height / (surface + 10)) + random.nextInt(3) - 1;
				if (!WTFOresConfig.enableDenseOres) {
					densityToSet = 0;
				}
				if (genOre(world, oreBlock, newOre.metadata, x, y, z, densityToSet)) {
					if (WTFOresConfig.enableDenseOres) {
						counter -= 3 - densityToSet;
					} else {
						counter--;
					}
				}
			}
		}
	}

	private void genSingle(AddCustomOre newOre) {
		Block oreBlock = newOre.oreBlock;

		int counter = MathHelper.floor_float(newOre.getPerChunk(biomeTypes) * surfaceMod);

		for (int fail = 0; (fail < 1000) && (counter > 0); fail++) {
			int x = chunkX + random.nextInt(16);
			int y = newOre.getHeight(surface);
			int z = chunkZ + random.nextInt(16);

			if (genOre(world, oreBlock, newOre.metadata, x, y, z, 0)) {
				if (WTFOresConfig.enableDenseOres) {
					counter -= 2;
				} else {
					counter--;
				}
			}
		}
	}

	private void genVertical(AddCustomOre newOre) {
		if (newOre.oreBlock == net.minecraft.init.Blocks.redstone_ore) {
			genVerticalRedstone(newOre);

		} else {

			Block oreBlock = newOre.oreBlock;

			int counter = MathHelper.floor_float(newOre.getPerChunk(biomeTypes) * surfaceMod);

			int length = newOre.var1;

			for (int fail = 0; (counter > 0) && (fail < 1000); fail++) {
				int height = newOre.getHeight(surface);

				float slopeXY = (random.nextFloat() - 0.5F) / 3.0F;
				float slopeZY = (random.nextFloat() - 0.5F) / 3.0F;
				int startX = random.nextInt(6) + 8;
				int startZ = random.nextInt(6) + 8;
				length = length + random.nextInt(5) + random.nextInt(5) - 4;

				for (int loopY = 0; loopY < length; loopY++) {
					int x = MathHelper.floor_double(chunkX + startX + slopeXY * loopY);
					int z = MathHelper.floor_double(chunkZ + startZ + slopeZY * loopY);
					int y = height - loopY;

					int densityToSet = random.nextInt(3);

					if (!WTFOresConfig.enableDenseOres)
						densityToSet = 0;
					if (genOre(world, oreBlock, newOre.metadata, x, y, z, densityToSet)) {
						if (WTFOresConfig.enableDenseOres) {
							counter -= 3 - densityToSet;
						} else {
							counter--;
						}
					}
				}
			}
		}
	}

	private void genVerticalRedstone(AddCustomOre newOre) {
		Block oreBlock = newOre.oreBlock;

		int counter = MathHelper.floor_float(newOre.getPerChunk(biomeTypes) * surfaceMod);

		int length = newOre.var1;

		for (int fail = 0; (counter > 0) && (fail < 1000); fail++) {
			float slopeXY = (random.nextFloat() - 0.5F) / 3.0F;
			float slopeZY = (random.nextFloat() - 0.5F) / 3.0F;
			int startX = random.nextInt(6) + 8;
			int startZ = random.nextInt(6) + 8;
			length = length + random.nextInt(5) + random.nextInt(5) - 4;
			int height = newOre.getHeight(surface) + length;

			int x = MathHelper.floor_double(chunkX + startX);
			int z = MathHelper.floor_double(chunkZ + startZ);

			for (int loopY = 0; loopY < length; loopY++) {
				x = MathHelper.floor_double(chunkX + startX + slopeXY * loopY);
				z = MathHelper.floor_double(chunkZ + startZ + slopeZY * loopY);
				int y = height - loopY;

				int densityToSet = random.nextInt(3);

				if (!WTFOresConfig.enableDenseOres) {
					densityToSet = 0;
				}
				if (genOre(world, oreBlock, newOre.metadata, x, y, z, densityToSet)) {
					if (WTFOresConfig.enableDenseOres) {
						counter -= 3 - densityToSet;
					} else {
						counter--;
					}

					if ((InterModBlocks.gen != null) && (world.isAirBlock(x, y - 1, z))) {
						int blocksRemaining = length - loopY;
						y--;

						Block[] speleothemSet = InterModBlocks.unlitRedstoneSpeleothems;
						int stalactiteCounter = 0;

						int numToSet;
						if ((blocksRemaining > 1) && (world.isAirBlock(x, y - 1, z))) {
							numToSet = 1;
						} else {
							numToSet = 0;
						}
						gen.setBlockWithoutNotify(world, x, y, z, speleothemSet[numToSet], 0);
						if (WTFOresConfig.enableDenseOres) {
							counter += 3 - densityToSet;
						} else {
							counter++;
						}
						height--;
						length--;

						boolean endLoop = false;
						for (int i = 1; (i < blocksRemaining) && (!endLoop); i++) {
							boolean isDown1Air = world.isAirBlock(x, y - (i + 1), z);

							if (isDown1Air) {
								if (i == blocksRemaining - 1) {
									numToSet = 2;
									endLoop = true;
								} else {
									numToSet = 3;
								}

							} else if (genOre(world, oreBlock, newOre.metadata, x, y - (i + 1), z, densityToSet)) {
								if (WTFOresConfig.enableDenseOres) {
									counter += 3 - densityToSet;
								} else {
									counter++;
								}
								height--;
								length--;
								numToSet = 5;
								endLoop = true;
							} else {
								numToSet = 2;
								endLoop = true;
							}

							gen.setBlockWithoutNotify(world, x, y - i, z, speleothemSet[numToSet], 0);
							if (WTFOresConfig.enableDenseOres) {
								counter -= 3 - densityToSet;
							} else {
								counter++;
							}
							height--;
							length--;
						}
					}
				}
			}
		}
	}

	private void genStar(AddCustomOre newOre) {
		Block oreBlock = newOre.oreBlock;
		int metadata = newOre.metadata;
		int counter = MathHelper.floor_float(newOre.getPerChunk(biomeTypes) * surfaceMod);

		for (int fail = 0; (fail < 1000) && (counter > 0); fail++) {
			int x = chunkX + random.nextInt(12) + 2;
			int y = newOre.getHeight(surface);
			if ((y > 1) && (y < 255)) {
				int z = chunkZ + random.nextInt(16);
				Block targetBlock = world.getBlock(x, y, z);
				Block blockToSet = (Block) BlockSets.oreUbifier
						.get(new OreBlockInfo(oreBlock, metadata, targetBlock, random.nextInt(3)));
				if (blockToSet != null) {
					if (genOre(world, oreBlock, metadata, x, y, z, random.nextInt(3)))
						counter--;
					if (genOre(world, oreBlock, metadata, x + 1, y, z, random.nextInt(3)))
						counter--;
					if (genOre(world, oreBlock, metadata, x - 1, y, z, random.nextInt(3)))
						counter--;
					if (genOre(world, oreBlock, metadata, x, y + 1, z, random.nextInt(3)))
						counter--;
					if (genOre(world, oreBlock, metadata, x, y - 1, z, random.nextInt(3)))
						counter--;
					if (genOre(world, oreBlock, metadata, x, y, z + 1, random.nextInt(3)))
						counter--;
					if (genOre(world, oreBlock, metadata, x, y, z - 1, random.nextInt(3)))
						counter--;
				}
			}
		}
	}

	private void genCaveFloor(AddCustomOre newOre) {
		if ((Loader.isModLoaded("CaveBiomes")) && (cavepositions.size() > 0)) {
			float numToGenerate = newOre.getPerChunk(biomeTypes) / 16384.0F * cavepositions.size()
					* (random.nextFloat() + 0.5F);
			while (numToGenerate > 1.0F) {
				CavePosition pos = (CavePosition) cavepositions.get(random.nextInt(cavepositions.size()));
				int densityToSet = random.nextInt(3);
				if (!WTFOresConfig.enableDenseOres)
					densityToSet = 0;
				if (genOre(world, newOre.oreBlock, newOre.metadata, pos.x, pos.floor, pos.z, densityToSet)) {
					numToGenerate -= 1.0F;
				}
			}

			if (random.nextFloat() > numToGenerate) {
				CavePosition pos = (CavePosition) cavepositions.get(random.nextInt(cavepositions.size()));
				int densityToSet = random.nextInt(3);
				if (!WTFOresConfig.enableDenseOres)
					densityToSet = 0;
				if (genOre(world, newOre.oreBlock, newOre.metadata, pos.x, pos.floor, pos.z, densityToSet)) {
					numToGenerate -= 1.0F;
				}
			}
		}
	}

	private boolean genOre(World world, Block oreBlock, int parentMeta, int x, int y, int z, int density) {
		if ((y > 0) && (y < 256)) {
			BlockInfo blockandmeta = gen.getBlockToReplace(world, x, y, z);

			Block blockToSet = (Block) BlockSets.oreUbifier
					.get(new OreBlockInfo(oreBlock, parentMeta, blockandmeta.block, density));
			if (blockToSet != null) {
				gen.setBlockWithoutNotify(world, x, y, z, blockToSet, blockandmeta.meta);
				return true;
			}
		}
		return false;
	}
}
