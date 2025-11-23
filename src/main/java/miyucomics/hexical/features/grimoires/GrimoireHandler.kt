package miyucomics.hexical.features.grimoires

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.features.item_cache.itemCache
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
		val expansion = (env.castingEntity!! as ServerPlayerEntity).itemCache().grimoireMacros[pattern.anglesSignature()] ?: return null
		return vm.queueExecuteAndWrapIotas(expansion, world)
	}
}