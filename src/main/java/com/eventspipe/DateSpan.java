package com.eventspipe;

import org.joda.time.Days;
import org.joda.time.LocalDate;

public class DateSpan {

  private LocalDate startingDate;

  private LocalDate endingDate;

  public DateSpan(
      LocalDate aStartingDate,
      LocalDate anEndingDate) {
    ExpressionAssertion.
      isLessThanDescribedBy(
          aStartingDate, 
          anEndingDate, 
          TimeExpressionException.DATE_SPAN_INVALID).
            assertTrue();
    startingDate = aStartingDate;
    endingDate = anEndingDate;
  }

  public LocalDate startingDate() {
    return startingDate;
  }

  public LocalDate endingDate() {
    return endingDate;
  }

  public boolean includes(LocalDate aDate) {
    return startsSameOrBeforeThan(aDate) && 
        endsSameOrAfterThan(aDate);
  }
  
  public boolean startsSameOrBeforeThan(LocalDate aDate) {
    return aDate.isEqual(this.startingDate()) || 
        aDate.isAfter(this.startingDate());
  }
  
  public boolean endsSameOrAfterThan(LocalDate aDate) {
    return aDate.isEqual(this.endingDate()) || 
        aDate.isBefore(this.endingDate());
  }
  
  public Days days() {
    return Days.daysBetween(
        startingDate, endingDate);
  }

  public static DateSpan fromTo(
      LocalDate aStartingDate,
      LocalDate anEndingDate) {
    return new DateSpan(
        aStartingDate, 
        anEndingDate);
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + endingDate.hashCode();
    result = prime * result + startingDate.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DateSpan other = (DateSpan) obj;
    if (!endingDate.equals(other.endingDate))
      return false;
    if (!startingDate.equals(other.startingDate))
      return false;
    return true;
  }
}
