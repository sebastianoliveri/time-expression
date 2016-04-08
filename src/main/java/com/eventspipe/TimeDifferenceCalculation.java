package com.eventspipe;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Years;
import org.joda.time.base.BaseSingleFieldPeriod;

public class TimeDifferenceCalculation {

  protected static Map<Class<? extends BaseSingleFieldPeriod>,
    TimeDifferenceCalculator> calculationRulesByUnit;
  
  static {
    calculationRulesByUnit =
        new HashMap<Class<? extends BaseSingleFieldPeriod>,
          TimeDifferenceCalculator>();

    calculationRulesByUnit.put(Days.class,
        new TimeDifferenceCalculator() {
          @Override
          public int differenteBetween(LocalDate aDate, LocalDate anotherDate) {
            return Days.daysBetween(aDate, anotherDate).getDays();
          }
        });
    
    calculationRulesByUnit.put(Months.class,
        new TimeDifferenceCalculator() {
          @Override
          public int differenteBetween(LocalDate aDate, LocalDate anotherDate) {
            if (aDate.isBefore(anotherDate)) {
              return (anotherDate.getYear() * 12 + anotherDate.getMonthOfYear()) -
                    (aDate.getYear() * 12 + aDate.getMonthOfYear());
            }
            return (aDate.getYear() * 12 + aDate.getMonthOfYear()) -
                (anotherDate.getYear() * 12 + anotherDate.getMonthOfYear());
          }
        });
    
    calculationRulesByUnit.put(Years.class,
        new TimeDifferenceCalculator() {
          @Override
          public int differenteBetween(LocalDate aDate, LocalDate anotherDate) {
            if (aDate.isBefore(anotherDate)) {
              return anotherDate.getYear() - aDate.getYear();
            }
            return aDate.getYear() - anotherDate.getYear();
          }
        });
  }
  
  public static int differenceBetween(
      Class<? extends BaseSingleFieldPeriod> aUnit, 
      LocalDate aDate, 
      LocalDate anotherDate) {
    return
        calculationRulesByUnit.
          get(aUnit).
            differenteBetween(
                aDate,
                anotherDate);
  }
  
  interface TimeDifferenceCalculator {
    int differenteBetween(LocalDate aDate, LocalDate anotherDate);
  }
}
