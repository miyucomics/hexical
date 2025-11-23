package miyucomics.hexical.features.item_cache

import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.features.player.getHexicalPlayerManager
import miyucomics.hexical.features.player.types.PlayerField
import net.minecraft.entity.player.PlayerEntity

// Hexical adds a lot of items that require quick and easy access, e.g. driver dots, scarabs, and grimoires
// Rather than do an inventory scan *per-pattern* which would outrageously tank performance,
// there is an item cache that is mixined into every player and updates at the start of every tick
// Then, it is trivial to look up whether a driver dot hex is registered for a given pattern
// or if a given pattern has a grimoire expansion associated with it
// or if the player has a scarab
class PlayerItemCacheField : PlayerField {
	val driverDotsMacros: HashMap<String, List<Iota>> = HashMap()
	val grimoireMacros: HashMap<String, List<Iota>> = HashMap()
	var scarabProgram: List<Iota>? = null
}

fun PlayerEntity.itemCache(): PlayerItemCacheField = this.getHexicalPlayerManager().get(PlayerItemCacheField::class)