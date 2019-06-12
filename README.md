# Linq based query library for java
This is a library for executing queries on Collections in java in a declarative fashion like with linq in C# that I wrote in my free time. It supports the following operations:
 - Filtering
 - Ordering by multiple conditions and directions (Comparable items or by a comparator)
 - Finding extremes (minimum/maximum)
 - Summing, Averaging, Counting elements that fit a condition
 - Skipping/Taking an amount of elements from the front or end of the collection
 - Joining and Cartesian product
 
## Usage
Let's assume we have a collection with the following items that we want to query:
```java
public class Person {
    public String name;
    public int age;
}

public class Car {
    public String ownerName;
    public String licensePlate;
    public int power;
}
```
### Creating a query
A query can be created by using the Linq class's static method called from. This initializes a QueryBuilder with the collection given as a parameter.
```java
QueryBuilder<Person> query = Linq.from(people);
```

### Filtering
If you want to filter the people (for example those, who are younger than 20 years) you can do it this way:
```java
query.where(p -> p.age < 20);
```
Note that the selectors work if the age field is private and you use the p.getAge() accessor instead of p.age directly.

### Ordering
You can also order your collection by using orderBy, and a selector which selects the field by which you want to order.
```java
query.orderBy(p -> p.age);
```
Note that ordering only takes place when skip/take/select/toList/toMap/etc. is called. orderBy and thenBy only queues the orderings. For that reason ordering is encouraged to be at the end of your statement.

### Projection
You can also project elements into new elements with the select method. You can select specific fields, or you can select a subset of fields into a new object.
```java
public class CarWithoutOwner {
    public String licensePlate;
    public int power;
}

List<String> licensePlates = Linq.from(cars)
    .select(c -> c.licensePlate)
    .toList();

List<CarWithoutOwner> carsWithoutOwners = Linq.from(cars)
    .select(c -> {
        var carWithoutOwner = new CarWithoutOwner();
        
        carWithoutOwner.licensePlate = c.licensePlate;
        carWithoutOwner.power = c.power;
    }).toList();
```

### Chaining statements
You can also chain the statements from the query creation to the end like this:
```java
List<String> names = Linq.from(people)
    .where(p -> p.age < 20)
    .orderBy(p -> p.age)
    .thenByDescending(p -> p.name)
    .select(p -> p.name)
    .toList();
```
This will select those people's names who are younger than 20 years ordered by their age ascending, and within the same age groups ordered by their names descending, and puts the result in a list.

### Joining collections
If you have to join 2 collections by a join condition, there's also an opportunity for that. First you have to create a joined element (as there are sadly no anonymous classes in java for now):
```java
public class PersonWithCar {
    public String name;
    public int age;
    public String carLicensePlate;
}

List<PersonWithCar> joinedCollection = Linq.from(people)
    .join(cars)
    .on((p, c) -> p.name.equals(c.ownerName))
    .into((p, c) -> {
        var personWithCar = new PersonWithCar();
    
        personWithCar.name = p.name;
        personWithCar.age = p.age;
        personWithCar.carLicensePlate = c.licensePlate;
    
        return personWithCar;
    }).toList();
```
This will join the collection of cars to the collection of people, by the name of the owner, into a PersonWithCar collection. Obviously this long lambda can be extracted as a method for nicer look.

### Any, All, None
There is also support for checking if the elements of the collection satisfy a specific condition.
```java
boolean everyoneYoungerThan20 = Linq.from(people).all(p -> p.age < 20);
boolean atLeastOneYoungerThan20 = Linq.from(people).any(p -> p.age < 20);
boolean noneYoungerThan20 = Linq.from(people).none(p -> p.age < 20);
```
The first statement will return true if all of the elements satisfy the condition, the second will return true if at leas one element satisfies the condition, and the last one will return true, if none of the elements satisfy the condition.

### Skip, Take
You can also specify how many elements you want in your result collection, or you can skip some.
```java
List<Person> oldest5Person = Linq.from(people)
    .orderByDescending(p -> p.age)
    .take(5)
    .toList();
  
List<Person> listWithoutFirst10Element = Linq.from(people)
    .skip(10)
    .toList();
```

### Single, First, Last, SingleOrDefault, FirstOrDefault, LastOrDefault
These support methods all select one element from the collection, but by different logic.
 - Single will expect that only one element satisfies the condition and will throw exception if none of more than one element is selected.
 - SingleOrDefault will not throw exception if no elements satisfy the condition.
 - First will expect that at least one element will satisfy the condition and returns the first of them. Last will return the last of them.
 - FirstOrDefault will return null if none satisfy the condition. Same happens when using last.
Some examples:
```java
Person firstPersonInCollection = Linq.from(people).first();
Person lastPersonInCollection = Linq.from(people).last();
Car onlyCarWithLicensePlate = Linq.from(cars).single(c -> c.licensePlate == "IMUNIQUE123");
Car firstCarOfMike = Linq.from(cars).first(c -> c.ownerName == "Mike");
```
