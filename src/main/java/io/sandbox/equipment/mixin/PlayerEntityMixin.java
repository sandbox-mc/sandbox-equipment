package io.sandbox.equipment.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.sandbox.equipment.enchantments.AwakenEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
  // Trigger the AwakenEnchantment Damage bonuses before armor is applied
  // @Inject(method = "applyDamage", at = @At("HEAD"))
  // protected void applyDamageBeforeArmor(DamageSource source, Float amount, CallbackInfo cb) {
  //   if (!(source.getAttacker() instanceof LivingEntity)) {
  //     return;
  //   }

  //   LivingEntity victim = (LivingEntity) (Object) this;

  //   if (amount > 0) {
  //     if (source.getSource() instanceof LivingEntity) {
  //       AwakenEnchantment.triggerDamageEffects(source, victim, amount);
  //     }
  //   }
  // }
}
