package io.sandbox.equipment.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import io.sandbox.equipment.enchantments.AwakenEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
  public LivingEntityMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  @Shadow
  abstract float applyArmorToDamage(DamageSource source, float amount);

  @Redirect(method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"))
  private float addDamageBeforeArmorCalc(LivingEntity entity, DamageSource source, float amount) {
    LivingEntity victim = (LivingEntity) (Object) this;

    if (amount > 0) {
      if (source.getSource() instanceof LivingEntity) {
        amount = AwakenEnchantment.triggerDamageEffects(source, victim, amount);
      }
    }

    return applyArmorToDamage(source, amount);
  }

  // @Inject(method = "applyArmorToDamage", at = @At(value = "HEAD"), cancellable = true)
  // protected void applyDamageBeforeArmor(DamageSource source, float amount, CallbackInfoReturnable<Float> cbir) {
  //   LivingEntity victim = (LivingEntity) (Object) this;

  //   if (amount > 0) {
  //     if (source.getSource() instanceof LivingEntity) {
  //       amount = AwakenEnchantment.triggerDamageEffects(source, victim, amount);
  //     }
  //   }

  //   cbir.setReturnValue(amount);
  // }
}
