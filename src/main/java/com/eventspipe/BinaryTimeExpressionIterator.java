package com.eventspipe;

import java.util.Iterator;
import java.util.List;

public class BinaryTimeExpressionIterator implements Iterator<DateTimeSpan> {

  private Iterator<DateTimeSpan>[] iterators;

  private int currentIndex;

  public BinaryTimeExpressionIterator(List<TimeExpression> timeExpressions) {
    initialize(timeExpressions);
  }

  @SuppressWarnings("unchecked")
  private void initialize(List<TimeExpression> timeExpressions) {
    this.iterators = new Iterator[timeExpressions.size()];
    for (int anIndex = 0; anIndex < timeExpressions.size(); anIndex++) {
      iterators[anIndex] = timeExpressions.get(anIndex).iterator();
    }
    this.currentIndex = 0;
  }

  @Override
  public boolean hasNext() {
    return this.iterators[currentIndex].hasNext();
  }

  @Override
  public DateTimeSpan next() {
    DateTimeSpan aLocalDate = this.iterators[currentIndex].next();
    if (currentIndex == (iterators.length - 1)) {
      this.currentIndex = 0;
    } else {
      this.currentIndex++;
    }
    return aLocalDate;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("not implemented");
  }
}
