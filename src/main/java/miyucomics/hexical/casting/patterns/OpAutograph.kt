package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.Hand

class OpAutograph : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		return Triple(Spell(ctx.otherHand), 0, listOf())
	}

	private data class Spell(val hand: Hand) : RenderedSpell {
		override fun cast(ctx: CastingEnvironment) {
			val item = ctx.caster.getStackInHand(hand)
			if (!item.orCreateNbt.contains("autographs"))
				item.orCreateNbt.put("autographs", NbtList())

			val list = item.orCreateNbt.getList("autographs", NbtCompound.COMPOUND_TYPE.toInt())
			val compound = NbtCompound()
			compound.putString("name", ctx.caster.entityName)
			compound.putCompound("pigment", IXplatAbstractions.INSTANCE.getColorizer(ctx.caster).serializeToNBT())
			list.add(compound)
		}
	}
}