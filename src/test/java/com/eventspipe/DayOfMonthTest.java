package com.eventspipe;

import com.eventspipe.DayOfMonth;

import junit.framework.TestCase;

public class DayOfMonthTest extends TestCase {

  public void testValueOfShouldThrowExceptionWhenDayOfMonthIndexIsInvalid() {
    try {
      DayOfMonth.valueOf(0);
      fail();
    } catch (RuntimeException exception) {
      assertEquals(DayOfMonth.DAY_OF_MONTH_INVALID, exception.getMessage());
    }
    try {
      DayOfMonth.valueOf(32);
      fail();
    } catch (RuntimeException exception) {
      assertEquals(DayOfMonth.DAY_OF_MONTH_INVALID, exception.getMessage());
    }
  }

  public void testEquals() {
    assertTrue(DayOfMonth.valueOf(1).equals(DayOfMonth.valueOf(1)));
    assertTrue(DayOfMonth.valueOf(2).equals(DayOfMonth.valueOf(2)));
    assertTrue(DayOfMonth.valueOf(3).equals(DayOfMonth.valueOf(3)));
    assertTrue(DayOfMonth.valueOf(31).equals(DayOfMonth.valueOf(31)));
    assertFalse(DayOfMonth.valueOf(1).equals(DayOfMonth.valueOf(31)));
  }

}
