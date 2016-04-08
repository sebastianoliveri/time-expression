package com.eventspipe;

public class DayOfMonth {

  private static final int MIN = 1;
  private static final int MAX = 31;

  public static final String DAY_OF_MONTH_INVALID =
      "Invalid day of month index";

  private int index;

  private DayOfMonth(int anIndex) {
    index = anIndex;
  }

  public static DayOfMonth valueOf(int anIndex) {
    if (anIndex < MIN || anIndex > MAX) {
      throw new RuntimeException(DAY_OF_MONTH_INVALID);
    }
    return new DayOfMonth(anIndex);
  }

  public int index() {
    return index;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + index;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DayOfMonth other = (DayOfMonth) obj;
    if (index != other.index)
      return false;
    return true;
  }
}
