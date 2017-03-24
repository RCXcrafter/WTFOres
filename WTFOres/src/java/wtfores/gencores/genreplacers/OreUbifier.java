package wtfores.gencores.genreplacers;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;
import wtfcore.api.BlockInfo;
import wtfcore.api.OreBlockInfo;
import wtfcore.api.Replacer;

public class OreUbifier extends Replacer {
	public OreUbifier(Block block) {
		super(block);
	}

	public void doReplace(Chunk chunk, int x, int y, int z, Block oldBlock) {
		BlockInfo ubcblock = getUBCStone(chunk.worldObj, x, y, z);
		Block blockToSet = (Block) wtfcore.api.BlockSets.oreUbifier.get(new OreBlockInfo(oldBlock, chunk.worldObj.getBlockLightOpacity(x, y, z), ubcblock.block, 0));
		setBlockWithoutNotify(chunk.worldObj, x, y, z, blockToSet, ubcblock.meta);
	}
}
