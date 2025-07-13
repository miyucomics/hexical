package miyucomics.hexical.client

import miyucomics.hexical.features.player_state.fields.LedgerField
import net.minecraft.util.math.Vec3d

object ClientStorage {
	@JvmField
	var ticks: Int = 0
	var ledger: LedgerField = LedgerField()
	var lesserSentinels: MutableList<Vec3d> = mutableListOf()
}