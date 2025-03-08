package miyucomics.hexical.casting.patterns.telepathy

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.registry.HexicalAdvancements
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent

class OpHallucinateSound(private val sound: RegistryEntry<SoundEvent>) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val caster = env.castingEntity
		if (caster !is ServerPlayerEntity)
			return listOf()
		caster.networkHandler.sendPacket(PlaySoundS2CPacket(sound, SoundCategory.MASTER, caster.x, caster.y, caster.z, 1f, 1f, 0))
		HexicalAdvancements.HALLUCINATE.trigger(caster)
		return listOf()
	}
}