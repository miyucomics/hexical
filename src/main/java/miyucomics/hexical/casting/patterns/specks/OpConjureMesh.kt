package miyucomics.hexical.casting.patterns.specks

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.entities.MeshEntity
import miyucomics.hexical.entities.SpeckEntity
import miyucomics.hexical.registry.HexicalAdvancements
import net.minecraft.command.argument.EntityAnchorArgumentType

class OpConjureMesh : ConstMediaAction {
	override val argc = 1
	override val mediaCost: Int = MediaConstants.DUST_UNIT / 10
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val position = args.getVec3(0, argc)
		ctx.assertVecInRange(position)
		val mesh = MeshEntity(ctx.world)
		mesh.setPosition(position)
		mesh.setPigment(IXplatAbstractions.INSTANCE.getColorizer(ctx.caster))
		mesh.setSize(1f)
		mesh.setThickness(1f)
		ctx.world.spawnEntity(mesh)
		HexicalAdvancements.AR.trigger(ctx.caster)
		return listOf(EntityIota(mesh))
	}
}