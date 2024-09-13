package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getList
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.inits.HexicalSounds
import miyucomics.hexical.state.EvokeState
import miyucomics.hexical.state.PersistentStateHandler
import miyucomics.hexical.state.PersistentStateHandler.Companion.getEvocation
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory

class OpInternalizeHex : SpellAction {
	override val argc = 1
	override val isGreat = true
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		args.getList(0, argc)
		CastingUtils.assertNoTruename(args[0], ctx.caster)
		return Triple(Spell(args[0]), MediaConstants.CRYSTAL_UNIT, listOf())
	}

	private data class Spell(val hex: Iota) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			PersistentStateHandler.setEvocation(ctx.caster, HexIotaTypes.serialize(hex))
		}
	}

	companion object {
		fun evoke(player: ServerPlayerEntity) {
			EvokeState.duration[player.uuid] = HexicalMain.EVOKE_DURATION
			val hex = getEvocation(player) ?: return
			CastingUtils.castSpecial(player.world as ServerWorld, player, (HexIotaTypes.deserialize(hex, player.world as ServerWorld) as ListIota).list.toList(), SpecializedSource.EVOCATION, false)
			player.world.playSound(null, player.x, player.y, player.z, HexicalSounds.EVOKING_CAST, SoundCategory.PLAYERS, 1f, 1f)
		}
	}
}