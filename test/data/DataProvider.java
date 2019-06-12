package data;

import java.util.ArrayList;

public class DataProvider {
    public static final String FIRSTNAME = "SAndor";
    public static final int LESSTHAN50YEARSOLDCOUNT = 2;
    public static final int MALECOUNT = 3;

    public static ArrayList<Person> getPeople() {
        var testCollection = new ArrayList<Person>();

        testCollection.add(new Person("SAndor", Gender.MALE, 48));
        testCollection.add(new Person("Maca", Gender.FEMALE, 56));
        testCollection.add(new Person("Papp Zsolt", Gender.MALE, 57));
        testCollection.add(new Person("Tamás", Gender.MALE, 22));

        return testCollection;
    }

    public static ArrayList<Person> getPeopleOrderedByAge() {
        var testCollection = new ArrayList<Person>();

        testCollection.add(new Person("Tamás", Gender.MALE, 22));
        testCollection.add(new Person("SAndor", Gender.MALE, 48));
        testCollection.add(new Person("Maca", Gender.FEMALE, 56));
        testCollection.add(new Person("Papp Zsolt", Gender.MALE, 57));

        return testCollection;
    }

    public static ArrayList<Car> getCars() {
        var testCollection = new ArrayList<Car>();

        testCollection.add(new Car("SAndor", "SQL-123"));
        testCollection.add(new Car("Tamás", "FOS-999"));
        testCollection.add(new Car("Tamás", "PRO-000"));

        return testCollection;
    }

    public static ArrayList<OwnerData> joinedCollection() {
        var joinedCollection = new ArrayList<OwnerData>();

        joinedCollection.add(new OwnerData("SAndor", 48, Gender.MALE, "SQL-123"));
        joinedCollection.add(new OwnerData("Tamás", 22, Gender.MALE, "FOS-999"));
        joinedCollection.add(new OwnerData("Tamás", 22, Gender.MALE, "PRO-000"));

        return joinedCollection;
    }
}
