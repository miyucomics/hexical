package miyucomics.hexical.features.player_state.fields

import at.petrak.hexcasting.api.utils.serializeToNBT
import miyucomics.hexical.features.player_state.PlayerField
import miyucomics.hexical.features.player_state.getHexicalPlayerState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

class WristpocketField : PlayerField {
	var wristpocket: ItemStack = ItemStack.EMPTY

	override fun readNbt(compound: NbtCompound) {
		if (!compound.contains("wristpocket"))
			return
		wristpocket = ItemStack.fromNbt(compound.getCompound("wristpocket"))
	}

	override fun writeNbt(compound: NbtCompound) {
		compound.put("wristpocket", wristpocket.serializeToNBT())
	}

	override fun handleRespawn(new: PlayerEntity, old: PlayerEntity) {
		new.wristpocket = old.wristpocket
	}
}

var PlayerEntity.wristpocket: ItemStack
	get() = this.getHexicalPlayerState().get(WristpocketField::class).wristpocket
	set(stack) { this.getHexicalPlayerState().get(WristpocketField::class).wristpocket = stack }