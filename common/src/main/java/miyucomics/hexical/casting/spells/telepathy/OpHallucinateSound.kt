package miyucomics.hexical.casting.spells.telepathy

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.registry.HexicalAdvancements
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent

class OpHallucinateSound(private val sound: SoundEvent) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		ctx.caster.networkHandler.sendPacket(PlaySoundS2CPacket(sound, SoundCategory.MASTER, ctx.caster.x, ctx.caster.y, ctx.caster.z, 1f, 1f, 0))
		HexicalAdvancements.HALLUCINATE.trigger(ctx.caster)
		return listOf()
	}
}