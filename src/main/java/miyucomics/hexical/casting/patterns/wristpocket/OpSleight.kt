package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.math.Vec3d

class OpSleight : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		when (args[0]) {
			is EntityIota -> {
				val item = args.getItemEntity(0, argc)
				ctx.assertEntityInRange(item)
				return Triple(SwapSpell(item), MediaConstants.DUST_UNIT / 4, listOf())
			}
			is Vec3Iota -> {
				val position = args.getVec3(0, argc)
				ctx.assertVecInRange(position)
				return Triple(ConjureSpell(position), MediaConstants.DUST_UNIT / 4, listOf())
			}
			else -> throw MishapInvalidIota.of(args[0], 0, "entity_or_vector")
		}
	}

	private data class ConjureSpell(val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val wristpocketed = PersistentStateHandler.getWristpocketStack(ctx.caster)
			if (wristpocketed != ItemStack.EMPTY && wristpocketed.item != Items.AIR)
				ctx.world.spawnEntity(ItemEntity(ctx.world, position.x, position.y, position.z, PersistentStateHandler.getWristpocketStack(ctx.caster)))
			PersistentStateHandler.setWristpocketStack(ctx.caster, ItemStack.EMPTY)
		}
	}

	private data class SwapSpell(val item: ItemEntity) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val wristpocketed = PersistentStateHandler.getWristpocketStack(ctx.caster)
			PersistentStateHandler.setWristpocketStack(ctx.caster, item.stack)
			if (wristpocketed != ItemStack.EMPTY && wristpocketed.item != Items.AIR)
				item.stack = wristpocketed
			else
				item.discard()
		}
	}
}