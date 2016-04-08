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
import org.joda.time.MonthDay;
import org.joda.time.Years;
import org.junit.Assert;
import org.junit.Test;

import com.eventspipe.DateTimeSpan;
import com.eventspipe.PeriodicTimeExpression;
import com.eventspipe.TimeExpression;
import com.eventspipe.TimeExpressionException;
import com.eventspipe.TimeSpan;
import com.eventspipe.YearlyTimeExpression;
import com.google.gson.JsonElement;

public class YearlyTimeExpressionTest {
  
  @Test
  public void shouldFailWhenAmountOfMonthsIsInvalid() {
    try {
      TimeExpression.yearlyEveryOnStartsOnEndsOn(
          Years.ZERO, 
          new MonthDay(1, 1), 
          2015, 
          2016, 
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
      TimeExpression.yearlyEveryOnStartsOnEndsOn(
          Years.years(-1), 
          new MonthDay(1, 1), 
          2015, 
          2016, 
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
  public void testYearlyWhenItDoesNotOccurr() {
    try {
      TimeExpression.yearlyEveryOnStartsOnEndsOn(
          Years.THREE, 
          new MonthDay(7, 8), 
          2012, 
          2014, 
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
  public void testYearlyShouldBeReadjusted() {
    YearlyTimeExpression timeExpression = 
        TimeExpression.yearlyEveryOnStartsOnEndsOn(
          Years.TWO, 
          new MonthDay(7, 8), 
          2012, 
          2015, 
          TimeSpan.fromTo(
              new LocalTime(9, 0), 
              new LocalTime(10, 0)));
    
    assertEquals(
        new LocalDate(2014, 7, 8), 
        timeExpression.endingDateTime().toLocalDate());
    
    Iterator<DateTimeSpan> iterator = timeExpression.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(new LocalDate(2012, 7, 8), 
        iterator.next().startingDateTime().toLocalDate());
    assertTrue(iterator.hasNext());
    assertEquals(new LocalDate(2014, 7, 8), 
        iterator.next().startingDateTime().toLocalDate());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testYearly() {
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);
    YearlyTimeExpression yearlyTimeExpression = 
        PeriodicTimeExpression.yearlyEveryOnStartsOnEndsOn(
            Years.ONE, 
            new MonthDay(7, 8), 
            2012,
            2017, 
            TimeSpan.fromTo(aStartingTime, anEndingTime));

    Iterator<DateTimeSpan> iterator = yearlyTimeExpression.iterator();

    LocalDateTime expectedDate = new LocalDateTime(2012, 7, 8,
        aStartingTime.getHourOfDay(), aStartingTime.getMinuteOfHour(),
        aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2013, 7, 8, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2014, 7, 8, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2015, 7, 8, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2016, 7, 8, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2017, 7, 8, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testYearlyEveryTwoYears() {
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);
    YearlyTimeExpression yearlyTimeExpression = 
        PeriodicTimeExpression.yearlyEveryOnStartsOnEndsOn(
            Years.TWO, 
            new MonthDay(8, 7), 
            2012,
            2017, 
            TimeSpan.fromTo(aStartingTime, anEndingTime));

    Iterator<DateTimeSpan> iterator = yearlyTimeExpression.iterator();

    LocalDateTime expectedDate = new LocalDateTime(2012, 8, 7,
        aStartingTime.getHourOfDay(), aStartingTime.getMinuteOfHour(),
        aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2014, 8, 7, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2016, 8, 7, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testYearlyAsString() {
    YearlyTimeExpression march27EveryOneYearFromThisYearToFiveMoreYears = 
        TimeExpression.yearlyEveryOnStartsOnEndsOn(
            Years.years(1), 
            MonthDay.now(), 
            LocalDate.now().getYear(), 
            LocalDate.now().getYear() + 5, 
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    assertEquals(march27EveryOneYearFromThisYearToFiveMoreYears,
        YearlyTimeExpression.from(march27EveryOneYearFromThisYearToFiveMoreYears.asJson()));
  }
  
  @Test
  public void testYearlyAsString2() {
    LocalTime oneOClock = new LocalTime(1, 0, 0);
    LocalTime threeOClock = new LocalTime(3, 0, 0);
    LocalTime threeThirtyOClock = new LocalTime(3, 30, 0);
    LocalTime fiveOClock = new LocalTime(5, 0, 0);
    LocalTime sevenOClock = new LocalTime(7, 0, 0);
    LocalTime nineOClock = new LocalTime(9, 0, 0);

    YearlyTimeExpression march27EveryOneYearFromThisYearToFiveMoreYears =
        TimeExpression.yearlyEveryOnStartsOnEndsOn(
            Years.years(1),
            MonthDay.now(),
            LocalDate.now().getYear(),
            LocalDate.now().getYear() + 5,
            TimeSpan.fromTo(
                oneOClock,
                threeOClock),
            TimeSpan.fromTo(
                threeThirtyOClock,
                fiveOClock),
            TimeSpan.fromTo(
                sevenOClock,
                nineOClock));

    assertEquals(
        march27EveryOneYearFromThisYearToFiveMoreYears,
        YearlyTimeExpression.from(march27EveryOneYearFromThisYearToFiveMoreYears.asJson()));
  }
  
  @Test
  public void testYearlyAsString3() {
    YearlyTimeExpression march27EveryOneYearFromThisYearToFiveMoreYears =
        TimeExpression.yearlyEveryOnStartsOnEndsOn(
            Years.years(1),
            MonthDay.now(),
            LocalDate.now().getYear(),
            LocalDate.now().getYear() + 5,
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    JsonElement march27EveryOneYearFromThisYearToFiveMoreYearsAsString =
        march27EveryOneYearFromThisYearToFiveMoreYears.asJson();

    assertEquals(march27EveryOneYearFromThisYearToFiveMoreYears,
        YearlyTimeExpression.from(march27EveryOneYearFromThisYearToFiveMoreYearsAsString));
  }

  @Test
  public void testEveryEightOfAugust() {
    Integer eightDay = Integer.valueOf(8);
    Integer august = Integer.valueOf(8);
    TimeExpression everyEightOfAugustTimeExpression = PeriodicTimeExpression
        .yearlyEveryOnStartsOnEndsOn(Years.ONE,
            new MonthDay(august, eightDay), 2012, 2015,
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));
    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek;
    firstMondayOfWeek = LocalDate
        .fromCalendarFields(firstMondayOf2012AsCalendar);
    LocalDate firstEightOfAugust = firstMondayOfWeek.plusDays(6).plusMonths(7);
    assertTrue(everyEightOfAugustTimeExpression.evaluate(firstEightOfAugust));
    assertFalse(everyEightOfAugustTimeExpression.evaluate(firstEightOfAugust
        .plusMonths(1)));
    assertTrue(everyEightOfAugustTimeExpression.evaluate(firstEightOfAugust
        .plusYears(1)));
    assertTrue(everyEightOfAugustTimeExpression.evaluate(firstEightOfAugust
        .plusYears(2)));
    assertTrue(everyEightOfAugustTimeExpression.evaluate(firstEightOfAugust
        .plusYears(3)));
    assertFalse(everyEightOfAugustTimeExpression.evaluate(firstEightOfAugust
        .plusYears(4)));
  }
  
  @Test
  public void testEveryEightOfAugustEndingAfter() {
    Integer eightDay = Integer.valueOf(8);
    Integer august = Integer.valueOf(8);
    int fourOccurrences = 4;
    TimeExpression everyEightOfAugustTimeExpression = PeriodicTimeExpression
        .yearlyEveryOnStartsOnEndsAfter(Years.ONE, new MonthDay(august,
            eightDay), 2012, fourOccurrences, TimeSpan.fromTo(
            LocalTime.now(), LocalTime.now().plusHours(1)));
    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek;
    firstMondayOfWeek = LocalDate
        .fromCalendarFields(firstMondayOf2012AsCalendar);
    LocalDate firstEightOfAugust = firstMondayOfWeek.plusDays(6).plusMonths(7);
    assertTrue(everyEightOfAugustTimeExpression.evaluate(firstEightOfAugust));
    assertFalse(everyEightOfAugustTimeExpression.evaluate(firstEightOfAugust
        .plusMonths(1)));
    assertTrue(everyEightOfAugustTimeExpression.evaluate(firstEightOfAugust
        .plusYears(1)));
    assertTrue(everyEightOfAugustTimeExpression.evaluate(firstEightOfAugust
        .plusYears(2)));
    assertTrue(everyEightOfAugustTimeExpression.evaluate(firstEightOfAugust
        .plusYears(3)));
    assertFalse(everyEightOfAugustTimeExpression.evaluate(firstEightOfAugust
        .plusYears(4)));
  }
}
