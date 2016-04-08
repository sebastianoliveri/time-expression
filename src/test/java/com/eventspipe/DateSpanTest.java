package com.eventspipe;

import junit.framework.TestCase;

import org.joda.time.LocalDate;

import com.eventspipe.DateSpan;
import com.eventspipe.TimeExpressionException;

public class DateSpanTest extends TestCase {

  public void testShouldFailWhenEndingDateIsBeforeToStartingDate() {
    try {
      DateSpan.fromTo(
          LocalDate.now(), 
          LocalDate.now().minusDays(1));
      fail();
    } catch (TimeExpressionException exception) {
      assertEquals(
          TimeExpressionException.DATE_SPAN_INVALID.getMessage(), 
          exception.getMessage());
    }
  }

  public void testShouldFailWhenEndingDateIsEqualToStartingDate() {
    try {
      DateSpan.fromTo(
          LocalDate.now(), 
          LocalDate.now());
      fail();
    } catch (TimeExpressionException exception) {
      assertEquals(
          TimeExpressionException.DATE_SPAN_INVALID.getMessage(), 
          exception.getMessage());
    }
  }

  public void testTimeSpanCreation() {
    LocalDate today = LocalDate.now();
    LocalDate tomorrow = today.plusDays(1);

    DateSpan aTimeSpan = 
        DateSpan.fromTo(
            today, 
            tomorrow);

    assertEquals(today, aTimeSpan.startingDate());
    assertEquals(tomorrow, aTimeSpan.endingDate());
  }

  public void testEquals() {
    LocalDate now = LocalDate.now();
    assertEquals(DateSpan.fromTo(now, now.plusDays(1)),
        DateSpan.fromTo(now, now.plusDays(1)));
    assertEquals(DateSpan.fromTo(now, now.plusWeeks(2)),
        DateSpan.fromTo(now, now.plusWeeks(2)));
    assertEquals(DateSpan.fromTo(now, now.plusMonths(3)),
        DateSpan.fromTo(now, now.plusMonths(3)));
    assertEquals(DateSpan.fromTo(now, now.plusYears(4)),
        DateSpan.fromTo(now, now.plusYears(4)));
    assertFalse(DateSpan.fromTo(now, now.plusDays(4)).equals(
        DateSpan.fromTo(now, now.plusDays(2))));
  }

  public void testIncludes() {
    LocalDate marchTen2013 = 
        new LocalDate(2013, 3, 10);
    LocalDate marchTwenty2013 = 
        new LocalDate(2013, 3, 20);
    DateSpan fromMarchTen2013ToMarchTwenty2013 = 
        DateSpan.fromTo(
          marchTen2013,
          marchTwenty2013);

    LocalDate marchFifteen2013 = new LocalDate(2013, 3, 15);
    assertTrue(fromMarchTen2013ToMarchTwenty2013.includes(marchFifteen2013));

    assertTrue(fromMarchTen2013ToMarchTwenty2013.includes(marchTen2013));

    assertTrue(fromMarchTen2013ToMarchTwenty2013.includes(marchTwenty2013));

    LocalDate marchTwentyOne2013 = new LocalDate(2013, 3, 21);
    assertFalse(fromMarchTen2013ToMarchTwenty2013.includes(marchTwentyOne2013));

    LocalDate marchNine2013 = new LocalDate(2013, 3, 9);
    assertFalse(fromMarchTen2013ToMarchTwenty2013.includes(marchNine2013));
  }

}
