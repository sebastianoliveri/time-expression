package com.eventspipe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

import com.eventspipe.DateTimeExpression;
import com.eventspipe.DateTimeSpan;
import com.eventspipe.PeriodicTimeExpression;
import com.eventspipe.TimeExpression;
import com.eventspipe.TimeSpan;
import com.google.gson.JsonElement;

public class DateTimeExpressionTest {

  @Test
  public void testOnDate() {
    LocalDate today = LocalDate.now();

    TimeExpression todayTimeExpression = PeriodicTimeExpression
        .onFromUntil(today,
            TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    assertTrue(todayTimeExpression.evaluate(today));
    assertFalse(todayTimeExpression.evaluate(today.minusDays(1)));
  }

  @Test
  public void testOnDateIterator() {
    LocalTime aStartingTime = LocalTime.now();
    LocalTime anEndingTime = aStartingTime.plusHours(1);

    LocalDate today = LocalDate.now();
    TimeExpression todayTimeExpression = PeriodicTimeExpression.onFromUntil(
        today, TimeSpan.fromTo(aStartingTime, anEndingTime));

    Iterator<DateTimeSpan> iterator = todayTimeExpression.iterator();
    assertTrue(iterator.hasNext());
    LocalDateTime expectedDate = new LocalDateTime(today.getYear(),
        today.getMonthOfYear(), today.getDayOfMonth(),
        aStartingTime.getHourOfDay(), aStartingTime.getMinuteOfHour(),
        aStartingTime.getSecondOfMinute());
    assertTrue(iterator.next().contains(expectedDate));
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testDateAsString() {
    LocalDate today = LocalDate.now();
    DateTimeExpression todayTimeExpression = TimeExpression.onFromUntil(today,
        TimeSpan.fromTo(LocalTime.now(), LocalTime.now().plusHours(1)));

    assertEquals(todayTimeExpression,
        DateTimeExpression.from(todayTimeExpression.asJson()));
  }

  @Test
  public void testDateAsString2() {
    LocalTime oneOClock = new LocalTime(1, 0, 0);
    LocalTime threeOClock = new LocalTime(3, 0, 0);
    LocalTime threeThirtyOClock = new LocalTime(3, 30, 0);
    LocalTime fiveOClock = new LocalTime(5, 0, 0);
    LocalTime sevenOClock = new LocalTime(7, 0, 0);
    LocalTime nineOClock = new LocalTime(9, 0, 0);

    LocalDate today = LocalDate.now();
    DateTimeExpression todayTimeExpression = TimeExpression.onFromUntil(today,
        TimeSpan.fromTo(oneOClock, threeOClock),
        TimeSpan.fromTo(threeThirtyOClock, fiveOClock),
        TimeSpan.fromTo(sevenOClock, nineOClock));

    assertEquals(todayTimeExpression,
        DateTimeExpression.from(todayTimeExpression.asJson()));
  }
  
  @Test
  public void testDateAsString3() {
    LocalDate today = LocalDate.now();
    DateTimeExpression todayTimeExpression =
      TimeExpression.onFromUntil(today,
        TimeSpan.fromTo(
            LocalTime.now(),
            LocalTime.now().plusHours(1)));

    JsonElement todayTimeExpressionAsString = todayTimeExpression.asJson();

    assertEquals(todayTimeExpression,
        DateTimeExpression.from(todayTimeExpressionAsString));
  }  
}
