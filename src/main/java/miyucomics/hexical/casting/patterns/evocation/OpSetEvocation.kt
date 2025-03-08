package miyucomics.hexical.casting.patterns.evocation

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.environments.EvocationCastEnv
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import miyucomics.hexical.registry.HexicalAdvancements
import miyucomics.hexical.registry.HexicalSounds
import miyucomics.hexical.state.EvokeState
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand

class OpSetEvocation : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()
		args.getList(0, argc)
		CastingUtils.assertNoTruename(args[0], env)
		return SpellAction.Result(Spell(args[0]), MediaConstants.CRYSTAL_UNIT, listOf())
	}

	private data class Spell(val hex: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			(env.castingEntity as PlayerEntityMinterface).setEvocation(IotaType.serialize(hex))
		}
	}

	companion object {
		@JvmStatic
		fun evoke(player: ServerPlayerEntity) {
			player.incrementStat(HexicalAdvancements.EVOCATION_STATISTIC)

			EvokeState.duration[player.uuid] = HexicalMain.EVOKE_DURATION
			val nbt = (player as PlayerEntityMinterface).getEvocation()
			val hex = IotaType.deserialize(nbt, player.world as ServerWorld)
			if (hex is ListIota) {
				val hand = if(!player.getStackInHand(Hand.MAIN_HAND).isEmpty && player.getStackInHand(Hand.OFF_HAND).isEmpty){ Hand.OFF_HAND } else { Hand.MAIN_HAND }
				val vm = CastingVM(CastingImage(), EvocationCastEnv(player, hand))
				vm.queueExecuteAndWrapIotas(hex.list.toList(), player.serverWorld)
				player.world.playSound(null, player.x, player.y, player.z, HexicalSounds.EVOKING_CAST, SoundCategory.PLAYERS, 1f, 1f)
			}
		}
	}
}