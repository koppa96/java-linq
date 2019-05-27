package tests;

import data.DataProvider;
import data.Person;
import linq.Linq;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class MinMaxTests {
    private ArrayList<Person> testCollection;

    @Before
    public void initialize() {
        testCollection = DataProvider.getPeople();
    }

    @Test (expected = IllegalStateException.class)
    public void testFindMinException() {
        Linq.from(testCollection).min();
    }

    @Test
    public void testFindMinComparable() {
        var names = Linq.from(testCollection).select(p -> p.getName()).toList();

        var firstName = Linq.from(names).min();
        Assert.assertEquals("Maca", firstName);
    }

    @Test (expected = IllegalStateException.class)
    public void testFinMaxException() {
        Linq.from(testCollection).max();
    }

    @Test
    public void testFindMaxComparable() {

    }
}
