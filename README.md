# time-expression


#Model to represent time expressions.

The whole development was done applying TDD as the development technique and DDD.

##Concrete examples of expressions:

###Non recurring:

- 1) Date expression: February the second, 2017 (DateTimeExpression.java)

###Recurring (subclasiffy PeriodicTimeExpression.java)

- 2) Daily: Every 2 days from february the second 2017 to april the fourth 2017  (DailyTimeExpression.java)
- 3) Semanalmente: Todas las semanas desde la primera semana de abril del 2017 hasta la tercera semana de mayo del 2017 ( 	WeeklyTimeExpression.java)
- 3) Weekly: Every week from april 2017 the first week until july 2017 the third week (WeeklyTimeExpression.java)
- 4) a) Monthly: Monthly every 2 months the third day. (DayOfMonthTimeExpression.java)
- 4) b) Mensualmente: Every month the third wednesday from january 2017 to april 2018. (DayOfWeekInWeekOfMonthTimeExpression.java)
- 5) Yearly: Every year on august the third day from 2016 to 2025. (YearlyTimeExpression.java)

###Composed:

- 6) Allows to combine any of the mentioned before (1 and 3 and...) (BinaryTimeExpression.java)


The root class of the hierarchy is TimeExpression. This class serves as a factory for creating instances of its subclasses, that is building an expression. 
A time expression understands among others the following messages:

-  boolean evaluate(aDateTime) > Returns whether is satisfies, that is the same to say if happens in the given date time.
-  iterator() -> returns an iterator that calculcates all the date times the time expression occurrs.
-  fromJsonString(aString) -> builds an instance from its string representation
-  asJsonString() -> serializes the expression as json string







