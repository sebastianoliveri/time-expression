# time-expression


#Model to represent time expressions.

The whole development was done by adhering to domain driven design building blocks and driven completely from test cases.

##Concrete examples of expressions:

###Non recurring:

- Date expression: February the second, 2017 (DateTimeExpression.java)

###Recurring (subclasiffy PeriodicTimeExpression.java)

- Daily: Every 2 days from february the second 2017 to april the fourth 2017  (DailyTimeExpression.java)
- Weekly: Every week from april 2017 the first week until july 2017 the third week (WeeklyTimeExpression.java)
- Monthly in day of month: Monthly every 2 months the third day from january 2017 to april 2018 (DayOfMonthTimeExpression.java)
- Monthly in day of week: Every month the third wednesday from january 2017 to april 2018 (DayOfWeekInWeekOfMonthTimeExpression.java)
- Yearly: Every year on august the third day from 2016 to 2025 (YearlyTimeExpression.java)

###Composed:

- Allows to combine any of the mentioned before (1 and 3 and...) (BinaryTimeExpression.java)


The root class of the hierarchy is TimeExpression. This class serves as a factory for creating immutable instances of its subclasses.
A time expression understands among others the following messages:

-  boolean evaluate(aDateTime) > Returns whether is satisfies, that is the same to say if happens in the given date time.
-  iterator() -> returns an iterator that calculcates all the date times the time expression occurrs.
-  fromJsonString(aString) -> builds an instance from its string representation
-  asJsonString() -> serializes the expression as json string







