package com.eventspipe;

import org.joda.time.Days;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.joda.time.MonthDay;

import com.eventspipe.ExpressionAssertion;

public class MonthOfYear {

  private static final int YEAR_MIN = 1;
  private static final int MONTH_MIN = 1;
  private static final int MONTH_MAX = 12;
  
  private int year;

  private int month;

  public MonthOfYear(int aMonth, int anYear) {
    ExpressionAssertion.
      isBetweenDescribedBy(
        aMonth, 
        MONTH_MIN, 
        MONTH_MAX, 
        TimeExpressionException.MONTH_INVALID).
          assertTrue();
    ExpressionAssertion.
      isGreatherThanOrEqualToDescribedBy(
        anYear, 
        YEAR_MIN,
        TimeExpressionException.YEAR_INVALID).
          assertTrue();
    month = aMonth;
    year = anYear;
  }

  public LocalDate dateOfOn(DayOfWeek aDayOfWeek, WeekOfMonth aWeekOfMonth) {
    LocalDate aLocalDate = dateForFirstOcurrenceOf(aDayOfWeek);
    aLocalDate = advanceTo(aLocalDate, aWeekOfMonth);
    return aLocalDate;
  }

  private LocalDate advanceTo(LocalDate aLocalDate, WeekOfMonth aWeekOfMonth) {
    int numberOfWeeks = aWeekOfMonth.index();
    if (aWeekOfMonth.equals(WeekOfMonth.LAST)) {
      numberOfWeeks = numberOfWeeks - 2;
    } else {
      numberOfWeeks = numberOfWeeks - 1;
    }
    aLocalDate = aLocalDate.plusWeeks(numberOfWeeks);
    if (aWeekOfMonth.equals(WeekOfMonth.LAST)) {
      if (!(Days.days(aLocalDate.getDayOfMonth()).plus(Days.SEVEN).
          isGreaterThan(Days.days(aLocalDate.dayOfMonth().getMaximumValue())))) {
        aLocalDate = aLocalDate.plusWeeks(1);
      }
    }
    return aLocalDate;
  }

  private LocalDate dateForFirstOcurrenceOf(DayOfWeek aDayOfWeek) {
    LocalDate firstDateOfMonth = new LocalDate(this.year, this.month, 1);
    if (firstDateOfMonth.getDayOfWeek() < aDayOfWeek.index()) {
      firstDateOfMonth = firstDateOfMonth.plusDays(
          aDayOfWeek.index() - firstDateOfMonth.getDayOfWeek());
    } else if (firstDateOfMonth.getDayOfWeek() > aDayOfWeek.index()) {
      firstDateOfMonth = firstDateOfMonth.
          plusDays((7 - firstDateOfMonth.getDayOfWeek()) + aDayOfWeek.index());
    }
    return firstDateOfMonth;
  }

  public LocalDate dateAt(DayOfMonth aDayOfMonth) {
    try {
      new MonthDay(month, aDayOfMonth.index());
    } catch (IllegalFieldValueException exception) {
      throw TimeExpressionException.DAY_OF_MONTH_INVALID;
    }
    return new LocalDate(year, month, aDayOfMonth.index());
  }

  public static MonthOfYear now() {
    LocalDate aLocalDate = LocalDate.now();
    return new MonthOfYear(
        aLocalDate.getMonthOfYear(),
        aLocalDate.getYear());
  }

  public static MonthOfYear on(int aMonth, int anYear) {
    return new MonthOfYear(aMonth, anYear);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + month;
    result = prime * result + year;
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
    MonthOfYear other = (MonthOfYear) obj;
    if (month != other.month)
      return false;
    if (year != other.year)
      return false;
    return true;
  }
}
