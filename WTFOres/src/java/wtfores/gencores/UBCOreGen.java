package wtfores.gencores;

import exterminatorJeff.undergroundBiomes.api.BlockCodes;
import exterminatorJeff.undergroundBiomes.api.UBAPIHook;
import exterminatorJeff.undergroundBiomes.api.UBDimensionalStrataColumnProvider;
import exterminatorJeff.undergroundBiomes.api.UBStrataColumn;
import exterminatorJeff.undergroundBiomes.api.UBStrataColumnProvider;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import wtfcore.api.BlockInfo;
import wtfcore.api.BlockSets;
import wtfcore.api.BlockSets.Modifier;

public class UBCOreGen extends VOreGen {
	public UBCOreGen() {
	}

	private static int DimensionID = 0;

	protected BlockInfo getUBCStone(World world, int x, int y, int z) {
		UBStrataColumnProvider columnProvider = UBAPIHook.ubAPIHook.dimensionalStrataColumnProvider.ubStrataColumnProvider(DimensionID);
		UBStrataColumn column = columnProvider.strataColumn(x, z);
		BlockCodes stoneCode = column.stone(y);
		return new BlockInfo(stoneCode.block, stoneCode.metadata);
	}

	public BlockInfo getBlockToReplace(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if (block.hashCode() == Blocks.stone.hashCode()) {
			return getUBCStone(world, x, y, z);
		}
		return new BlockInfo(block, world.getBlockMetadata(x, y, z));
	}

	public void genFloatingStone(World world, int x, int y, int z, BlockSets.Modifier modifier) {
		BlockInfo blockandmeta = getUBCStone(world, x, y, z);
		Block blockToSet = (Block) BlockSets.blockTransformer.get(new BlockInfo(blockandmeta.block, blockandmeta.meta, modifier));
		if (blockToSet != null) {
			setBlockWithoutNotify(world, x, y + 1, z, blockToSet, blockandmeta.meta);
		}
	}
}
