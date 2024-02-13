package miyucomics.hexical.api.config;

import miyucomics.hexical.Hexical;

/**
 * Platform-agnostic class for statically accessing current config values.
 * If any of the config types (common, client, server) are not needed in your mod,
 * feel free to remove anything related to them in this class and platform-specific config implementations.
 */
public class HexicalConfig {
	private static final CommonConfigAccess dummyCommon = new CommonConfigAccess() {
	};
	private static final ClientConfigAccess dummyClient = new ClientConfigAccess() {
	};
	private static final ServerConfigAccess dummyServer = new ServerConfigAccess() {
		@Override
		public int getCongratsCost() {
			throw new IllegalStateException("Attempted to access property of Dummy Config Object");
		}

		@Override
		public int getSignumCost() {
			throw new IllegalStateException("Attempted to access property of Dummy Config Object");
		}
	};
	private static CommonConfigAccess common = dummyCommon;
	private static ClientConfigAccess client = dummyClient;
	private static ServerConfigAccess server = dummyServer;

	public static CommonConfigAccess getCommon() {
		return common;
	}

	public static void setCommon(CommonConfigAccess common) {
		if (HexicalConfig.common != dummyCommon) {
			Hexical.LOGGER.warn("CommonConfigAccess was replaced! Old {} New {}", HexicalConfig.common.getClass().getName(), common.getClass().getName());
		}
		HexicalConfig.common = common;
	}

	public static ClientConfigAccess getClient() {
		return client;
	}

	public static void setClient(ClientConfigAccess client) {
		if (HexicalConfig.client != dummyClient) {
			Hexical.LOGGER.warn("ClientConfigAccess was replaced! Old {} New {}", HexicalConfig.client.getClass().getName(), client.getClass().getName());
		}
		HexicalConfig.client = client;
	}

	public static ServerConfigAccess getServer() {
		return server;
	}

	public static void setServer(ServerConfigAccess server) {

		if (HexicalConfig.server != dummyServer) {
			Hexical.LOGGER.warn("ServerConfigAccess was replaced! Old {} New {}", HexicalConfig.server.getClass().getName(), server.getClass().getName());
		}
		HexicalConfig.server = server;
	}

	public static int bound(int toBind, int lower, int upper) {
		return Math.min(Math.max(toBind, lower), upper);
	}

	public static double bound(double toBind, double lower, double upper) {
		return Math.min(Math.max(toBind, lower), upper);
	}

	public interface CommonConfigAccess {
	}

	public interface ClientConfigAccess {
	}

	public interface ServerConfigAccess {
		double DEF_MIN_COST = 0.0001;
		double DEF_MAX_COST = 10_000.0;
		double DEFAULT_CONGRATS_COST = 1.5;
		double DEFAULT_SIGNUM_COST = 1.0;

		int getCongratsCost();

		int getSignumCost();
	}
}