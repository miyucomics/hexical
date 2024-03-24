package miyucomics.hexical.casting.spells.telepathy

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents

class OpSoundPing(private val type: Int) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		when (type) {
			0 -> ctx.caster.networkHandler.sendPacket(PlaySoundS2CPacket(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, ctx.caster.x, ctx.caster.y, ctx.caster.z, 1f, 1f, 0))
			1 -> ctx.caster.networkHandler.sendPacket(PlaySoundS2CPacket(SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, ctx.caster.x, ctx.caster.y, ctx.caster.z, 1f, 1f, 0))
		}
		return listOf()
	}
}