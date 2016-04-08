package com.eventspipe;

public class TimeExpressionException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public static final TimeExpressionException DAYS_AMOUNT_INVALID = 
      new TimeExpressionException("DAYS_AMOUNT_INVALID");

  public static final TimeExpressionException DATE_SPAN_INVALID = 
      new TimeExpressionException("DATE_SPAN_INVALID");

  public static final TimeExpressionException TIME_SPAN_INVALID = 
      new TimeExpressionException("TIME_SPAN_INVALID");

  public static final TimeExpressionException ENDING_DATE_INVALID = 
      new TimeExpressionException("ENDING_DATE_INVALID");

  public static final TimeExpressionException DAYS_OF_WEEK_INVALID = 
      new TimeExpressionException("DAYS_OF_WEEK_INVALID");

  public static final TimeExpressionException DAY_OF_WEEK_DUPLICATED = 
      new TimeExpressionException("DAY_OF_WEEK_DUPLICATED");

  public static final TimeExpressionException MONTH_INVALID = 
      new TimeExpressionException("MONTH_INVALID");

  public static final TimeExpressionException YEAR_INVALID = 
      new TimeExpressionException("YEAR_INVALID");

  public static final TimeExpressionException DAY_OF_WEEK_INVALID = 
      new TimeExpressionException("DAY_OF_WEEK_INVALID");

  public static final TimeExpressionException WEEK_OF_MONTH_INVALID = 
      new TimeExpressionException("WEEK_OF_MONTH_INVALID");

  public static final TimeExpressionException DAY_OF_MONTH_INVALID = 
      new TimeExpressionException("DAY_OF_MONTH_INVALID");

  public static final TimeExpressionException EVERY_AMOUNT_INVALID = 
      new TimeExpressionException("EVERY_AMOUNT_INVALID");
  
  public TimeExpressionException(String aFailureMessage) {
    super(aFailureMessage);
  }
}
