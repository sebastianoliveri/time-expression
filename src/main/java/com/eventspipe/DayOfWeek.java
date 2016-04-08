package com.eventspipe;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTimeConstants;

import com.eventspipe.ExpressionAssertion;

@SuppressWarnings("serial")
public class DayOfWeek implements Comparable<DayOfWeek>, Serializable {

  public static final DayOfWeek MONDAY =
      new DayOfWeek(DateTimeConstants.MONDAY, "Lunes");

  public static final DayOfWeek TUESDAY =
      new DayOfWeek(DateTimeConstants.TUESDAY, "Martes");

  public static final DayOfWeek WEDNESDAY =
      new DayOfWeek(DateTimeConstants.WEDNESDAY, "Miercoles");

  public static final DayOfWeek THURSDAY =
      new DayOfWeek(DateTimeConstants.THURSDAY, "Jueves");

  public static final DayOfWeek FRIDAY =
      new DayOfWeek(DateTimeConstants.FRIDAY, "Viernes");

  public static final DayOfWeek SATURDAY =
      new DayOfWeek(DateTimeConstants.SATURDAY, "Sabado");

  public static final DayOfWeek SUNDAY =
      new DayOfWeek(DateTimeConstants.SUNDAY, "Domingo");

  private static final Map<Integer, DayOfWeek> DAYS_OF_WEEK =
      new HashMap<Integer, DayOfWeek>();

  static {
    DAYS_OF_WEEK.put(DateTimeConstants.MONDAY, MONDAY);
    DAYS_OF_WEEK.put(DateTimeConstants.TUESDAY, TUESDAY);
    DAYS_OF_WEEK.put(DateTimeConstants.WEDNESDAY, WEDNESDAY);
    DAYS_OF_WEEK.put(DateTimeConstants.THURSDAY, THURSDAY);
    DAYS_OF_WEEK.put(DateTimeConstants.FRIDAY, FRIDAY);
    DAYS_OF_WEEK.put(DateTimeConstants.SATURDAY, SATURDAY);
    DAYS_OF_WEEK.put(DateTimeConstants.SUNDAY, SUNDAY);
  }

  private Integer index;

  private String name;

  public DayOfWeek(int index, String aName) {
    this.index = index;
    this.name = aName;
  }

  public String name() {
    return this.name;
  }

  public static DayOfWeek valueOf(int index) {
    ExpressionAssertion.isBetweenDescribedBy(
        index, 
        DateTimeConstants.MONDAY, 
        DateTimeConstants.SUNDAY, 
        TimeExpressionException.DAY_OF_WEEK_INVALID).
          assertTrue();
    
    return DAYS_OF_WEEK.get(index);
  }

  public static Collection<DayOfWeek> allDays() {
    return DAYS_OF_WEEK.values();
  }

  public boolean isLessThan(DayOfWeek aDayOfWeek) {
    return this.index < aDayOfWeek.index;
  }

  @Override
  public int compareTo(DayOfWeek aDayOfWeek) {
    return this.index.compareTo(aDayOfWeek.index);
  }

  public Integer index() {
    return index;
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
    DayOfWeek other = (DayOfWeek) obj;
    if (index != other.index)
      return false;
    return true;
  }
}
