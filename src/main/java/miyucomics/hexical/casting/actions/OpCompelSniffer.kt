package miyucomics.hexical.casting.actions

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.features.lei.SnifferEntityMinterface
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.entity.passive.SnifferEntity
import net.minecraft.item.ItemStack

object OpCompelSniffer : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val sniffer = args.getEntity(0, argc)
		env.assertEntityInRange(sniffer)
		if (sniffer !is SnifferEntity)
			throw MishapBadEntity.of(sniffer, "sniffer")
		return SpellAction.Result(Spell(sniffer), MediaConstants.CRYSTAL_UNIT, listOf(ParticleSpray.cloud(sniffer.eyePos, 1.0)))
	}

	private data class Spell(val sniffer: SnifferEntity) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			(sniffer as SnifferEntityMinterface).produceItem(ItemStack(HexicalBlocks.PERIWINKLE_FLOWER_ITEM, sniffer.random.nextBetween(5, 7)))
		}
	}
}