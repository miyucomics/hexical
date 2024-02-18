package miyucomics.hexical.api.config;

public class HexicalConfig {
	private static final CommonConfigAccess dummyCommon = new CommonConfigAccess() {};
	private static final ClientConfigAccess dummyClient = new ClientConfigAccess() {};
	private static final ServerConfigAccess dummyServer = new ServerConfigAccess() {};
	private static CommonConfigAccess common = dummyCommon;
	private static ClientConfigAccess client = dummyClient;
	private static ServerConfigAccess server = dummyServer;

	public interface CommonConfigAccess {}
	public interface ClientConfigAccess {}
	public interface ServerConfigAccess {}
	public static CommonConfigAccess getCommon() { return common; }
	public static ClientConfigAccess getClient() { return client; }
	public static ServerConfigAccess getServer() { return server; }

	public static void setCommon(CommonConfigAccess common) {
		HexicalConfig.common = common;
	}

	public static void setClient(ClientConfigAccess client) {
		HexicalConfig.client = client;
	}

	public static void setServer(ServerConfigAccess server) {
		HexicalConfig.server = server;
	}
}