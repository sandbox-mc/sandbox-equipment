package io.sandbox.equipment.attributes;

import io.sandbox.equipment.Main;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AttributeLoader {
  
  public static final EntityAttribute DODGE_ATTRIBUTE =
  new ClampedEntityAttribute("attribute.sandbox-equipment.dodge_chance", 0, 0, 20).setTracked(true);
  
  public static final EntityAttribute PARRY_ATTRIBUTE =
  new ClampedEntityAttribute("attribute.sandbox-equipment.parry_chance", 0, 0, 20).setTracked(true);
  
  public static final EntityAttribute LIFE_ON_HIT_CHANCE_ATTRIBUTE =
  new ClampedEntityAttribute("attribute.sandbox-equipment.life_on_hit_chance", 0, 0, 20).setTracked(true);
  
  public static final EntityAttribute LIFE_ON_HIT_AMOUNT_ATTRIBUTE =
  new ClampedEntityAttribute("attribute.sandbox-equipment.life_on_hit_amount", 0, 0, 5).setTracked(true);
  
  public static final EntityAttribute BLOCK_RECOVERY_ATTRIBUTE =
    new ClampedEntityAttribute("attribute.sandbox-equipment.block_recovery", 0, 0, 50).setTracked(true);
  
  // This is used for tracking current block strength
  // used max block strength for the attrubute
  public static final EntityAttribute BLOCK_STRENGTH_ATTRIBUTE =
    new ClampedEntityAttribute("attribute.sandbox-equipment.block_strength", 20, 0, 200).setTracked(true);

  public static final EntityAttribute MAX_BLOCK_STRENGTH_ATTRIBUTE =
    new ClampedEntityAttribute("attribute.sandbox-equipment.max_block_strength", 20, 0, 200).setTracked(true);
    
  public static final EntityAttribute RAGE_GENERATION_ATTRIBUTE =
    new ClampedEntityAttribute("attribute.sandbox-equipment.rage_generation", 0, 0, 100).setTracked(true);
  
  public static final EntityAttribute RAGE_ACTIVE_ATTRIBUTE =
    new ClampedEntityAttribute("attribute.sandbox-equipment.rage_active", 0, 0, 10).setTracked(true);
    
  // This value is used for tracking the current amount of rage
  // Not for setting as an attribute
  public static final EntityAttribute RAGE_LEVEL_ATTRIBUTE =
    new ClampedEntityAttribute("attribute.sandbox-equipment.rage_level", 0, 0, 40).setTracked(true);
  
  public static final EntityAttribute MAX_RAGE_ATTRIBUTE =
    new ClampedEntityAttribute("attribute.sandbox-equipment.max_rage", 20, 0, 40).setTracked(true);

  public static final EntityAttribute VIGILANT_STRIKE_CHANCE_ATTRIBUTE =
    new ClampedEntityAttribute("attribute.sandbox-equipment.vigilant_strike_chance", 0, 0, 20).setTracked(true);
  
  public static final EntityAttribute VIGILANT_STRIKE_MULTIPLIER_ATTRIBUTE =
    new ClampedEntityAttribute("attribute.sandbox-equipment.vigilant_strike_multiplier", 0, 0, 175).setTracked(true);

  public static void init() {
    Registry.register(Registries.ATTRIBUTE, Main.id("dodge_chance"), DODGE_ATTRIBUTE);
    Registry.register(Registries.ATTRIBUTE, Main.id("parry_chance"), PARRY_ATTRIBUTE);
    Registry.register(Registries.ATTRIBUTE, Main.id("life_on_hit_chance"), LIFE_ON_HIT_CHANCE_ATTRIBUTE);
    Registry.register(Registries.ATTRIBUTE, Main.id("life_on_hit_amount"), LIFE_ON_HIT_AMOUNT_ATTRIBUTE);
    Registry.register(Registries.ATTRIBUTE, Main.id("block_recovery"), BLOCK_RECOVERY_ATTRIBUTE);
    Registry.register(Registries.ATTRIBUTE, Main.id("block_strength"), BLOCK_STRENGTH_ATTRIBUTE);
    Registry.register(Registries.ATTRIBUTE, Main.id("max_block_strength"), MAX_BLOCK_STRENGTH_ATTRIBUTE);
    Registry.register(Registries.ATTRIBUTE, Main.id("rage_active"), RAGE_ACTIVE_ATTRIBUTE);
    Registry.register(Registries.ATTRIBUTE, Main.id("rage_generation"), RAGE_GENERATION_ATTRIBUTE);
    Registry.register(Registries.ATTRIBUTE, Main.id("rage_level"), RAGE_LEVEL_ATTRIBUTE);
    Registry.register(Registries.ATTRIBUTE, Main.id("max_rage"), MAX_RAGE_ATTRIBUTE);
    Registry.register(Registries.ATTRIBUTE, Main.id("vigilant_strike_chance"), VIGILANT_STRIKE_CHANCE_ATTRIBUTE);
    Registry.register(Registries.ATTRIBUTE, Main.id("vigilant_strike_multiplier"), VIGILANT_STRIKE_MULTIPLIER_ATTRIBUTE);
  }
}