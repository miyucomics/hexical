package miyucomics.hexical.registry

import miyucomics.hexical.Hexical
import net.minecraft.block.Block
import net.minecraft.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object HexicalTags {
	val CONCRETES: TagKey<Block> = TagKey.of(Registry.BLOCK_KEY, Identifier(Hexical.MOD_ID, "concretes"))
	val CONCRETE_POWDERS: TagKey<Block> = TagKey.of(Registry.BLOCK_KEY, Identifier(Hexical.MOD_ID, "concrete_powders"))
	val GLAZED_TERRACOTTA: TagKey<Block> = TagKey.of(Registry.BLOCK_KEY, Identifier(Hexical.MOD_ID, "glazed_terracotta"))
	val STAINED_GLASS_PANES: TagKey<Block> = TagKey.of(Registry.BLOCK_KEY, Identifier(Hexical.MOD_ID, "stained_glass_panes"))
	val STAINED_GLASS: TagKey<Block> = TagKey.of(Registry.BLOCK_KEY, Identifier(Hexical.MOD_ID, "stained_glasses"))
	val TERRACOTTA: TagKey<Block> = TagKey.of(Registry.BLOCK_KEY, Identifier(Hexical.MOD_ID, "terracotta"))
}