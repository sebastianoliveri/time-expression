package com.eventspipe;

import org.joda.time.Weeks;

import com.eventspipe.ExpressionAssertion;

public class DayOfWeekOfWeeklyTimeExpression implements Comparable<DayOfWeekOfWeeklyTimeExpression> {

  private DayOfWeek dayOfWeek;

  private Weeks amountOfWeeks;

  private TimeSpan[] timePeriods;

  public DayOfWeekOfWeeklyTimeExpression(
      DayOfWeek aDayOfWeek,
      Weeks anAmountOfWeeks,
      TimeSpan... theTimePeriods) {
    assertIsValid(anAmountOfWeeks);
    dayOfWeek = aDayOfWeek;
    amountOfWeeks = anAmountOfWeeks;
    timePeriods = theTimePeriods;
  }
  
  private static void assertIsValid(Weeks weeks) {
    ExpressionAssertion.
      isGreatherThanDescribedBy(
        weeks, 
        Weeks.ZERO, 
        TimeExpressionException.DAYS_AMOUNT_INVALID).
          assertTrue();
  }

  public DayOfWeek dayOfWeek() {
    return dayOfWeek;
  }

  public Weeks amountOfWeeks() {
    return amountOfWeeks;
  }

  public TimeSpan[] timePeriods() {
    return timePeriods;
  }

  public static DayOfWeekOfWeeklyTimeExpression onEveryDuring(
      DayOfWeek aDayOfWeek,
      Weeks anAmountOfWeeks,
      TimeSpan... theTimePeriods) {
    return new DayOfWeekOfWeeklyTimeExpression(
        aDayOfWeek, anAmountOfWeeks, theTimePeriods);
  }

  @Override
  public int compareTo(DayOfWeekOfWeeklyTimeExpression anotherDayOfWeekOfWeeklyTimeExpression) {
    return dayOfWeek.compareTo(anotherDayOfWeekOfWeeklyTimeExpression.dayOfWeek());
  }

  public boolean sameAs(int aDayOfWeekIndex) {
    return dayOfWeek.index() == aDayOfWeekIndex;
  }
}
