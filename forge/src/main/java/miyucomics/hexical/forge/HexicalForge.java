package miyucomics.hexical.forge;

import dev.architectury.platform.forge.EventBuses;
import miyucomics.hexical.Hexical;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * This is your loading entrypoint on forge, in case you need to initialize
 * something platform-specific.
 */
@Mod(Hexical.MOD_ID)
public class HexicalForge {
    public HexicalForge() {
        // Submit our event bus to let architectury register our content on the right time
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Hexical.MOD_ID, bus);
        bus.addListener(HexicalClientForge::init);
        Hexical.init();
    }
}
