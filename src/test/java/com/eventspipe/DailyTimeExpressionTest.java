package com.eventspipe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonElement;

public class DailyTimeExpressionTest {
  
  @Test
  public void testAmountOfDaysShouldBeGreaterThanOne() {
    try {
      TimeExpression.dailyEveryStartsOnEndsOn(
          Days.days(0), 
          LocalDate.now(), 
          LocalDate.now().plusDays(1), 
          TimeSpan.fromTo(
              LocalTime.now(), 
              LocalTime.now().plusHours(1)));
      fail();
    } catch (TimeExpressionException exception) {
      assertEquals(
          TimeExpressionException.EVERY_AMOUNT_INVALID.getMessage(), 
          exception.getMessage());
    }

    try {
      TimeExpression.dailyEveryStartsOnEndsOn(
          Days.days(-1), 
          LocalDate.now(), 
          LocalDate.now().plusDays(1), 
          TimeSpan.fromTo(
              LocalTime.now(), 
              LocalTime.now().plusHours(1)));
      fail();
    } catch (TimeExpressionException exception) {
      assertEquals(
          TimeExpressionException.EVERY_AMOUNT_INVALID.getMessage(), 
          exception.getMessage());
    }
  }
  
  @Test
  public void shouldFailWhenStartingDateIsGreaterToOrEqualThanEndingDate() {
    try {
      TimeExpression.dailyEveryStartsOnEndsOn(
          Days.days(1), 
          LocalDate.now(), 
          LocalDate.now(), 
          TimeSpan.fromTo(
              LocalTime.now(), 
              LocalTime.now().plusHours(1)));
      fail();
    } catch (TimeExpressionException exception) {
      assertEquals(
          TimeExpressionException.DATE_SPAN_INVALID.getMessage(), 
          exception.getMessage());
    }
    
    try {
      TimeExpression.dailyEveryStartsOnEndsOn(
          Days.days(2), 
          LocalDate.now().plusDays(1), 
          LocalDate.now(), 
          TimeSpan.fromTo(
              LocalTime.now(), 
              LocalTime.now().plusHours(1)));
      fail();
    } catch (TimeExpressionException exception) {
      assertEquals(
          TimeExpressionException.DATE_SPAN_INVALID.getMessage(), 
          exception.getMessage());
    }
  }
  
  @Test
  public void testEveryDaysFromTodayUntilTomorrow() {
    LocalDate today = LocalDate.now();
    LocalDate tomorrow = today.plusDays(1);
    TimeExpression fromTodayUntilTomorrowEveryDays = 
        TimeExpression.dailyEveryStartsOnEndsOn(
            Days.ONE, 
            today, 
            tomorrow, 
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    Iterator<DateTimeSpan> iterator = fromTodayUntilTomorrowEveryDays.iterator();
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(today, iterator.next().startingDateTime().toLocalDate());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(tomorrow, iterator.next().startingDateTime().toLocalDate());
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testEveryTwoDaysFromTodayUntilTomorrow() {
    LocalDate today = LocalDate.now();
    LocalDate tomorrow = today.plusDays(1);
    try {
      TimeExpression.dailyEveryStartsOnEndsOn(
          Days.TWO, 
          today, 
          tomorrow, 
          TimeSpan.fromTo(
              LocalTime.now(), 
              LocalTime.now().plusHours(1)));
      Assert.fail();
    } catch (RuntimeException exception) {
      assertEquals(
          TimeExpressionException.ENDING_DATE_INVALID.getMessage(), 
          exception.getMessage());
    }
  }

  @Test
  public void testDailyEveryDays() {
    Days oneDay = Days.ONE;
    LocalDate today = LocalDate.now();
    LocalDate todayPlusSixDays = today.plusDays(6);

    TimeExpression dailyEveryDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            oneDay, 
            today, 
            todayPlusSixDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertTrue(dailyEveryDaysTimeExpression.evaluate(today));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(1)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(2)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(3)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(4)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryDaysTimeExpression.evaluate(today.plusDays(7)));
  }

  @Test
  public void testDailyEveryDaysEndingAfter() {
    Days oneDay = Days.ONE;
    LocalDate today = LocalDate.now();
    int sevenOcurrences = 7;

    TimeExpression dailyEveryDaysTimeExpression = 
        TimeExpression.dailyEveryStartsOnEndsAfter(
            oneDay, 
            today, 
            sevenOcurrences,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertTrue(dailyEveryDaysTimeExpression.evaluate(today));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(1)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(2)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(3)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(4)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryDaysTimeExpression.evaluate(today.plusDays(7)));
  }
  
  @Test
  public void testDailyEveryDaysEndsNever() {
    Days oneDay = Days.ONE;
    LocalDate today = LocalDate.now();

    PeriodicTimeExpression dailyEveryDaysTimeExpression = 
        TimeExpression.dailyEveryStartsOnEndsNever(
            oneDay, 
            today, 
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryDaysTimeExpression.numberOfOcurringDays() == Integer.MAX_VALUE);

    assertTrue(dailyEveryDaysTimeExpression.evaluate(today));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(1)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(2)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(3)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(4)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(6)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(7)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(8)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(9)));
    assertTrue(dailyEveryDaysTimeExpression.evaluate(today.plusDays(1002)));
  }

  @Test
  public void testDailyEveryTwoDays() {
    Days twoDays = Days.TWO;
    LocalDate today = LocalDate.now();
    LocalDate todayPlusSevenDays = today.plusDays(7);

    PeriodicTimeExpression dailyEveryTwoDaysTimeExpression = 
        PeriodicTimeExpression.dailyEveryStartsOnEndsOn(
            twoDays, 
            today, 
            todayPlusSevenDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertTrue(dailyEveryTwoDaysTimeExpression.numberOfOcurringDays() == 4);

    assertTrue(dailyEveryTwoDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(1)));
    assertTrue(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(2)));
    assertFalse(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(3)));
    assertTrue(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(8)));
  }

  @Test
  public void testDailyEveryTwoDaysEndingAfter() {
    Days twoDays = Days.TWO;
    LocalDate today = LocalDate.now();
    int fourOcurrences = 4;

    PeriodicTimeExpression dailyEveryTwoDaysTimeExpression = 
        PeriodicTimeExpression.dailyEveryStartsOnEndsAfter(
            twoDays, 
            today, 
            fourOcurrences,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryTwoDaysTimeExpression.numberOfOcurringDays() == 4);

    assertTrue(dailyEveryTwoDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(1)));
    assertTrue(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(2)));
    assertFalse(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(3)));
    assertTrue(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEveryTwoDaysTimeExpression.evaluate(today.plusDays(8)));
  }

  @Test
  public void testDailyEveryThreeDays() {
    Days threeDays = Days.THREE;
    LocalDate today = LocalDate.now();
    LocalDate todayPlusNineDays = today.plusDays(9);

    PeriodicTimeExpression dailyEveryThreeDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            threeDays, 
            today, 
            todayPlusNineDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
            LocalTime.now().plusHours(1)), 
            TimeSpan.fromTo(
                LocalTime.now().plusHours(1), 
                LocalTime.now().plusHours(2)));
    
    assertTrue(dailyEveryThreeDaysTimeExpression.numberOfOcurringDays() == 4);
    assertTrue(dailyEveryThreeDaysTimeExpression.numberOfOccurrences() == 8);

    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(2)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(8)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(11)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(12)));
  }

  @Test
  public void testDailyEveryThreeDays5() {
    Days threeDays = Days.THREE;
    LocalDate today = LocalDate.now();
    LocalDate todayPlusNineDays = today.plusDays(10);

    PeriodicTimeExpression dailyEveryThreeDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            threeDays, 
            today, 
            todayPlusNineDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryThreeDaysTimeExpression.numberOfOcurringDays() == 4);

    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(2)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(8)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(11)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(12)));
  }
  
  @Test
  public void testDailyEveryThreeDays6() {
    Days threeDays = Days.THREE;
    LocalDate today = LocalDate.now();
    LocalDate todayPlusNineDays = today.plusDays(11);

    PeriodicTimeExpression dailyEveryThreeDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            threeDays, 
            today, 
            todayPlusNineDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryThreeDaysTimeExpression.numberOfOcurringDays() == 4);

    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(2)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(8)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(11)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(12)));
  }  
  
  @Test
  public void testDailyEveryThreeDays7() {
    Days threeDays = Days.THREE;
    LocalDate today = LocalDate.now();
    LocalDate todayPlusNineDays = today.plusDays(12);

    PeriodicTimeExpression dailyEveryThreeDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            threeDays, 
            today, 
            todayPlusNineDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryThreeDaysTimeExpression.numberOfOcurringDays() == 5);

    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(2)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(8)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(11)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(12)));
  }    
  
  @Test
  public void testDailyEveryThreeDays2() {
    Days threeDays = Days.THREE;
    LocalDate today = LocalDate.now();
    LocalDate todayPlusNineDays = today.plusDays(8);

    PeriodicTimeExpression dailyEveryThreeDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            threeDays, 
            today, 
            todayPlusNineDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryThreeDaysTimeExpression.numberOfOcurringDays() == 3);

    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(2)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(8)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(11)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(12)));
  }  
  
  @Test
  public void testDailyEveryThreeDays3() {
    Days threeDays = Days.THREE;
    LocalDate today = LocalDate.now();
    LocalDate todayPlusNineDays = today.plusDays(7);

    PeriodicTimeExpression dailyEveryThreeDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            threeDays, 
            today, 
            todayPlusNineDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryThreeDaysTimeExpression.numberOfOcurringDays() == 3);

    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(2)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(8)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(11)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(12)));
  }
  
  @Test
  public void testDailyEveryThreeDays4() {
    Days threeDays = Days.THREE;
    LocalDate today = LocalDate.now();
    LocalDate todayPlusNineDays = today.plusDays(6);

    PeriodicTimeExpression dailyEveryThreeDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            threeDays, 
            today, 
            todayPlusNineDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryThreeDaysTimeExpression.numberOfOcurringDays() == 3);

    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(2)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(8)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(11)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(12)));
  }  

  @Test
  public void testDailyEveryThreeDaysEndingAfter() {
    Days threeDays = Days.THREE;
    LocalDate today = LocalDate.now();
    int fourOcurrences = 4;

    PeriodicTimeExpression dailyEveryThreeDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsAfter(
            threeDays, 
            today, 
            fourOcurrences,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryThreeDaysTimeExpression.numberOfOcurringDays() == 4);

    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(2)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(8)));
    assertTrue(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(11)));
    assertFalse(dailyEveryThreeDaysTimeExpression.evaluate(today.plusDays(12)));
  }

  @Test
  public void testDailyEveryFourDays() {
    Days fourDays = Days.FOUR;
    LocalDate today = LocalDate.now();
    LocalDate todayPlusTwelveDays = today.plusDays(12);

    PeriodicTimeExpression dailyEveryFourDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            fourDays, 
            today, 
            todayPlusTwelveDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryFourDaysTimeExpression.numberOfOcurringDays() == 4);

    assertTrue(dailyEveryFourDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(2)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(3)));
    assertTrue(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(5)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(7)));
    assertTrue(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(8)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(11)));
    assertTrue(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(12)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(13)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(14)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(15)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(16)));
  }

  @Test
  public void testDailyEveryFourDaysEndingAfter() {
    Days fourDays = Days.FOUR;
    LocalDate today = LocalDate.now();
    int fourOccurrences = 4;

    PeriodicTimeExpression dailyEveryFourDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsAfter(
            fourDays, 
            today, 
            fourOccurrences,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryFourDaysTimeExpression.numberOfOcurringDays() == 4);

    assertTrue(dailyEveryFourDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(2)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(3)));
    assertTrue(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(5)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(7)));
    assertTrue(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(8)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(11)));
    assertTrue(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(12)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(13)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(14)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(15)));
    assertFalse(dailyEveryFourDaysTimeExpression.evaluate(today.plusDays(16)));
  }

  @Test
  public void testDailyEveryFiveDays() {
    Days fiveDays = Days.FIVE;
    LocalDate today = LocalDate.now();
    LocalDate todaysPlusFifteenDays = today.plusDays(15);

    PeriodicTimeExpression dailyEveryFiveDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            fiveDays, 
            today, 
            todaysPlusFifteenDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryFiveDaysTimeExpression.numberOfOcurringDays() == 4);

    assertTrue(dailyEveryFiveDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(2)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(4)));
    assertTrue(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(5)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(8)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(9)));
    assertTrue(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(11)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(12)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(13)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(14)));
    assertTrue(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(15)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(16)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(17)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(18)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(19)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(20)));
  }

  @Test
  public void testDailyEveryFiveDaysEndingAfter() {
    Days fiveDays = Days.FIVE;
    LocalDate today = LocalDate.now();
    int fourOccurrences = 4;

    PeriodicTimeExpression dailyEveryFiveDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsAfter(
            fiveDays, 
            today, 
            fourOccurrences,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEveryFiveDaysTimeExpression.numberOfOcurringDays() == 4);

    assertTrue(dailyEveryFiveDaysTimeExpression.evaluate(today));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(2)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(4)));
    assertTrue(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(5)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(8)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(9)));
    assertTrue(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(11)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(12)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(13)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(14)));
    assertTrue(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(15)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(16)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(17)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(18)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(19)));
    assertFalse(dailyEveryFiveDaysTimeExpression.evaluate(today.plusDays(20)));
  }

  @Test
  public void testDailyEverySixDaysEndingAfter() {
    Days sixDays = Days.SIX;
    LocalDate today = LocalDate.now();
    int fourOccurrences = 4;

    PeriodicTimeExpression dailyEverySixDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsAfter(
            sixDays, 
            today, 
            fourOccurrences,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));
    
    assertTrue(dailyEverySixDaysTimeExpression.numberOfOcurringDays() == 4);

    assertTrue(dailyEverySixDaysTimeExpression.evaluate(today));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(2)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(8)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(11)));
    assertTrue(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(12)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(13)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(14)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(15)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(16)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(17)));
    assertTrue(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(18)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(19)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(20)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(21)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(22)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(23)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(24)));
  }

  @Test
  public void testDailyEverySixDays() {
    Days sixDays = Days.SIX;
    LocalDate today = LocalDate.now();
    LocalDate todayPlusTwentyThreeDays = today.plusDays(23);

    TimeExpression dailyEverySixDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            sixDays, 
            today, 
            todayPlusTwentyThreeDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertTrue(dailyEverySixDaysTimeExpression.evaluate(today));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(2)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(5)));
    assertTrue(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(6)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(8)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(11)));
    assertTrue(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(12)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(13)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(14)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(15)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(16)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(17)));
    assertTrue(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(18)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(19)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(20)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(21)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(22)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(23)));
    assertFalse(dailyEverySixDaysTimeExpression.evaluate(today.plusDays(24)));
  }

  @Test
  public void testDailyEverySevenDays() {
    Days sevenDays = Days.SEVEN;
    LocalDate today = LocalDate.now();
    LocalDate todayPlusTwentySevenDays = today.plusDays(27);

    TimeExpression dailyEverySevenDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            sevenDays, 
            today, 
            todayPlusTwentySevenDays,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertTrue(dailyEverySevenDaysTimeExpression.evaluate(today));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(2)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(5)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(6)));
    assertTrue(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(8)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(11)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(12)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(13)));
    assertTrue(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(14)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(15)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(16)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(17)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(18)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(19)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(20)));
    assertTrue(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(21)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(22)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(23)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(24)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(25)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(26)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(27)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(28)));
  }

  @Test
  public void testDailyEverySevenDaysEndingAfter() {
    Days sevenDays = Days.SEVEN;
    LocalDate today = LocalDate.now();
    int fourOcurrences = 4;

    TimeExpression dailyEverySevenDaysTimeExpression = 
        DailyTimeExpression.dailyEveryStartsOnEndsAfter(
            sevenDays, 
            today, 
            fourOcurrences,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertTrue(dailyEverySevenDaysTimeExpression.evaluate(today));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(1)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(2)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(3)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(4)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(5)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(6)));
    assertTrue(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(7)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(8)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(9)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(10)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(11)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(12)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(13)));
    assertTrue(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(14)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(15)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(16)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(17)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(18)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(19)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(20)));
    assertTrue(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(21)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(22)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(23)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(24)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(25)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(26)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(27)));
    assertFalse(dailyEverySevenDaysTimeExpression.evaluate(today.plusDays(28)));
  }

  @Test
  public void testDailyIteratorEveryOneDay() {
    LocalDate aStartingDate = LocalDate.now();
    LocalDate anEndingDate = aStartingDate.plusDays(14);
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);

    DailyTimeExpression aDailyTimeExpression = 
        PeriodicTimeExpression.dailyEveryStartsOnEndsOn(
            Days.ONE, 
            aStartingDate, 
            anEndingDate,
            TimeSpan.fromTo(
                aStartingTime, 
                anEndingTime));

    Iterator<DateTimeSpan> iterator = aDailyTimeExpression.iterator();

    DateTimeSpan nextDateTimeSpan = null;
    LocalDateTime expectedLocalDateTime = 
        new LocalDateTime(
          aStartingDate.getYear(), aStartingDate.getMonthOfYear(), aStartingDate.getDayOfMonth(), 
          aStartingTime.getHourOfDay(), aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    while (iterator.hasNext()) {
      nextDateTimeSpan = iterator.next();
      assertTrue(nextDateTimeSpan.contains(expectedLocalDateTime));
      expectedLocalDateTime = expectedLocalDateTime.plusDays(1);
    }
  }

  @Test
  public void testDailyIteratorEveryOneDayInDateTime() {
    LocalDate aStartingDate = LocalDate.now();
    LocalDate anEndingDate = aStartingDate.plusDays(14);
    LocalTime aFirstStartingTime = LocalTime.now().minusHours(8);
    LocalTime aFirstEndingTime = aFirstStartingTime.plusHours(1);

    LocalTime aSecondStartingTime = aFirstEndingTime.plusMinutes(1);
    LocalTime anSecondEndingTime = aSecondStartingTime.plusHours(1);

    DailyTimeExpression aDailyTimeExpression = 
        PeriodicTimeExpression.dailyEveryStartsOnEndsOn(
            Days.ONE, 
            aStartingDate, 
            anEndingDate,
            TimeSpan.fromTo(
                aFirstStartingTime, aFirstEndingTime),
            TimeSpan.fromTo(
                aSecondStartingTime, anSecondEndingTime));

    Iterator<DateTimeSpan> iterator = aDailyTimeExpression.iterator();

    DateTimeSpan expectedFirstDateTimeSpan = 
        DateTimeSpan.fromTo(
          new LocalDateTime(aStartingDate.getYear(), aStartingDate
              .getMonthOfYear(), aStartingDate.getDayOfMonth(),
              aFirstStartingTime.getHourOfDay(), aFirstStartingTime
                  .getMinuteOfHour(), aFirstStartingTime.getSecondOfMinute()),
          new LocalDateTime(aStartingDate.getYear(), aStartingDate
              .getMonthOfYear(), aStartingDate.getDayOfMonth(), aFirstEndingTime
              .getHourOfDay(), aFirstEndingTime.getMinuteOfHour(),
              aFirstEndingTime.getSecondOfMinute()));
    
    DateTimeSpan expectedSecondDateTimeSpan = 
        DateTimeSpan.fromTo(
          new LocalDateTime(aStartingDate.getYear(), aStartingDate
              .getMonthOfYear(), aStartingDate.getDayOfMonth(),
              aSecondStartingTime.getHourOfDay(), aSecondStartingTime
                  .getMinuteOfHour(), aSecondStartingTime.getSecondOfMinute()),
          new LocalDateTime(aStartingDate.getYear(), aStartingDate
              .getMonthOfYear(), aStartingDate.getDayOfMonth(),
              anSecondEndingTime.getHourOfDay(), anSecondEndingTime
                  .getMinuteOfHour(), anSecondEndingTime.getSecondOfMinute()));

    DateTimeSpan nextDateTimeSpan = null;
    while (iterator.hasNext()) {
      nextDateTimeSpan = iterator.next();
      assertEquals(expectedFirstDateTimeSpan, nextDateTimeSpan);
      expectedFirstDateTimeSpan = 
          expectedFirstDateTimeSpan.moveForward(Days.ONE);

      nextDateTimeSpan = iterator.next();
      assertEquals(expectedSecondDateTimeSpan, nextDateTimeSpan);
      expectedSecondDateTimeSpan = 
          expectedSecondDateTimeSpan.moveForward(Days.ONE);
    }
  }

  @Test
  public void testDailyIteratorEveryThreeDays() {
    LocalDate aStartingDate = LocalDate.now();
    LocalDate anEndingDate = aStartingDate.plusDays(30);
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);

    DailyTimeExpression aDailyTimeExpression = 
        PeriodicTimeExpression.dailyEveryStartsOnEndsOn(
            Days.THREE, 
            aStartingDate, 
            anEndingDate,
            TimeSpan.fromTo(
                aStartingTime, 
                anEndingTime));

    Iterator<DateTimeSpan> iterator = aDailyTimeExpression.iterator();

    DateTimeSpan nextDateTimeSpan = null;
    LocalDateTime expectedLocalDateTime = new LocalDateTime(
        aStartingDate.getYear(), aStartingDate.getMonthOfYear(),
        aStartingDate.getDayOfMonth(), aStartingTime.getHourOfDay(),
        aStartingTime.getMinuteOfHour(), aStartingTime.getSecondOfMinute());
    while (iterator.hasNext()) {
      nextDateTimeSpan = iterator.next();
      assertTrue(nextDateTimeSpan.contains(expectedLocalDateTime));
      expectedLocalDateTime = expectedLocalDateTime.plusDays(3);
    }
  }
  
  @Test
  public void testDailyTimeExpressionEndingDateIsReadjust() {
    LocalDate januaryFirst2014 = new LocalDate(2014, 1, 1);
    LocalDate januaryTwelve2014 = new LocalDate(2014, 1, 12);
    DailyTimeExpression everyThreeDaysFromJanuaryFirst2014ToJanuaryTwelve2014 = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            Days.THREE, 
            januaryFirst2014,
            januaryTwelve2014,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    LocalDate januaryTen2014 = new LocalDate(2014, 1, 10);
    assertEquals(
        januaryTen2014,
        everyThreeDaysFromJanuaryFirst2014ToJanuaryTwelve2014.dateSpan().endingDate());
  }
  
  @Test
  public void testDailyTimeExpressionEndingDateIsReadjust2() {
    LocalDate januaryFirst2014 = new LocalDate(2014, 1, 1);
    LocalDate januaryThirteen2014 = new LocalDate(2014, 1, 13);
    DailyTimeExpression everyThreeDaysFromJanuaryFirst2014ToJanuaryEleven2014 = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            Days.THREE, 
            januaryFirst2014,
            januaryThirteen2014,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertEquals(januaryThirteen2014,
        everyThreeDaysFromJanuaryFirst2014ToJanuaryEleven2014.dateSpan().endingDate());
  }
  
  @Test
  public void testDailyTimeExpressionEndingDateIsReadjust3() {
    LocalDate januaryFirst2014 = new LocalDate(2014, 1, 1);
    LocalDate januaryTwentyFour2014 = new LocalDate(2014, 1, 24);
   
    DailyTimeExpression everyFiveDaysFromJanuaryFirst2014ToJanuaryTwentyFour2014 = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            Days.FIVE, 
            januaryFirst2014,
            januaryTwentyFour2014,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    LocalDate januaryTwentyOne2014 = new LocalDate(2014, 1, 21);
    assertEquals(januaryTwentyOne2014,
        everyFiveDaysFromJanuaryFirst2014ToJanuaryTwentyFour2014.dateSpan().endingDate());
  }
  
  @Test
  public void testDailyTimeExpressionEndingDateIsReadjust4() {
    LocalDate januaryFirst2014 = new LocalDate(2014, 1, 1);
    LocalDate januaryTwentySix2014 = new LocalDate(2014, 1, 26);
    
    DailyTimeExpression everyFiveDaysFromJanuaryFirst2014ToJanuaryTwentySix2014 = 
        DailyTimeExpression.dailyEveryStartsOnEndsOn(
            Days.FIVE, 
            januaryFirst2014,
            januaryTwentySix2014,
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertEquals(januaryTwentySix2014,
        everyFiveDaysFromJanuaryFirst2014ToJanuaryTwentySix2014.dateSpan().endingDate());
  }
  
  @Test
  public void testDailyAsString() {
    DailyTimeExpression everyTwoDaysFromTodayUpToOneMonth = 
        TimeExpression.dailyEveryStartsOnEndsOn(
            Days.TWO, 
            LocalDate.now(), 
            LocalDate.now().plusMonths(1), 
            TimeSpan.fromTo(
                LocalTime.now(), 
                LocalTime.now().plusHours(1)));

    assertEquals(
        everyTwoDaysFromTodayUpToOneMonth,
        DailyTimeExpression.from(everyTwoDaysFromTodayUpToOneMonth.asJson()));
  }
  
  @Test
  public void testDailyAsString2() {
    LocalTime oneOClock = new LocalTime(1, 0, 0);
    LocalTime threeOClock = new LocalTime(3, 0, 0);
    LocalTime threeThirtyOClock = new LocalTime(3, 30, 0);
    LocalTime fiveOClock = new LocalTime(5, 0, 0);

    DailyTimeExpression everyTwoDaysFromTodayUpToOneMonth = 
        TimeExpression.dailyEveryStartsOnEndsOn(
            Days.TWO, 
            LocalDate.now(), 
            LocalDate.now().plusMonths(1), 
            TimeSpan.fromTo(oneOClock, threeOClock),
            TimeSpan.fromTo(threeThirtyOClock, fiveOClock));

    assertEquals(
        everyTwoDaysFromTodayUpToOneMonth,
        DailyTimeExpression.from(everyTwoDaysFromTodayUpToOneMonth.asJson()));
  }
  
  @Test
  public void testDailyAsString3() {
    LocalTime oneOClock = new LocalTime(1, 0, 0);
    LocalTime threeOClock = new LocalTime(3, 0, 0);
    LocalTime threeThirtyOClock = new LocalTime(3, 30, 0);
    LocalTime fiveOClock = new LocalTime(5, 0, 0);
    LocalTime sevenOClock = new LocalTime(7, 0, 0);
    LocalTime nineOClock = new LocalTime(9, 0, 0);

    DailyTimeExpression everyTwoDaysFromTodayUpToOneMonth =
        TimeExpression.dailyEveryStartsOnEndsOn(
            Days.TWO,
            LocalDate.now(),
            LocalDate.now().plusMonths(1),
            TimeSpan.fromTo(oneOClock, threeOClock),
            TimeSpan.fromTo(threeThirtyOClock, fiveOClock),
            TimeSpan.fromTo(sevenOClock, nineOClock));

    assertEquals(everyTwoDaysFromTodayUpToOneMonth,
        DailyTimeExpression.from(everyTwoDaysFromTodayUpToOneMonth.asJson()));
  }
  
  @Test
  public void testDailyAsString4() {
    DailyTimeExpression everyTwoDaysFromTodayUpToOneMonth =
        TimeExpression.dailyEveryStartsOnEndsOn(
            Days.TWO,
            LocalDate.now(),
            LocalDate.now().plusMonths(1),
            TimeSpan.fromTo(
                LocalTime.now(),
                LocalTime.now().plusHours(1)));

    JsonElement everyTwoDaysFromTodayUpToOneMonthAsString =
        everyTwoDaysFromTodayUpToOneMonth.asJson();

    assertEquals(everyTwoDaysFromTodayUpToOneMonth,
        DailyTimeExpression.from(everyTwoDaysFromTodayUpToOneMonthAsString));
  }
  
  @Test
  public void testIteratorFromTo() {
    PeriodicTimeExpression everyDayFromTodayUpTo30Days = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        LocalDate.now(), 
        LocalDate.now().plusMonths(1), 
        TimeSpan.fromTo(
            LocalTime.now(),
            LocalTime.now().plusHours(1)));
    
    Iterator<DateTimeSpan> iterator = 
        everyDayFromTodayUpTo30Days.iteratorFromTo(
            LocalDate.now(), LocalDate.now().plusDays(4));
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(LocalDate.now(), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(LocalDate.now().plusDays(1), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(LocalDate.now().plusDays(2), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(LocalDate.now().plusDays(3), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(LocalDate.now().plusDays(4), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertFalse(iterator.hasNext());
    
    iterator = 
        everyDayFromTodayUpTo30Days.iteratorFromTo(
            LocalDate.now().plusDays(5), LocalDate.now().plusDays(9));
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(LocalDate.now().plusDays(5), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(LocalDate.now().plusDays(6), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(LocalDate.now().plusDays(7), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(LocalDate.now().plusDays(8), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(LocalDate.now().plusDays(9), 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertFalse(iterator.hasNext());
  }

  @Test
  public void testIteratorFromToShouldNotReturnAnyValueWhenDateSpanIsFromThePast() {
    LocalDate juneTheFirst = new LocalDate(2015, 6, 1);
    LocalDate decemberTheFirst = new LocalDate(2015, 12, 1);
    DailyTimeExpression dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        juneTheFirst, 
        decemberTheFirst, 
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));
    
    LocalDate januaryTheFirst = new LocalDate(2015, 1, 1);
    LocalDate mayTheThirtyONe = new LocalDate(2015, 5, 31);
    Iterator<DateTimeSpan> iterator = 
        dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst.iteratorFromTo(
           januaryTheFirst, mayTheThirtyONe);
    
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testIteratorFromToShouldWhenFromDateMatchesTimeSpanEndingDate() {
    LocalDate juneTheFirst = new LocalDate(2015, 6, 1);
    LocalDate decemberTheFirst = new LocalDate(2015, 12, 1);
    DailyTimeExpression dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        juneTheFirst, 
        decemberTheFirst, 
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));
    
    LocalDate januaryTheFirst = new LocalDate(2015, 1, 1);
    Iterator<DateTimeSpan> iterator = 
        dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst.iteratorFromTo(
           januaryTheFirst, juneTheFirst);

    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(juneTheFirst, 
        iterator.next().startingDateTime().toLocalDate());
    
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testIteratorFromToShouldWhenToDateIsGreaterThanTimeSpanStartingDate() {
    LocalDate juneTheFirst = new LocalDate(2015, 6, 1);
    LocalDate decemberTheFirst = new LocalDate(2015, 12, 1);
    DailyTimeExpression dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        juneTheFirst, 
        decemberTheFirst, 
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));
    
    LocalDate januaryTheFirst = new LocalDate(2015, 1, 1);
    LocalDate augustTheFirst = new LocalDate(2015, 8, 1);
    Iterator<DateTimeSpan> iterator = 
        dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst.iteratorFromTo(
           januaryTheFirst, augustTheFirst);

    LocalDate date = juneTheFirst;
    while (iterator.hasNext()) {
      Assert.assertEquals(date, 
          iterator.next().startingDateTime().toLocalDate());
      date = date.plusDays(1);
    }
    Assert.assertEquals(augustTheFirst, date.minusDays(1));
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testIteratorFromToShouldWhenToDateMatchesTimeSpanEndingDate() {
    LocalDate juneTheFirst = new LocalDate(2015, 6, 1);
    LocalDate decemberTheFirst = new LocalDate(2015, 12, 1);
    DailyTimeExpression dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        juneTheFirst, 
        decemberTheFirst, 
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));
    
    LocalDate januaryTheFirst = new LocalDate(2015, 1, 1);
    Iterator<DateTimeSpan> iterator = 
        dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst.iteratorFromTo(
           januaryTheFirst, decemberTheFirst);

    LocalDate date = juneTheFirst;
    while (iterator.hasNext()) {
      Assert.assertEquals(date, 
          iterator.next().startingDateTime().toLocalDate());
      date = date.plusDays(1);
    }
    Assert.assertEquals(decemberTheFirst, date.minusDays(1));
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testIteratorFromToShouldWhenToDateIsGreaterThanTimeSpanEndingDate() {
    LocalDate juneTheFirstOf2015 = new LocalDate(2015, 6, 1);
    LocalDate decemberTheFirstOf2015 = new LocalDate(2015, 12, 1);
    DailyTimeExpression dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        juneTheFirstOf2015, 
        decemberTheFirstOf2015, 
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));
    
    LocalDate januaryTheFirstOf2015 = new LocalDate(2015, 1, 1);
    LocalDate juneTheFirstOf2016 = new LocalDate(2016, 6, 1);
    Iterator<DateTimeSpan> iterator = 
        dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst.iteratorFromTo(
           januaryTheFirstOf2015, juneTheFirstOf2016);

    LocalDate date = juneTheFirstOf2015;
    while (iterator.hasNext()) {
      Assert.assertEquals(date, 
          iterator.next().startingDateTime().toLocalDate());
      date = date.plusDays(1);
    }
    Assert.assertEquals(decemberTheFirstOf2015, date.minusDays(1));
    Assert.assertFalse(iterator.hasNext());
  }  
  
  @Test
  public void testIteratorFromToShouldWhenFromDateMatchesTimeSpanStartingDateAndToDateIsLessThanDateSpanEndingDate() {
    LocalDate juneTheFirstOf2015 = new LocalDate(2015, 6, 1);
    LocalDate decemberTheFirstOf2015 = new LocalDate(2015, 12, 1);
    DailyTimeExpression dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        juneTheFirstOf2015, 
        decemberTheFirstOf2015, 
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));
    
    LocalDate augustTheFirst = new LocalDate(2015, 8, 1);
    Iterator<DateTimeSpan> iterator = 
        dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst.iteratorFromTo(
            juneTheFirstOf2015, augustTheFirst);

    LocalDate date = juneTheFirstOf2015;
    while (iterator.hasNext()) {
      Assert.assertEquals(date, 
          iterator.next().startingDateTime().toLocalDate());
      date = date.plusDays(1);
    }
    Assert.assertEquals(augustTheFirst, date.minusDays(1));
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testIteratorFromToShouldWhenFromDateMatchesTimeSpanStartingDateAndToDateMatchesDateSpanEndingDate() {
    LocalDate juneTheFirstOf2015 = new LocalDate(2015, 6, 1);
    LocalDate decemberTheFirstOf2015 = new LocalDate(2015, 12, 1);
    DailyTimeExpression dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        juneTheFirstOf2015, 
        decemberTheFirstOf2015, 
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));
    
    Iterator<DateTimeSpan> iterator = 
        dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst.iteratorFromTo(
            juneTheFirstOf2015, decemberTheFirstOf2015);

    LocalDate date = juneTheFirstOf2015;
    while (iterator.hasNext()) {
      Assert.assertEquals(date, 
          iterator.next().startingDateTime().toLocalDate());
      date = date.plusDays(1);
    }
    Assert.assertEquals(decemberTheFirstOf2015, date.minusDays(1));
    Assert.assertFalse(iterator.hasNext());
  }  
  
  @Test
  public void testIteratorFromToShouldWhenFromDateIsGreaterThanTimeSpanStartingDateAndToDateIsLessThanDateSpanEndingDate() {
    LocalDate juneTheFirstOf2015 = new LocalDate(2015, 6, 1);
    LocalDate decemberTheFirstOf2015 = new LocalDate(2015, 12, 1);
    DailyTimeExpression dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        juneTheFirstOf2015, 
        decemberTheFirstOf2015, 
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    LocalDate augustTheFirst = new LocalDate(2015, 8, 1);
    LocalDate octoberTheFirst = new LocalDate(2015, 10, 1);
    Iterator<DateTimeSpan> iterator = 
        dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst.iteratorFromTo(
            augustTheFirst, octoberTheFirst);

    LocalDate date = augustTheFirst;
    while (iterator.hasNext()) {
      Assert.assertEquals(date, 
          iterator.next().startingDateTime().toLocalDate());
      date = date.plusDays(1);
    }
    Assert.assertEquals(octoberTheFirst, date.minusDays(1));
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testIteratorFromToShouldWhenFromDateIsGreaterThanTimeSpanStartingDateAndToDateMatchesDateSpanEndingDate() {
    LocalDate juneTheFirstOf2015 = new LocalDate(2015, 6, 1);
    LocalDate decemberTheFirstOf2015 = new LocalDate(2015, 12, 1);
    DailyTimeExpression dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        juneTheFirstOf2015, 
        decemberTheFirstOf2015, 
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    LocalDate augustTheFirst = new LocalDate(2015, 8, 1);
    Iterator<DateTimeSpan> iterator = 
        dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst.iteratorFromTo(
            augustTheFirst, decemberTheFirstOf2015);

    LocalDate date = augustTheFirst;
    while (iterator.hasNext()) {
      Assert.assertEquals(date, 
          iterator.next().startingDateTime().toLocalDate());
      date = date.plusDays(1);
    }
    Assert.assertEquals(decemberTheFirstOf2015, date.minusDays(1));
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testIteratorFromToShouldWhenFromDateIsGreaterThanTimeSpanStartingDateAndToDateIsGreaterThanDateSpanEndingDate() {
    LocalDate juneTheFirstOf2015 = new LocalDate(2015, 6, 1);
    LocalDate decemberTheFirstOf2015 = new LocalDate(2015, 12, 1);
    DailyTimeExpression dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        juneTheFirstOf2015, 
        decemberTheFirstOf2015, 
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    LocalDate augustTheFirst = new LocalDate(2015, 8, 1);
    LocalDate juneTheFirstOf2016 = new LocalDate(2016, 6, 1);
    Iterator<DateTimeSpan> iterator = 
        dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst.iteratorFromTo(
            augustTheFirst, juneTheFirstOf2016);

    LocalDate date = augustTheFirst;
    while (iterator.hasNext()) {
      Assert.assertEquals(date, 
          iterator.next().startingDateTime().toLocalDate());
      date = date.plusDays(1);
    }
    Assert.assertEquals(decemberTheFirstOf2015, date.minusDays(1));
    Assert.assertFalse(iterator.hasNext());
  } 
  
  @Test
  public void testIteratorFromToShouldWhenFromDateMatchesDateSpanEndingDate() {
    LocalDate juneTheFirstOf2015 = new LocalDate(2015, 6, 1);
    LocalDate decemberTheFirstOf2015 = new LocalDate(2015, 12, 1);
    DailyTimeExpression dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        juneTheFirstOf2015, 
        decemberTheFirstOf2015, 
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    LocalDate juneTheFirstOf2016 = new LocalDate(2016, 6, 1);
    Iterator<DateTimeSpan> iterator = 
        dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst.iteratorFromTo(
            decemberTheFirstOf2015, juneTheFirstOf2016);

    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(decemberTheFirstOf2015,
        iterator.next().startingDateTime().toLocalDate());

    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testIteratorFromToShouldWhenFromDateIsGreaterThanDateSpanEndingDate() {
    LocalDate juneTheFirstOf2015 = new LocalDate(2015, 6, 1);
    LocalDate decemberTheFirstOf2015 = new LocalDate(2015, 12, 1);
    DailyTimeExpression dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst = 
      DailyTimeExpression.dailyEveryStartsOnEndsOn(
        Days.ONE, 
        juneTheFirstOf2015, 
        decemberTheFirstOf2015, 
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    LocalDate juneTheFirstOf2016 = new LocalDate(2016, 6, 1);
    LocalDate januaryTheFirstOf2016 = new LocalDate(2016, 1, 1);
    Iterator<DateTimeSpan> iterator = 
        dailyEveryOneDayFromJuneTheFirstToDecemberTheFirst.iteratorFromTo(
            januaryTheFirstOf2016, juneTheFirstOf2016);

    Assert.assertFalse(iterator.hasNext());
  }  
}
