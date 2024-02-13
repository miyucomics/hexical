package miyucomics.hexical.fabric;

import at.petrak.hexcasting.api.misc.MediaConstants;
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

		if (Platform.getEnv().equals(EnvType.CLIENT)) {
			HexicalConfig.setClient(instance.client);
		}

		// Needed for logical server in singleplayer, do not access server configs from client code
		HexicalConfig.setServer(instance.server);
	}


	@Config(name = "common")
	private static class Common implements ConfigData, HexicalConfig.CommonConfigAccess {
	}

	@Config(name = "client")
	private static class Client implements ConfigData, HexicalConfig.ClientConfigAccess {
	}


	@Config(name = "server")
	private static class Server implements ConfigData, HexicalConfig.ServerConfigAccess {

		@ConfigEntry.Gui.CollapsibleObject
		private Costs costs = new Costs();

		@Override
		public void validatePostLoad() throws ValidationException {
			this.costs.signumCost = HexicalConfig.bound(this.costs.signumCost, DEF_MIN_COST, DEF_MAX_COST);
			this.costs.congratsCost = HexicalConfig.bound(this.costs.congratsCost, DEF_MIN_COST, DEF_MAX_COST);
		}

		@Override
		public int getSignumCost() {
			return (int) (costs.signumCost * MediaConstants.DUST_UNIT);
		}

		@Override
		public int getCongratsCost() {
			return (int) (costs.congratsCost * MediaConstants.DUST_UNIT);
		}

		static class Costs {
			// costs of actions
			double signumCost = DEFAULT_SIGNUM_COST;
			double congratsCost = DEFAULT_CONGRATS_COST;
		}
	}
}