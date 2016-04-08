package com.eventspipe;

import java.util.Iterator;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class TimeExpressionIterator implements Iterator<DateTimeSpan> {

  private DateSpan dateSpan;
  private LocalDateProvider localDateProvider;
  private LocalDate currentDate;
  private TimeSpan[] timeSpans;
  private int timePeriodIterationIndex;
  private LocalDate fromDate;
  private LocalDate toDate;

  public TimeExpressionIterator(
      DateSpan aDateSpan,
      TimeSpan[] aListOfTimeSpans,
      LocalDateProvider aLocalDateProvider, 
      LocalDate aDate, 
      LocalDate anotherDate) {
    dateSpan = aDateSpan;
    timeSpans = aListOfTimeSpans;
    localDateProvider = aLocalDateProvider;
    currentDate = aDateSpan.startingDate();
    fromDate = aDate;
    toDate = anotherDate;
    timePeriodIterationIndex = 0;
    advanceTo(fromDate);
  }

  private void advanceTo(LocalDate aDate) {
    while (currentDate.isBefore(aDate)) {
      next();
    }
  }

  @Override
  public boolean hasNext() {
    boolean hasNext = 
        dateSpan.includes(currentDate) && 
        currentDate.isBefore(toDate) || currentDate.isEqual(toDate);
    return hasNext;
  }

  @Override
  public DateTimeSpan next() {
    LocalDate nextStartingDate = this.currentDate;
    LocalDate nextEndingDate = this.currentDate;
    DateTimeSpan nextDateTimeSpan =
        nextDateTimeSpan(
            nextStartingDate,
            nextEndingDate);
    timePeriodIterationIndex++;
    if (timePeriodIterationIndex == timeSpans.length) {
      this.currentDate =
          this.localDateProvider.provideNextFrom(this.currentDate);
      timePeriodIterationIndex = 0;
    }
    return nextDateTimeSpan;
  }

  private DateTimeSpan nextDateTimeSpan(
      LocalDate nextStartingDate,
      LocalDate nextEndingDate) {
    LocalTime startingTime =
        timeSpans[timePeriodIterationIndex].startingTime();
    LocalTime endingTime = timeSpans[timePeriodIterationIndex].endingTime();
    if (endingTime.isBefore(startingTime) || endingTime.isEqual(startingTime)) {
      nextEndingDate = nextEndingDate.plusDays(1);
    }
    return new DateTimeSpan(
        new LocalDateTime(
            nextStartingDate.getYear(),
            nextStartingDate.getMonthOfYear(),
            nextStartingDate.getDayOfMonth(),
            startingTime.getHourOfDay(),
            startingTime.getMinuteOfHour(),
            startingTime.getSecondOfMinute()),
        new LocalDateTime(
            nextEndingDate.getYear(),
            nextEndingDate.getMonthOfYear(),
            nextEndingDate.getDayOfMonth(),
            endingTime.getHourOfDay(),
            endingTime.getMinuteOfHour(),
            endingTime.getSecondOfMinute()));
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  public static Iterator<DateTimeSpan> on(
      PeriodicTimeExpression periodicTimeExpression, 
      LocalDate aDate,
      LocalDate anotherDate) {
    LocalDateProvider aLocalDateProvider = new LocalDateProvider() {
      @Override
      public LocalDate provideNextFrom(LocalDate aLocalDate) {
        return aLocalDate.plus(periodicTimeExpression.magnitude());
      }
    };
    return new TimeExpressionIterator(
        periodicTimeExpression.dateSpan(),
        periodicTimeExpression.timeSpans(),
        aLocalDateProvider, 
        aDate, 
        anotherDate);
  }

  public static Iterator<DateTimeSpan> on(
      final DayOfWeekInWeekOfMonthTimeExpression dayOfWeekInWeekOfMonthTimeExpression, 
      LocalDate aDate,
      LocalDate anotherDate) {
    final DateSpan aDateSpan = dayOfWeekInWeekOfMonthTimeExpression.dateSpan();
    LocalDateProvider aLocalDateProvider = new LocalDateProvider() {
      @Override
      public LocalDate provideNextFrom(LocalDate aDate) {
        for (int anIndex = 0; anIndex <
            dayOfWeekInWeekOfMonthTimeExpression.months().getMonths(); anIndex++) {
          aDate = aDate.plusWeeks(4);
          if (!dayOfWeekInWeekOfMonthTimeExpression.matchesWeekOfMonth(aDate)) {
            aDate = aDate.plusWeeks(1);
          }
        }
        return aDate;
      }
    };
    return new TimeExpressionIterator(
        aDateSpan,
        dayOfWeekInWeekOfMonthTimeExpression.timeSpans(),
        aLocalDateProvider, 
        aDate,
        anotherDate);
  }
  
  interface LocalDateProvider {
    LocalDate provideNextFrom(LocalDate aLocalDate);
  }
}
