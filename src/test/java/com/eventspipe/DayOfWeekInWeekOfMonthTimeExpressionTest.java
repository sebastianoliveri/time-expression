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

import com.eventspipe.DateTimeSpan;
import com.eventspipe.DayOfWeek;
import com.eventspipe.DayOfWeekInWeekOfMonthTimeExpression;
import com.eventspipe.MonthOfYear;
import com.eventspipe.PeriodicTimeExpression;
import com.eventspipe.TimeExpression;
import com.eventspipe.TimeExpressionException;
import com.eventspipe.TimeSpan;
import com.eventspipe.WeekOfMonth;
import com.google.gson.JsonElement;

public class DayOfWeekInWeekOfMonthTimeExpressionTest {

  @Test
  public void shouldFailWhenAmountOfMonthsIsInvalid() {
    try {
      TimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.ZERO, 
          DayOfWeek.MONDAY, 
          WeekOfMonth.FIRST, 
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
          DayOfWeek.MONDAY, 
          WeekOfMonth.FIRST, 
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
  public void testDayOfWeekInWeekOfMonth() {
    DayOfWeekInWeekOfMonthTimeExpression timeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.ONE, 
          DayOfWeek.THURSDAY,
          WeekOfMonth.SECOND, 
          MonthOfYear.on(1, 2012),
          MonthOfYear.on(2, 2012),
          TimeSpan.fromTo(
              new LocalTime(9, 0), 
              new LocalTime(10, 0)));
    
    Iterator<DateTimeSpan> iterator = timeExpression.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(new LocalDate(2012, 1, 12), 
        iterator.next().startingDateTime().toLocalDate());
    assertTrue(iterator.hasNext());
    assertEquals(new LocalDate(2012, 2, 9), 
        iterator.next().startingDateTime().toLocalDate());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testDayOfWeekInWeekOfMonthShouldBeReadjusted() {
    DayOfWeekInWeekOfMonthTimeExpression timeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.TWO, 
          DayOfWeek.THURSDAY,
          WeekOfMonth.SECOND, 
          MonthOfYear.on(1, 2012),
          MonthOfYear.on(4, 2012),
          TimeSpan.fromTo(
              new LocalTime(9, 0), 
              new LocalTime(10, 0)));
    
    assertEquals(new LocalDate(2012, 3, 8), 
        timeExpression.endingDateTime().toLocalDate());
    
    Iterator<DateTimeSpan> iterator = timeExpression.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(new LocalDate(2012, 1, 12), 
        iterator.next().startingDateTime().toLocalDate());
    assertTrue(iterator.hasNext());
    assertEquals(new LocalDate(2012, 3, 8), 
        iterator.next().startingDateTime().toLocalDate());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testDayOfWeekInWeekOfMonthWhenItDoesNotReccurrs() {
    try {
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.SIX, 
          DayOfWeek.THURSDAY,
          WeekOfMonth.SECOND, 
          MonthOfYear.on(1, 2012),
          MonthOfYear.on(5, 2012),
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
  public void testDayOfWeekInWeekOfMonthShouldBeReadjusted2() {
    DayOfWeekInWeekOfMonthTimeExpression timeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
          Months.SIX, 
          DayOfWeek.THURSDAY,
          WeekOfMonth.SECOND, 
          MonthOfYear.on(1, 2012),
          MonthOfYear.on(9, 2013),
          TimeSpan.fromTo(
              new LocalTime(9, 0), 
              new LocalTime(10, 0)));
    
    assertEquals(new LocalDate(2013, 7, 11), 
        timeExpression.endingDateTime().toLocalDate());
    
    Iterator<DateTimeSpan> iterator = timeExpression.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(new LocalDate(2012, 1, 12), 
        iterator.next().startingDateTime().toLocalDate());
    assertTrue(iterator.hasNext());
    assertEquals(new LocalDate(2012, 7, 12), 
        iterator.next().startingDateTime().toLocalDate());
    assertTrue(iterator.hasNext());
    assertEquals(new LocalDate(2013, 1, 10), 
        iterator.next().startingDateTime().toLocalDate());
    assertTrue(iterator.hasNext());
    assertEquals(new LocalDate(2013, 7, 11), 
        iterator.next().startingDateTime().toLocalDate());
    assertFalse(iterator.hasNext());
  }  
  
  @Test  
  public void testDayOfWeekInWeekOfMonth2() {
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);
    DayOfWeekInWeekOfMonthTimeExpression dayOfWeekInWeekOfMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.ONE, 
            DayOfWeek.THURSDAY,
            WeekOfMonth.SECOND, 
            MonthOfYear.on(1, 2012),
            MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(aStartingTime, anEndingTime));

    Iterator<DateTimeSpan> iterator = 
        dayOfWeekInWeekOfMonthTimeExpression.iterator();

    LocalDateTime expectedDate = new LocalDateTime(2012, 1, 12,
        aStartingTime.getHourOfDay(), aStartingTime.getMinuteOfHour(),
        aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 2, 9, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 3, 8, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 4, 12, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 5, 10, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    assertFalse(iterator.hasNext());
  }

  @Test
  public void testDayOfWeekInWeekOfMonthEveryTwoMonths() {
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);
    DayOfWeekInWeekOfMonthTimeExpression dayOfWeekInWeekOfMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.TWO, 
            DayOfWeek.THURSDAY,
            WeekOfMonth.SECOND, 
            MonthOfYear.on(1, 2012),
            MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(aStartingTime, anEndingTime));

    Iterator<DateTimeSpan> iterator = 
        dayOfWeekInWeekOfMonthTimeExpression.iterator();

    LocalDateTime expectedDate = new LocalDateTime(2012, 1, 12,
        aStartingTime.getHourOfDay(), aStartingTime.getMinuteOfHour(),
        aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 3, 8, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 5, 10, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    assertFalse(iterator.hasNext());
  }

  @Test
  public void testDayOfWeekInWeekOfMonthEveryThreeMonths() {
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);
    DayOfWeekInWeekOfMonthTimeExpression dayOfWeekInWeekOfMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.THREE, 
            DayOfWeek.THURSDAY,
            WeekOfMonth.SECOND, 
            MonthOfYear.on(1, 2012),
            MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(aStartingTime, anEndingTime));

    Iterator<DateTimeSpan> iterator = 
        dayOfWeekInWeekOfMonthTimeExpression.iterator();

    LocalDateTime expectedDate = new LocalDateTime(2012, 1, 12,
        aStartingTime.getHourOfDay(), aStartingTime.getMinuteOfHour(),
        aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 4, 12, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    assertFalse(iterator.hasNext());
  }

  @Test
  public void testDayOfWeekInWeekOfMonth3() {
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);
    DayOfWeekInWeekOfMonthTimeExpression dayOfWeekInWeekOfMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.ONE, 
            DayOfWeek.MONDAY,
            WeekOfMonth.LAST, 
            MonthOfYear.on(1, 2012), MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(aStartingTime, anEndingTime));

    Iterator<DateTimeSpan> iterator = 
        dayOfWeekInWeekOfMonthTimeExpression.iterator();

    LocalDateTime expectedDate = new LocalDateTime(2012, 1, 30,
        aStartingTime.getHourOfDay(), aStartingTime.getMinuteOfHour(),
        aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 2, 27, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 3, 26, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 4, 30, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 5, 28, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    assertFalse(iterator.hasNext());
  }

  @Test
  public void testDayOfWeekInWeekOfMonthEveryTwoMonths2() {
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);
    DayOfWeekInWeekOfMonthTimeExpression dayOfWeekInWeekOfMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.TWO, 
            DayOfWeek.MONDAY,
            WeekOfMonth.LAST, 
            MonthOfYear.on(1, 2012), MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(aStartingTime, anEndingTime));

    Iterator<DateTimeSpan> iterator = 
        dayOfWeekInWeekOfMonthTimeExpression.iterator();

    LocalDateTime expectedDate = new LocalDateTime(2012, 1, 30,
        aStartingTime.getHourOfDay(), aStartingTime.getMinuteOfHour(),
        aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 3, 26, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 5, 28, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    assertFalse(iterator.hasNext());
  }

  @Test
  public void testDayOfWeekInWeekOfMonthEveryThreeMonths2() {
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);
    DayOfWeekInWeekOfMonthTimeExpression dayOfWeekInWeekOfMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.THREE, 
            DayOfWeek.MONDAY,
            WeekOfMonth.LAST, 
            MonthOfYear.on(1, 2012), MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(aStartingTime, anEndingTime));

    Iterator<DateTimeSpan> iterator = 
        dayOfWeekInWeekOfMonthTimeExpression.iterator();

    LocalDateTime expectedDate = new LocalDateTime(2012, 1, 30,
        aStartingTime.getHourOfDay(), aStartingTime.getMinuteOfHour(),
        aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    expectedDate = new LocalDateTime(2012, 4, 30, aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().contains(expectedDate));

    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testDayOfWeekInWeekOfMonthAsString() {
    DayOfWeekInWeekOfMonthTimeExpression firstFridaysEveryTwoMonths = 
        TimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.TWO, 
            DayOfWeek.FRIDAY,
            WeekOfMonth.FIRST, 
            MonthOfYear.now(), 
            MonthOfYear.on(LocalDate.now().getMonthOfYear(), LocalDate.now().getYear() + 1),
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    assertEquals(firstFridaysEveryTwoMonths,
        DayOfWeekInWeekOfMonthTimeExpression.from(firstFridaysEveryTwoMonths.asJson()));
  }

  @Test
  public void testDayOfWeekInWeekOfMonthAsString2() {
    LocalTime oneOClock = new LocalTime(1, 0, 0);
    LocalTime threeOClock = new LocalTime(3, 0, 0);
    LocalTime threeThirtyOClock = new LocalTime(3, 30, 0);
    LocalTime fiveOClock = new LocalTime(5, 0, 0);
    LocalTime sevenOClock = new LocalTime(7, 0, 0);
    LocalTime nineOClock = new LocalTime(9, 0, 0);

    DayOfWeekInWeekOfMonthTimeExpression firstFridaysEveryTwoMonths = 
        TimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.TWO, 
            DayOfWeek.FRIDAY,
            WeekOfMonth.FIRST, 
            MonthOfYear.now(), 
            MonthOfYear.on(LocalDate.now().getMonthOfYear(), LocalDate.now().getYear() + 1),
            TimeSpan.fromTo(oneOClock, threeOClock), 
            TimeSpan.fromTo(threeThirtyOClock, fiveOClock), 
            TimeSpan.fromTo(sevenOClock, nineOClock));

    assertEquals(firstFridaysEveryTwoMonths,
        DayOfWeekInWeekOfMonthTimeExpression.from(firstFridaysEveryTwoMonths.asJson()));
  }
  
  @Test
  public void testDayOfWeekInWeekOfMonthAsString3() {
    DayOfWeekInWeekOfMonthTimeExpression firstFridaysEveryTwoMonths =
        TimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.TWO,
            DayOfWeek.FRIDAY,
            WeekOfMonth.FIRST,
            MonthOfYear.now(),
            MonthOfYear.on(
                LocalDate.now().getMonthOfYear(),
                LocalDate.now().getYear() + 1),
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    JsonElement firstFridaysEveryTwoMonthsAsString =
        firstFridaysEveryTwoMonths.asJson();

    assertEquals(firstFridaysEveryTwoMonths,
        DayOfWeekInWeekOfMonthTimeExpression
            .from(firstFridaysEveryTwoMonthsAsString));
  }
  
  @Test
  public void testFirstFridayEveryMonth() {
    TimeExpression firstFridayOfEveryMonthTimeExpression = PeriodicTimeExpression
        .monthlyEveryOnStartsOnEndsOn(Months.ONE, DayOfWeek.FRIDAY,
            WeekOfMonth.FIRST, MonthOfYear.on(1, 2012),
            MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek = LocalDate
        .fromCalendarFields(firstMondayOf2012AsCalendar);
    
    LocalDate firstFridayOfMonth = firstMondayOfWeek.plusDays(4);
    assertTrue(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth)); // first friday of january
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(1)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(2)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(3)));
    assertTrue(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(4))); // first friday of february
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(5)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(6)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(7)));
    assertTrue(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(8))); // first friday of march
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(9)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(10)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(11)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(12)));
    assertTrue(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(13))); // first friday of april
  }
  
  @Test
  public void testFirstFridayEveryMonthEndingAfter() {
    int fourOccurrences = 4;
    TimeExpression firstFridayOfEveryMonthTimeExpression = PeriodicTimeExpression
        .monthlyEveryOnStartsOnEndsAfter(Months.ONE, DayOfWeek.FRIDAY,
            WeekOfMonth.FIRST, MonthOfYear.on(1, 2012), fourOccurrences,
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek = LocalDate
        .fromCalendarFields(firstMondayOf2012AsCalendar);    
    
    LocalDate firstFridayOfMonth = firstMondayOfWeek.plusDays(4);
    assertTrue(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth)); // first friday of january
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(1)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(2)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(3)));
    assertTrue(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(4))); // first friday of february
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(5)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(6)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(7)));
    assertTrue(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(8))); // first friday of march
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(9)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(10)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(11)));
    assertFalse(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(12)));
    assertTrue(firstFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(13))); // first friday of april
  }
  
  @Test
  public void testFourthFridayEveryMonth() {
    TimeExpression fourthFridayOfEveryMonthTimeExpression = PeriodicTimeExpression
        .monthlyEveryOnStartsOnEndsOn(Months.ONE, DayOfWeek.FRIDAY,
            WeekOfMonth.FOURTH, MonthOfYear.on(1, 2012),
            MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek = LocalDate
        .fromCalendarFields(firstMondayOf2012AsCalendar);        
    
    LocalDate firstFridayOfMonth = firstMondayOfWeek.plusDays(4);
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(1)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(2)));
    assertTrue(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(3))); // fourth friday of january
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(4)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(5)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(6)));
    assertTrue(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(7))); // fourth friday of
                                                     // february
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(8)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(9)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(10)));
    assertTrue(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(11))); // fourth friday of april
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(12)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(13)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(14)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(15)));
    assertTrue(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(16))); // fourth friday of april
  }
  
  @Test
  public void testLastFridayEveryMonth() {
    TimeExpression fourthFridayOfEveryMonthTimeExpression = PeriodicTimeExpression
        .monthlyEveryOnStartsOnEndsOn(Months.ONE, DayOfWeek.FRIDAY,
            WeekOfMonth.LAST, MonthOfYear.on(1, 2012), MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek = LocalDate
        .fromCalendarFields(firstMondayOf2012AsCalendar);
    
    LocalDate firstFridayOfMonth = firstMondayOfWeek.plusDays(4);
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(1)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(2)));
    assertTrue(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(3))); // last friday of january
                                                     // is fourth
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(4)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(5)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(6)));
    assertTrue(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(7))); // last friday of february
                                                     // is fourth
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(8)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(9)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(10)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(11)));
    assertTrue(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(12))); // last friday of april is
                                                      // fifth
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(13)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(14)));
    assertFalse(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(15)));
    assertTrue(fourthFridayOfEveryMonthTimeExpression
        .evaluate(firstFridayOfMonth.plusWeeks(16))); // fourth friday of april
  }
  
  @Test
  public void testFirstSaturdayEveryMonth() {
    TimeExpression firstSaturdayOfEveryMonthTimeExpression = PeriodicTimeExpression
        .monthlyEveryOnStartsOnEndsOn(Months.ONE, DayOfWeek.SATURDAY,
            WeekOfMonth.FIRST, MonthOfYear.on(1, 2012),
            MonthOfYear.on(5, 2012),
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek = LocalDate
        .fromCalendarFields(firstMondayOf2012AsCalendar);
    
    LocalDate firstSaturdayOfMonth = firstMondayOfWeek.plusDays(5);
    assertTrue(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth)); // first saturday of january
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(1)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(2)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(3)));
    assertTrue(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(4))); // first saturday of
                                                       // february
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(5)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(6)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(7)));
    assertTrue(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(8))); // first saturday of
                                                       // march
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(9)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(10)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(11)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(12)));
    assertTrue(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(13))); // first saturday of
                                                        // april
  }
  
  @Test
  public void testFirstSaturdayEveryTwoMonth() {
    TimeExpression firstSaturdayOfEveryMonthTimeExpression = 
        PeriodicTimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.TWO, 
            DayOfWeek.SATURDAY,
            WeekOfMonth.FIRST, 
            MonthOfYear.on(1, 2012),
            MonthOfYear.on(6, 2012),
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek = 
        LocalDate.fromCalendarFields(firstMondayOf2012AsCalendar);
    
    LocalDate firstSaturdayOfMonth = firstMondayOfWeek.plusDays(5);
    assertTrue(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth)); // first saturday of january
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(1)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(2)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(3)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(4))); // first saturday of
                                                       // february
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(5)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(6)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(7)));
    assertTrue(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(8))); // first saturday of
                                                       // march
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(9)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(10)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(11)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(12)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(13))); // first saturday of
                                                        // april
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(14)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(15)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(16)));
    assertTrue(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(17))); // first saturday of may
  }
  
  @Test
  public void testFirstSaturdayEveryThreeMonth() {
    TimeExpression firstSaturdayOfEveryMonthTimeExpression = PeriodicTimeExpression
        .monthlyEveryOnStartsOnEndsOn(Months.THREE, DayOfWeek.SATURDAY,
            WeekOfMonth.FIRST, MonthOfYear.on(1, 2012),
            MonthOfYear.on(7, 2012),
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    GregorianCalendar firstMondayOf2012AsCalendar = (GregorianCalendar) GregorianCalendar
        .getInstance();
    firstMondayOf2012AsCalendar.set(2012, 0 /* january */, 2);
    LocalDate firstMondayOfWeek = LocalDate
        .fromCalendarFields(firstMondayOf2012AsCalendar);
    
    LocalDate firstSaturdayOfMonth = firstMondayOfWeek.plusDays(5);
    assertTrue(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth)); // first saturday of january
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(1)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(2)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(3)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(4))); // first saturday of
                                                       // february
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(5)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(6)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(7)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(8))); // first saturday of
                                                       // march
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(9)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(10)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(11)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(12)));
    assertTrue(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(13))); // first saturday of
                                                        // april
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(14)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(15)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(16)));
    assertFalse(firstSaturdayOfEveryMonthTimeExpression
        .evaluate(firstSaturdayOfMonth.plusWeeks(17))); // first saturday of may
  }
  
  @Test
  public void testIteratorFromTo() {
    DayOfWeekInWeekOfMonthTimeExpression firstFridayOfFirstWeekEveryOneMonth = 
        DayOfWeekInWeekOfMonthTimeExpression.monthlyEveryOnStartsOnEndsOn(
            Months.ONE, 
            DayOfWeek.FRIDAY, 
            WeekOfMonth.FIRST, 
            MonthOfYear.on(1, 2015), 
            MonthOfYear.on(12, 2015), 
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    Iterator<DateTimeSpan> iterator = 
        firstFridayOfFirstWeekEveryOneMonth.iteratorFromTo(
            new LocalDate(2015, 1, 1), new LocalDate(2015, 5, 31));

    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2015, 1, 2), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2015, 2, 6), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2015, 3, 6), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2015, 4, 3), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2015, 5, 1), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertFalse(iterator.hasNext());
    
    iterator = 
        firstFridayOfFirstWeekEveryOneMonth.iteratorFromTo(
            new LocalDate(2015, 5, 15), new LocalDate(2015, 10, 3));
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(new LocalDate(2015, 6, 5), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertEquals(new LocalDate(2015, 7, 3), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertEquals(new LocalDate(2015, 8, 7), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertEquals(new LocalDate(2015, 9, 4), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertEquals(new LocalDate(2015, 10, 2), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertFalse(iterator.hasNext());
    
    iterator = 
        firstFridayOfFirstWeekEveryOneMonth.iteratorFromTo(
            new LocalDate(2015, 10, 3), new LocalDate(2016, 2, 24));
    
    Assert.assertEquals(new LocalDate(2015, 11, 6), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertEquals(new LocalDate(2015, 12, 4), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertFalse(iterator.hasNext());
  }
}
