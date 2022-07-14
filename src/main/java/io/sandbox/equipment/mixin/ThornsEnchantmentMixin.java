package io.sandbox.equipment.mixin;

import net.minecraft.enchantment.ThornsEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThornsEnchantment.class)
public class ThornsEnchantmentMixin {
	private static final Item[] CHESTPLATES = {
		Items.IRON_CHESTPLATE,
		Items.DIAMOND_CHESTPLATE,
		Items.NETHERITE_CHESTPLATE,
		Items.LEATHER_CHESTPLATE,
		Items.GOLDEN_CHESTPLATE,
		Items.CHAINMAIL_CHESTPLATE
	};

	@Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
	private void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cbir) {
		if (!(stack.getItem() instanceof ArmorItem)) {
			cbir.setReturnValue(false);
			cbir.cancel();
			return;
		}

		for (int i = 0; i < CHESTPLATES.length; i++) {
			if (CHESTPLATES[i] == stack.getItem()) {
				cbir.setReturnValue(true);
				cbir.cancel();
				return;
			}
		}

		cbir.setReturnValue(false);
		cbir.cancel();
	}

	@Inject(method = "onUserDamaged", at = @At("HEAD"), cancellable = true)
	private void onUserDamaged(LivingEntity user, Entity attacker, int level, CallbackInfo cbi) {
		if (attacker != null) {
			attacker.damage(DamageSource.thorns(user), level * 1.0F);
		}
		cbi.cancel();
	}
}
