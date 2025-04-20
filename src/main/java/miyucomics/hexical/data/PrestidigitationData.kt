package miyucomics.hexical.data

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.interfaces.PrestidigitationEffect
import miyucomics.hexical.prestidigitation.*
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import java.io.InputStream
import java.io.InputStreamReader

object PrestidigitationData {
	private val PRESTIDIGTAION_EFFECTS_REGISTRY_KEY: RegistryKey<Registry<PrestidigitationEffect>> = RegistryKey.ofRegistry(HexicalMain.id("prestidigitation"))
	private val PRESTIDIGITATION_EFFECTS: SimpleRegistry<PrestidigitationEffect> = FabricRegistryBuilder.createSimple(PRESTIDIGTAION_EFFECTS_REGISTRY_KEY).attribute(RegistryAttribute.MODDED).buildAndRegister()
	private val BLOCK_LOOKUP: HashMap<Identifier, Identifier> = HashMap()
	private val ENTITY_LOOKUP: HashMap<Identifier, Identifier> = HashMap()

	@JvmStatic
	fun init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(object :
			SimpleSynchronousResourceReloadListener {
			override fun getFabricId() = HexicalMain.id("prestidigitation")
			override fun reload(manager: ResourceManager) = manager.findResources("prestidigitation") { path -> path.path.endsWith(".json") }.keys.forEach { id -> loadData(manager.getResource(id).get().inputStream) }
		})

		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("arm_stands"), ArmStandsEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("deprime_tnt"), DeprimeTntEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("trigger_impetus"), TriggerImpetusEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("ring_bell"), RingBellEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("dispense"), DispenseEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("learn"), AkashicCopyEffect())

		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("note"), NoteblockEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("right_click"), UseItemOnEffect(ItemStack.EMPTY))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("use_axe"), UseItemOnEffect(ItemStack(Items.DIAMOND_AXE)))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("use_hoe"), UseItemOnEffect(ItemStack(Items.DIAMOND_HOE)))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("use_shears"), UseItemOnEffect(ItemStack(Items.SHEARS)))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("use_shovel"), UseItemOnEffect(ItemStack(Items.DIAMOND_SHOVEL)))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("ignite"), UseItemOnEffect(ItemStack(Items.FLINT_AND_STEEL)))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("piston"), PistonEffect())

		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("delete"), TransformBlockEffect(Blocks.AIR.defaultState))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("drain_cauldron"), TransformBlockEffect(Blocks.CAULDRON.defaultState))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("dry_mud"), TransformBlockEffect(Blocks.CLAY.defaultState))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("light"), BooleanPropertyEffect(Properties.LIT))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("open"), BooleanPropertyEffect(Properties.OPEN))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("power"), BooleanPropertyEffect(Properties.POWERED))
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("squirt_squid"), SquirtSquidsEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("sneeze_panda"), PandaSneezeEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("puff_fish"), InflatePufferfishEffect())
		Registry.register(PRESTIDIGITATION_EFFECTS, HexicalMain.id("creeper"), CreeperEffect())
	}

	fun blockEffect(block: Block): PrestidigitationEffect? =
		PRESTIDIGITATION_EFFECTS.get(BLOCK_LOOKUP[Registries.BLOCK.getId(block)])

	fun entityEffect(entity: Entity): PrestidigitationEffect? =
		PRESTIDIGITATION_EFFECTS.get(ENTITY_LOOKUP[Registries.ENTITY_TYPE.getId(entity.type)])

	private fun loadData(stream: InputStream) {
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