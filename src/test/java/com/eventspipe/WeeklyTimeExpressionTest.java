package com.eventspipe;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Weeks;
import org.junit.Assert;
import org.junit.Test;

import com.eventspipe.DateTimeSpan;
import com.eventspipe.DayOfWeek;
import com.eventspipe.DayOfWeekOfWeeklyTimeExpression;
import com.eventspipe.TimeExpression;
import com.eventspipe.TimeExpressionException;
import com.eventspipe.TimeSpan;
import com.eventspipe.WeeklyTimeExpression;
import com.google.gson.JsonElement;

public class WeeklyTimeExpressionTest {
  
  @Test
  public void testShouldFailWhenDaysOfWeekIsEmpty() {
    try {
      new WeeklyTimeExpression(
          LocalDate.now(), 
          LocalDate.now().plusMonths(1), 
          new ArrayList<DayOfWeekOfWeeklyTimeExpression>());
      Assert.fail();
    } catch (TimeExpressionException exception) {
      Assert.assertEquals(
          TimeExpressionException.DAYS_OF_WEEK_INVALID.getMessage(), 
          exception.getMessage());
    }
  }
  
  @Test
  public void testShouldFailWhenDaysOfWeekAreDuplicated() {
    ArrayList<DayOfWeekOfWeeklyTimeExpression> daysOfWeek = 
        new ArrayList<DayOfWeekOfWeeklyTimeExpression>();
    daysOfWeek.add(
            DayOfWeekOfWeeklyTimeExpression.onEveryDuring(
                DayOfWeek.valueOf(1), 
                Weeks.ONE, 
                TimeSpan.fromTo(new LocalTime(9, 0), new LocalTime(10, 0))));
    daysOfWeek.add(
        DayOfWeekOfWeeklyTimeExpression.onEveryDuring(
            DayOfWeek.valueOf(2), 
            Weeks.ONE, 
            TimeSpan.fromTo(new LocalTime(9, 0), new LocalTime(10, 0))));
    daysOfWeek.add(
        DayOfWeekOfWeeklyTimeExpression.onEveryDuring(
            DayOfWeek.valueOf(3),
            Weeks.ONE, 
            TimeSpan.fromTo(new LocalTime(9, 0), new LocalTime(10, 0))));
    daysOfWeek.add(
        DayOfWeekOfWeeklyTimeExpression.onEveryDuring(
            DayOfWeek.valueOf(1), 
            Weeks.ONE, 
            TimeSpan.fromTo(new LocalTime(9, 0), new LocalTime(10, 0))));
    
    try {
      new WeeklyTimeExpression(
          LocalDate.now(), 
          LocalDate.now().plusMonths(1), 
          daysOfWeek);
      Assert.fail();
    } catch (TimeExpressionException exception) {
      Assert.assertEquals(
          TimeExpressionException.DAY_OF_WEEK_DUPLICATED.getMessage(), 
          exception.getMessage());
    }
  }
  
  @Test
  public void testWednesdayWhenItDoesNotReccurrs() {
    LocalDate octoberMonday19 = new LocalDate(2015, 10, 19);
    LocalDate octoberSunday25 = new LocalDate(2015, 10, 25);
    List<DayOfWeekOfWeeklyTimeExpression> theDaysOfweek = 
        new ArrayList<DayOfWeekOfWeeklyTimeExpression>();
    theDaysOfweek.add(
        new DayOfWeekOfWeeklyTimeExpression(
            DayOfWeek.WEDNESDAY, 
            Weeks.ONE, 
            TimeSpan.fromTo(new LocalTime(9, 0), new LocalTime(10, 0))));
    try {
      new WeeklyTimeExpression(
          octoberMonday19,
          octoberSunday25,
          theDaysOfweek);
      Assert.fail();
    } catch (RuntimeException exception) {
      assertEquals(
          TimeExpressionException.ENDING_DATE_INVALID.getMessage(), 
          exception.getMessage());
    }
  }
  
  @Test
  public void testWednesdayWhenItDoesNotReccurrs2() {
    LocalDate octoberMonday19 = new LocalDate(2015, 10, 19);
    LocalDate octoberTuesday27 = new LocalDate(2015, 10, 27);
    List<DayOfWeekOfWeeklyTimeExpression> theDaysOfweek = 
        new ArrayList<DayOfWeekOfWeeklyTimeExpression>();
    theDaysOfweek.add(
        new DayOfWeekOfWeeklyTimeExpression(
            DayOfWeek.WEDNESDAY, 
            Weeks.ONE, 
            TimeSpan.fromTo(new LocalTime(9, 0), new LocalTime(10, 0))));
    try {
      new WeeklyTimeExpression(
          octoberMonday19,
          octoberTuesday27,
          theDaysOfweek);
      Assert.fail();
    } catch (RuntimeException exception) {
      assertEquals(
          TimeExpressionException.ENDING_DATE_INVALID.getMessage(), 
          exception.getMessage());
    }
  }
  
  @Test
  public void testWednesdayWhenReccurrsTwoTimes() {
    LocalDate octoberMonday19 = new LocalDate(2015, 10, 19);
    LocalDate octoberWednesday28 = new LocalDate(2015, 10, 28);
    List<DayOfWeekOfWeeklyTimeExpression> theDaysOfweek = 
        new ArrayList<DayOfWeekOfWeeklyTimeExpression>();
    theDaysOfweek.add(
        new DayOfWeekOfWeeklyTimeExpression(
            DayOfWeek.WEDNESDAY, 
            Weeks.ONE, 
            TimeSpan.fromTo(new LocalTime(9, 0), new LocalTime(10, 0))));
    Iterator<DateTimeSpan> iterator = 
        new WeeklyTimeExpression(
          octoberMonday19,
          octoberWednesday28,
          theDaysOfweek).
            iterator();
    
    LocalDate octoberWednesday21 = octoberMonday19.plusDays(2);
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(octoberWednesday21, 
        iterator.next().startingDateTime().toLocalDate());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(octoberWednesday28, 
        iterator.next().startingDateTime().toLocalDate());
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testIterator() {
    LocalDate october15 = new LocalDate(2015, 10, 15);
    LocalDate november2 = new LocalDate(2015, 11, 2);
    List<DayOfWeekOfWeeklyTimeExpression> theDaysOfweek = 
        new ArrayList<DayOfWeekOfWeeklyTimeExpression>();
    TimeSpan from9To10 = TimeSpan.fromTo(new LocalTime(9, 0), new LocalTime(10, 0));
    theDaysOfweek.add(new DayOfWeekOfWeeklyTimeExpression(DayOfWeek.TUESDAY, Weeks.ONE, from9To10));
    theDaysOfweek.add(new DayOfWeekOfWeeklyTimeExpression(DayOfWeek.FRIDAY, Weeks.ONE, from9To10));
    theDaysOfweek.add(new DayOfWeekOfWeeklyTimeExpression(DayOfWeek.SATURDAY, Weeks.ONE, from9To10));
     
    WeeklyTimeExpression weeklyOnTuesdayFridayAndSaturdayFromOctoberThurdsday15ToOctoberNovember2;
    weeklyOnTuesdayFridayAndSaturdayFromOctoberThurdsday15ToOctoberNovember2 = 
        new WeeklyTimeExpression(
            october15, 
            november2, 
            theDaysOfweek);
    
    Iterator<DateTimeSpan> iterator = 
        weeklyOnTuesdayFridayAndSaturdayFromOctoberThurdsday15ToOctoberNovember2.iterator();
    
    List<LocalDate> expectedDates = 
        new LinkedList<LocalDate>();
    expectedDates.add(new LocalDate(2015, 10, 16));
    expectedDates.add(new LocalDate(2015, 10, 17));
    expectedDates.add(new LocalDate(2015, 10, 20));
    expectedDates.add(new LocalDate(2015, 10, 23));
    expectedDates.add(new LocalDate(2015, 10, 24));
    expectedDates.add(new LocalDate(2015, 10, 27));
    expectedDates.add(new LocalDate(2015, 10, 30));
    expectedDates.add(new LocalDate(2015, 10, 31));
    
    while (iterator.hasNext()) {
      Assert.assertEquals(
          expectedDates.remove(0), 
          iterator.next().startingDateTime().toLocalDate());
    }
    Assert.assertTrue(expectedDates.isEmpty());
  }
  
  @Test
  public void testAsString() {
    LocalDate october15 = new LocalDate(2015, 10, 15);
    LocalDate saturday31 = new LocalDate(2015, 10, 31);
    List<DayOfWeekOfWeeklyTimeExpression> theDaysOfweek = 
        new ArrayList<DayOfWeekOfWeeklyTimeExpression>();
    TimeSpan from9To10 = TimeSpan.fromTo(new LocalTime(9, 0), new LocalTime(10, 0));
    theDaysOfweek.add(new DayOfWeekOfWeeklyTimeExpression(DayOfWeek.TUESDAY, Weeks.ONE, from9To10));
    theDaysOfweek.add(new DayOfWeekOfWeeklyTimeExpression(DayOfWeek.FRIDAY, Weeks.ONE, from9To10));
    theDaysOfweek.add(new DayOfWeekOfWeeklyTimeExpression(DayOfWeek.SATURDAY, Weeks.ONE, from9To10));
     
    WeeklyTimeExpression weeklyTimeExpression = 
        new WeeklyTimeExpression(
            october15, 
            saturday31, 
            theDaysOfweek);
    
    JsonElement weeklyTimeExpressionAsString = weeklyTimeExpression.asJson();
    Assert.assertTrue(
        TimeExpression.from(weeklyTimeExpressionAsString).equals(weeklyTimeExpression));
  }
}
