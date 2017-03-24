package wtfores.gencores;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.BlockSnapshot;
import wtfcore.api.BlockInfo;

public class VOreGen {
	public VOreGen() {
	}

	public Random random = new Random();

	public static VOreGen getGenMethods() {
		if (cpw.mods.fml.common.Loader.isModLoaded("UndergroundBiomes")) {
			return new UBCOreGen();
		}

		return new VOreGen();
	}

	public BlockInfo getBlockToReplace(World world, int x, int y, int z) {
		return new BlockInfo(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
	}

	public boolean setBlockWithoutNotify(World world, int x, int y, int z, net.minecraft.block.Block block,
			int metadata) {
		int flags = 0;
		Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
		BlockSnapshot blockSnapshot = null;
		if (((flags & 0x1) == 0) || (

		(world.captureBlockSnapshots) && (!world.isRemote))) {
			blockSnapshot = BlockSnapshot.getBlockSnapshot(world, x, y, z, flags);
			world.capturedBlockSnapshots.add(blockSnapshot);
		}
		boolean flag = chunk.func_150807_a(x & 0xF, y, z & 0xF, block, metadata);
		if ((!flag) && (blockSnapshot != null)) {
			world.capturedBlockSnapshots.remove(blockSnapshot);
			blockSnapshot = null;
		}
		world.theProfiler.startSection("checkLight");
		world.func_147451_t(x, y, z);
		world.theProfiler.endSection();
		return flag;
	}
}
