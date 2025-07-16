package miyucomics.hexical.features.peripherals

import miyucomics.hexical.features.player.getHexicalPlayerManager
import miyucomics.hexical.features.player.types.PlayerField
import net.minecraft.entity.player.PlayerEntity

class KeybindField : PlayerField {
	var active: HashMap<String, Boolean> = HashMap()
	var duration: HashMap<String, Int> = HashMap()
	var scroll: Int = 0
}

fun PlayerEntity.serverKeybindActive() = this.getHexicalPlayerManager().get(KeybindField::class).active
fun PlayerEntity.serverKeybindDuration() = this.getHexicalPlayerManager().get(KeybindField::class).duration
var PlayerEntity.serverScroll: Int
	get() = this.getHexicalPlayerManager().get(KeybindField::class).scroll
	set(scroll) { this.getHexicalPlayerManager().get(KeybindField::class).scroll = scroll }