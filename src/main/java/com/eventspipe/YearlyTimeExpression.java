package com.eventspipe;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.MonthDay;
import org.joda.time.Years;

import com.eventspipe.TimeExpressionDuration.TimeExpressionDateSpanDuration;
import com.eventspipe.TimeExpressionDuration.TimeExpressionEndsNeverDuration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class YearlyTimeExpression extends PeriodicTimeExpression {

  public static final String TYPE_NAME = "YearlyTimeExpression";

  private MonthDay monthDay;

  public YearlyTimeExpression(
      Years years,
      MonthDay aMonthDay,
      TimeExpressionDuration theDuration, 
      TimeSpan[] timePeriods) {
    super(years, theDuration, timePeriods);
    this.monthDay = aMonthDay;
  }
  
  public Days days() {
    return (Days) magnitude();
  }

  @Override
  public boolean doEvaluate(LocalDate aDate) {
    return monthDay.equals(
        new MonthDay(
            aDate.getMonthOfYear(),
            aDate.getDayOfMonth()));
  }

  public MonthDay monthDay() {
    return monthDay;
  }

  public Years years() {
    return (Years) magnitude();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + monthDay.hashCode();
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
    YearlyTimeExpression other = (YearlyTimeExpression) obj;
    if (!monthDay.equals(other.monthDay))
      return false;
    return true;
  }

  @Override
  public JsonElement asJson() {
    JsonObject timeExpressionAsJson = new JsonObject();
    timeExpressionAsJson.addProperty("type", "#Yearly");
    timeExpressionAsJson.addProperty("every", years().getYears());
    timeExpressionAsJson.addProperty("day", monthDay.getDayOfMonth());
    timeExpressionAsJson.addProperty("month", monthDay.getMonthOfYear());
    timeExpressionAsJson.addProperty("durationCriteria", duration.name());
    
    if (TimeExpressionEndsNeverDuration.NAME.equals(duration.name())) {
      timeExpressionAsJson.addProperty("startingYear",
          dateSpan().startingDate().getYear());
    }
    if (TimeExpressionDateSpanDuration.NAME.equals(duration.name())) {
      timeExpressionAsJson.addProperty("startingYear",
          dateSpan().startingDate().getYear());
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
