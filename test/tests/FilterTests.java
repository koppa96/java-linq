package tests;

import data.DataProvider;
import data.Gender;
import data.Person;
import linq.Linq;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class FilterTests {
    private ArrayList<Person> testCollection;

    @Before
    public void initialize() {
        testCollection = DataProvider.getPeople();
    }

    @Test
    public void testWhere() {
        var filteredCollection = Linq.from(testCollection)
                .where(p -> p.getAge() < 50)
                .toList();

        Assert.assertEquals(DataProvider.LESSTHAN50YEARSOLDCOUNT, filteredCollection.size());
    }

    @Test
    public void testWhereByEnum() {
        var selectedCollection = Linq.from(testCollection)
                .where(p -> p.getGender() == Gender.MALE)
                .select(p -> p.getName())
                .toList();

        Assert.assertEquals(DataProvider.MALECOUNT, selectedCollection.size());
    }
}
