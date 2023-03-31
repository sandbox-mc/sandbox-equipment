package io.sandbox.equipment.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class RageEffect extends StatusEffect {
  public static final String RAGE_DAMAGE_BONUS_ID = "de0eb08c-5ee6-4264-97a2-ed58476d699b";
  protected final double modifier;

  public RageEffect(double modifier) {
    super(StatusEffectCategory.BENEFICIAL, 0xEA4335);
    this.modifier = modifier;
    this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "b44b1e45-7b6b-467a-9ad2-496b5f1f0c35", 0.20000000298023224D, Operation.MULTIPLY_TOTAL);
    this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "056688f9-f6fe-4245-857d-97f40c061af0", 0.20000000298023224D, Operation.MULTIPLY_TOTAL);
    this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, RAGE_DAMAGE_BONUS_ID, 0, Operation.ADDITION);
  }

  @Override
  public boolean canApplyUpdateEffect(int duration, int amplifier) {
    // In our case, we just make it return true so that it applies the status effect every tick.
    return true;
  }

  @Override
  public void applyUpdateEffect(LivingEntity entity, int amplifier) {
    // This is basically the same as poison
    if (entity.getHealth() > 1.0F && entity.getWorld().getTime() % 40 == 0) {
      entity.damage(DamageSource.MAGIC, 2.0F);
    }
  }
  
  public double adjustModifierAmount(int amplifier, EntityAttributeModifier modifier) {
    if (RAGE_DAMAGE_BONUS_ID.equals(modifier.getId().toString())) {
      return this.modifier * (double)(amplifier + 1);
    }

    return amplifier;
  }
}