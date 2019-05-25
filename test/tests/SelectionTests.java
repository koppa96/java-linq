package tests;

import data.DataProvider;
import data.Gender;
import data.Person;
import linq.Linq;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SelectionTests {
    private ArrayList<Person> testCollection;

    @Before
    public void initialize() {
        testCollection = DataProvider.getPeople();
    }

    @Test
    public void testSelect() {
        var selectedCollection = Linq.from(testCollection)
                .select();

        Assert.assertEquals(testCollection.size(), selectedCollection.size());
        Assert.assertEquals(DataProvider.FIRSTNAME, selectedCollection.get(0).getName());
    }

    @Test
    public void testSelectWithPredicate() {
        var selectedCollection = Linq.from(testCollection)
                .select(p -> p.getName());

        Assert.assertEquals(testCollection.size(), selectedCollection.size());
        for (int i = 0; i < selectedCollection.size(); i++) {
            Assert.assertEquals(testCollection.get(i).getName(), selectedCollection.get(i));
        }
    }
}
