package miyucomics.hexical.client

import miyucomics.hexical.state.LedgerData

object ClientStorage {
	@JvmField
	var time: Int = 0
	var ledger: LedgerData = LedgerData()
}