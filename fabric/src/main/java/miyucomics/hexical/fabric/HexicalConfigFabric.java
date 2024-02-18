package miyucomics.hexical.fabric;

import dev.architectury.platform.Platform;
import miyucomics.hexical.Hexical;
import miyucomics.hexical.api.config.HexicalConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.EnvType;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
@Config(name = Hexical.MOD_ID)
public class HexicalConfigFabric extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("common")
	@ConfigEntry.Gui.TransitiveObject
	public final Common common = new Common();
	@ConfigEntry.Category("client")
	@ConfigEntry.Gui.TransitiveObject
	public final Client client = new Client();
	@ConfigEntry.Category("server")
	@ConfigEntry.Gui.TransitiveObject
	public final Server server = new Server();

	public static void init() {
		AutoConfig.register(HexicalConfigFabric.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		var instance = AutoConfig.getConfigHolder(HexicalConfigFabric.class).getConfig();
		HexicalConfig.setCommon(instance.common);
		if (Platform.getEnv().equals(EnvType.CLIENT))
			HexicalConfig.setClient(instance.client);
		HexicalConfig.setServer(instance.server);
	}

	@Config(name = "common")
	private static class Common implements ConfigData, HexicalConfig.CommonConfigAccess {}
	@Config(name = "client")
	private static class Client implements ConfigData, HexicalConfig.ClientConfigAccess {}
	@Config(name = "server")
	private static class Server implements ConfigData, HexicalConfig.ServerConfigAccess {
		@Override
		public void validatePostLoad() {}
	}
}