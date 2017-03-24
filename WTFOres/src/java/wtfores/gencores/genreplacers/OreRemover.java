package wtfores.gencores.genreplacers;

import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;
import wtfcore.api.BlockInfo;
import wtfcore.api.Replacer;

public class OreRemover extends Replacer {
	public OreRemover(Block block) {
		super(block);
	}

	public void doReplace(Chunk chunk, int x, int y, int z, Block oldBlock) {
		if (Loader.isModLoaded("UndergroundBiomes")) {
			BlockInfo ubcblock = getUBCStone(chunk.worldObj, x, y, z);
			setBlockWithoutNotify(chunk.worldObj, x, y, z, ubcblock.block, ubcblock.meta);
		} else {
			setBlockWithoutNotify(chunk.worldObj, x, y, z, Blocks.stone, 0);
		}
	}
}
