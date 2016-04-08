package com.eventspipe;

import org.joda.time.Days;

import com.eventspipe.TimeExpressionDuration.TimeExpressionDateSpanDuration;
import com.eventspipe.TimeExpressionDuration.TimeExpressionEndsNeverDuration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DailyTimeExpression extends PeriodicTimeExpression {

  public static final String TYPE_NAME = "DailyTimeExpression";

  public DailyTimeExpression(
      Days days,
      TimeExpressionDuration theDuration,
      TimeSpan[] timePeriods) {
    super(days, 
        theDuration,
        timePeriods);
  }

  public Days days() {
    return (Days) magnitude();
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  public JsonElement asJson() {
    JsonObject timeExpressionAsJson = new JsonObject();
    timeExpressionAsJson.addProperty("type", "#Daily");
    timeExpressionAsJson.addProperty("every", days().getDays());
    timeExpressionAsJson.addProperty("durationCriteria", duration.name());
    if (TimeExpressionEndsNeverDuration.NAME.equals(duration.name())) {
      timeExpressionAsJson.addProperty("startingDate",
          duration.startingDate().toString("yyyy-MM-dd"));
    }
    if (TimeExpressionDateSpanDuration.NAME.equals(duration.name())) {
      timeExpressionAsJson.addProperty("startingDate",
          duration.startingDate().toString("yyyy-MM-dd"));
      timeExpressionAsJson.addProperty("endingDate",
          duration.endingDate().toString("yyyy-MM-dd"));
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
