package miyucomics.hexical.features.player_state

import miyucomics.hexical.features.player_state.fields.*
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import kotlin.reflect.KClass

class PlayerState {
	@Suppress("UNCHECKED_CAST")
	fun <T : PlayerField> get(clazz: KClass<T>): T { return fields[clazz] as? T ?: error("No field registered for $clazz") }
	private fun register(field: PlayerField) { fields[field::class] = field }
	private val fields = mutableMapOf<KClass<out PlayerField>, PlayerField>()

	init {
		register(ArchLampField())
		register(EvocationField())
		register(KeybindField())
		register(LedgerField())
		register(LesserSentinelField())
		register(WristpocketField())
	}

	fun tick(player: PlayerEntity) {
		for (field in fields.values)
			field.tick(player)
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

fun PlayerEntity.getHexicalPlayerState() = (this as PlayerEntityMinterface).getPlayerState()