package miyucomics.hexical.features.patchouli;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageWithText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class DyeingPage extends PageWithText {
	IVariable uncolored, white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black;
	private final transient List<ItemStack> validOptions = new ArrayList<>();

	@Override
	public void build(World level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(level, entry, builder, pageNum);
		validOptions.addAll(
			Stream.of(uncolored, white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black)
				.filter(Objects::nonNull)
				.map(var -> var.as(ItemStack.class))
				.filter(stack -> !stack.isEmpty())
				.toList()
		);
	}

	@Override
	public void render(DrawContext graphics, int mouseX, int mouseY, float pticks) {
		int index = 0;
		int numberOfOptions = validOptions.size();
		final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2;

		final double SCALE = 12.0;
		for (ItemStack stack : validOptions) {
			double goldenAngle = index * MathHelper.TAU / (GOLDEN_RATIO * GOLDEN_RATIO);
			double radius = SCALE * Math.sqrt(index + 1);
			int x = GuiBook.PAGE_WIDTH / 2 + (int) (Math.cos(goldenAngle) * radius) - 8;
			int y = 45 + (int) (Math.sin(goldenAngle) * radius);
			graphics.drawItem(stack, x, y);
			index++;
		}

		super.render(graphics, mouseX, mouseY, pticks);
	}

	@Override
	public int getTextHeight() {
		return 0;
	}
}