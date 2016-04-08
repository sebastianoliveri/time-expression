package com.eventspipe;

import junit.framework.TestCase;

import org.joda.time.LocalDate;

import com.eventspipe.DayOfMonth;
import com.eventspipe.DayOfWeek;
import com.eventspipe.MonthOfYear;
import com.eventspipe.TimeExpressionException;
import com.eventspipe.WeekOfMonth;

public class MonthOfYearTest extends TestCase {

  public void testCreationShouldThrowExceptionWhenMonthIsInvalid() {
    int anInvalidMonth = -1;
    try {
      MonthOfYear.on(anInvalidMonth, 2012);
      fail();
    } catch (RuntimeException exception) {
      assertEquals(TimeExpressionException.MONTH_INVALID.getMessage(), 
          exception.getMessage());
    }
    anInvalidMonth = 0;
    try {
      MonthOfYear.on(anInvalidMonth, 2012);
      fail();
    } catch (RuntimeException exception) {
      assertEquals(TimeExpressionException.MONTH_INVALID.getMessage(), 
          exception.getMessage());
    }    
    anInvalidMonth = 13;
    try {
      MonthOfYear.on(anInvalidMonth, 2012);
      fail();
    } catch (RuntimeException exception) {
      assertEquals(TimeExpressionException.MONTH_INVALID.getMessage(), 
          exception.getMessage());
    }
  }

  public void testCreationShouldThrowExceptionWhenYearIsInvalid() {
    int anInvalidYear = -1;
    int january = 1;
    try {
      MonthOfYear.on(january, anInvalidYear);
      fail();
    } catch (RuntimeException exception) {
      assertEquals(TimeExpressionException.YEAR_INVALID.getMessage(), 
          exception.getMessage());
    }
  }

  public void testEquals() {
    assertTrue(MonthOfYear.on(8, 2012).equals(MonthOfYear.on(8, 2012)));
    assertTrue(MonthOfYear.on(2, 2013).equals(MonthOfYear.on(2, 2013)));
    assertFalse(MonthOfYear.on(2, 2012).equals(MonthOfYear.on(2, 2013)));
    assertFalse(MonthOfYear.on(2, 2013).equals(MonthOfYear.on(1, 2013)));
  }

  public void testDateAtOn() {
    LocalDate expectedDate = new LocalDate(2012, 1, 27);
    MonthOfYear januaryOf2012 = MonthOfYear.on(1, 2012);
    DayOfWeek aFriday = DayOfWeek.FRIDAY;
    WeekOfMonth lastWeek = WeekOfMonth.LAST;
    assertEquals(expectedDate, januaryOf2012.dateOfOn(aFriday, lastWeek));

    expectedDate = new LocalDate(2012, 1, 30);
    januaryOf2012 = MonthOfYear.on(1, 2012);
    DayOfWeek aMonday = DayOfWeek.MONDAY;
    lastWeek = WeekOfMonth.LAST;
    assertEquals(expectedDate, januaryOf2012.dateOfOn(aMonday, lastWeek));

    expectedDate = new LocalDate(2012, 1, 23);
    januaryOf2012 = MonthOfYear.on(1, 2012);
    aMonday = DayOfWeek.MONDAY;
    WeekOfMonth fourthWeek = WeekOfMonth.FOURTH;
    assertEquals(expectedDate, januaryOf2012.dateOfOn(aMonday, fourthWeek));

    expectedDate = new LocalDate(2012, 1, 10);
    januaryOf2012 = MonthOfYear.on(1, 2012);
    DayOfWeek aTuesday = DayOfWeek.TUESDAY;
    WeekOfMonth secondWeek = WeekOfMonth.SECOND;
    assertEquals(expectedDate, januaryOf2012.dateOfOn(aTuesday, secondWeek));

    expectedDate = new LocalDate(2012, 1, 1);
    januaryOf2012 = MonthOfYear.on(1, 2012);
    DayOfWeek aSunday = DayOfWeek.SUNDAY;
    WeekOfMonth firstWeek = WeekOfMonth.FIRST;
    assertEquals(expectedDate, januaryOf2012.dateOfOn(aSunday, firstWeek));

    expectedDate = new LocalDate(2012, 1, 2);
    januaryOf2012 = MonthOfYear.on(1, 2012);
    firstWeek = WeekOfMonth.FIRST;
    assertEquals(expectedDate, januaryOf2012.dateOfOn(aMonday, firstWeek));
  }

  public void testDateOn() {
    LocalDate expectedDate = new LocalDate(2012, 1, 4);
    MonthOfYear januaryOf2012 = MonthOfYear.on(1, 2012);
    DayOfMonth fourthDay = DayOfMonth.valueOf(4);
    assertEquals(expectedDate, januaryOf2012.dateAt(fourthDay));
  }

  public void testDateOnShouldThrowExceptionWhenDayOfMonthIsGreaterThanMaxDayOfMonth() {
    MonthOfYear februaryOf2012 = MonthOfYear.on(2, 2012);
    try {
      februaryOf2012.dateAt(DayOfMonth.valueOf(30));
      fail();
    } catch (RuntimeException exception) {
      assertEquals(TimeExpressionException.DAY_OF_MONTH_INVALID.getMessage(), 
          exception.getMessage());
    }
  }
}
