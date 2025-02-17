package miyucomics.hexical.utils

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate
import at.petrak.hexcasting.api.casting.eval.vm.FrameFinishEval
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.frames.ScarabFrame
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.items.ScarabBeetleItem
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

object InjectionHelper {
	@JvmStatic
	fun handleScarab(vm: CastingVM, iota: PatternIota, continuation: SpellContinuation, world: ServerWorld): CastResult? {
		val env = vm.env
		if (env !is PlayerBasedCastEnv)
			return null
		val pattern = iota.pattern
		if (ScarabBeetleItem.isScarabAlreadyActive(pattern.anglesSignature(), continuation))
			return null
		val scarab = getScarab(env.castingEntity!! as ServerPlayerEntity, pattern) ?: return null
		val expand = IotaType.deserialize(scarab.getOrCreateNbt().getCompound("expansion"), world) as? ListIota ?: return null
		return CastResult(
			iota,
			continuation
				.pushFrame(FrameFinishEval)
				.pushFrame(ScarabFrame(pattern.anglesSignature()))
				.pushFrame(FrameEvaluate(expand.list, false)),
			null,
			listOf(),
			ResolvedPatternType.EVALUATED,
			HexEvalSounds.NOTHING
		)
	}

	private fun getScarab(player: ServerPlayerEntity, pattern: HexPattern): ItemStack? {
		val inventory = player.inventory
		for (smallInventory in listOf(inventory.main, inventory.armor, inventory.offHand)) {
			for (stack in smallInventory) {
				val nbt = stack.nbt
				if (stack.isOf(HexicalItems.SCARAB_BEETLE_ITEM) && nbt != null && nbt.getBoolean("active") && HexPattern.fromNBT(nbt.getCompound("pattern")).sigsEqual(pattern))
					return stack
			}
		}
		return null
	}

	@JvmStatic
	fun handleGrimoire(vm: CastingVM, iota: Iota, world: ServerWorld): ExecutionClientView? {
		val env = vm.env
		if (env !is StaffCastEnv)
			return null
		if (vm.image.escapeNext || iota.type !== HexIotaTypes.PATTERN)
			return null

		val pattern = (iota as PatternIota).pattern
		val grimoire = getGrimoire(env.castingEntity!! as ServerPlayerEntity, pattern) ?: return null

		val data = grimoire.getOrCreateNbt().getCompound("expansions")
		if (!data.contains(pattern.anglesSignature())) return null
		val deserialized = IotaType.deserialize(data.getCompound(pattern.anglesSignature()), world) as? ListIota ?: return null

		return vm.queueExecuteAndWrapIotas(deserialized.list.toList(), world)
	}

	private fun getGrimoire(player: ServerPlayerEntity, pattern: HexPattern): ItemStack? {
		val inventory = player.inventory
		for (smallInventory in listOf(inventory.main, inventory.armor, inventory.offHand)) {
			for (stack in smallInventory) {
				val nbt = stack.nbt
				if (stack.isOf(HexicalItems.GRIMOIRE_ITEM) && nbt != null && nbt.getCompound("expansions").contains(pattern.anglesSignature()))
					return stack
			}
		}
		return null
	}
}