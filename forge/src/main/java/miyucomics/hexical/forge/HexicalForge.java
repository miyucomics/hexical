package miyucomics.hexical.forge;

import dev.architectury.platform.forge.EventBuses;
import miyucomics.hexical.Hexical;
import miyucomics.hexical.HexicalClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Hexical.MOD_ID)
public class HexicalForge {
	public HexicalForge() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		EventBuses.registerModEventBus(Hexical.MOD_ID, bus);
		bus.addListener(HexicalClientForge::init);
		Hexical.init();
	}
}