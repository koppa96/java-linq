package tests;

import data.DataProvider;
import data.Gender;
import data.Person;
import linq.Linq;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

public class OrderTests {
    private ArrayList<Person> testCollection;

    @Before
    public void initialize() {
        testCollection = DataProvider.getPeople();
    }

    @Test
    public void testOrderByAge() {
        var orderedCollection = Linq.from(testCollection)
                .orderBy(p -> p.getAge())
                .select(p -> p.getName())
                .toList();

        var comparedCollection = DataProvider.getPeopleOrderedByAge();
        for (int i = 0; i < orderedCollection.size(); i++) {
            Assert.assertEquals(comparedCollection.get(i).getName(), orderedCollection.get(i));
        }
    }

    @Test
    public void testOrderByAgeWithComparator() {
        var orderedCollection = Linq.from(testCollection)
                .orderBy(p -> p.getAge(), (age1, age2) -> age1.compareTo(age2))
                .select(p -> p.getName())
                .toList();

        var comparedCollection = DataProvider.getPeopleOrderedByAge();
        for (int i = 0; i < orderedCollection.size(); i++) {
            Assert.assertEquals(comparedCollection.get(i).getName(), orderedCollection.get(i));
        }
    }

    @Test
    public void testOrderByCustomDate() {
        var orderedCollection = Linq.from(testCollection)
                .orderBy(p -> p.getBirthDate(), (date1, date2) -> {
                    if (date1.getYear() == date2.getYear()) {
                        if (date1.getMonth() == date2.getMonth()) {
                            return date1.getDay() - date2.getDay();
                        } else {
                            return date1.getMonth() - date2.getMonth();
                        }
                    }

                    return date1.getYear() - date2.getYear();
                })
                .select(p -> p.getName())
                .toList();

        var comparedCollection = DataProvider.getPeopleOrderedByAge();
        Collections.reverse(comparedCollection);
        for (int i = 0; i < orderedCollection.size(); i++) {
            Assert.assertEquals(comparedCollection.get(i).getName(), orderedCollection.get(i));
        }
    }

    @Test
    public void testOrderByMultiple() {
        var orderedCollection = Linq.from(testCollection)
                .where(p -> p.getAge() > 30)
                .orderBy(p -> p.getGender())
                .thenBy(p -> p.getAge())
                .toList();

        Assert.assertEquals(3, orderedCollection.size());
        Assert.assertEquals(Gender.FEMALE, orderedCollection.get(orderedCollection.size() - 1).getGender());
    }

    public void testOrderByDescending() {
        var orderedCollection = Linq.from(testCollection)
                .orderByDescending(p -> p.getName())
                .toList();

        Assert.assertEquals("Tamás", orderedCollection.get(0).getName());
    }
}
