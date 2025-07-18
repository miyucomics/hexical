package miyucomics.hexical.features.grimoires

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.misc.SerializationUtils
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

object GrimoireHandler {
	@JvmStatic
	fun handleGrimoire(vm: CastingVM, iota: Iota, world: ServerWorld): ExecutionClientView? {
		val env = vm.env
		if (env !is StaffCastEnv)
			return null
		if (vm.image.escapeNext || iota.type !== HexIotaTypes.PATTERN)
			return null

		val pattern = (iota as PatternIota).pattern
		val expansion = getExpansion(env.castingEntity!! as ServerPlayerEntity, pattern) ?: return null
		return vm.queueExecuteAndWrapIotas(expansion, world)
	}

	private fun getExpansion(player: ServerPlayerEntity, pattern: HexPattern): List<Iota>? {
		val inventory = player.inventory
		inventory.offHand.toMutableList().also {
			it.addAll(inventory.main)
			it.addAll(inventory.armor)
		}.forEach {
			val nbt = it.nbt?.getCompound("expansions")
			if (it.isOf(HexicalItems.GRIMOIRE_ITEM) && nbt != null && nbt.contains(pattern.anglesSignature()))
				return SerializationUtils.backwardsCompatibleReadHex(it.getOrCreateNbt().getCompound("expansions"), pattern.anglesSignature(), player.serverWorld)
		}
		return null
	}
}