package com.eventspipe;

import org.joda.time.LocalTime;

import com.eventspipe.ExpressionAssertion;

public class TimeSpan implements Comparable<TimeSpan> {

  public static final String PERIOD_INVALID =
      "La fecha de fin no puede ser la misma que la de comienzo";

  private LocalTime startingTime;

  private LocalTime endingTime;

  public TimeSpan(
      LocalTime aStartingTime, 
      LocalTime anEndingTime) {
    ExpressionAssertion.isNotEqualTo(
        aStartingTime, 
        anEndingTime, 
        TimeExpressionException.TIME_SPAN_INVALID).
          assertTrue();
    startingTime = aStartingTime;
    endingTime = anEndingTime;
  }

  public LocalTime startingTime() {
    return startingTime;
  }

  public LocalTime endingTime() {
    return endingTime;
  }
  
  public boolean endsAt(LocalTime aTime) {
    return endingTime.equals(aTime);
  }

  public boolean startsAt(LocalTime aTime) {
    return startingTime.equals(aTime);
  }

  @Override
  public int compareTo(TimeSpan anotherTimePeriod) {
    return this.startingTime().compareTo(
        anotherTimePeriod.startingTime());
  }

  public boolean includes(LocalTime aTime) {
    boolean contains = true;
    contains &= 
        startingTime.isBefore(aTime) || 
        startingTime.isEqual(aTime);
    contains &= 
        endingTime.isAfter(aTime) || 
        endingTime.isEqual(aTime);
    return contains;
  }

  public static TimeSpan fromTo(
      LocalTime aStartingTime,
      LocalTime anEndingTime) {
    return new TimeSpan(
        aStartingTime, 
        anEndingTime);
  }
  
  @Override
  public String toString() {
    StringBuffer aString = new StringBuffer("De ")
        .append(this.startingTime.toString()).append(" a ")
        .append(this.endingTime.toString());
    if (this.endingTime.isBefore(this.startingTime)) {
      aString.append(" del dia siguiente");
    }
    return aString.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + endingTime.hashCode();
    result = prime * result + startingTime.hashCode();
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
    TimeSpan other = (TimeSpan) obj;
    if (!endingTime.equals(other.endingTime))
      return false;
    if (!startingTime.equals(other.startingTime))
      return false;
    return true;
  }
}
