package miyucomics.hexical.data

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.interfaces.PrestidigitationEffect
import miyucomics.hexical.prestidigitation.*
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.SimpleRegistry
import java.io.InputStream
import java.io.InputStreamReader

object PrestidigitationData {
	val PRESTIDIGITATION_EFFECTS: SimpleRegistry<PrestidigitationEffect> = FabricRegistryBuilder.createSimple(PrestidigitationEffect::class.java, HexicalMain.id("prestidigitation")).attribute(RegistryAttribute.MODDED).buildAndRegister()
	private val BLOCK_LOOKUP: HashMap<Identifier, Identifier> = HashMap()
	private val ENTITY_LOOKUP: HashMap<Identifier, Identifier> = HashMap()

	@JvmStatic
	fun init() {
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("arm_stands"), ArmStandsEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("deprime_tnt"), DeprimeTntEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("trigger_impetus"), TriggerImpetusEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("ring_bell"), RingBellEffect())

		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("right_click"), UseItemOnEffect(ItemStack.EMPTY))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("use_shears"), UseItemOnEffect(ItemStack(Items.SHEARS)))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("use_axe"), UseItemOnEffect(ItemStack(Items.DIAMOND_AXE)))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("ignite"), UseItemOnEffect(ItemStack(Items.FLINT_AND_STEEL)))

		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("delete"), TransformBlockEffect(Blocks.AIR.defaultState))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("drain_cauldron"), TransformBlockEffect(Blocks.CAULDRON.defaultState))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("light"), BooleanPropertyEffect(Properties.LIT))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("open"), BooleanPropertyEffect(Properties.OPEN))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("squirt_squid"), SquirtSquidsEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("sneeze_panda"), PandaSneezeEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("puff_fish"), InflatePufferfishEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("creeper"), CreeperEffect())
	}

	fun blockEffect(block: Block): PrestidigitationEffect? {
		return PRESTIDIGITATION_EFFECTS.get(BLOCK_LOOKUP[Registry.BLOCK.getId(block)])
	}

	fun entityEffect(entity: Entity): PrestidigitationEffect? {
		return PRESTIDIGITATION_EFFECTS.get(ENTITY_LOOKUP[Registry.ENTITY_TYPE.getId(entity.type)])
	}

	fun loadData(stream: InputStream) {
		val json = JsonParser.parseReader(InputStreamReader(stream, "UTF-8")) as JsonObject
		if (json.has("blocks")) {
			val blocks = json.get("blocks").asJsonObject
			blocks.keySet().forEach { block -> BLOCK_LOOKUP[Identifier(block)] = Identifier(blocks.get(block).asString) }
		}
		if (json.has("entities")) {
			val entities = json.get("entities").asJsonObject
			entities.keySet().forEach { entity -> ENTITY_LOOKUP[Identifier(entity)] = Identifier(entities.get(entity).asString) }
		}
	}
}