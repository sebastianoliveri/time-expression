package com.eventspipe;

import java.util.Iterator;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import com.eventspipe.TimeExpressionDuration.TimeExpressionDateSpanDuration;
import com.eventspipe.TimeExpressionDuration.TimeExpressionEndsNeverDuration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DayOfWeekInWeekOfMonthTimeExpression extends
    PeriodicTimeExpression {

  public static final String TYPE_NAME = "DayOfWeekInWeekOfMonthTimeExpression";

  private WeekOfMonth weekOfMonth;

  private DayOfWeek dayOfWeek;

  public DayOfWeekInWeekOfMonthTimeExpression(
      Months months,
      DayOfWeek aDayOfWeek,
      WeekOfMonth aWeekOfMonth,
      TimeExpressionDuration theDuration, 
      TimeSpan[] timePeriods) {
    super(months, theDuration, timePeriods);
    this.weekOfMonth = aWeekOfMonth;
    this.dayOfWeek = aDayOfWeek;
  }
  
  @Override
  public boolean doEvaluate(LocalDate aDate) {
    return matchesDayOfWeek(aDate) && matchesWeekOfMonth(aDate);
  }

  public boolean matchesDayOfWeek(LocalDate aDate) {
    return DayOfWeek.valueOf(aDate.getDayOfWeek()).equals(dayOfWeek);
  }

  public boolean matchesWeekOfMonth(LocalDate aDate) {
    if (WeekOfMonth.LAST.equals(weekOfMonth)) {
      return Days.days(aDate.getDayOfMonth()).plus(Days.SEVEN).
          isGreaterThan(Days.days(aDate.dayOfMonth().getMaximumValue()));
    } else {
      int lastDayOfWeek = weekOfMonth.index() * DateTimeConstants.DAYS_PER_WEEK;
      int firstDayOfWeek = lastDayOfWeek - DateTimeConstants.DAYS_PER_WEEK;
      return firstDayOfWeek < aDate.getDayOfMonth()
          && aDate.getDayOfMonth() <= lastDayOfWeek;
    }
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
  
  public WeekOfMonth weekOfMonth() {
    return this.weekOfMonth;
  }

  public DayOfWeek dayOfWeek() {
    return dayOfWeek;
  }

  public Months months() {
    return (Months) magnitude();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + dayOfWeek.hashCode();
    result = prime * result + weekOfMonth.hashCode();
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
    DayOfWeekInWeekOfMonthTimeExpression other =
        (DayOfWeekInWeekOfMonthTimeExpression) obj;
    if (!dayOfWeek.equals(other.dayOfWeek))
      return false;
    if (!weekOfMonth.equals(other.weekOfMonth))
      return false;
    return true;
  }

  @Override
  public JsonElement asJson() {
    JsonObject timeExpressionAsJson = new JsonObject();
    timeExpressionAsJson.addProperty("type", "#DayOfWeekInWeekOfMonth");
    timeExpressionAsJson.addProperty("every", months().getMonths());
    timeExpressionAsJson.addProperty("dayOfWeek", dayOfWeek.index());
    timeExpressionAsJson.addProperty("weekOfMonth", weekOfMonth.index());
    timeExpressionAsJson.addProperty("durationCriteria", duration.name());
    
    if (TimeExpressionEndsNeverDuration.NAME.equals(duration.name())) {
      timeExpressionAsJson.addProperty("startingMonth",
          dateSpan().startingDate().getMonthOfYear());
      timeExpressionAsJson.addProperty("startingYear",
          dateSpan().startingDate().getYear());
    }
    if (TimeExpressionDateSpanDuration.NAME.equals(duration.name())) {
      timeExpressionAsJson.addProperty("startingMonth",
          dateSpan().startingDate().getMonthOfYear());
      timeExpressionAsJson.addProperty("startingYear",
          dateSpan().startingDate().getYear());
      timeExpressionAsJson.addProperty("endingMonth",
          dateSpan().endingDate().getMonthOfYear());
      timeExpressionAsJson.addProperty("endingYear",
          dateSpan().endingDate().getYear());
    }

    JsonArray timePeriodsAsJson = new JsonArray();
    for (TimeSpan aTimePeriod : timeSpans()) {
      JsonObject timePeriodAsJson = new JsonObject();
      timePeriodAsJson.addProperty("startingTime",
          aTimePeriod.startingTime().toString("HH:mm:ss.SSS"));
      timePeriodAsJson.addProperty("endingTime",
          aTimePeriod.endingTime().toString("HH:mm:ss.SSS"));
      timePeriodsAsJson.add(timePeriodAsJson);
    }
    timeExpressionAsJson.add("timePeriods", timePeriodsAsJson);
    return timeExpressionAsJson;
  }
}
