package com.eventspipe;

import junit.framework.TestCase;

import org.joda.time.LocalTime;

import com.eventspipe.TimeExpressionException;
import com.eventspipe.TimeSpan;

public class TimeSpanTest extends TestCase {

  public void testCreationShouldFailWhenStartingTimeIsEqualToEndingTime() {
    try {
      TimeSpan.fromTo(
          new LocalTime(18, 0), 
          new LocalTime(18, 0));
      fail();
    } catch (TimeExpressionException exception) {
      assertEquals(
          TimeExpressionException.TIME_SPAN_INVALID.getMessage(), 
          exception.getMessage());
    }
  }

  public void testCreation() {
    LocalTime sixAM = new LocalTime(6, 0);
    LocalTime nineAM = new LocalTime(9, 0);
    TimeSpan fromSixAMtoNineAM = 
        TimeSpan.fromTo(
            sixAM, 
            nineAM);
    assertEquals(sixAM, fromSixAMtoNineAM.startingTime());
    assertEquals(nineAM, fromSixAMtoNineAM.endingTime());
  }

  public void testEquals() {
    LocalTime sixAM = new LocalTime(6, 0);
    LocalTime nineAM = new LocalTime(9, 0);
    TimeSpan fromSixAMtoNineAM = TimeSpan.fromTo(sixAM, nineAM);
    assertEquals(fromSixAMtoNineAM, TimeSpan.fromTo(sixAM, nineAM));
  }
}
