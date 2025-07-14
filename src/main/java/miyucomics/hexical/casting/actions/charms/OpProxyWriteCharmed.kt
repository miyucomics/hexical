package miyucomics.hexical.casting.actions.charms

import at.petrak.hexcasting.api.addldata.ADIotaHolder
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.casting.environments.CharmedItemCastEnv
import miyucomics.hexical.casting.mishaps.NeedsCharmedItemMishap
import miyucomics.hexical.misc.CastingUtils

class OpProxyWriteCharmed : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env !is CharmedItemCastEnv)
			throw NeedsCharmedItemMishap()

		val iota = args[0]
		val dataHolder = IXplatAbstractions.INSTANCE.findDataHolder(env.stack)
		if (dataHolder == null)
			throw MishapBadOffhandItem.of(env.stack, "iota.write")
		if (!dataHolder.writeIota(iota, true))
			throw MishapBadOffhandItem.of(env.stack, "iota.readonly", iota.display())
		CastingUtils.assertNoTruename(iota, env)

		return SpellAction.Result(Spell(iota, dataHolder), 0, listOf())
	}

	private data class Spell(val iota: Iota, val dataHolder: ADIotaHolder) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			dataHolder.writeIota(iota, false)
		}
	}
}