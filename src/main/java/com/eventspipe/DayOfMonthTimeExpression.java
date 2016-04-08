package com.eventspipe;

import org.joda.time.LocalDate;
import org.joda.time.Months;

import com.eventspipe.TimeExpressionDuration.TimeExpressionDateSpanDuration;
import com.eventspipe.TimeExpressionDuration.TimeExpressionEndsNeverDuration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DayOfMonthTimeExpression extends PeriodicTimeExpression {

  public static final String TYPE_NAME = "DayOfMonthTimeExpression";

  private DayOfMonth dayOfMonth;

  public DayOfMonthTimeExpression(
      Months months,
      DayOfMonth aDayOfMonth,
      TimeExpressionDuration theDuration, 
      TimeSpan[] timePeriods) {
    super(months, theDuration, timePeriods);
    this.dayOfMonth = aDayOfMonth;
  }
  
  @Override
  public boolean doEvaluate(LocalDate aDate) {
    return DayOfMonth.valueOf(aDate.getDayOfMonth()).
          equals(dayOfMonth);
  }

  public Months months() {
    return (Months) magnitude();
  }

  public DayOfMonth dayOfMonth() {
    return dayOfMonth;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + dayOfMonth.hashCode();
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
    DayOfMonthTimeExpression other = (DayOfMonthTimeExpression) obj;
    if (!dayOfMonth.equals(other.dayOfMonth))
      return false;
    return true;
  }

  @Override
  public JsonElement asJson() {
    JsonObject timeExpressionAsJson = new JsonObject();
    timeExpressionAsJson.addProperty("type", "#DayInMonth");
    timeExpressionAsJson.addProperty("every", months().getMonths());
    timeExpressionAsJson.addProperty("day", dayOfMonth.index());
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
