package tests;

import data.DataProvider;
import data.Gender;
import data.Person;
import linq.Linq;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

public class QueryBuilderBaseTest {
    private ArrayList<Person> testCollection;

    @Before
    public void initialize() {
        testCollection = DataProvider.getPeople();
    }

    @Test
    public void testAny() {
        var anyMaca = Linq.from(testCollection).any(p -> p.getName().equals("Maca"));
        var anyPeti = Linq.from(testCollection).any(p -> p.getName().equals("Peti"));

        Assert.assertTrue(anyMaca);
        Assert.assertFalse(anyPeti);
    }

    @Test
    public void testAll() {
        var allAbove18 = Linq.from(testCollection).all(p -> p.getAge() > 18);
        var allMale = Linq.from(testCollection).all(p -> p.getGender() == Gender.MALE);

        Assert.assertTrue(allAbove18);
        Assert.assertFalse(allMale);
    }

    @Test
    public void testNone() {
        var noneAbove70 = Linq.from(testCollection).none(p -> p.getAge() > 70);
        var noneFemale = Linq.from(testCollection).none(p -> p.getGender() == Gender.FEMALE);

        Assert.assertTrue(noneAbove70);
        Assert.assertFalse(noneFemale);
    }

    @Test (expected = NoSuchElementException.class)
    public void testFirstOnEmpty() {
        var first = Linq.from(new ArrayList<>()).first();
    }

    @Test (expected = NoSuchElementException.class)
    public void testFirstOnPredicateNoMatch() {
        var first = Linq.from(testCollection).first(p -> p.getAge() == 20);
    }

    @Test
    public void testFirst() {
        var first = Linq.from(testCollection).first();
        Assert.assertEquals("SAndor", first.getName());
    }

    @Test
    public void testFirstPredicate() {
        var first = Linq.from(testCollection).first(p -> p.getGender() == Gender.FEMALE);
        Assert.assertEquals("Maca", first.getName());
    }
}
