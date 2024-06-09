package miyucomics.hexical.client

import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.api.layered.ModifierLayer

interface PlayerAnimations {
	fun hexical_getModAnimation(): ModifierLayer<IAnimation>
}