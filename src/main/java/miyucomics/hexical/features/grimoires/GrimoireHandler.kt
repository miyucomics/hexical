package miyucomics.hexical.features.grimoires

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.item.ItemStack
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