package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getList
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.mishaps.MishapOthersName
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.items.lampCast
import miyucomics.hexical.registry.HexicalItems.NULL_MEDIA_ITEM
import miyucomics.hexical.state.EvokeState
import miyucomics.hexical.state.PersistentStateHandler
import miyucomics.hexical.state.PersistentStateHandler.Companion.getEvocation
import miyucomics.hexical.utils.HexicalUtils
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand

class OpInternalizeHex : SpellAction {
	override val argc = 1
	override val isGreat = true
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		args.getList(0, argc)
		val trueName = MishapOthersName.getTrueNameFromDatum(args[0], ctx.caster)
		if (trueName != null)
			throw MishapOthersName(trueName)
		return Triple(Spell(args[0]), MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val hex: Iota) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			PersistentStateHandler.setEvocation(ctx.caster, HexIotaTypes.serialize(hex))
		}
	}

	companion object {
		fun evoke(player: ServerPlayerEntity) {
			EvokeState.duration[player.uuid] = 0
			val hex = getEvocation((player as ServerPlayerEntity?)!!) ?: return
			val stack = player.mainHandStack
			player.setStackInHand(Hand.MAIN_HAND, ItemStack(NULL_MEDIA_ITEM))
			HexicalUtils.castSpecial(player.world as ServerWorld, player, (HexIotaTypes.deserialize(hex, player.world as ServerWorld) as ListIota).list.toList(), SpecializedSource.EVOCATION, false)
			player.world.playSound(null, player.x, player.y, player.z, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1f, 1f)
			player.setStackInHand(Hand.MAIN_HAND, stack)
		}
	}
}