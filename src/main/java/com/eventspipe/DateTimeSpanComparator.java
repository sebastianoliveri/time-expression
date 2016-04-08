package com.eventspipe;

import java.util.Comparator;

public class DateTimeSpanComparator implements Comparator<DateTimeSpan> {

  @Override
  public int compare(DateTimeSpan o1, DateTimeSpan o2) {
    return (o1.startingDateTime().toLocalDate()).
        compareTo(o2.startingDateTime().toLocalDate());
  }
}
