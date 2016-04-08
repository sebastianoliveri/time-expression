package com.eventspipe;

import java.util.Collection;

import junit.framework.TestCase;

import org.joda.time.DateTimeConstants;

import com.eventspipe.DayOfWeek;
import com.eventspipe.TimeExpressionException;

public class DayOfWeekTest extends TestCase {

  public void testValueOfShouldThrowExceptionWhenIndexIsInvalid() {
    try {
      DayOfWeek.valueOf(DateTimeConstants.MONDAY - 1);
      fail();
    } catch (RuntimeException exception) {
      assertEquals(TimeExpressionException.DAY_OF_WEEK_INVALID.getMessage(), 
          exception.getMessage());
    }

    try {
      DayOfWeek.valueOf(DateTimeConstants.SUNDAY + 1);
      fail();
    } catch (RuntimeException exception) {
      assertEquals(TimeExpressionException.DAY_OF_WEEK_INVALID.getMessage(), 
          exception.getMessage());
    }
  }

  public void testEquals() {
    assertEquals(DayOfWeek.MONDAY, DayOfWeek.valueOf(DateTimeConstants.MONDAY));
    assertEquals(DayOfWeek.TUESDAY,
        DayOfWeek.valueOf(DateTimeConstants.TUESDAY));
    assertEquals(DayOfWeek.WEDNESDAY,
        DayOfWeek.valueOf(DateTimeConstants.WEDNESDAY));
    assertEquals(DayOfWeek.THURSDAY,
        DayOfWeek.valueOf(DateTimeConstants.THURSDAY));
    assertEquals(DayOfWeek.FRIDAY, DayOfWeek.valueOf(DateTimeConstants.FRIDAY));
    assertEquals(DayOfWeek.SATURDAY,
        DayOfWeek.valueOf(DateTimeConstants.SATURDAY));
    assertEquals(DayOfWeek.SUNDAY, DayOfWeek.valueOf(DateTimeConstants.SUNDAY));
  }

  public void testDaysOfWeek() {
    Collection<DayOfWeek> daysOfWeek = DayOfWeek.allDays();
    assertTrue(daysOfWeek.size() == 7);
    assertTrue(daysOfWeek.contains(DayOfWeek.MONDAY));
    assertTrue(daysOfWeek.contains(DayOfWeek.TUESDAY));
    assertTrue(daysOfWeek.contains(DayOfWeek.WEDNESDAY));
    assertTrue(daysOfWeek.contains(DayOfWeek.THURSDAY));
    assertTrue(daysOfWeek.contains(DayOfWeek.FRIDAY));
    assertTrue(daysOfWeek.contains(DayOfWeek.SATURDAY));
    assertTrue(daysOfWeek.contains(DayOfWeek.SUNDAY));
  }

}
