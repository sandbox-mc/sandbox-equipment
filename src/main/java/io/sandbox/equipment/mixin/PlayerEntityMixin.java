package io.sandbox.equipment.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

  protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
    super(entityType, world);
  }

  @Shadow
  public abstract ItemCooldownManager getItemCooldownManager();
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

  // @Inject(method = "tryAttack", at = @At("RETURN"))
  // public void tryAttack(Entity target, CallbackInfoReturnable<Boolean> cbir) {
  //   Boolean successfulAttack = cbir.getReturnValue();
  //   System.out.println("HIT DING: " + successfulAttack);
  // }

  // @Inject(method = "tick", at = @At("TAIL"))
  // public void tick(CallbackInfo cbi) {
  //   if (this.world.isClient) {
  //     return;
  //   }

  //   if (!this.activeItemStack.isOf(Items.SHIELD) && !this.getItemCooldownManager().isCoolingDown(Items.SHIELD)) {
  //     ticksSinceShieldUsed++;
  //   }

  //   if (ticksSinceShieldUsed >= 60 && ticksSinceShieldUsed % 5 == 0) { // 5 is 4 points per second aka (5 second recovery)
  //     double maxBlockStrengthValue = this.getAttributeValue(AttributeLoader.MAX_BLOCK_STRENGTH_ATTRIBUTE);
  //     EntityAttributeInstance blockStrengthAttr = this.getAttributeInstance(AttributeLoader.BLOCK_STRENGTH_ATTRIBUTE);
  //     this.shieldBlockStrengthAmount = this.shieldBlockStrengthAmount + 1;

  //     // run this check to prevent unneeded updates
  //     // Once blockStrength is full no need to update and trigger network calls
  //     if (this.shieldBlockStrengthAmount < maxBlockStrengthValue) {
  //       if (this.shieldBlockStrengthAmount < maxBlockStrengthValue) {
  //         blockStrengthAttr.setBaseValue(this.shieldBlockStrengthAmount);
  //       } else {
  //         blockStrengthAttr.setBaseValue(maxBlockStrengthValue);
  //       }
  //     }
  //   }
  // }

  // @Inject(method = "damageShield", at = @At("HEAD"))
  // protected void damageShield(float amount, CallbackInfo cbi) {
  //   if (this.activeItemStack.isOf(Items.SHIELD)) {
  //     ticksSinceShieldUsed = 0;
  //     EntityAttributeInstance blockStrengthAttr = this.getAttributeInstance(AttributeLoader.BLOCK_STRENGTH_ATTRIBUTE);
  //     double amountToDamage = blockStrengthAttr.getBaseValue() - amount;
  //     // double overDamage = amountToDamage < 0 ? amountToDamage * (-1) : 0;
  //     this.shieldBlockStrengthAmount = amountToDamage > 0 ? amountToDamage : 0;
  //     blockStrengthAttr.setBaseValue(this.shieldBlockStrengthAmount);

  //     // Force player to stop using shield at 0
  //     if (this.shieldBlockStrengthAmount <= 0) {;
  //       this.getItemCooldownManager().set(Items.SHIELD, 60);
  //       this.clearActiveItem();
  //     }
  //   }
  // }
}