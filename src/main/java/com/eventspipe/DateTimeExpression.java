package com.eventspipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DateTimeExpression extends TimeExpression {

  public static final String TYPE_NAME = "DateTimeExpression";

  private LocalDate date;

  public DateTimeExpression(LocalDate aDate, TimeSpan[] timePeriods) {
    super(timePeriods);
    this.date = aDate;
  }

  public LocalDate date() {
    return this.date;
  }
  
  @Override
  public int numberOfOcurringDays() {
    return 1;
  }
  
  @Override
  public int numberOfOccurrences() {
    return numberOfOcurringDays() * timeSpans.length;
  }

  @Override
  public LocalDateTime endingDateTime() {
    return this.date.toLocalDateTime(
        timeSpans() [timeSpans().length - 1].endingTime());
  }

  @Override
  public LocalDateTime startingDateTime() {
    return this.date.toLocalDateTime(timeSpans()[0].startingTime());
  }

  @Override
  public void endingOn(LocalDateTime anEndingDateTime) {
    this.timeSpans[timeSpans.length - 1] =
        TimeSpan.fromTo(
            this.timeSpans[timeSpans.length - 1].startingTime(),
            anEndingDateTime.toLocalTime());
  }

  @Override
  public Iterator<DateTimeSpan> iterator() {
    LocalDate startingDate = this.date;
    LocalDate endingDate = this.date;
    LocalTime startingTime = timeSpans()[0].startingTime();
    LocalTime endingTime = timeSpans()[0].endingTime();

    if (endingTime.isBefore(startingTime) || endingTime.isEqual(startingTime)) {
      endingDate = endingDate.plusDays(1);
    }
    DateTimeSpan dateTimeSpan =
        new DateTimeSpan(
            new LocalDateTime(
                startingDate.getYear(), startingDate.getMonthOfYear(),
                startingDate.getDayOfMonth(), startingTime.getHourOfDay(),
                startingTime.getMinuteOfHour(), startingTime.getSecondOfMinute()),
            new LocalDateTime(
                endingDate.getYear(), endingDate.getMonthOfYear(),
                endingDate.getDayOfMonth(), endingTime.getHourOfDay(),
                endingTime.getMinuteOfHour(), endingTime.getSecondOfMinute()));
    return Arrays.asList(dateTimeSpan).iterator();
  }
  
  @Override
  public Iterator<DateTimeSpan> iteratorFromTo(
      LocalDate aDate,
      LocalDate anotherDate) {
    Iterator<DateTimeSpan> iterator =
        new ArrayList<DateTimeSpan>().iterator();
    if (DateSpan.fromTo(aDate, anotherDate).includes(aDate)) {
      iterator = iterator();
    }
    return iterator;
  }

  @Override
  public boolean evaluate(LocalDate aDate) {
    boolean matches = true;
    matches &= this.date.equals(aDate);
    return matches;
  }

  @Override
  public String toString() {
    return new StringBuffer("El dia ").
        append(this.date.toString()).append(" ").
        append(timeSpans().toString()).toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * date.hashCode();
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
    DateTimeExpression other = (DateTimeExpression) obj;
    if (!date.equals(other.date))
      return false;
    return true;
  }

  @Override
  public JsonElement asJson() {
    JsonObject timeExpressionAsJson = new JsonObject();
    timeExpressionAsJson.addProperty("type", "#Date");
    timeExpressionAsJson.addProperty("date",
        date.toString("yyyy-MM-dd"));
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
