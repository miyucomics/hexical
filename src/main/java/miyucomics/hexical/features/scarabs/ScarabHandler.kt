package miyucomics.hexical.features.scarabs

import at.petrak.hexcasting.api.casting.PatternShapeMatch
import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.common.casting.PatternRegistryManifest
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexical.casting.frames.ScarabFrame
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

object ScarabHandler {
	@JvmStatic
	fun handleScarab(vm: CastingVM, iota: PatternIota, continuation: SpellContinuation, world: ServerWorld): CastResult? {
		val env = vm.env
		if (env !is PlayerBasedCastEnv)
			return null

		val pattern = iota.pattern
		val patternTest = PatternRegistryManifest.matchPattern(pattern, env, false)
		if (patternTest !is PatternShapeMatch.Nothing)
			return null

		if (wouldBeRecursive(pattern.anglesSignature(), continuation))
			return null
		val scarab = getScarab(env.castingEntity!! as ServerPlayerEntity) ?: return null
		val program = IotaType.deserialize(scarab.getOrCreateNbt().getCompound("program"), world) as? ListIota ?: return null

		val newStack = vm.image.stack.toMutableList()
		newStack.add(iota)

		return CastResult(
			iota,
			continuation
				.pushFrame(ScarabFrame(pattern.anglesSignature()))
				.pushFrame(FrameEvaluate(program.list, false)),
			vm.image.copy(stack = newStack),
			listOf(),
			ResolvedPatternType.EVALUATED,
			HexEvalSounds.NOTHING
		)
	}

	private fun wouldBeRecursive(pattern: String, continuation: SpellContinuation): Boolean {
		var cont = continuation
		while (cont is SpellContinuation.NotDone) {
			if (cont.frame is ScarabFrame && (cont.frame as ScarabFrame).signature == pattern)
				return true
			cont = cont.next
		}
		return false
	}

	private fun getScarab(player: ServerPlayerEntity): ItemStack? {
		val inventory = player.inventory
		for (smallInventory in listOf(inventory.main, inventory.armor, inventory.offHand)) {
			for (stack in smallInventory) {
				val nbt = stack.nbt
				if (stack.isOf(HexicalItems.SCARAB_BEETLE_ITEM) && nbt != null && nbt.getBoolean("active"))
					return stack
			}
		}
		return null
	}
}