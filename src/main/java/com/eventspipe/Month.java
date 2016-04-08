package com.eventspipe;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTimeConstants;

@SuppressWarnings("serial")
public class Month implements Serializable {

  public static final Month JANUARY =
      new Month(DateTimeConstants.JANUARY, "Enero");

  public static final Month FEBRUARY =
      new Month(DateTimeConstants.FEBRUARY, "Febrero");

  public static final Month MARCH =
      new Month(DateTimeConstants.MARCH, "Marzo");

  public static final Month APRIL =
      new Month(DateTimeConstants.APRIL, "Abril");

  public static final Month MAY =
      new Month(DateTimeConstants.MAY, "Mayo");

  public static final Month JUNE =
      new Month(DateTimeConstants.JUNE, "Junio");

  public static final Month JULY =
      new Month(DateTimeConstants.JULY, "Julio");

  public static final Month AUGUST =
      new Month(DateTimeConstants.AUGUST, "Agosto");

  public static final Month SEPTEMBER =
      new Month(DateTimeConstants.SEPTEMBER, "Septiembre");

  public static final Month OCTOBER =
      new Month(DateTimeConstants.OCTOBER, "Octubre");

  public static final Month NOVEMBER =
      new Month(DateTimeConstants.NOVEMBER, "Noviembre");

  public static final Month DECEMBER =
      new Month(DateTimeConstants.DECEMBER, "Diciembre");

  private static final Map<Integer, Month> MONTHS =
      new HashMap<Integer, Month>();

  static {
    MONTHS.put(DateTimeConstants.JANUARY, JANUARY);
    MONTHS.put(DateTimeConstants.FEBRUARY, FEBRUARY);
    MONTHS.put(DateTimeConstants.MARCH, MARCH);
    MONTHS.put(DateTimeConstants.APRIL, APRIL);
    MONTHS.put(DateTimeConstants.MAY, MAY);
    MONTHS.put(DateTimeConstants.JUNE, JUNE);
    MONTHS.put(DateTimeConstants.JULY, JULY);
    MONTHS.put(DateTimeConstants.AUGUST, AUGUST);
    MONTHS.put(DateTimeConstants.SEPTEMBER, SEPTEMBER);
    MONTHS.put(DateTimeConstants.OCTOBER, OCTOBER);
    MONTHS.put(DateTimeConstants.NOVEMBER, NOVEMBER);
    MONTHS.put(DateTimeConstants.DECEMBER, DECEMBER);
  }

  private int index;

  private String name;

  public Month(int anIndex, String aName) {
    this.index = anIndex;
    this.name = aName;
  }

  public String name() {
    return this.name;
  }

  public int index() {
    return this.index;
  }

  public static Collection<Month> allMonths() {
    return MONTHS.values();
  }

  @Override
  public String toString() {
    return name();
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
    Month other = (Month) obj;
    if (index != other.index)
      return false;
    if (!name.equals(other.name))
      return false;
    return true;
  }
}
