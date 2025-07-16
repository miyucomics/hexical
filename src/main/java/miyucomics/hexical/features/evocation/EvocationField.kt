package miyucomics.hexical.features.evocation

import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.features.player.getHexicalPlayerManager
import miyucomics.hexical.features.player.types.PlayerField
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound

class EvocationField : PlayerField {
	var active: Boolean = false
	var duration: Int = -1
	var evocation: NbtCompound? = NbtCompound()

	override fun readNbt(compound: NbtCompound) {
		if (compound.contains("evocation"))
			this.evocation = compound.getCompound("evocation")
	}

	override fun writeNbt(compound: NbtCompound) {
		this.evocation?.let { compound.putCompound("evocation", it) }
	}

	override fun handleRespawn(new: PlayerEntity, old: PlayerEntity) {
		new.evocation = old.evocation
	}
}

var PlayerEntity.evocationActive: Boolean
	get() = this.getHexicalPlayerManager().get(EvocationField::class).active
	set(active) { this.getHexicalPlayerManager().get(EvocationField::class).active = active }
var PlayerEntity.evocationDuration: Int
	get() = this.getHexicalPlayerManager().get(EvocationField::class).duration
	set(duration) { this.getHexicalPlayerManager().get(EvocationField::class).duration = duration }
var PlayerEntity.evocation: NbtCompound?
	get() = this.getHexicalPlayerManager().get(EvocationField::class).evocation
	set(hex) { this.getHexicalPlayerManager().get(EvocationField::class).evocation = hex }