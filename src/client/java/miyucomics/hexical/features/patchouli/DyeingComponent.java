package miyucomics.hexical.features.patchouli;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.gui.GuiBook;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
public class DyeingComponent implements ICustomComponent {
	public IVariable uncolored, white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black;
	private final transient List<ItemStack> validOptions = new ArrayList<>();
	private transient int verticalOffset;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		verticalOffset = componentY;
	}

	@Override
	public void render(DrawContext graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		int numberOfOptions = validOptions.size();
		int index = 0;
		for (ItemStack stack : validOptions) {
			float angle = index * MathHelper.TAU / numberOfOptions;
			graphics.drawItem(stack, GuiBook.PAGE_WIDTH / 2 + (int) (MathHelper.sin(angle) * 45) - 8, (int) (MathHelper.cos(angle) * -45) + verticalOffset - 8);
			index++;
		}
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		for (IVariable variable : List.of(uncolored, white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black)) {
			if (variable == null)
				continue;
			ItemStack stack = lookup.apply(variable).as(ItemStack.class);
			if (!stack.isEmpty())
				validOptions.add(stack);
		}
	}
}