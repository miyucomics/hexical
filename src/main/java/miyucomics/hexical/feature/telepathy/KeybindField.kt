package miyucomics.hexical.feature.telepathy

import miyucomics.hexical.feature.player_state.getHexicalPlayerState
import miyucomics.hexical.feature.player_state.types.PlayerField
import net.minecraft.entity.player.PlayerEntity

class KeybindField : PlayerField {
	var active: HashMap<String, Boolean> = HashMap()
	var duration: HashMap<String, Int> = HashMap()
	var scroll: Int = 0
}

fun PlayerEntity.serverKeybindActive() = this.getHexicalPlayerState().get(KeybindField::class).active
fun PlayerEntity.serverKeybindDuration() = this.getHexicalPlayerState().get(KeybindField::class).duration
var PlayerEntity.serverScroll: Int
	get() = this.getHexicalPlayerState().get(KeybindField::class).scroll
	set(scroll) { this.getHexicalPlayerState().get(KeybindField::class).scroll = scroll }