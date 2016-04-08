package com.eventspipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MonthDay;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.eventspipe.TimeExpressionDuration.TimeExpressionDateSpanDuration;
import com.eventspipe.TimeExpressionDuration.TimeExpressionEndsNeverDuration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class TimeExpression {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormat.forPattern("yyyy-MM-dd");

  private static final DateTimeFormatter LOCAL_TIME_FORMATTER =
      DateTimeFormat.forPattern("HH:mm:ss.SSS");

  protected TimeSpan[] timeSpans;

  public TimeExpression(TimeSpan... aListOfTimeSpans) {
    List<TimeSpan> timePeriodsList = 
        Arrays.asList(aListOfTimeSpans);
    if (timePeriodsList.size() > 1) {
      Collections.sort(timePeriodsList);
    }
    timeSpans = (TimeSpan[]) timePeriodsList.toArray();
  }

  public TimeExpression() {
  }

  protected TimeSpan[] timeSpans() {
    return timeSpans;
  }

  public static DateTimeExpression onFromUntil(
      LocalDate aDate,
      TimeSpan... timePeriods) {
    return new DateTimeExpression(aDate, timePeriods);
  }

  public static TimeExpression weeklyFromToStartsOnEndsNever(
      LocalDate aStartingDate,
      List<DayOfWeekOfWeeklyTimeExpression> daysOfweeks) {
    return new WeeklyTimeExpression(
        aStartingDate,
        daysOfweeks);
  }
  
  public static TimeExpression weeklyFromToStartsOnEndsOn(
      LocalDate aStartingDate,
      LocalDate anEndingDate,
      List<DayOfWeekOfWeeklyTimeExpression> daysOfweeks) {
    return new WeeklyTimeExpression(
        aStartingDate,
        anEndingDate,
        daysOfweeks);
  }
  
  public static DailyTimeExpression dailyEveryStartsOnEndsNever(
      Days anAmountOfDays,
      LocalDate aDate, 
      TimeSpan... timePeriods) {
    return new DailyTimeExpression(
        anAmountOfDays,
        TimeExpressionDuration.startsOnEndsNever(
            aDate),
        timePeriods);
  }
  
  public static DailyTimeExpression dailyEveryStartsOnEndsOn(
      Days anAmountOfDays,
      LocalDate aDate,
      LocalDate anotherDate,
      TimeSpan... timePeriods) {
    return new DailyTimeExpression(
        anAmountOfDays,
        TimeExpressionDuration.during(
            DateSpanReadjustment.forDailyTimeExpression(
                DateSpan.fromTo(
                    aDate, 
                    anotherDate), 
                anAmountOfDays).
                  apply()),
        timePeriods);
  }

  public static DailyTimeExpression dailyEveryStartsOnEndsAfter(
      Days anAmountOfDays,
      LocalDate aDate,
      int aNumberOfOcurrences,
      TimeSpan... timePeriods) {
    return new DailyTimeExpression(
        anAmountOfDays,
        TimeExpressionDuration.startsOnEndsAfter(
            aDate, 
            aNumberOfOcurrences, 
            (date, numberOfOccurrences) -> {
              return date.plusDays((anAmountOfDays.getDays() * numberOfOccurrences)
                  - anAmountOfDays.getDays());
            }),
        timePeriods);
  }

  public static DayOfMonthTimeExpression monthlyEveryOnStartsOnEndsNever(
      Months anAmountOfMonths,
      DayOfMonth aDayOfMonth,
      MonthOfYear aMonthOfYear,
      TimeSpan... timePeriods) {
    LocalDate aStartingDate = aMonthOfYear.dateAt(aDayOfMonth);
    return new DayOfMonthTimeExpression(
        anAmountOfMonths,
        aDayOfMonth, 
        TimeExpressionDuration.startsOnEndsNever(
            aStartingDate), 
        timePeriods);
  }

  public static DayOfMonthTimeExpression monthlyEveryOnStartsOnEndsOn(
      Months anAmountOfMonths,
      DayOfMonth aDayOfMonth,
      MonthOfYear aMonthOfYear,
      MonthOfYear anotherMonthOfYear,
      TimeSpan... timePeriods) {
    LocalDate aStartingDate = 
        aMonthOfYear.dateAt(aDayOfMonth);
    LocalDate anEndingDate = 
        anotherMonthOfYear.dateAt(aDayOfMonth);
    return new DayOfMonthTimeExpression(
        anAmountOfMonths,
        aDayOfMonth, 
        TimeExpressionDuration.during(
            DateSpanReadjustment.forDayOfMonthTimeExpression(
                DateSpan.fromTo(
                    aStartingDate, 
                    anEndingDate), 
                anAmountOfMonths).
                  apply()), 
        timePeriods);
  }

  public static DayOfMonthTimeExpression monthlyEveryOnStartsOnEndsAfter(
      Months anAmountOfMonths,
      DayOfMonth aDayOfMonth,
      MonthOfYear aMonthOfYear,
      int anAmountOfOcurrences,
      TimeSpan... timePeriods) {
    LocalDate aStartingDate =
        aMonthOfYear.dateAt(aDayOfMonth);
    return new DayOfMonthTimeExpression(
        anAmountOfMonths,
        aDayOfMonth,
        TimeExpressionDuration.startsOnEndsAfter(
            aStartingDate, 
            anAmountOfOcurrences, 
            (date, numberOfOccurrences) -> {
              return date.plusMonths((anAmountOfMonths.getMonths() * numberOfOccurrences)
                  - anAmountOfMonths.getMonths());
            }), 
        timePeriods);
  }
  

  public static DayOfWeekInWeekOfMonthTimeExpression monthlyEveryOnStartsOnEndsNever(
      Months anAmountOfMonths,
      DayOfWeek aDayOfWeek,
      WeekOfMonth aWeekOfMonth,
      MonthOfYear aMonthOfYear,
      TimeSpan... timePeriods) {
    LocalDate aStartingDate =
        aMonthOfYear.dateOfOn(
            aDayOfWeek,
            aWeekOfMonth);
    return new DayOfWeekInWeekOfMonthTimeExpression(
        anAmountOfMonths,
        aDayOfWeek,
        aWeekOfMonth,
        TimeExpressionDuration.startsOnEndsNever(
            aStartingDate), 
        timePeriods);
  }  

  public static DayOfWeekInWeekOfMonthTimeExpression monthlyEveryOnStartsOnEndsOn(
      Months anAmountOfMonths,
      DayOfWeek aDayOfWeek,
      WeekOfMonth aWeekOfMonth,
      MonthOfYear aMonthOfYear,
      MonthOfYear anotherMonthOfYear,
      TimeSpan... timePeriods) {
    LocalDate aStartingDate =
        aMonthOfYear.dateOfOn(
            aDayOfWeek,
            aWeekOfMonth);
    LocalDate anEndingDate =
        anotherMonthOfYear.dateOfOn(
            aDayOfWeek,
            aWeekOfMonth);
    return new DayOfWeekInWeekOfMonthTimeExpression(
        anAmountOfMonths,
        aDayOfWeek,
        aWeekOfMonth,
        TimeExpressionDuration.during(
            DateSpanReadjustment.forDayOfWeekInWeekOfMonthTimeExpression(
                DateSpan.fromTo(
                    aStartingDate, 
                    anEndingDate), 
                anAmountOfMonths, 
                aDayOfWeek, 
                aWeekOfMonth).
                  apply()),
        timePeriods);
  }

  public static DayOfWeekInWeekOfMonthTimeExpression monthlyEveryOnStartsOnEndsAfter(
      Months anAmountOfMonths,
      DayOfWeek aDayOfWeek,
      WeekOfMonth aWeekOfMonth,
      MonthOfYear aMonthOfYear,
      int anAmountOfOccurrences,
      TimeSpan... timePeriods) {
    LocalDate aStartingDate =
        aMonthOfYear.dateOfOn(
            aDayOfWeek,
            aWeekOfMonth);
    return new DayOfWeekInWeekOfMonthTimeExpression(
        anAmountOfMonths,
        aDayOfWeek,
        aWeekOfMonth,
        TimeExpressionDuration.startsOnEndsAfter(
            aStartingDate, 
            anAmountOfOccurrences, 
            (date, numberOfOccurrences) -> {
              return date.plusMonths((anAmountOfMonths.getMonths() * numberOfOccurrences)
                  - anAmountOfMonths.getMonths());
            }), 
        timePeriods);
  }
  
  public static YearlyTimeExpression yearlyEveryOnStartsOnEndsNever(
      Years anAmountOfYears,
      MonthDay aMonthDay,
      int aYear,
      TimeSpan... timePeriods) {
    LocalDate aStartingDate =
        new LocalDate(
            aYear,
            aMonthDay.getMonthOfYear(),
            aMonthDay.getDayOfMonth());
    return new YearlyTimeExpression(
        anAmountOfYears,
        aMonthDay,
        TimeExpressionDuration.startsOnEndsNever(
            aStartingDate),  
        timePeriods);
  }  

  public static YearlyTimeExpression yearlyEveryOnStartsOnEndsOn(
      Years anAmountOfYears,
      MonthDay aMonthDay,
      int aYear,
      int anotherYear,
      TimeSpan... timePeriods) {
    LocalDate aStartingDate =
        new LocalDate(
            aYear,
            aMonthDay.getMonthOfYear(),
            aMonthDay.getDayOfMonth());
    LocalDate anEndingDate =
        new LocalDate(
            anotherYear,
            aMonthDay.getMonthOfYear(),
            aMonthDay.getDayOfMonth());
    return new YearlyTimeExpression(
        anAmountOfYears,
        aMonthDay,
        TimeExpressionDuration.during(
            DateSpanReadjustment.forYearlyTimeExpression(
                DateSpan.fromTo(
                    aStartingDate, 
                    anEndingDate), 
                anAmountOfYears).
                  apply()),
        timePeriods);
  }

  public static YearlyTimeExpression yearlyEveryOnStartsOnEndsAfter(
      Years anAmountOfYears,
      MonthDay aMonthDay,
      int aYear,
      int anAmountOfOccurrences,
      TimeSpan... timePeriods) {
    LocalDate aStartingDate =
        new LocalDate(
            aYear,
            aMonthDay.getMonthOfYear(),
            aMonthDay.getDayOfMonth());
    return new YearlyTimeExpression(
        anAmountOfYears,
        aMonthDay,
        TimeExpressionDuration.startsOnEndsAfter(
            aStartingDate, 
            anAmountOfOccurrences, 
            (date, numberOfOccurrences) -> {
              return date.plusYears((anAmountOfYears.getYears() * numberOfOccurrences)
                  - anAmountOfYears.getYears());
            }), 
        timePeriods);
  }

  public static TimeExpression from(List<TimeExpression> timeExpressions) {
    TimeExpression aTimeExpression = NullTimeExpression.INSTANCE;
    for (TimeExpression anotherTimeExpression : timeExpressions) {
      aTimeExpression = aTimeExpression.and(anotherTimeExpression);
    }
    return aTimeExpression;
  }

  public boolean evaluate(LocalDateTime aDateTime) {
    boolean matches = this.evaluate(aDateTime.toLocalDate());
    matches &= within(aDateTime.toLocalTime());
    return matches;
  }

  public abstract boolean evaluate(LocalDate aDate);

  public abstract Iterator<DateTimeSpan> iterator();
  
  public abstract Iterator<DateTimeSpan> iteratorFromTo(
      LocalDate aDate, LocalDate anotherDate);

  public TimeExpression and(TimeExpression anotherTimeExpression) {
    return new BinaryTimeExpression(
        this,
        anotherTimeExpression);
  }

  public List<TimeExpression> subexpressions() {
    List<TimeExpression> subexpressions =
        new LinkedList<TimeExpression>();
    subexpressions.add(this);
    return subexpressions;
  }
  
  public abstract int numberOfOcurringDays();
  
  public abstract int numberOfOccurrences();

  public abstract LocalDateTime endingDateTime();
  
  public boolean endsAfter(LocalDate aDate) {
    return endingDateTime().toLocalDate().isAfter(aDate);
  }

  public abstract LocalDateTime startingDateTime();

  public LocalDate startingDate() {
    return startingDateTime().toLocalDate();
  }
  
  public LocalDate endingDate() {
    return endingDateTime().toLocalDate();
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(timeSpans);
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
    TimeExpression other = (TimeExpression) obj;
    if (!Arrays.equals(timeSpans, other.timeSpans))
      return false;
    return true;
  }

  public boolean matches(TimeExpression aTimeExpression) {
    return this.equals(aTimeExpression);
  }

  public List<TimeExpression> asList() {
    return this.subexpressions();
  }

  public void endingOn(LocalDateTime anEndingDateTime) {
  }

  protected boolean within(LocalTime aTime) {
    boolean within = false;
    for (TimeSpan aTimePeriod : timeSpans) {
      within |= aTimePeriod.includes(aTime);
    }
    return within;
  }

  public boolean conflictsWith(DateTimeSpan aDateTimeSpan) {
    boolean conflicts = false;
    conflicts |= this.evaluate(aDateTimeSpan.startingDateTime());
    conflicts |= this.evaluate(aDateTimeSpan.endingDateTime());
    return conflicts;
  }

  public boolean includes(LocalDateTime aDateTime) {
    return (
        this.startingDateTime().isBefore(aDateTime) ||
        this.startingDateTime().isEqual(aDateTime)) && (
            this.endingDateTime().isAfter(aDateTime) ||
            this.endingDateTime().isEqual(aDateTime));
  }

  public String asJsonString() {
    return asJson().toString();
  }

  public static TimeExpression fromJsonString(String aJsonString) {
    return from(new JsonParser().parse(aJsonString));
  }

  public JsonElement asJson() {
    throw new RuntimeException("Implement");
  }

  public static TimeExpression from(JsonElement aTimeExpressionAsElement) {
    if (aTimeExpressionAsElement.isJsonArray()) {
      return newBinaryFrom(aTimeExpressionAsElement.getAsJsonArray());
    } else {
      return newFrom(aTimeExpressionAsElement.getAsJsonObject());
    }
  }

  private static TimeExpression newBinaryFrom(JsonArray aTimeExpressionAsJsonArray) {
    TimeExpression timeExpression =
        newFrom(aTimeExpressionAsJsonArray.get(0).getAsJsonObject());
    for (int index = 1; index < aTimeExpressionAsJsonArray.size(); index++) {
      timeExpression = timeExpression.and(
          newFrom(aTimeExpressionAsJsonArray.get(index).getAsJsonObject()));
    }
    return timeExpression;
  }

  private static TimeSpan[] timePeriodsOf(JsonObject timeExpressionAsJsonObject) {
    JsonArray timePeriodsAsJson =
        timeExpressionAsJsonObject.get("timePeriods").getAsJsonArray();
    TimeSpan [] timePeriods = new TimeSpan[timePeriodsAsJson.size()];
    for (int index = 0; index < timePeriodsAsJson.size(); index++) {
      JsonObject timePeriodAsJson =
          timePeriodsAsJson.get(index).getAsJsonObject();
      timePeriods[index] =
          new TimeSpan(
            LOCAL_TIME_FORMATTER.parseLocalTime(
                timePeriodAsJson.get("startingTime").getAsString()),
            LOCAL_TIME_FORMATTER.parseLocalTime(
                timePeriodAsJson.get("endingTime").getAsString()));
    }
    return timePeriods;
  }

  private static TimeExpression newFrom(JsonObject timeExpressionAsJsonObject) {
    switch (timeExpressionAsJsonObject.get("type").getAsString()) {

    case "#Date":
        return onFromUntil(
            DATE_TIME_FORMATTER.parseLocalDate(
                timeExpressionAsJsonObject.get("date").getAsString()),
            timePeriodsOf(timeExpressionAsJsonObject));

      case "#Daily":
        String durationType = 
        timeExpressionAsJsonObject.get(
            "durationCriteria").getAsString();

        if (TimeExpressionEndsNeverDuration.NAME.equals(durationType)) {
          return dailyEveryStartsOnEndsNever(
              Days.days(timeExpressionAsJsonObject.get("every").getAsInt()), 
              LocalDate.parse(timeExpressionAsJsonObject.get("startingDate").getAsString()), 
              timePeriodsOf(timeExpressionAsJsonObject));
        }
        if (TimeExpressionDateSpanDuration.NAME.equals(durationType)) {
          return dailyEveryStartsOnEndsOn(
              Days.days(timeExpressionAsJsonObject.get("every").getAsInt()), 
              LocalDate.parse(timeExpressionAsJsonObject.get("startingDate").getAsString()), 
              LocalDate.parse(timeExpressionAsJsonObject.get("endingDate").getAsString()), 
              timePeriodsOf(timeExpressionAsJsonObject));
        }

      case "#Weekly":
        durationType = 
          timeExpressionAsJsonObject.get(
            "durationCriteria").getAsString();
        JsonArray dailyTimeExpressionsAsJson = timeExpressionAsJsonObject.
          get("dailyTimeExpressions").getAsJsonArray();
        
        List<DayOfWeekOfWeeklyTimeExpression> daysOfWeekOfWeeklyTimeExpressions = 
            new ArrayList<DayOfWeekOfWeeklyTimeExpression>();
        for (int index = 0; index < dailyTimeExpressionsAsJson.size(); index++) {
          JsonObject dailyTimeExpressionAsJson =
              dailyTimeExpressionsAsJson.get(index).getAsJsonObject();
          daysOfWeekOfWeeklyTimeExpressions.add(
              new DayOfWeekOfWeeklyTimeExpression(
                  DayOfWeek.valueOf(dailyTimeExpressionAsJson.get("dayOfWeek").getAsInt()),
                  Weeks.weeks(dailyTimeExpressionAsJson.get("amountOfWeeks").getAsInt()),
                  timePeriodsOf(dailyTimeExpressionAsJson)));
        }
        if (TimeExpressionDateSpanDuration.NAME.equals(durationType)) {
          return weeklyFromToStartsOnEndsOn(
              DATE_TIME_FORMATTER.parseLocalDate(
                  timeExpressionAsJsonObject.get("startingDate").getAsString()),
              DATE_TIME_FORMATTER.parseLocalDate(
                  timeExpressionAsJsonObject.get("endingDate").getAsString()),
              daysOfWeekOfWeeklyTimeExpressions);
        }
        if (TimeExpressionEndsNeverDuration.NAME.equals(durationType)) {
          return weeklyFromToStartsOnEndsNever(
              DATE_TIME_FORMATTER.parseLocalDate(
                  timeExpressionAsJsonObject.get("startingDate").getAsString()),
              daysOfWeekOfWeeklyTimeExpressions);
        }

      case "#DayInMonth":
        durationType = 
          timeExpressionAsJsonObject.get(
            "durationCriteria").getAsString();
        if (TimeExpressionEndsNeverDuration.NAME.equals(durationType)) {
          return monthlyEveryOnStartsOnEndsNever(
              Months.months(timeExpressionAsJsonObject.get("every").getAsInt()),
              DayOfMonth.valueOf(timeExpressionAsJsonObject.get("day").getAsInt()),
              new MonthOfYear(
                  timeExpressionAsJsonObject.get("startingMonth").getAsInt(),
                  timeExpressionAsJsonObject.get("startingYear").getAsInt()), 
              timePeriodsOf(timeExpressionAsJsonObject));
        }
        if (TimeExpressionDateSpanDuration.NAME.equals(durationType)) {
          return monthlyEveryOnStartsOnEndsOn(
              Months.months(timeExpressionAsJsonObject.get("every").getAsInt()), 
              DayOfMonth.valueOf(timeExpressionAsJsonObject.get("day").getAsInt()),
              new MonthOfYear(
                  timeExpressionAsJsonObject.get("startingMonth").getAsInt(),
                  timeExpressionAsJsonObject.get("startingYear").getAsInt()), 
              new MonthOfYear(
                  timeExpressionAsJsonObject.get("endingMonth").getAsInt(),
                  timeExpressionAsJsonObject.get("endingYear").getAsInt()),
              timePeriodsOf(timeExpressionAsJsonObject));
        }

      case "#DayOfWeekInWeekOfMonth":
        durationType = 
          timeExpressionAsJsonObject.get(
            "durationCriteria").getAsString();
        if (TimeExpressionEndsNeverDuration.NAME.equals(durationType)) {
          return monthlyEveryOnStartsOnEndsNever(
              Months.months(timeExpressionAsJsonObject.get("every").getAsInt()),
              DayOfWeek.valueOf(timeExpressionAsJsonObject.get("dayOfWeek").getAsInt()),
              WeekOfMonth.valueOf(timeExpressionAsJsonObject.get("weekOfMonth").getAsInt()), 
              new MonthOfYear(
                  timeExpressionAsJsonObject.get("startingMonth").getAsInt(),
                  timeExpressionAsJsonObject.get("startingYear").getAsInt()), 
              timePeriodsOf(timeExpressionAsJsonObject));
        }
        if (TimeExpressionDateSpanDuration.NAME.equals(durationType)) {
          return monthlyEveryOnStartsOnEndsOn(
              Months.months(timeExpressionAsJsonObject.get("every").getAsInt()),
              DayOfWeek.valueOf(timeExpressionAsJsonObject.get("dayOfWeek").getAsInt()),
              WeekOfMonth.valueOf(timeExpressionAsJsonObject.get("weekOfMonth").getAsInt()),
              new MonthOfYear(
                  timeExpressionAsJsonObject.get("startingMonth").getAsInt(),
                  timeExpressionAsJsonObject.get("startingYear").getAsInt()),
              new MonthOfYear(
                  timeExpressionAsJsonObject.get("endingMonth").getAsInt(),
                  timeExpressionAsJsonObject.get("endingYear").getAsInt()),
              timePeriodsOf(timeExpressionAsJsonObject));
        }

      case "#Yearly":
        durationType = 
          timeExpressionAsJsonObject.get(
            "durationCriteria").getAsString();
        if (TimeExpressionEndsNeverDuration.NAME.equals(durationType)) {
          return yearlyEveryOnStartsOnEndsNever(
              Years.years(timeExpressionAsJsonObject.get("every").getAsInt()), 
              new MonthDay(
                  timeExpressionAsJsonObject.get("month").getAsInt(),
                  timeExpressionAsJsonObject.get("day").getAsInt()), 
              timeExpressionAsJsonObject.get("startingYear").getAsInt(),
              timePeriodsOf(timeExpressionAsJsonObject));
        }
        if (TimeExpressionDateSpanDuration.NAME.equals(durationType)) {
          return yearlyEveryOnStartsOnEndsOn(
              Years.years(timeExpressionAsJsonObject.get("every").getAsInt()),
              new MonthDay(
                  timeExpressionAsJsonObject.get("month").getAsInt(),
                  timeExpressionAsJsonObject.get("day").getAsInt()),
              timeExpressionAsJsonObject.get("startingYear").getAsInt(),
              timeExpressionAsJsonObject.get("endingYear").getAsInt(),
              timePeriodsOf(timeExpressionAsJsonObject));
        }

      default:
        throw new RuntimeException(String.format(
            "Time expression not recognized for type %s.", timeExpressionAsJsonObject));
    }
  }
}
