package com.eventspipe;

import java.util.Iterator;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.base.BaseSingleFieldPeriod;

public abstract class PeriodicTimeExpression extends TimeExpression {

  private BaseSingleFieldPeriod magnitude;

  protected TimeExpressionDuration duration;

  public PeriodicTimeExpression(
      BaseSingleFieldPeriod aMagnitude,
      TimeExpressionDuration theDuration,
      TimeSpan... timePeriods) {
    super(timePeriods);
    ExpressionAssertion.
      isGreatherThanDescribedBy(
        aMagnitude.getValue(0), 
        0, 
        TimeExpressionException.EVERY_AMOUNT_INVALID).
          assertTrue();
    this.magnitude = aMagnitude;
    this.duration = theDuration;
  }
  
  @Override
  public boolean evaluate(LocalDate aDate) {
    boolean matches = duration.includes(aDate);
    matches &= this.repeatsEvery(aDate, this.magnitude);
    matches &= this.doEvaluate(aDate);
    return matches;
  }

  private boolean repeatsEvery(
      LocalDate aDate,
      BaseSingleFieldPeriod aMagnitude) {
    int difference = 
        TimeDifferenceCalculation.
          differenceBetween(
            magnitude.getClass(), 
            aDate, 
            this.duration.startingDate());
    return difference % this.magnitude.getValue(0) == 0;
  }

  public boolean doEvaluate(LocalDate aDate) {
    return true;
  }

  @Override
  public Iterator<DateTimeSpan> iterator() {
    return TimeExpressionIterator.on(
        this, startingDate(), endingDate());
  }
  
  @Override
  public Iterator<DateTimeSpan> iteratorFromTo(
      LocalDate aDate, LocalDate anotherDate) {
    return TimeExpressionIterator.on(
        this, aDate, anotherDate);
  }

  @Override
  public TimeExpression and(TimeExpression anotherTimeExpression) {
    return new BinaryTimeExpression(this, anotherTimeExpression);
  }

  public DateSpan dateSpan() {
    return duration.dateSpan();
  }

  protected BaseSingleFieldPeriod magnitude() {
    return this.magnitude;
  }

  @Override
  public LocalDateTime endingDateTime() {
    return duration.endingDate().toLocalDateTime(
        timeSpans()[timeSpans().length - 1].endingTime());
  }

  @Override
  public LocalDateTime startingDateTime() {
    return duration.startingDate().toLocalDateTime(
        timeSpans()[0].startingTime());
  }
  
  @Override
  public void endingOn(LocalDateTime anEndingDateTime) {
    duration = 
      TimeExpressionDuration.during(
        DateSpan.fromTo(
          this.dateSpan().startingDate(),
          anEndingDateTime.toLocalDate()));
  }
  
  public int numberOfOcurringDays() {
    return duration.numberOfOcurringDays(this);
  }
  
  public int numberOfOccurrences() {
    return duration.numberOfOccurrences(this);
  }
  
  public boolean neverEnds() {
    return duration.neverEnds();
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + magnitude.hashCode();
    result = prime * result + duration.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    PeriodicTimeExpression other = (PeriodicTimeExpression) obj;
    if (!magnitude.equals(other.magnitude))
      return false;
    if (!duration.equals(other.duration))
      return false;
    return true;
  }
}
