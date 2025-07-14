package miyucomics.hexical.datagen.generators

import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.common.lib.HexItems
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
			.display(ItemStack(HexicalItems.CONJURED_COMPASS_ITEM),
				Text.translatable("advancement.hexical:root.title"),
				Text.translatable("advancement.hexical:root.description"),
				Identifier("minecraft", "textures/block/blackstone.png"),
				AdvancementFrame.TASK, true, true, true)
			.criterion("start_hexcasting", InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(HexTags.Items.GRANTS_ROOT_ADVANCEMENT).build()))
			.build(consumer, "hexical:root")

		fun registerAdvancement(name: String, frame: AdvancementFrame, icon: Item, hidden: Boolean, condition: CriterionConditions, parent: Advancement = root): Advancement {
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

		registerAdvancement("conjure_cake", AdvancementFrame.CHALLENGE, Items.CAKE, true, ConjureCakeCriterion.Condition())
		registerAdvancement("conjure_hexxy", AdvancementFrame.CHALLENGE, HexItems.SCRYING_LENS, true, HexxyCriterion.Condition())
		registerAdvancement("hallucinate", AdvancementFrame.TASK, Items.WHITE_BANNER, false, HallucinateCriterion.Condition())
		registerAdvancement("diy_conjuring", AdvancementFrame.TASK, Items.SCAFFOLDING, false, DIYCriterion.Condition())
		registerAdvancement("specklike", AdvancementFrame.TASK, Items.BEACON, false, SpecklikeCriterion.Condition())

		val baseLamp = registerAdvancement("acquire_hand_lamp", AdvancementFrame.TASK, HexicalItems.HAND_LAMP_ITEM, false, InventoryChangedCriterion.Conditions.items(HexicalItems.HAND_LAMP_ITEM))
		registerAdvancement("educate_lamp", AdvancementFrame.TASK, Items.ENCHANTED_BOOK, false, EducateGenieCriterion.Condition(), baseLamp)
		registerAdvancement("reload_lamp", AdvancementFrame.TASK, Items.LIGHTNING_ROD, false, ReloadLampCriterion.Condition(), baseLamp)
		registerAdvancement("acquire_arch_lamp", AdvancementFrame.GOAL, HexicalItems.ARCH_LAMP_ITEM, false, InventoryChangedCriterion.Conditions.items(HexicalItems.ARCH_LAMP_ITEM), baseLamp)
	}
}