package miyucomics.hexical.client

import miyucomics.hexical.data.LedgerInstance

object ClientStorage {
	@JvmField
	var time: Int = 0
	var ledger: LedgerInstance = LedgerInstance()
}