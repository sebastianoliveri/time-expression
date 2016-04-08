package com.eventspipe;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.eventspipe.ExpressionAssertion;

@SuppressWarnings("serial")
public class WeekOfMonth implements Serializable {

  public static final int FIRST_WEEK_INDEX = 1;
  public static final int SECOND_WEEK_INDEX = 2;
  public static final int THIRD_WEEK_INDEX = 3;
  public static final int FOURTH_WEEK_INDEX = 4;
  public static final int LAST_WEEK_INDEX = 5;
  
  public static final WeekOfMonth FIRST = new WeekOfMonth(1, "Primera");
  public static final WeekOfMonth SECOND = new WeekOfMonth(2, "Segunda");
  public static final WeekOfMonth THIRD = new WeekOfMonth(3, "Tercera");
  public static final WeekOfMonth FOURTH = new WeekOfMonth(4, "Cuarta");
  public static final WeekOfMonth LAST = new WeekOfMonth(5, "Ultima");

  private static final Map<Integer, WeekOfMonth> ALL_WEEKS =
      new HashMap<Integer, WeekOfMonth>();

  static {
    ALL_WEEKS.put(FIRST_WEEK_INDEX, FIRST);
    ALL_WEEKS.put(SECOND_WEEK_INDEX, SECOND);
    ALL_WEEKS.put(THIRD_WEEK_INDEX, THIRD);
    ALL_WEEKS.put(FOURTH_WEEK_INDEX, FOURTH);
    ALL_WEEKS.put(LAST_WEEK_INDEX, LAST);
  }

  private int index;

  private String name;

  private WeekOfMonth(int anIndex, String aName) {
    this.index = anIndex;
    this.name = aName;
  }

  public String name() {
    return this.name;
  }

  public int index() {
    return this.index;
  }

  public static WeekOfMonth valueOf(int index) {
    ExpressionAssertion.isBetweenDescribedBy(
        index, 
        FIRST_WEEK_INDEX, 
        LAST_WEEK_INDEX, 
        TimeExpressionException.WEEK_OF_MONTH_INVALID).
          assertTrue();
    return ALL_WEEKS.get(index);
  }

  public static Collection<WeekOfMonth> allWeeks() {
    return ALL_WEEKS.values();
  }

  @Override
  public String toString() {
    return this.name();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + index;
    result = prime * result + name.hashCode();
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
    WeekOfMonth other = (WeekOfMonth) obj;
    if (index != other.index)
      return false;
    if (!name.equals(other.name))
      return false;
    return true;
  }
}
