package com.eventspipe;

import java.util.ArrayList;
import java.util.Iterator;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class NullTimeExpression extends TimeExpression {

  public static final TimeExpression INSTANCE = new NullTimeExpression();

  @Override
  public boolean evaluate(LocalDate aDate) {
    return false;
  }

  @Override
  public Iterator<DateTimeSpan> iterator() {
    return new ArrayList<DateTimeSpan>().iterator();
  }
  
  @Override
  public Iterator<DateTimeSpan> iteratorFromTo(
      LocalDate aDate,
      LocalDate anotherDate) {
    return new ArrayList<DateTimeSpan>().iterator();
  }
  
  @Override
  public int numberOfOcurringDays() {
    return 0;
  }
  
  @Override
  public int numberOfOccurrences() {
    return 0;
  }
  
  @Override
  public boolean endsAfter(LocalDate aDate) {
    throw new UnsupportedOperationException();
  }

  @Override
  public LocalDateTime endingDateTime() {
    throw new UnsupportedOperationException();
  }

  @Override
  public LocalDateTime startingDateTime() {
    throw new UnsupportedOperationException();
  }

  @Override
  public TimeExpression and(TimeExpression anotherTimeExpression) {
    return anotherTimeExpression;
  }
}
