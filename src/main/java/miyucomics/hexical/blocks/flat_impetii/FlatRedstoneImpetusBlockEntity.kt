package miyucomics.hexical.blocks.flat_impetii

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.utils.putTag
import com.mojang.authlib.GameProfile
import com.mojang.datafixers.util.Pair
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.enums.WallMountLocation
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtHelper
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.*

class FlatRedstoneImpetusBlockEntity(position: BlockPos, state: BlockState) : BlockEntityAbstractImpetus(HexicalBlocks.FLAT_REDSTONE_IMPETUS_BLOCK_ENTITY, position, state) {
	override fun getStartDirection(): Direction {
		if (this.cachedState.get(FlatImpetusBlock.ATTACH_FACE) == WallMountLocation.WALL)
			return Direction.DOWN
		return super.getStartDirection()
	}

	private var storedPlayerProfile: GameProfile? = null
	private var storedPlayer: UUID? = null

	private var cachedDisplayProfile: GameProfile? = null
	private var cachedDisplayStack: ItemStack? = null

	private fun getPlayerName(): GameProfile? {
		if (world is ServerWorld) {
			val player: PlayerEntity? = getStoredPlayer()
			if (player != null)
				return player.gameProfile
		}
		return this.storedPlayerProfile
	}

	fun setPlayer(profile: GameProfile?, player: UUID?) {
		this.storedPlayerProfile = profile
		this.storedPlayer = player
		this.markDirty()
	}

	fun clearPlayer() {
		this.storedPlayerProfile = null
		this.storedPlayer = null
	}

	fun updatePlayerProfile() {
		val player = getStoredPlayer()
		if (player != null) {
			val newProfile = player.gameProfile
			if (newProfile != this.storedPlayerProfile) {
				this.storedPlayerProfile = newProfile
				this.markDirty()
			}
		} else {
			this.storedPlayerProfile = null
		}
	}

	fun getStoredPlayer(): ServerPlayerEntity? {
		if (this.storedPlayer == null)
			return null
		if (world !is ServerWorld) {
			HexAPI.LOGGER.error("Called getStoredPlayer on the client")
			return null
		}
		val e = (world as ServerWorld).getEntity(this.storedPlayer)
		if (e is ServerPlayerEntity) {
			return e
		} else {
			HexAPI.LOGGER.error("Entity {} stored in a cleric impetus wasn't a player somehow", e)
			return null
		}
	}

	override fun applyScryingLensOverlay(lines: MutableList<Pair<ItemStack?, Text?>?>, state: BlockState?, pos: BlockPos?, observer: PlayerEntity?, world: World?, hitFace: Direction?) {
		super.applyScryingLensOverlay(lines, state, pos, observer, world, hitFace)
		val name = this.getPlayerName()
		if (name != null) {
			if (name != cachedDisplayProfile || cachedDisplayStack == null) {
				cachedDisplayProfile = name
				val head = ItemStack(Items.PLAYER_HEAD)
				head.putTag("SkullOwner", NbtHelper.writeGameProfile(NbtCompound(), name))
				head.item.postProcessNbt(head.getOrCreateNbt())
				cachedDisplayStack = head
			}
			lines.add(Pair(cachedDisplayStack, Text.translatable("hexcasting.tooltip.lens.impetus.redstone.bound", name.name)))
		} else {
			lines.add(Pair(ItemStack(Items.BARRIER), Text.translatable("hexcasting.tooltip.lens.impetus.redstone.bound.none")))
		}
	}

	override fun saveModData(tag: NbtCompound) {
		super.saveModData(tag)
		if (this.storedPlayer != null) {
			tag.putUuid(TAG_STORED_PLAYER, this.storedPlayer)
		}
		if (this.storedPlayerProfile != null) {
			tag.put(TAG_STORED_PLAYER_PROFILE, NbtHelper.writeGameProfile(NbtCompound(), storedPlayerProfile))
		}
	}

	override fun loadModData(tag: NbtCompound) {
		super.loadModData(tag)
		if (tag.contains(TAG_STORED_PLAYER, NbtElement.INT_ARRAY_TYPE.toInt())) {
			this.storedPlayer = tag.getUuid(TAG_STORED_PLAYER)
		} else {
			this.storedPlayer = null
		}
		if (tag.contains(TAG_STORED_PLAYER_PROFILE, NbtElement.COMPOUND_TYPE.toInt())) {
			this.storedPlayerProfile = NbtHelper.toGameProfile(tag.getCompound(TAG_STORED_PLAYER_PROFILE))
		} else {
			this.storedPlayerProfile = null
		}
	}

	companion object {
		private const val TAG_STORED_PLAYER: String = "stored_player"
		private const val TAG_STORED_PLAYER_PROFILE: String = "stored_player_profile"
	}
}