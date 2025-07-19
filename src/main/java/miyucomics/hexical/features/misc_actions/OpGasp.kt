package miyucomics.hexical.features.misc_actions

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.inits.HexicalSounds
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory

object OpGasp : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val target = args.getEntity(0, argc)
		env.assertEntityInRange(target)
		return SpellAction.Result(Spell(target), MediaConstants.DUST_UNIT, listOf(ParticleSpray.cloud(target.pos, 1.0)))
	}

	private data class Spell(val target: Entity) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			target.air = target.maxAir
			if (target is ServerPlayerEntity)
				env.world.playSound(target.x, target.y, target.z, HexicalSounds.REPLENISH_AIR, SoundCategory.MASTER, 1f, 1f, true)
		}
	}
}