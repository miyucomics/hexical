package miyucomics.hexical.features.evocation

import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.features.player.getHexicalPlayerManager
import miyucomics.hexical.features.player.types.PlayerField
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList

class EvocationField : PlayerField {
	var active: Boolean = false
	var duration: Int = -1
	var evocation: NbtList = NbtList()

	override fun readNbt(compound: NbtCompound) {
		this.evocation = compound.getList("evocation_hex", NbtElement.COMPOUND_TYPE.toInt())
	}

	override fun writeNbt(compound: NbtCompound) {
		compound.putList("evocation_hex", evocation)
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
var PlayerEntity.evocation: NbtList
	get() = this.getHexicalPlayerManager().get(EvocationField::class).evocation
	set(hex) { this.getHexicalPlayerManager().get(EvocationField::class).evocation = hex }