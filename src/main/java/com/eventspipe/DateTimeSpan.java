package com.eventspipe;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;

public class DateTimeSpan {

  private LocalDateTime startingDateTime;

  private LocalDateTime endingDateTime;

  public DateTimeSpan(
      LocalDateTime aStartingDateTime,
      LocalDateTime anEndingDateTime) {
    startingDateTime = aStartingDateTime;
    endingDateTime = anEndingDateTime;
  }

  public LocalDateTime startingDateTime() {
    return startingDateTime;
  }

  public LocalDateTime endingDateTime() {
    return endingDateTime;
  }

  public boolean contains(LocalDateTime aDateTime) {
    boolean contains = true;
    contains &= 
        startingDateTime.isBefore(aDateTime) || 
        startingDateTime.isEqual(aDateTime);
    contains &= 
        endingDateTime.isAfter(aDateTime) || 
        endingDateTime.isEqual(aDateTime);
    return contains;
  }

  public DateTimeSpan moveForward(Days days) {
    return new DateTimeSpan(
        startingDateTime.plusDays(1),
        endingDateTime.plusDays(1));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + endingDateTime.hashCode();
    result = prime * result + startingDateTime.hashCode();
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
    DateTimeSpan other = (DateTimeSpan) obj;
    if (!endingDateTime.equals(other.endingDateTime))
      return false;
    if (!startingDateTime.equals(other.startingDateTime))
      return false;
    return true;
  }

  public static DateTimeSpan fromTo(
      LocalDateTime aStartingDateTime,
      LocalDateTime anEndingDateTime) {
    return new DateTimeSpan(
        aStartingDateTime, 
        anEndingDateTime);
  }
}
