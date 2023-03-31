package io.sandbox.equipment.configTypes;

import java.util.ArrayList;
import java.util.List;

public class Attribute {
  public String attributeName = "";
  public List<String> allowedItems = new ArrayList<>();
  public List<String> allowedTags = new ArrayList<>();
  public List<AttributeLevelRoll> levelRolls = new ArrayList<>();
}
