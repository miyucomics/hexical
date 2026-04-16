package miyucomics.hexical.feature.evocation

import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.feature.player_state.getHexicalPlayerState
import miyucomics.hexical.feature.player_state.types.PlayerField
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
	get() = this.getHexicalPlayerState().get(EvocationField::class).active
	set(active) { this.getHexicalPlayerState().get(EvocationField::class).active = active }
var PlayerEntity.evocationDuration: Int
	get() = this.getHexicalPlayerState().get(EvocationField::class).duration
	set(duration) { this.getHexicalPlayerState().get(EvocationField::class).duration = duration }
var PlayerEntity.evocation: NbtList
	get() = this.getHexicalPlayerState().get(EvocationField::class).evocation
	set(hex) { this.getHexicalPlayerState().get(EvocationField::class).evocation = hex }