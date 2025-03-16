package miyucomics.hexical.data

import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

object LedgerData {
	private var ledgers = HashMap<UUID, LedgerInstance>()

	@JvmStatic
	fun getLedger(player: ServerPlayerEntity) = ledgers.computeIfAbsent(player.uuid) { LedgerInstance() }
	fun clearLedger(player: ServerPlayerEntity) {
		ledgers[player.uuid] = LedgerInstance()
	}
}