package io.sandbox.equipment.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.sandbox.equipment.attributes.AttributeLoader;
import io.sandbox.equipment.effects.EffectLoader;
import io.sandbox.equipment.enchantments.AwakenEnchantment;
import io.sandbox.equipment.tags.TagsLoader;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
  protected final Random random = Random.create();
  private Integer ticksSinceShieldUsed = 0;
  private double shieldBlockStrengthAmount = 0.0;

  public LivingEntityMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  @Shadow
  abstract float applyArmorToDamage(DamageSource source, float amount);

  @Shadow
  public abstract double getAttributeValue(EntityAttribute attribute);

  @Shadow
  public abstract ItemStack getMainHandStack();

  @Shadow
  public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

  @Shadow
  public abstract ItemStack getActiveItem();

  @Shadow
  public abstract void clearActiveItem();

  @Redirect(method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"))
  private float addDamageBeforeArmorCalc(LivingEntity entity, DamageSource source, float amount) {
    LivingEntity victim = (LivingEntity) (Object) this;

    if (amount > 0) {
      Entity attacker = source.getSource();
      if (attacker instanceof LivingEntity) {
        LivingEntity livingAttacker = (LivingEntity)attacker;
        double vigilantStrikeChance = livingAttacker.getAttributeValue(AttributeLoader.VIGILANT_STRIKE_CHANCE_ATTRIBUTE);
        double vigilantStrikeMultiplier = livingAttacker.getAttributeValue(AttributeLoader.VIGILANT_STRIKE_MULTIPLIER_ATTRIBUTE);
        if (random.nextFloat() < vigilantStrikeChance / 100) {
          amount = amount * (1 + (float)(vigilantStrikeMultiplier / 100));
        }

        // Awaken enchant stuff... for later
        amount = AwakenEnchantment.triggerDamageEffects(source, victim, amount);
      }
    }

    return applyArmorToDamage(source, amount);
  }

  @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"))
  public void shieldDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cbir) {
    if (this.getActiveItem().isOf(Items.SHIELD)) {
      if (source.isProjectile()) {
        amount = amount / 2; // projectiles deal half damage to shieldStrength
      } else {
        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity) {
          LivingEntity livingAttacker = (LivingEntity) attacker;
          if (livingAttacker.getMainHandStack().getItem() instanceof AxeItem) {
            amount = amount * 2; // axes deal double damage to shieldStrength
          }
        }
      }
      ticksSinceShieldUsed = 0;
      EntityAttributeInstance blockStrengthAttr = this.getAttributeInstance(AttributeLoader.BLOCK_STRENGTH_ATTRIBUTE);
      double amountToDamage = blockStrengthAttr.getBaseValue() - amount;
      this.shieldBlockStrengthAmount = amountToDamage > 0 ? amountToDamage : 0;
      blockStrengthAttr.setBaseValue(this.shieldBlockStrengthAmount);

      LivingEntity livingEntity = (LivingEntity) (Object) this;
      // Force player to stop using shield at 0
      if (this.shieldBlockStrengthAmount <= 0 && livingEntity instanceof PlayerEntity) {
        PlayerEntity player = (PlayerEntity) livingEntity;
        player.getItemCooldownManager().set(Items.SHIELD, 100);
        this.clearActiveItem();
      }
    }
  }

  @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
  public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cbir) {
    if (source.getAttacker() != null) {
      double dodgeChance = this.getAttributeValue(AttributeLoader.DODGE_ATTRIBUTE);
      double parryChance = this.getAttributeValue(AttributeLoader.PARRY_ATTRIBUTE);
      if (
        random.nextFloat() < dodgeChance / 100 ||
        (
          random.nextFloat() < parryChance / 100 &&
          this.getMainHandStack().isIn(TagsLoader.PARRY_ITEMS)
        )
      ) {
        // If they dodge or parry
        cbir.setReturnValue(false);
      }
    }
  }

  @Inject(method = "damage", at = @At("RETURN"))
  public void damageResult(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cbir) {
    Entity attacker = source.getAttacker();
    if (attacker != null && attacker instanceof LivingEntity) {
      // This is basically a player,
      // at least players will usually be the attacker with these attributes
      LivingEntity livingAttacker = (LivingEntity)attacker;

      // Life on Hit logic
      double lifeOnHitChance = livingAttacker.getAttributeValue(AttributeLoader.LIFE_ON_HIT_CHANCE_ATTRIBUTE);
      double lifeOnHitAmount = livingAttacker.getAttributeValue(AttributeLoader.LIFE_ON_HIT_AMOUNT_ATTRIBUTE);
      if (random.nextFloat() < lifeOnHitChance / 100) {
        livingAttacker.heal((float)(0.5 + lifeOnHitAmount));
      }

      // Rage generation logic
      double rageGeneration = livingAttacker.getAttributeValue(AttributeLoader.RAGE_GENERATION_ATTRIBUTE);
      // add rage here if they can generate rage
      if (rageGeneration > 0) {
        EntityAttributeInstance rageLevel = livingAttacker.getAttributeInstance(AttributeLoader.RAGE_LEVEL_ATTRIBUTE);
        double rageLevelValue = rageLevel.getBaseValue() + 0.5 + (rageGeneration / 20);

        // Rage Generation is 1/20 of it's value so 20 should add 1 rage generation to the base 0.5
        rageLevel.setBaseValue(rageLevelValue);
        if (rageLevelValue >= 20) {
          // Apply Rage Effect
          livingAttacker.addStatusEffect(new StatusEffectInstance(EffectLoader.RAGE_EFFECT, 200));
          // TODO: set nbt to have Rage = active
          // if we do this in both server and client... does it matter?
          // Make timer line up with the amount of Rage that will drain
          // Rage also cannot be generated while draining, unless time is like 3 seconds...
        }
      }
    }
  }

  @Inject(method = "tick", at = @At("TAIL"))
  public void tick(CallbackInfo cbi) {
    if (this.world.isClient) {
      return;
    }

    LivingEntity livingEntity = (LivingEntity) (Object) this;
    if (livingEntity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) livingEntity;
      if (!this.getActiveItem().isOf(Items.SHIELD) && !player.getItemCooldownManager().isCoolingDown(Items.SHIELD)) {
        ticksSinceShieldUsed++;
      }

      double blockRecoveryValue = this.getAttributeValue(AttributeLoader.BLOCK_RECOVERY_ATTRIBUTE);
      double maxBlockStrengthValue = this.getAttributeValue(AttributeLoader.MAX_BLOCK_STRENGTH_ATTRIBUTE);
      double recoveryPercent = blockRecoveryValue / 100;
      
      if (ticksSinceShieldUsed >= 100 * (1 - recoveryPercent) && ticksSinceShieldUsed % 5 == 0) { // 5 is 4 points per second aka (5 second recovery)
        EntityAttributeInstance blockStrengthAttr = this.getAttributeInstance(AttributeLoader.BLOCK_STRENGTH_ATTRIBUTE);

        double oneTwentieth = maxBlockStrengthValue / 20 * (1 + blockRecoveryValue / 100);
        this.shieldBlockStrengthAmount = this.shieldBlockStrengthAmount + oneTwentieth;
  
        // run this check to prevent unneeded updates
        // Once blockStrength is full no need to update and trigger network calls
        if (this.shieldBlockStrengthAmount < maxBlockStrengthValue) {
          blockStrengthAttr.setBaseValue(this.shieldBlockStrengthAmount);
        } else {
          blockStrengthAttr.setBaseValue(maxBlockStrengthValue);
        }
      }
    }
  }

  @Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> builder) {
		builder.getReturnValue().add(AttributeLoader.DODGE_ATTRIBUTE);
    builder.getReturnValue().add(AttributeLoader.PARRY_ATTRIBUTE);
    builder.getReturnValue().add(AttributeLoader.LIFE_ON_HIT_AMOUNT_ATTRIBUTE);
    builder.getReturnValue().add(AttributeLoader.LIFE_ON_HIT_CHANCE_ATTRIBUTE);
    builder.getReturnValue().add(AttributeLoader.BLOCK_RECOVERY_ATTRIBUTE);
    builder.getReturnValue().add(AttributeLoader.BLOCK_STRENGTH_ATTRIBUTE);
    builder.getReturnValue().add(AttributeLoader.MAX_BLOCK_STRENGTH_ATTRIBUTE);
    builder.getReturnValue().add(AttributeLoader.RAGE_GENERATION_ATTRIBUTE);
    builder.getReturnValue().add(AttributeLoader.RAGE_LEVEL_ATTRIBUTE);
    builder.getReturnValue().add(AttributeLoader.MAX_RAGE_ATTRIBUTE);
    builder.getReturnValue().add(AttributeLoader.VIGILANT_STRIKE_CHANCE_ATTRIBUTE);
    builder.getReturnValue().add(AttributeLoader.VIGILANT_STRIKE_MULTIPLIER_ATTRIBUTE);
	}
}
