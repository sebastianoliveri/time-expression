package com.eventspipe;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class BinaryTimeExpression extends TimeExpression {

  public static final String DATE_TIME_CONFLICT = "DATE_TIME_CONFLICT";

  public static final String TYPE_NAME = "BinaryTimeExpression";

  private TimeExpression leftExpression;

  private TimeExpression rightExpression;

  public BinaryTimeExpression(
      TimeExpression aTimeExpression,
      TimeExpression anotherTimeExpression) {
//    assertNoConflictsBetweenAnd(
//        aTimeExpression,
//        anotherTimeExpression);
    this.leftExpression = aTimeExpression;
    this.rightExpression = anotherTimeExpression;
  }

  private void assertNoConflictsBetweenAnd(
      TimeExpression aTimeExpression,
      TimeExpression anotherTimeExpression) {
    Iterator<DateTimeSpan> anotherTimeExpressionIterator =
        anotherTimeExpression.iterator();
    while (anotherTimeExpressionIterator.hasNext()) {
      DateTimeSpan anotherTimeExpressionOcurrence =
          anotherTimeExpressionIterator.next();
      if (aTimeExpression.conflictsWith(anotherTimeExpressionOcurrence)) {
        throw new TimeExpressionException(DATE_TIME_CONFLICT);
      }
    }
  }

  @Override
  public boolean evaluate(LocalDateTime aDateTime) {
    boolean result = false;
    result |= this.leftExpression.evaluate(aDateTime);
    result |= this.rightExpression.evaluate(aDateTime);
    return result;
  }

  @Override
  public boolean evaluate(LocalDate aDate) {
    boolean result = false;
    result |= this.leftExpression.evaluate(aDate);
    result |= this.rightExpression.evaluate(aDate);
    return result;
  }

  @Override
  public List<TimeExpression> subexpressions() {
    List<TimeExpression> subexpressions =
        new LinkedList<TimeExpression>();
    subexpressions.addAll(this.leftExpression.subexpressions());
    subexpressions.addAll(this.rightExpression.subexpressions());
    return subexpressions;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Iterator<DateTimeSpan> iterator() {
    return IteratorUtils.collatedIterator(
        new DateTimeSpanComparator(),
        this.leftExpression.iterator(),
        this.rightExpression.iterator());
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Iterator<DateTimeSpan> iteratorFromTo(
      LocalDate aDate, LocalDate anotherDate) {
    return IteratorUtils.collatedIterator(
        new DateTimeSpanComparator(),
        this.leftExpression.iteratorFromTo(aDate, anotherDate),
        this.rightExpression.iteratorFromTo(aDate, anotherDate));
  }

  public TimeExpression leftExpression() {
    return leftExpression;
  }

  public TimeExpression rightExpression() {
    return rightExpression;
  }

  @Override
  public LocalDateTime endingDateTime() {
    LocalDateTime leftExpressionLastOccurrence =
        leftExpression.endingDateTime();
    LocalDateTime rightExpressionLastOccurrence =
        rightExpression.endingDateTime();
    if (leftExpressionLastOccurrence.isBefore(rightExpressionLastOccurrence)) {
      return rightExpressionLastOccurrence;
    } else {
      return leftExpressionLastOccurrence;
    }
  }

  @Override
  public int numberOfOcurringDays() {
    int numberOfOccurreneces = 0;
    numberOfOccurreneces = 
        numberOfOccurreneces + 
        leftExpression.numberOfOcurringDays();
    numberOfOccurreneces = 
        numberOfOccurreneces + 
        rightExpression.numberOfOcurringDays();
    return numberOfOccurreneces;
  }
  
  @Override
  public int numberOfOccurrences() {
    int numberOfOccurreneces = 0;
    numberOfOccurreneces = 
        numberOfOccurreneces + 
        leftExpression.numberOfOccurrences();
    numberOfOccurreneces = 
        numberOfOccurreneces + 
        rightExpression.numberOfOccurrences();
    return numberOfOccurreneces;
  }
  
  @Override
  public LocalDateTime startingDateTime() {
    LocalDateTime leftExpressionLastOccurrence =
        leftExpression.startingDateTime();
    LocalDateTime rightExpressionLastOccurrence =
        rightExpression.startingDateTime();
    if (leftExpressionLastOccurrence.isBefore(rightExpressionLastOccurrence)) {
      return leftExpressionLastOccurrence;
    } else {
      return rightExpressionLastOccurrence;
    }
  }

  @Override
  public boolean matches(TimeExpression aTimeExpression) {
    return this.equals(aTimeExpression)
        || this.subexpressions().contains(aTimeExpression);
  }

  @Override
  public List<TimeExpression> asList() {
    return this.subexpressions();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + leftExpression.hashCode();
    result = prime * result + rightExpression.hashCode();
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
    BinaryTimeExpression other = (BinaryTimeExpression) obj;
    if (!leftExpression.equals(other.leftExpression))
      return false;
    if (!rightExpression.equals(other.rightExpression))
      return false;
    return true;
  }

  @Override
  public JsonElement asJson() {
    JsonArray timeExpressionsAsJson = new JsonArray();
    List<TimeExpression> timeExpressions = subexpressions();
    for (TimeExpression timeExpression : timeExpressions) {
        timeExpressionsAsJson.add(timeExpression.asJson());
      }
    return timeExpressionsAsJson;
  }
}
