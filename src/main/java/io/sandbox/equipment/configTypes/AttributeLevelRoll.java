package io.sandbox.equipment.configTypes;

import java.util.Random;

import net.minecraft.entity.EquipmentSlot;

public class AttributeLevelRoll {
  private static Random random = new Random();
  public String attributeName = "";
  public EquipmentSlot slot;
  public LevelRange ilvl = new LevelRange();
  public double constant = 0;
  public double max = 0;
  public double min = 0;
  public Integer decimalCount = 0; //
  public Rounded rounded = Rounded.ROUND;
  public Integer weight = 1;

  public double rollValue() {
    if (constant > 0) {
      return this.constant;
    }

    return round(random.nextDouble(this.min, this.max), decimalCount);
  }

  public double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    double tmp;
    switch(this.rounded) {
      case ROUND:
        tmp = (double) Math.round(value);
        break;
      case CEIL:
        tmp = Math.ceil(value);
        break;
      case FLOOR:
        tmp = Math.floor(value);
        break;
      default:
        return (double) 0;
    }

    return (double) tmp / factor;
  }
}
