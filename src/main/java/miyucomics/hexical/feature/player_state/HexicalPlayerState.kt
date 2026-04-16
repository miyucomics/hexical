package miyucomics.hexical.feature.player_state

import miyucomics.hexical.feature.evocation.EvocationField
import miyucomics.hexical.feature.evocation.EvocationTicker
import miyucomics.hexical.feature.item_cache.PlayerItemCacheField
import miyucomics.hexical.feature.item_cache.PlayerItemCacheTicker
import miyucomics.hexical.feature.lesser_sentinel.LesserSentinelField
import miyucomics.hexical.feature.media_log.MediaLogField
import miyucomics.hexical.feature.player_state.types.PlayerField
import miyucomics.hexical.feature.player_state.types.PlayerTicker
import miyucomics.hexical.feature.telepathy.KeybindField
import miyucomics.hexical.feature.telepathy.KeybindTicker
import miyucomics.hexical.feature.wristpocket.WristpocketField
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import kotlin.reflect.KClass

class HexicalPlayerState {
	@Suppress("UNCHECKED_CAST")
	fun <T : PlayerField> get(clazz: KClass<T>): T { return fields[clazz] as? T ?: error("No field registered for $clazz") }
	private val fields = mutableMapOf<KClass<out PlayerField>, PlayerField>()
	private fun registerField(field: PlayerField) { fields[field::class] = field }

	private val tickers = mutableListOf<PlayerTicker>()
	private fun registerTicker(ticker: PlayerTicker) { tickers.add(ticker) }

	init {
		registerField(EvocationField())
		registerField(KeybindField())
		registerField(MediaLogField())
		registerField(LesserSentinelField())
		registerField(PlayerItemCacheField())
		registerField(WristpocketField())
		registerTicker(EvocationTicker())
		registerTicker(KeybindTicker())
		registerTicker(PlayerItemCacheTicker())
	}

	fun tick(player: PlayerEntity) {
		for (ticker in tickers)
			ticker.tick(player)
	}

	fun readNbt(compound: NbtCompound) {
		for (field in fields.values)
			field.readNbt(compound)
	}

	fun writeNbt(compound: NbtCompound) {
		for (field in fields.values)
			field.writeNbt(compound)
	}

	fun handleRespawn(new: PlayerEntity, old: PlayerEntity) {
		for (field in fields)
			field.value.handleRespawn(new, old)
	}
}

fun PlayerEntity.getHexicalPlayerState() = (this as PlayerEntityMinterface).getHexicalPlayerState()