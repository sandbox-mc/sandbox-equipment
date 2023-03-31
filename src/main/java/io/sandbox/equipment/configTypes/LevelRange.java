package io.sandbox.equipment.configTypes;

public class LevelRange {
  public Integer constant;
  public Integer min;
  public Integer max;

  public Boolean matchLevel(int level) {
    // if nothing is set then always return true
    if (this.constant == null && this.min == null && this.max == null) {
      return true;
    }

    // Must either use constant or min/max, can't use both
    if (this.constant != null && this.constant != level) {
      return false;
    }

    if (this.min != null && level < this.min) {
      return false;
    }

    if (this.max != null && level > this.max) {
      return false;
    }

    return true;
  }
}
