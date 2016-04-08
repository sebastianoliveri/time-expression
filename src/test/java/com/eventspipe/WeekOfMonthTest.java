package com.eventspipe;

import java.util.Collection;

import com.eventspipe.TimeExpressionException;
import com.eventspipe.WeekOfMonth;

import junit.framework.TestCase;

public class WeekOfMonthTest extends TestCase {

  public void testValueOfShouldThrowExceptionWhenWeekOfMonthIndexIsInvalid() {
    try {
      WeekOfMonth.valueOf(6);
      fail();
    } catch (RuntimeException exception) {
      assertEquals(TimeExpressionException.WEEK_OF_MONTH_INVALID.getMessage(), 
          exception.getMessage());
    }

    try {
      WeekOfMonth.valueOf(0);
      fail();
    } catch (RuntimeException exception) {
      assertEquals(TimeExpressionException.WEEK_OF_MONTH_INVALID.getMessage(), 
          exception.getMessage());
    }
  }

  public void testEquals() {
    assertTrue(WeekOfMonth.FIRST.equals(WeekOfMonth
        .valueOf(WeekOfMonth.FIRST_WEEK_INDEX)));
    assertTrue(WeekOfMonth.SECOND.equals(WeekOfMonth
        .valueOf(WeekOfMonth.SECOND_WEEK_INDEX)));
    assertTrue(WeekOfMonth.THIRD.equals(WeekOfMonth
        .valueOf(WeekOfMonth.THIRD_WEEK_INDEX)));
    assertTrue(WeekOfMonth.FOURTH.equals(WeekOfMonth
        .valueOf(WeekOfMonth.FOURTH_WEEK_INDEX)));
    assertTrue(WeekOfMonth.LAST.equals(WeekOfMonth
        .valueOf(WeekOfMonth.LAST_WEEK_INDEX)));
  }

  public void testAllWeeks() {
    Collection<WeekOfMonth> weeks = WeekOfMonth.allWeeks();
    assertTrue(weeks.size() == 5);
    assertTrue(weeks.contains(WeekOfMonth.FIRST));
    assertTrue(weeks.contains(WeekOfMonth.SECOND));
    assertTrue(weeks.contains(WeekOfMonth.THIRD));
    assertTrue(weeks.contains(WeekOfMonth.FOURTH));
    assertTrue(weeks.contains(WeekOfMonth.LAST));
  }
}
