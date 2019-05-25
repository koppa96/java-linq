package tests;

import data.DataProvider;
import data.Gender;
import data.Person;
import linq.Linq;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
                .select(p -> p.getName());

        var comparedCollection = DataProvider.getPeopleOrderedByAge();
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
                .select();

        Assert.assertEquals(3, orderedCollection.size());
        Assert.assertEquals(Gender.FEMALE, orderedCollection.get(orderedCollection.size() - 1).getGender());
    }
}
