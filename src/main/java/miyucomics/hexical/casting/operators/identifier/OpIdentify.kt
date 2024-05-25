package miyucomics.hexical.casting.operators.identifier

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.iota.IdentifierIota
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry

class OpIdentify : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		return when (val arg = args[0]) {
			is EntityIota -> listOf(IdentifierIota(Registry.ENTITY_TYPE.getId(args.getEntity(0, argc).type)))
			is Vec3Iota -> {
				ctx.assertVecInRange(arg.vec3)
				listOf(IdentifierIota(Registry.BLOCK.getId(ctx.world.getBlockState(BlockPos(arg.vec3)).block)))
			}
			else -> throw MishapInvalidIota.of(arg, 0, "identifiable")
		}
	}
}