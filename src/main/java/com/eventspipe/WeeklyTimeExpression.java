package com.eventspipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.eventspipe.TimeExpressionDuration.TimeExpressionDateSpanDuration;
import com.eventspipe.TimeExpressionDuration.TimeExpressionEndsNeverDuration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class WeeklyTimeExpression extends TimeExpression {

  private List<DailyTimeExpression> dailyTimeExpressions;

  public WeeklyTimeExpression(
      LocalDate aStartingDate,
      LocalDate anEndingDate,
      List<DayOfWeekOfWeeklyTimeExpression> theDaysOfweek) {
    assertAtLeastOneDayOfWeekSpecified(theDaysOfweek);
    assertDaysOfWeekAreUnique(theDaysOfweek);
    parseDailyTimeExpressionsFrom(
        aStartingDate, anEndingDate, theDaysOfweek);
  }

  public WeeklyTimeExpression(
      LocalDate aStartingDate,
      List<DayOfWeekOfWeeklyTimeExpression> theDaysOfweek) {
    assertAtLeastOneDayOfWeekSpecified(theDaysOfweek);
    assertDaysOfWeekAreUnique(theDaysOfweek);
    parseDailyTimeExpressionsFrom(
        aStartingDate, theDaysOfweek);
  }

  private void parseDailyTimeExpressionsFrom(
      LocalDate aStartingDate,
      List<DayOfWeekOfWeeklyTimeExpression> theDaysOfweek) {
    parseDailyTimeExpressionsFrom(
        aStartingDate,
        theDaysOfweek,
        (aDayOfWeek, aDate) -> {
          return dailyEveryStartsOnEndsNever(
              Days.days(
                  aDayOfWeek.amountOfWeeks().getWeeks() * 
                  Days.SEVEN.getDays()),
                  aDate,
              aDayOfWeek.timePeriods());
        });
  }

  private void parseDailyTimeExpressionsFrom(
      LocalDate aStartingDate,
      LocalDate anEndingDate,
      List<DayOfWeekOfWeeklyTimeExpression> theDaysOfweek) {
    parseDailyTimeExpressionsFrom(
        aStartingDate,
        theDaysOfweek,
        (aDayOfWeek, aDate) -> {
          return dailyEveryStartsOnEndsOn(
              Days.days(
                  aDayOfWeek.amountOfWeeks().getWeeks() * 
                  Days.SEVEN.getDays()),
                  aDate,
              anEndingDate,
              aDayOfWeek.timePeriods());
        });
  }
  
  private void parseDailyTimeExpressionsFrom(
      LocalDate aStartingDate,
      List<DayOfWeekOfWeeklyTimeExpression> theDaysOfweek,
      LambdaTwoParams<DayOfWeekOfWeeklyTimeExpression, 
        LocalDate, DailyTimeExpression> lambda) {
    dailyTimeExpressions = new LinkedList<DailyTimeExpression>();
    LocalDate firstWeekEndingDate = aStartingDate.plusWeeks(1);
    LocalDate aDate = aStartingDate;
    while (aDate.isBefore(firstWeekEndingDate)) {
      for (DayOfWeekOfWeeklyTimeExpression aDayOfWeek : theDaysOfweek) {
        if (aDayOfWeek.sameAs(aDate.getDayOfWeek())) {
          dailyTimeExpressions.add(lambda.apply(aDayOfWeek, aDate));
        }
      }
      aDate = aDate.plusDays(1);
    }
  }

  private void assertAtLeastOneDayOfWeekSpecified(
      List<DayOfWeekOfWeeklyTimeExpression> theDaysOfweek) {
    ExpressionAssertion.notEmptyDescribedBy(
        theDaysOfweek, 
        TimeExpressionException.DAYS_OF_WEEK_INVALID).
          assertTrue();
  }

  private void assertDaysOfWeekAreUnique(
      List<DayOfWeekOfWeeklyTimeExpression> theDaysOfweek) {
    Collections.sort(theDaysOfweek);
    for (int index = 1; index < theDaysOfweek.size(); index++) {
      ExpressionAssertion.isNotEqualTo(
          theDaysOfweek.get(index - 1).dayOfWeek(), 
          theDaysOfweek.get(index).dayOfWeek(),
          TimeExpressionException.DAY_OF_WEEK_DUPLICATED).
            assertTrue();
    }
  }

  @Override
  public int numberOfOcurringDays() {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public int numberOfOccurrences() {
    throw new UnsupportedOperationException();
  }
  
  public boolean evaluate(LocalDateTime aDateTime) {
    boolean matches = false;
    for (DailyTimeExpression aDailyTimeExpression : dailyTimeExpressions) {
      matches |= aDailyTimeExpression.evaluate(aDateTime);
    }
    return matches;
  }
  
  @Override
  public boolean evaluate(LocalDate aDate) {
    boolean matches = false;
    for (DailyTimeExpression aDailyTimeExpression : dailyTimeExpressions) {
      matches |= aDailyTimeExpression.evaluate(aDate);
    }
    return matches;
  }  
  
  @SuppressWarnings("unchecked")
  @Override
  public Iterator<DateTimeSpan> iterator() {
    List<Iterator<DateTimeSpan>> iterators = new ArrayList<>();
    for (TimeExpression aDailyTimeExpression : dailyTimeExpressions) {
      iterators.add(aDailyTimeExpression.iterator());
    }
    return IteratorUtils.collatedIterator(
        new DateTimeSpanComparator(),
        iterators.toArray(new Iterator[iterators.size()]));
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Iterator<DateTimeSpan> iteratorFromTo(
      LocalDate aDate, LocalDate anotherDate) {
    List<Iterator<DateTimeSpan>> iterators = new ArrayList<>();
    for (TimeExpression aDailyTimeExpression : dailyTimeExpressions) {
      iterators.add(aDailyTimeExpression.iteratorFromTo(aDate, anotherDate));
    }
    return IteratorUtils.collatedIterator(
        new DateTimeSpanComparator(),
        iterators.toArray(new Iterator[iterators.size()]));
  }

  @Override
  public boolean conflictsWith(DateTimeSpan aDateTimeSpan) {
    for (DailyTimeExpression aDailyTimeExpression : dailyTimeExpressions) {
      if (aDailyTimeExpression.conflictsWith(aDateTimeSpan)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public LocalDateTime endingDateTime() {
    Iterator<DailyTimeExpression> iterator = dailyTimeExpressions.iterator();
    DailyTimeExpression dailyTimeExpression = iterator.next();
    LocalDateTime endingDateTime = dailyTimeExpression.endingDateTime();
    while (iterator.hasNext()) {
      dailyTimeExpression = iterator.next();
      LocalDateTime altEndingDateTime = dailyTimeExpression.endingDateTime();
      if (altEndingDateTime.isAfter(endingDateTime)) {
        endingDateTime = altEndingDateTime;
      }
    }
    return endingDateTime;
  }

  @Override
  public LocalDateTime startingDateTime() {
    Iterator<DailyTimeExpression> iterator = dailyTimeExpressions.iterator();
    DailyTimeExpression dailyTimeExpression = iterator.next();
    LocalDateTime startingDateTime = dailyTimeExpression.startingDateTime();
    while (iterator.hasNext()) {
      dailyTimeExpression = iterator.next();
      LocalDateTime altStartingDateTime = dailyTimeExpression.startingDateTime();
      if (altStartingDateTime.isBefore(startingDateTime)) {
        startingDateTime = altStartingDateTime;
      }
    }
    return startingDateTime;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + dailyTimeExpressions.hashCode();
    return result;
  }

  @Override
  public JsonElement asJson() {
    JsonObject weeklyTimeExpressionAsJson = new JsonObject();
    weeklyTimeExpressionAsJson.addProperty("type", "#Weekly");

    LocalDateTime startingDateTime = startingDateTime();
    LocalDateTime endingDateTime = endingDateTime();
    
    if (dailyTimeExpressions.get(0).neverEnds()) {
      weeklyTimeExpressionAsJson.addProperty("durationCriteria", 
          TimeExpressionEndsNeverDuration.NAME);
      weeklyTimeExpressionAsJson.addProperty("startingDate",
          startingDateTime.toLocalDate().toString("yyyy-MM-dd"));
    } else {
      weeklyTimeExpressionAsJson.addProperty("durationCriteria", 
          TimeExpressionDateSpanDuration.NAME);
      weeklyTimeExpressionAsJson.addProperty("startingDate",
          startingDateTime.toLocalDate().toString("yyyy-MM-dd"));
      weeklyTimeExpressionAsJson.addProperty("endingDate",
          endingDateTime.toLocalDate().toString("yyyy-MM-dd"));
    }

    JsonArray dailyTimeExpressionsAsJson = new JsonArray();
    for (DailyTimeExpression aDailyTimeExpression : dailyTimeExpressions) {
      JsonObject dailyTimeExpressionAsJson = new JsonObject();
      dailyTimeExpressionAsJson.addProperty("dayOfWeek", aDailyTimeExpression.startingDateTime().getDayOfWeek());
      dailyTimeExpressionAsJson.addProperty("amountOfWeeks", aDailyTimeExpression.days().getDays() / 7);

      // time periods
      JsonArray timePeriodsAsJson = new JsonArray();
      for (TimeSpan aTimePeriod : aDailyTimeExpression.timeSpans()) {
        JsonObject timePeriodAsJson = new JsonObject();
        timePeriodAsJson.addProperty("startingTime",
            aTimePeriod.startingTime().toString("HH:mm:ss.SSS"));
        timePeriodAsJson.addProperty("endingTime",
            aTimePeriod.endingTime().toString("HH:mm:ss.SSS"));
        timePeriodsAsJson.add(timePeriodAsJson);
      }
      dailyTimeExpressionAsJson.add("timePeriods", timePeriodsAsJson);
      dailyTimeExpressionsAsJson.add(dailyTimeExpressionAsJson);
    }
    weeklyTimeExpressionAsJson.add("dailyTimeExpressions", dailyTimeExpressionsAsJson);
    return weeklyTimeExpressionAsJson;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    WeeklyTimeExpression other = (WeeklyTimeExpression) obj;
    if (!dailyTimeExpressions.equals(other.dailyTimeExpressions))
      return false;
    return true;
  }
}
