package miyucomics.hexical.items

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class ConjuredStaffItem : ItemPackagedHex(Settings().maxCount(1)) {
	override fun canDrawMediaFromInventory(stack: ItemStack?): Boolean { return false }
	override fun breakAfterDepletion(): Boolean { return true }
	override fun use(world: World, player: PlayerEntity, usedHand: Hand): TypedActionResult<ItemStack> { return TypedActionResult.pass(player.getStackInHand(usedHand)); }

	fun cast(world: World, user: LivingEntity, stack: ItemStack, castStack: MutableList<Iota>) {
		val hex = getHex(stack, world as ServerWorld) ?: return
		val harness = CastingHarness(CastingContext((user as ServerPlayerEntity), user.getActiveHand(), CastingContext.CastSource.PACKAGED_HEX))
		harness.stack = castStack
		harness.executeIotas(hex, world)
	}
}