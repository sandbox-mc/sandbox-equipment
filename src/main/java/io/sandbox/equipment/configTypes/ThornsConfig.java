package io.sandbox.equipment.configTypes;

public class ThornsConfig {
  public boolean enabled;
  public int maxLevel;
  public float damagePerLevel;

  public ThornsConfig() {
    enabled = true;
    maxLevel = 5;
    damagePerLevel = 1.0F;
  }
}
