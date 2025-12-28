package miyucomics.hexical.features.mute

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import miyucomics.hexical.inits.HexicalCardinalComponents
import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound

class MutedComponent(private var entity: Entity) : AutoSyncedComponent {
	var muted = false

	fun toggleMute() {
		this.muted = !this.muted
		HexicalCardinalComponents.MUTED_COMPONENT.sync(entity)
	}

	override fun readFromNbt(compound: NbtCompound) {
		this.muted = compound.getBoolean("muted")
	}

	override fun writeToNbt(compound: NbtCompound) {
		compound.putBoolean("muted", muted)
	}
}