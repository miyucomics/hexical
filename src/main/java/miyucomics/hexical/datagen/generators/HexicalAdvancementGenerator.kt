package miyucomics.hexical.datagen.generators

import at.petrak.hexcasting.api.mod.HexTags
import miyucomics.hexical.inits.*
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.function.Consumer

class HexicalAdvancementGenerator(generator: FabricDataOutput) : FabricAdvancementProvider(generator) {
	override fun generateAdvancement(consumer: Consumer<Advancement>) {
		val root = Advancement.Builder.create()
			.display(
				ItemStack(HexicalItems.CURIO_COMPASS),
				Text.translatable("advancement.hexical:root.title"),
				Text.translatable("advancement.hexical:root.description"),
				Identifier("minecraft", "textures/block/blackstone.png"),
				AdvancementFrame.TASK, true, true, true
			)
			.criterion("start_hexcasting", InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(HexTags.Items.GRANTS_ROOT_ADVANCEMENT).build()))
			.build(consumer, "hexical:root")

		fun registerAdvancement(name: String, icon: Item, condition: CriterionConditions, frame: AdvancementFrame = AdvancementFrame.TASK, hidden: Boolean = false, parent: Advancement = root): Advancement {
			return Advancement.Builder.create()
				.parent(parent)
				.display(ItemStack(icon),
					Text.translatable("advancements.hexical.$name.title"),
					Text.translatable("advancements.hexical.$name.description"),
					null, frame, true, true, hidden
				)
				.criterion(name, condition)
				.build(consumer, "hexical:$name")
		}

		registerAdvancement("conjure_cake", Items.CAKE, ConjureCakeCriterion.Condition(), frame = AdvancementFrame.CHALLENGE, hidden = true)
		registerAdvancement("diy_conjuring", Items.SCAFFOLDING, DIYCriterion.Condition())
		registerAdvancement("hextito_quine", HexicalItems.HEXTITO_ITEM, HextitoQuineCriterion.Condition())
		registerAdvancement("specklike", Items.BEACON, SpecklikeCriterion.Condition())

		registerAdvancement("acquire_hand_lamp", HexicalItems.HAND_LAMP_ITEM, InventoryChangedCriterion.Conditions.items(HexicalItems.HAND_LAMP_ITEM)).also {
			registerAdvancement("educate_lamp", Items.ENCHANTED_BOOK, EducateGenieCriterion.Condition(), parent = it)
			registerAdvancement("reload_lamp", Items.LIGHTNING_ROD, ReloadLampCriterion.Condition(), parent = it)
			registerAdvancement("acquire_arch_lamp", HexicalItems.ARCH_LAMP_ITEM, InventoryChangedCriterion.Conditions.items(HexicalItems.ARCH_LAMP_ITEM), frame = AdvancementFrame.GOAL, parent = it)
		}
	}
}