package com.eventspipe;

import static org.junit.Assert.assertEquals;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Months;
import org.junit.Test;

import com.eventspipe.BinaryTimeExpression;
import com.eventspipe.DailyTimeExpression;
import com.eventspipe.DayOfWeek;
import com.eventspipe.DayOfWeekInWeekOfMonthTimeExpression;
import com.eventspipe.MonthOfYear;
import com.eventspipe.TimeExpression;
import com.eventspipe.TimeSpan;
import com.eventspipe.WeekOfMonth;

public class BinaryTimeExpressionTest {

  @Test
  public void testBinaryAsString() {
    DayOfWeekInWeekOfMonthTimeExpression firstFridaysEveryTwoMonths = TimeExpression
        .monthlyEveryOnStartsOnEndsOn(Months.TWO, DayOfWeek.FRIDAY,
            WeekOfMonth.FIRST, MonthOfYear.now(), MonthOfYear.on(LocalDate
                .now().getMonthOfYear(), LocalDate.now().getYear() + 1),
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));
    DailyTimeExpression everyTwoDaysFromTodayUpToOneMonth = TimeExpression
        .dailyEveryStartsOnEndsOn(Days.TWO, LocalDate.now(), LocalDate.now()
            .plusMonths(1), TimeSpan.fromTo(LocalTime.now(), LocalTime.now()
            .plusHours(1)));
    TimeExpression firstFridaysEveryTwoMonthsAndEveryTwoDaysFromTodayUpToOneMonth = firstFridaysEveryTwoMonths
        .and(everyTwoDaysFromTodayUpToOneMonth);

    assertEquals(
        firstFridaysEveryTwoMonthsAndEveryTwoDaysFromTodayUpToOneMonth,
        BinaryTimeExpression
            .from(firstFridaysEveryTwoMonthsAndEveryTwoDaysFromTodayUpToOneMonth
                .asJson()));
  }

  @Test
  public void testBinaryAsString2() {
    LocalTime oneOClock = new LocalTime(1, 0, 0);
    LocalTime threeOClock = new LocalTime(3, 0, 0);
    LocalTime threeThirtyOClock = new LocalTime(3, 30, 0);
    LocalTime fiveOClock = new LocalTime(5, 0, 0);
    LocalTime sevenOClock = new LocalTime(7, 0, 0);
    LocalTime nineOClock = new LocalTime(9, 0, 0);

    DayOfWeekInWeekOfMonthTimeExpression firstFridaysEveryTwoMonths = TimeExpression
        .monthlyEveryOnStartsOnEndsOn(Months.TWO, DayOfWeek.FRIDAY,
            WeekOfMonth.FIRST, MonthOfYear.now(), MonthOfYear.on(LocalDate
                .now().getMonthOfYear(), LocalDate.now().getYear() + 1),
            TimeSpan.fromTo(oneOClock, threeOClock), TimeSpan.fromTo(
                threeThirtyOClock, fiveOClock), TimeSpan.fromTo(sevenOClock,
                nineOClock));
    DailyTimeExpression everyTwoDaysFromTodayUpToOneMonth = TimeExpression
        .dailyEveryStartsOnEndsOn(Days.TWO, LocalDate.now(), LocalDate.now()
            .plusMonths(1), TimeSpan.fromTo(oneOClock, threeOClock), TimeSpan
            .fromTo(threeThirtyOClock, fiveOClock));
    TimeExpression firstFridaysEveryTwoMonthsAndEveryTwoDaysFromTodayUpToOneMonth = firstFridaysEveryTwoMonths
        .and(everyTwoDaysFromTodayUpToOneMonth);

    assertEquals(
        firstFridaysEveryTwoMonthsAndEveryTwoDaysFromTodayUpToOneMonth,
        BinaryTimeExpression
            .from(firstFridaysEveryTwoMonthsAndEveryTwoDaysFromTodayUpToOneMonth
                .asJson()));
  }

  @Test
  public void testBinaryAsString3() {
    DayOfWeekInWeekOfMonthTimeExpression firstFridaysEveryTwoMonths = TimeExpression
        .monthlyEveryOnStartsOnEndsOn(Months.TWO, DayOfWeek.FRIDAY,
            WeekOfMonth.FIRST, MonthOfYear.now(), MonthOfYear.on(LocalDate
                .now().getMonthOfYear(), LocalDate.now().getYear() + 1),
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));
    DailyTimeExpression everyTwoDaysFromTodayUpToOneMonth = TimeExpression
        .dailyEveryStartsOnEndsOn(Days.TWO, LocalDate.now(), LocalDate.now()
            .plusMonths(1), TimeSpan.fromTo(LocalTime.now().plusHours(2),
            LocalTime.now().plusHours(2).plusMinutes(30)));

    TimeExpression firstFridaysEveryTwoMonthsAndEveryTwoDaysFromTodayUpToOneMonth = firstFridaysEveryTwoMonths
        .and(everyTwoDaysFromTodayUpToOneMonth)
        .and(
            TimeExpression.onFromUntil(LocalDate.now(),
                TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1))));

    assertEquals(
        firstFridaysEveryTwoMonthsAndEveryTwoDaysFromTodayUpToOneMonth,
        BinaryTimeExpression
            .from(firstFridaysEveryTwoMonthsAndEveryTwoDaysFromTodayUpToOneMonth
                .asJson()));
  }
}
