package miyucomics.hexical.features.media_jar

import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.serializeToNBT
import miyucomics.hexical.features.transmuting.TransmutationResult
import miyucomics.hexical.features.transmuting.TransmutingHelper
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.inits.HexicalSounds
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.sound.SoundCategory
import net.minecraft.util.math.BlockPos
import kotlin.math.max
import kotlin.math.min

class MediaJarBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(HexicalBlocks.MEDIA_JAR_BLOCK_ENTITY, pos, state), Inventory {
	private var media: Long = 0
	private var heldStack = ItemStack.EMPTY

	fun getMedia() = this.media
	private fun setMedia(media: Long) {
		this.media = max(min(media, MediaJarBlock.MAX_CAPACITY), 0)
		markDirty()
		if (!world!!.isClient)
			world!!.updateListeners(pos, cachedState, cachedState, Block.NOTIFY_ALL)
	}
	fun insertMedia(media: Long): Long {
		val currentMedia = this.media
		setMedia(currentMedia + media)
		return this.getMedia() - currentMedia
	}
	fun withdrawMedia(media: Long): Boolean {
		if (getMedia() >= media) {
			setMedia(getMedia() - media)
			return true
		} else {
			setMedia(0)
			return false
		}
	}

	override fun writeNbt(nbt: NbtCompound) {
		nbt.putLong("media", media)
		nbt.putCompound("heldStack", heldStack.serializeToNBT())
	}

	override fun readNbt(nbt: NbtCompound) {
		this.media = nbt.getLong("media")
		this.heldStack = ItemStack.fromNbt(nbt.getCompound("heldStack"))
	}

	override fun size() = 1
	override fun getMaxCountPerStack() = 1
	override fun canPlayerUse(playerEntity: PlayerEntity) = false
	override fun getStack(i: Int): ItemStack = if (i == 0) heldStack else ItemStack.EMPTY
	override fun isEmpty() = heldStack.isEmpty

	override fun removeStack(i: Int, amount: Int): ItemStack {
		if (i == 0) {
			markDirty()
			return heldStack.split(amount)
		}
		return ItemStack.EMPTY
	}

	override fun removeStack(i: Int): ItemStack {
		if (i == 0) {
			val originalHeld = heldStack
			heldStack = ItemStack.EMPTY
			markDirty()
			return originalHeld
		}
		return ItemStack.EMPTY
	}

	override fun setStack(i: Int, stack: ItemStack) {
		if (i != 0)
			return
		if (world == null)
			return

		when (val result = TransmutingHelper.transmuteItem(world!!, stack, getMedia(), ::insertMedia, ::withdrawMedia)) {
			is TransmutationResult.AbsorbedMedia -> {
				world!!.playSound(null, pos, HexicalSounds.AMETHYST_MELT, SoundCategory.BLOCKS, 1f, 1f)
				heldStack = stack
			}
			is TransmutationResult.TransmutedItems -> {
				val outputs = result.output.toMutableList()
				heldStack = outputs.removeAt(0).copy()
				val spawnPosition = pos.down().toCenterPos()
				outputs.forEach { world!!.spawnEntity(ItemEntity(world!!, spawnPosition.x, spawnPosition.y, spawnPosition.z, it.copy(), 0.0, 0.0, 0.0)) }
				world!!.playSound(null, pos, HexicalSounds.ITEM_DUNKS, SoundCategory.BLOCKS, 1f, 1f)
			}
			is TransmutationResult.RefilledHolder -> {
				world!!.playSound(null, pos, HexicalSounds.ITEM_DUNKS, SoundCategory.BLOCKS, 1f, 1f)
				heldStack = stack
			}
			is TransmutationResult.Pass -> {
				heldStack = stack
			}
		}

		markDirty()
	}

	override fun clear() {
		heldStack = ItemStack.EMPTY
		markDirty()
	}

	override fun toInitialChunkDataNbt(): NbtCompound = createNbt()
	override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)
}