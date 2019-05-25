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
}
