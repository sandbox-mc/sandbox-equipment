package io.sandbox.equipment.effects;

import io.sandbox.equipment.Main;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EffectLoader {
  public static StatusEffect RAGE_EFFECT = new RageEffect(5);

  public static void init() {
    Registry.register(Registries.STATUS_EFFECT, Main.id("rage_effect"), RAGE_EFFECT);
  }
}
