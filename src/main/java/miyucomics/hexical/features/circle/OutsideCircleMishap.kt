package miyucomics.hexical.features.circle

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import miyucomics.hexical.HexicalMain
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

class OutsideCircleMishap : Mishap() {
	override fun accentColor(env: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.GREEN)
	override fun errorMessage(env: CastingEnvironment, errorCtx: Context): Text = error(HexicalMain.MOD_ID + ":outside_circle")
	override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {
		if (env.castingEntity == null)
			return
		env.castingEntity!!.addStatusEffect(StatusEffectInstance(StatusEffects.SLOWNESS, 60))
	}
}