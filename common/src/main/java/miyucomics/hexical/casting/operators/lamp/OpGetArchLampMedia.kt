package miyucomics.hexical.casting.operators.lamp

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.casting.mishaps.NeedsActiveArchLampMishap
import miyucomics.hexical.items.ArchLampItem
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.entity.ai.pathing.PathNodeType
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.util.math.BlockPos

class OpGetArchLampMedia : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if (!CastingUtils.doesPlayerHaveActiveArchLamp(ctx.caster))
			throw NeedsActiveArchLampMishap()
		for (stack in ctx.caster.inventory.main)
			if (stack.item == HexicalItems.ARCH_LAMP_ITEM && stack.orCreateNbt.getBoolean("active"))
				return listOf(DoubleIota((stack.item as ArchLampItem).getMedia(stack).toDouble() / MediaConstants.DUST_UNIT))
		for (stack in ctx.caster.inventory.offHand)
			if (stack.item == HexicalItems.ARCH_LAMP_ITEM && stack.orCreateNbt.getBoolean("active"))
				return listOf(DoubleIota((stack.item as ArchLampItem).getMedia(stack).toDouble() / MediaConstants.DUST_UNIT))
		return listOf(NullIota())
	}
}