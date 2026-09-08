package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.misc.MediaConstants;
import miyucomics.hexical.HexicalMain;
import miyucomics.hexical.init.HexicalItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin extends MerchantEntity {
	@SuppressWarnings("unused")
	public WanderingTraderEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "fillRecipes", at = @At("RETURN"))
	public void addNewTrades(CallbackInfo info) {
		TradeOfferList tradeOfferList = getOffers();
		if (tradeOfferList == null)
			return;

		if (random.nextFloat() < 0.5) {
			ItemStack trade = HexicalItems.randomPlush();
			tradeOfferList.add(new TradeOffer(new ItemStack(Items.EMERALD, 3), trade, 1, 1, 1));
		}

		if (random.nextFloat() < 0.4) {
			ItemStack trade = new ItemStack(HexicalItems.HAND_LAMP_ITEM);
			trade.getOrCreateNbt().putLong("media", (long) (HexicalMain.RANDOM.nextFloat() * MediaConstants.DUST_UNIT * 160));
			trade.getOrCreateNbt().putLong("max_media", MediaConstants.DUST_UNIT * 320);
			tradeOfferList.add(new TradeOffer(new ItemStack(Items.EMERALD, 32), trade, 1, 1, 1));
		}

		if (random.nextFloat() < 0.1) {
			ItemStack trade = new ItemStack(HexicalItems.ARCH_LAMP_ITEM);
			trade.getOrCreateNbt().putLong("media", (long) (HexicalMain.RANDOM.nextFloat() * MediaConstants.DUST_UNIT * 160));
			trade.getOrCreateNbt().putLong("max_media", MediaConstants.DUST_UNIT * 320);
			tradeOfferList.add(new TradeOffer(new ItemStack(Items.EMERALD, 64), trade, 1, 1, 1));
		}
	}
}