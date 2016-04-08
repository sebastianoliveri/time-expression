package com.eventspipe;

import org.joda.time.LocalDate;

public abstract class TimeExpressionDuration {

  public abstract boolean includes(LocalDate aDate);

  public abstract LocalDate endingDate();
  
  public abstract LocalDate startingDate();
  
  public abstract DateSpan dateSpan();
  
  public abstract int numberOfOcurringDays(
      PeriodicTimeExpression aPeriodicTimeExpression);
  
  public abstract int numberOfOccurrences(
      PeriodicTimeExpression aPeriodicTimeExpression);
  
  public abstract String name();
  
  public abstract boolean neverEnds();

  public static class TimeExpressionEndsNeverDuration extends
      TimeExpressionDuration {

    public static final String NAME = "ENDS_NEVER";
    
    private LocalDate startingDate;

    public TimeExpressionEndsNeverDuration(LocalDate aStartingDate) {
      startingDate = aStartingDate;
    }

    @Override
    public boolean includes(LocalDate aDate) {
      return aDate.isEqual(startingDate) || aDate.isAfter(startingDate);
    }

    @Override
    public LocalDate endingDate() {
      return new LocalDate(9999, 1, 1);
    }

    @Override
    public LocalDate startingDate() {
      return startingDate;
    }

    @Override
    public DateSpan dateSpan() {
      return DateSpan.fromTo(
          startingDate(), 
          endingDate());
    }
    
    @Override
    public int numberOfOcurringDays(
        PeriodicTimeExpression aPeriodicTimeExpression) {
      return Integer.MAX_VALUE;
    }
    
    @Override
    public int numberOfOccurrences(
        PeriodicTimeExpression aPeriodicTimeExpression) {
      return Integer.MAX_VALUE;
    }
    
    @Override
    public String name() {
      return NAME;
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + startingDate.hashCode();
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
      TimeExpressionEndsNeverDuration other = 
          (TimeExpressionEndsNeverDuration) obj;
      if (!startingDate.equals(other.startingDate))
        return false;
      return true;
    }

    @Override
    public boolean neverEnds() {
      return true;
    }
  }

  public static class TimeExpressionOccurrencesBasedDuration extends
      TimeExpressionDuration {
    
    public static final String NAME = "OCCURRENCES";

    private LocalDate startingDate;

    private int numberOfRepeatingDays;

    private LambdaTwoParams<LocalDate, Integer, LocalDate> endingDateCalculator;

    public TimeExpressionOccurrencesBasedDuration(
        LocalDate aStartingDate,
        int aNumberOfRepeatingDays,
        LambdaTwoParams<LocalDate, Integer, LocalDate> anEndingDateCalculator) {
      startingDate = aStartingDate;
      numberOfRepeatingDays = aNumberOfRepeatingDays;
      endingDateCalculator = anEndingDateCalculator;
    }

    @Override
    public boolean includes(LocalDate aDate) {
      return DateSpan.fromTo(
          startingDate, 
          endingDate()).
            includes(aDate);
    }

    @Override
    public LocalDate endingDate() {
      LocalDate endingDate = 
          endingDateCalculator.apply(
            startingDate,
            numberOfRepeatingDays);
      return endingDate;
    }

    @Override
    public LocalDate startingDate() {
      return startingDate;
    }
    
    @Override
    public DateSpan dateSpan() {
      return DateSpan.fromTo(
          startingDate, 
          endingDate());
    }

    @Override
    public int numberOfOcurringDays(
        PeriodicTimeExpression aPeriodicTimeExpression) {
      return numberOfRepeatingDays;
    }
    
    @Override
    public int numberOfOccurrences(
        PeriodicTimeExpression aPeriodicTimeExpression) {
      return numberOfOcurringDays(aPeriodicTimeExpression) * 
          aPeriodicTimeExpression.timeSpans().length;
    }
    
    @Override
    public String name() {
      return NAME;
    }
    
    @Override
    public boolean neverEnds() {
      return false;
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + numberOfRepeatingDays;
      result = prime * result + startingDate.hashCode();
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
      TimeExpressionOccurrencesBasedDuration other = 
          (TimeExpressionOccurrencesBasedDuration) obj;
      if (numberOfRepeatingDays != other.numberOfRepeatingDays)
        return false;
      if (!startingDate.equals(other.startingDate))
        return false;
      return true;
    }
  }

  public static class TimeExpressionDateSpanDuration extends
      TimeExpressionDuration {

    public static final String NAME = "DATE_SPAN";
    
    private DateSpan dateSpan;

    public TimeExpressionDateSpanDuration(DateSpan aDateSpan) {
      dateSpan = aDateSpan;
    }

    @Override
    public boolean includes(LocalDate aDate) {
      return dateSpan.includes(aDate);
    }

    @Override
    public LocalDate endingDate() {
      return dateSpan.endingDate();
    }

    @Override
    public LocalDate startingDate() {
      return dateSpan.startingDate();
    }
    
    @Override
    public DateSpan dateSpan() {
      return dateSpan;
    }

    @Override
    public int numberOfOcurringDays(
        PeriodicTimeExpression aPeriodicTimeExpression) {
      int difference = 
          TimeDifferenceCalculation.
            differenceBetween(
                aPeriodicTimeExpression.magnitude().getClass(), 
              startingDate(),
              endingDate());
      return (difference / aPeriodicTimeExpression.magnitude().getValue(0)) + 1;
    }
    
    @Override
    public int numberOfOccurrences(
        PeriodicTimeExpression aPeriodicTimeExpression) {
      return numberOfOcurringDays(aPeriodicTimeExpression) * 
          aPeriodicTimeExpression.timeSpans().length;
    }

    @Override
    public String name() {
      return NAME;
    }
    
    @Override
    public boolean neverEnds() {
      return false;
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + dateSpan.hashCode();
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
      TimeExpressionDateSpanDuration other = 
          (TimeExpressionDateSpanDuration) obj;
      if (!dateSpan.equals(other.dateSpan))
        return false;
      return true;
    }
  }

  public static TimeExpressionDuration during(DateSpan aDateSpan) {
    return new TimeExpressionDateSpanDuration(aDateSpan);
  }

  public static TimeExpressionDuration startsOnEndsAfter(
      LocalDate aDate, 
      int aNumberOfRepeatingDays, 
      LambdaTwoParams<LocalDate, Integer, LocalDate> endingDateCalculator) {
    return new TimeExpressionOccurrencesBasedDuration(aDate,
        aNumberOfRepeatingDays, endingDateCalculator);
  }

  public static TimeExpressionDuration startsOnEndsNever(LocalDate aDate) {
    return new TimeExpressionEndsNeverDuration(aDate);
  }
}
