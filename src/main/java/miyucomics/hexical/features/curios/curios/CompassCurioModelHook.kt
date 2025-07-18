package miyucomics.hexical.features.curios.curios

import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.inits.InitHook
import net.minecraft.client.item.CompassAnglePredicateProvider
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.GlobalPos

object CompassCurioModelHook : InitHook() {
	override fun init() {
		ModelPredicateProviderRegistry.register(
			HexicalItems.CURIO_COMPASS,
			Identifier("angle"),
			CompassAnglePredicateProvider(
				CompassAnglePredicateProvider.CompassTarget { world: ClientWorld, stack: ItemStack, player: Entity ->
					if (!stack.hasNbt() || !stack.nbt?.contains("needle")!!)
						return@CompassTarget null
					val needle = stack.nbt!!.getIntArray("needle")
					return@CompassTarget GlobalPos.create(
						player.world.registryKey,
						BlockPos(needle[0], needle[1], needle[2])
					)
				}
			))
	}
}