package com.eventspipe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.GregorianCalendar;
import java.util.Iterator;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Months;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonElement;

public class DayOfMonthTimeExpressionTest {

  @Test
  public void shouldFailWhenAmountOfMonthsIsInvalid() {
    try {
      TimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.ZERO, 
          DayOfMonth.valueOf(1), 
          MonthOfYear.on(1, 2015), 
          MonthOfYear.on(4, 2015), 
          TimeSpan.fromTo(
              new LocalTime(9, 0), 
              new LocalTime(10, 0)));
      fail();
    } catch (TimeExpressionException exception) {
      assertEquals(
          TimeExpressionException.EVERY_AMOUNT_INVALID.getMessage(), 
          exception.getMessage());
    }
    try {
      TimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.months(-1), 
          DayOfMonth.valueOf(1), 
          MonthOfYear.on(1, 2015), 
          MonthOfYear.on(4, 2015), 
          TimeSpan.fromTo(
              new LocalTime(9, 0), 
              new LocalTime(10, 0)));
      fail();
    } catch (TimeExpressionException exception) {
      assertEquals(
          TimeExpressionException.EVERY_AMOUNT_INVALID.getMessage(), 
          exception.getMessage());
    }    
  }
  
  @Test
  public void shouldFailWhenStartingMonthOfYearIsEqualToOrGreaterThanEndingMonth() {
    try {
      TimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.months(1), 
          DayOfMonth.valueOf(1), 
          MonthOfYear.on(1, 2015), 
          MonthOfYear.on(1, 2015), 
          TimeSpan.fromTo(
              new LocalTime(9, 0), 
              new LocalTime(10, 0)));
      fail();
    } catch (TimeExpressionException exception) {
      assertEquals(
          TimeExpressionException.DATE_SPAN_INVALID.getMessage(), 
          exception.getMessage());
    }
    try {
      TimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.months(1), 
          DayOfMonth.valueOf(1), 
          MonthOfYear.on(2, 2015), 
          MonthOfYear.on(1, 2015), 
          TimeSpan.fromTo(
              new LocalTime(9, 0), 
              new LocalTime(10, 0)));
      fail();
    } catch (TimeExpressionException exception) {
      assertEquals(
          TimeExpressionException.DATE_SPAN_INVALID.getMessage(), 
          exception.getMessage());
    }    
  }
  
  @Test
  public void testDayOfMondayWhenItDoesNotReccurrs() {
    try {
      PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.TWO, 
          DayOfMonth.valueOf(7),
          MonthOfYear.on(1, 2012),
          MonthOfYear.on(2, 2012),
          TimeSpan.fromTo(
              new LocalTime(9, 0),
              new LocalTime(10, 0)));
      Assert.fail();
    } catch (TimeExpressionException exception) {
      assertEquals(
          TimeExpressionException.ENDING_DATE_INVALID.getMessage(), 
          exception.getMessage());
    }
  }
  
  @Test
  public void testDayOfMonday() {
    Iterator<DateTimeSpan> iterator = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.ONE, 
          DayOfMonth.valueOf(7),
          MonthOfYear.on(1, 2012),
          MonthOfYear.on(2, 2012),
          TimeSpan.fromTo(
              new LocalTime(9, 0),
              new LocalTime(10, 0))).
                 iterator();
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2012, 1, 7), 
        iterator.next().startingDateTime().toLocalDate());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2012, 2, 7), 
        iterator.next().startingDateTime().toLocalDate());
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testDayOfMondayShouldReadjustEndingDate() {
    DayOfMonthTimeExpression timeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.TWO, 
          DayOfMonth.valueOf(7),
          MonthOfYear.on(1, 2012),
          MonthOfYear.on(4, 2012),
          TimeSpan.fromTo(
              new LocalTime(9, 0),
              new LocalTime(10, 0)));
    
    Assert.assertEquals(
        new LocalDate(2012, 3, 7), 
        timeExpression.endingDateTime().toLocalDate());
    
    Iterator<DateTimeSpan> iterator = timeExpression.iterator();
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2012, 1, 7), 
        iterator.next().startingDateTime().toLocalDate());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2012, 3, 7), 
        iterator.next().startingDateTime().toLocalDate());
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testDayOfMondayShouldReadjustEndingDate2() {
    DayOfMonthTimeExpression timeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.SIX, 
          DayOfMonth.valueOf(1),
          MonthOfYear.on(1, 2012),
          MonthOfYear.on(9, 2013),
          TimeSpan.fromTo(
              new LocalTime(9, 0),
              new LocalTime(10, 0)));
    
    Assert.assertEquals(
        new LocalDate(2013, 7, 1), 
        timeExpression.endingDateTime().toLocalDate());
    
    Iterator<DateTimeSpan> iterator = timeExpression.iterator();
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2012, 1, 1), 
        iterator.next().startingDateTime().toLocalDate());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2012, 7, 1), 
        iterator.next().startingDateTime().toLocalDate());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2013, 1, 1), 
        iterator.next().startingDateTime().toLocalDate());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2013, 7, 1), 
        iterator.next().startingDateTime().toLocalDate());
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testDayOfMonth() {
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);
    DayOfMonthTimeExpression aDayOfMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.ONE, 
            DayOfMonth.valueOf(7),
            MonthOfYear.on(1, 2012), 
            MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(aStartingTime, anEndingTime));

    assertTrue(aDayOfMonthTimeExpression.numberOfOcurringDays() == 5);
    
    Iterator<DateTimeSpan> iterator = aDayOfMonthTimeExpression.iterator();

    LocalDateTime expectedDate = new LocalDateTime(2012, 1, 7,
        aStartingTime.getHourOfDay(), aStartingTime.getMinuteOfHour(),
        aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);

    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);

    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);

    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);

    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testDayOfMonthEndsNever() {
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);
    DayOfMonthTimeExpression aDayOfMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsNever(
            Months.ONE, 
            DayOfMonth.valueOf(7),
            MonthOfYear.on(1, 2012), 
            TimeSpan.fromTo(aStartingTime, anEndingTime));

    assertTrue(aDayOfMonthTimeExpression.numberOfOcurringDays() == Integer.MAX_VALUE);
    
    Iterator<DateTimeSpan> iterator = aDayOfMonthTimeExpression.iterator();

    LocalDateTime expectedDate = 
        new LocalDateTime(
            2012, 1, 7,
            aStartingTime.getHourOfDay(), 
            aStartingTime.getMinuteOfHour(),
            aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);

    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);

    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);

    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);

    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);

    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);
    
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);
    
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);
    
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));
    expectedDate = expectedDate.plusMonths(1);
    
    //ETC
  }
  
  @Test
  public void testDayOfMonthEveryTwoMonths() {
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);
    DayOfMonthTimeExpression aDayOfMonthTimeExpression = PeriodicTimeExpression
        .monthlyEveryOnStartsOnEndsOn(Months.TWO, DayOfMonth.valueOf(7),
            MonthOfYear.on(1, 2012), MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(aStartingTime, anEndingTime));

    assertTrue(aDayOfMonthTimeExpression.numberOfOcurringDays() == 3);

    Iterator<DateTimeSpan> iterator = aDayOfMonthTimeExpression.iterator();

    LocalDateTime expectedDate = new LocalDateTime(2012, 1, 7,
        aStartingTime.getHourOfDay(), aStartingTime.getMinuteOfHour(),
        aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = expectedDate.plusMonths(2);
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = expectedDate.plusMonths(2);
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    assertFalse(iterator.hasNext());
  }

  @Test
  public void testDayOfMonthAsString() {
    DayOfMonthTimeExpression thirdDayOfEveryMonthFromThisMonthUpToOneYear = 
        TimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.ONE, 
            DayOfMonth.valueOf(3),
            MonthOfYear.now(), 
            MonthOfYear.on(LocalDate.now().getMonthOfYear(),
            LocalDate.now().getYear() + 1), TimeSpan.fromTo(
            LocalTime.now(), LocalTime.now().plusHours(1)));

    assertEquals(
        thirdDayOfEveryMonthFromThisMonthUpToOneYear,
        DayOfMonthTimeExpression.from(thirdDayOfEveryMonthFromThisMonthUpToOneYear.asJson()));
  }

  @Test
  public void testDayOfMonthAsString2() {
    LocalTime oneOClock = new LocalTime(1, 0, 0);
    LocalTime threeOClock = new LocalTime(3, 0, 0);
    LocalTime threeThirtyOClock = new LocalTime(3, 30, 0);
    LocalTime fiveOClock = new LocalTime(5, 0, 0);
    LocalTime sevenOClock = new LocalTime(7, 0, 0);
    LocalTime nineOClock = new LocalTime(9, 0, 0);

    DayOfMonthTimeExpression thirdDayOfEveryMonthFromThisMonthUpToOneYear = 
        TimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.ONE, 
            DayOfMonth.valueOf(3),
            MonthOfYear.now(), 
            MonthOfYear.on(LocalDate.now().getMonthOfYear(),
            LocalDate.now().getYear() + 1), 
            TimeSpan.fromTo(oneOClock, threeOClock), 
            TimeSpan.fromTo(threeThirtyOClock, fiveOClock),
            TimeSpan.fromTo(sevenOClock, nineOClock));

    assertEquals(thirdDayOfEveryMonthFromThisMonthUpToOneYear,
        DayOfMonthTimeExpression.from(thirdDayOfEveryMonthFromThisMonthUpToOneYear.asJson()));
  }
  
  @Test
  public void testDayOfMonthAsString3() {
    DayOfMonthTimeExpression thirdDayOfEveryMonthFromThisMonthUpToOneYear =
      TimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.ONE,
          DayOfMonth.valueOf(3),
          MonthOfYear.now(),
          MonthOfYear.on(
              LocalDate.now().getMonthOfYear(),
              LocalDate.now().getYear() + 1),
          TimeSpan.fromTo(
              LocalTime.now(),
              LocalTime.now().plusHours(1)));

    JsonElement thirdDayOfEveryMonthFromThisMonthUpToOneYearAsString =
        thirdDayOfEveryMonthFromThisMonthUpToOneYear.asJson();

    assertEquals(thirdDayOfEveryMonthFromThisMonthUpToOneYear,
        DayOfMonthTimeExpression.from(thirdDayOfEveryMonthFromThisMonthUpToOneYearAsString));
  }
  
  @Test
  public void testSecondDayOfMonthEveryOneMonth() {
    Months oneMonth = Months.ONE;
    DayOfMonthTimeExpression secondDayOfEveryMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
            oneMonth, 
            DayOfMonth.valueOf(2),
            MonthOfYear.on(1, 2012), 
            MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertTrue(secondDayOfEveryMonthTimeExpression.numberOfOcurringDays() == 5);
    
    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek;
    firstMondayOfWeek = LocalDate
        .fromCalendarFields(firstMondayOf2012AsCalendar);
    
    LocalDate secondDayOfFirstMonth = firstMondayOfWeek;
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth));
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(1)));
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(2)));
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(3)));
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(4)));
    assertFalse(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(5)));
  }
  
  @Test
  public void testSecondDayOfMonthEveryOneMonthEndingAfter() {
    Months oneMonth = Months.ONE;
    int fiveOcurrences = 5;
    DayOfMonthTimeExpression secondDayOfEveryMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsAfter(
            oneMonth,
            DayOfMonth.valueOf(2), 
            MonthOfYear.on(1, 2012), 
            fiveOcurrences,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertTrue(secondDayOfEveryMonthTimeExpression.numberOfOcurringDays() == 5);
    
    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek;
    firstMondayOfWeek = LocalDate
        .fromCalendarFields(firstMondayOf2012AsCalendar);
    
    LocalDate secondDayOfFirstMonth = firstMondayOfWeek;
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth));
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(1)));
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(2)));
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(3)));
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(4)));
    assertFalse(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(5)));
  }
  
  @Test
  public void testSecondDayOfMonthEveryTwoMonth() {
    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek;
    firstMondayOfWeek = LocalDate
        .fromCalendarFields(firstMondayOf2012AsCalendar);
    LocalDate secondDayOfFirstMonth = firstMondayOfWeek;
    LocalDate timeSpanEndingDate = secondDayOfFirstMonth.plusMonths(4);
    DayOfMonthTimeExpression secondDayOfEveryMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.TWO, 
            DayOfMonth.valueOf(2),
            MonthOfYear.on(1, 2012), 
            MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertTrue(secondDayOfEveryMonthTimeExpression.numberOfOcurringDays() == 3);
    
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth));
    assertFalse(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(1)));
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(2)));
    assertFalse(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(3)));
    assertTrue(secondDayOfEveryMonthTimeExpression.evaluate(timeSpanEndingDate));
    assertFalse(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(5)));
  }
  
  @Test
  public void testSecondDayOfMonthEveryThreeMonth() {
    Months threeMonths = Months.THREE;
    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek;
    firstMondayOfWeek = LocalDate
        .fromCalendarFields(firstMondayOf2012AsCalendar);
    LocalDate secondDayOfFirstMonth = firstMondayOfWeek;
    LocalDate timeSpanEndingDate = secondDayOfFirstMonth.plusMonths(6);
    DayOfMonthTimeExpression secondDayOfEveryMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
            threeMonths, 
            DayOfMonth.valueOf(2),
            MonthOfYear.on(1, 2012), 
            MonthOfYear.on(7, 2012),
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertTrue(secondDayOfEveryMonthTimeExpression.numberOfOcurringDays() == 3);
    
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth));
    assertFalse(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(1)));
    assertFalse(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(2)));
    assertTrue(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(3)));
    assertFalse(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(4)));
    assertFalse(secondDayOfEveryMonthTimeExpression
        .evaluate(secondDayOfFirstMonth.plusMonths(5)));
    assertTrue(secondDayOfEveryMonthTimeExpression.evaluate(timeSpanEndingDate));
  }
}
