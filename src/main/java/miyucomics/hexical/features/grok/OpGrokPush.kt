package miyucomics.hexical.features.grok

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

object OpGrokPush : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()

		val newStack = args.getList(0, argc).map { if (MishapOthersName.getTrueNameFromDatum(it, env.castingEntity as? ServerPlayerEntity) == null) it else NullIota() }
		val newParenthesized = args.getList(1, argc).map { if (MishapOthersName.getTrueNameFromDatum(it, env.castingEntity as? ServerPlayerEntity) == null) it else NullIota() }

		return SpellAction.Result(Spell(IXplatAbstractions.INSTANCE.getStaffcastVM(env.castingEntity as ServerPlayerEntity, Hand.MAIN_HAND).image.copy(
			stack = newStack.toList(),
			parenthesized = newParenthesized.map { CastingImage.ParenthesizedIota(it, true) }
		)), 0, listOf())
	}

	private data class Spell(val image: CastingImage) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			IXplatAbstractions.INSTANCE.setStaffcastImage(env.castingEntity as ServerPlayerEntity, image)
		}
	}
}