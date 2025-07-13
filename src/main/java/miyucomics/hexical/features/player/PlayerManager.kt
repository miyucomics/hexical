package miyucomics.hexical.features.player

import miyucomics.hexical.features.player.fields.*
import miyucomics.hexical.features.player.fields.MediaLogField
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import kotlin.reflect.KClass

class PlayerManager {
	@Suppress("UNCHECKED_CAST")
	fun <T : PlayerField> get(clazz: KClass<T>): T { return fields[clazz] as? T ?: error("No field registered for $clazz") }
	private fun registerField(field: PlayerField) { fields[field::class] = field }
	private val fields = mutableMapOf<KClass<out PlayerField>, PlayerField>()

	init {
		registerField(ArchLampField())
		registerField(EvocationField())
		registerField(KeybindField())
		registerField(MediaLogField())
		registerField(LesserSentinelField())
		registerField(WristpocketField())
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

fun PlayerEntity.getHexicalPlayerManager() = (this as PlayerEntityMinterface).getPlayerManager()