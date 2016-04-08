package com.eventspipe;

import java.util.function.Function;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.YearMonth;
import org.joda.time.Years;
import org.joda.time.base.BaseSingleFieldPeriod;

public class DateSpanReadjustment {

  private DateSpan dateSpan;
  
  private BaseSingleFieldPeriod magnitude;

  private Function<Integer, LocalDate> function;

  public DateSpanReadjustment(
      DateSpan aDateSpan, 
      BaseSingleFieldPeriod aMagnitude, 
      Function<Integer, LocalDate> aFunction) {
    dateSpan = aDateSpan;
    magnitude = aMagnitude;
    function = aFunction;
  }
  
  public DateSpan apply() {
    ExpressionAssertion.
    isGreatherThanDescribedBy(
      magnitude.getValue(0), 0, 
      TimeExpressionException.EVERY_AMOUNT_INVALID).
        assertTrue();
    
    int difference = 
        TimeDifferenceCalculation.
          differenceBetween(
              magnitude.getClass(), 
              dateSpan.startingDate(),
              dateSpan.endingDate());
  
    LocalDate readjustedEndingDate =
        function.apply(difference % magnitude.getValue(0));
  
    ExpressionAssertion.isNotEqualTo(
        dateSpan.startingDate(), 
        readjustedEndingDate, 
        TimeExpressionException.ENDING_DATE_INVALID).
          assertTrue();
  
    return DateSpan.fromTo(
        dateSpan.startingDate(), 
        readjustedEndingDate);
  }
  
  public static DateSpanReadjustment forDailyTimeExpression(
      DateSpan aDateSpan, 
      Days anAmountOfDays) {
    return new DateSpanReadjustment(
        aDateSpan, 
        anAmountOfDays,
        (difference) -> 
          aDateSpan.endingDate().minusDays(difference));
  }

  public static DateSpanReadjustment forDayOfMonthTimeExpression(
      DateSpan aDateSpan,
      Months anAmountOfMonths) {
    return new DateSpanReadjustment(
        aDateSpan, 
        anAmountOfMonths,
        (difference) -> 
          aDateSpan.endingDate().minusMonths(difference));
  }
  
  public static DateSpanReadjustment forYearlyTimeExpression(
      DateSpan aDateSpan,
      Years anAmountOfYears) {
    return new DateSpanReadjustment(
        aDateSpan, 
        anAmountOfYears,
        (difference) -> 
          aDateSpan.endingDate().minusYears(difference));
  }

  public static DateSpanReadjustment forDayOfWeekInWeekOfMonthTimeExpression(
      DateSpan aDateSpan, 
      Months anAmountOfMonths, 
      DayOfWeek aDayOfWeek, 
      WeekOfMonth aWeekOfMonth) {
    return new DateSpanReadjustment(
        aDateSpan, 
        anAmountOfMonths,
        (difference) -> {
          LocalDate readjustedEndingDate = aDateSpan.endingDate();
          if (difference > 0) {
            YearMonth readjustatedYearMonth = 
                new YearMonth(
                  aDateSpan.endingDate().getYear(), 
                  aDateSpan.endingDate().getMonthOfYear()).
                    minusMonths(difference % anAmountOfMonths.getValue(0));
            readjustedEndingDate = 
              MonthOfYear.on(
                readjustatedYearMonth.getMonthOfYear(), 
                readjustatedYearMonth.getYear()).
                  dateOfOn(aDayOfWeek, aWeekOfMonth);
          }
          return readjustedEndingDate;
        });
  }
}
