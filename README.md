# time-expression

Modelo para representar expresiones temporales.
El desarrollo fue realizado 100% con TDD y todos los tests se encuentran en time-expression/src/test/java/com/eventspipe.

Ejemplos concretos de expresiones que se pueden crear:

No recurrentes:
1) Una fecha concreta: el dia 2 de febrero del 2017 (DateTimeExpression.java)

Recurrentes (subclasifican PeriodicTimeExpression.java)
2) Diariamente: cada 2 dias desde el 2 de febrero del 2017 hasta el 30 de marzo del 2017 (DailyTimeExpression.java)
3) Semanalmente: Todas las semanas desde la primera semana de abril del 2017 hasta la tercera semana de mayo del 2017 ( 	WeeklyTimeExpression.java)
4) a) Mensualmente: Cada 2 meses el dia 3. (DayOfMonthTimeExpression.java)
4) b) Mensualmente: Todos los meses el tercer viernes de cada mes. (DayOfWeekInWeekOfMonthTimeExpression.java)
5) Anualmente: Todos los años el dia 7 de agosto. (YearlyTimeExpression.java)

Compuestas:
6) Permite componer combinaciones de las anteriores mencionadas (1 Y 3 Y...) (BinaryTimeExpression.java)

Entre parentesis describo la clase que modela cada una de esas expresiones.

La clase raiz de la jerarquia es "TimeExpression.java". Esta clase actua como factory para crear desde el "exterior" instancias de sus diferentes implementaciones, es decir, de alguna expresion.
Una TimeExpression sabe responder dos mensajes principales que son

1) boolean evaluate(aDateTime) > Retorna si satisface, es decir, ocurre en una fecha-hora-minutos.
2) iterator() -> retorna un iterador que calcula todas las fechas-hora-dia que ocurre la expression.
3) fromJsonString(aString) -> construye una instancia a partir de su representacion json
4) asJsonString() -> serializa la expresion en un json

Todos estos mensajes principales y tambien otros se resuelven polimorficamente en esta jerarquia de clases.





