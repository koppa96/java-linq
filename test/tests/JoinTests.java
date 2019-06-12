package tests;

import data.Car;
import data.DataProvider;
import data.OwnerData;
import data.Person;
import linq.Linq;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class JoinTests {
    private ArrayList<Person> people;
    private ArrayList<Car> cars;

    @Before
    public void initialize() {
        people = DataProvider.getPeople();
        cars = DataProvider.getCars();
    }

    @Test
    public void testJoinPeopleWithCars() {
        var joinedCollection = Linq.from(people)
                .join(cars)
                .on((p, c) -> p.getName().equals(c.getOwnerName()))
                .into((p, c) -> {
                    var data = new OwnerData();

                    data.name = p.getName();
                    data.age = p.getAge();
                    data.gender = p.getGender();
                    data.licensePlate = c.getLicensePlate();

                    return data;
                }).toList();

        var referenceCollection = DataProvider.joinedCollection();
        Assert.assertEquals(referenceCollection.size(), joinedCollection.size());
        for (int i = 0; i < joinedCollection.size(); i++) {
            Assert.assertEquals(referenceCollection.get(i), joinedCollection.get(i));
        }
    }
}
