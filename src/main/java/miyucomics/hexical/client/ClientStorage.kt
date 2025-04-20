package miyucomics.hexical.client

import miyucomics.hexical.data.LedgerInstance
import net.minecraft.util.math.Vec3d

object ClientStorage {
	@JvmField
	var ticks: Int = 0
	var ledger: LedgerInstance = LedgerInstance()
	var lesserSentinels: MutableList<Vec3d> = mutableListOf()
}