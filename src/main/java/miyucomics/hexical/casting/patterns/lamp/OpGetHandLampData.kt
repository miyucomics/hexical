package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.casting.mishaps.NeedsSourceMishap
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.CastingContextMinterface
import net.minecraft.nbt.NbtCompound

class OpGetHandLampData(private val process: (CastingContext, NbtCompound) -> List<Iota>) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		if ((ctx as CastingContextMinterface).getSpecializedSource() != SpecializedSource.HAND_LAMP)
			throw NeedsSourceMishap("hand_lamp")
		val nbt = ctx.caster.activeItem.nbt ?: return listOf(NullIota())
		return process(ctx, nbt)
	}
}