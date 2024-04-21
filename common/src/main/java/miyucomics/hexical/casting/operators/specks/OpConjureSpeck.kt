package miyucomics.hexical.casting.operators.specks

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.entities.SpeckEntity
import miyucomics.hexical.registry.HexicalAdvancements
import miyucomics.hexical.registry.HexicalEntities
import net.minecraft.command.argument.EntityAnchorArgumentType

class OpConjureSpeck : ConstMediaAction {
	override val argc = 3
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val iota = args[0]
		val position = args.getVec3(1, argc)
		val rotation = args.getVec3(2, argc)
		ctx.assertVecInRange(position)
		val speck = SpeckEntity(HexicalEntities.SPECK_ENTITY, ctx.world)
		speck.setPosition(position)
		speck.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, speck.pos.add(rotation))
		speck.setIota(iota)
		speck.setPigment(IXplatAbstractions.INSTANCE.getColorizer(ctx.caster))
		speck.setSize(1f)
		speck.setThickness(1f)
		ctx.world.spawnEntity(speck)
		HexicalAdvancements.AR.trigger(ctx.caster)
		return listOf(EntityIota(speck))
	}
}