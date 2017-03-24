package wtfores;

import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;

public class VanillOreGenCatcher {
	public VanillOreGenCatcher() {
	}

	@cpw.mods.fml.common.eventhandler.SubscribeEvent
	public void catchOreGen(GenerateMinable event) {
		if ((event.type == EventType.COAL) ||
			(event.type == EventType.IRON) ||
			(event.type == EventType.GOLD) ||
			(event.type == EventType.DIAMOND) ||
			(event.type == EventType.LAPIS) ||
			(event.type == EventType.REDSTONE) ||
			(event.type == EventType.QUARTZ)) {

			event.setResult(Result.DENY);
		}
	}
}
