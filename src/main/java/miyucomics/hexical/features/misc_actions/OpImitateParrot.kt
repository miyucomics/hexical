package miyucomics.hexical.features.misc_actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexpose.iotas.getIdentifier
import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.ParrotEntity
import net.minecraft.registry.Registries
import net.minecraft.sound.SoundCategory
import net.minecraft.util.math.Vec3d

object OpImitateParrot : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getVec3(0, argc)
		env.assertVecInRange(pos)
		val id = args.getIdentifier(1, argc)
		if (!Registries.ENTITY_TYPE.containsId(id))
			throw MishapInvalidIota.of(args[0], 0, "entity_id")
		return SpellAction.Result(Spell(pos, Registries.ENTITY_TYPE.get(id)), MediaConstants.DUST_UNIT / 2, listOf())
	}

	private data class Spell(val pos: Vec3d, val type: EntityType<*>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val sound = ParrotEntity.MOB_SOUNDS[type] ?: return
			env.world.playSound(null, pos.x, pos.y, pos.z, sound, SoundCategory.MASTER, 1.0f, ParrotEntity.getSoundPitch(env.world.random))
		}
	}
}