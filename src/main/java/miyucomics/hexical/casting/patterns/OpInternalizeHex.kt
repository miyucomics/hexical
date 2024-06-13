package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingContext.CastSource
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.mishaps.MishapOthersName
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.registry.HexicalItems.NULL_MEDIA_ITEM
import miyucomics.hexical.state.EvokeState
import miyucomics.hexical.state.PersistentStateHandler
import miyucomics.hexical.state.PersistentStateHandler.Companion.getEvocation
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand

class OpInternalizeHex : SpellAction {
	override val argc = 1
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
			val hex = getEvocation((player as ServerPlayerEntity?)!!) ?: return
			val stack: ItemStack = player.mainHandStack
			player.setStackInHand(Hand.MAIN_HAND, ItemStack(NULL_MEDIA_ITEM, 1))
			val ctx = CastingContext((player as ServerPlayerEntity?)!!, Hand.MAIN_HAND, CastSource.PACKAGED_HEX)
			CastingHarness(ctx).executeIotas((HexIotaTypes.deserialize(hex, player.world as ServerWorld) as ListIota).list.toList(), player.world as ServerWorld)
			player.setStackInHand(Hand.MAIN_HAND, stack)
			EvokeState.duration[player.uuid] = 0
		}
	}
}