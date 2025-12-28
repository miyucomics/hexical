package miyucomics.hexical.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.hexical.inits.HexicalCardinalComponents;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class EntityMixin {
	@WrapMethod(method = "isSilent")
	private boolean muteEntities(Operation<Boolean> original) {
		if (HexicalCardinalComponents.MUTED_COMPONENT.get(this).getMuted())
			return true;
		return original.call();
	}
}