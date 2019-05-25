package main;

import linq.Linq;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        var people = new ArrayList<Person>();

        people.add(new Person("Nagy", 36));
        people.add(new Person("Nagy", 23));
        people.add(new Person("Kis", 24));
        people.add(new Person("SAndor", 52));

        var result = Linq.from(people)
                .where(p -> p.getAge() < 50)
                .orderBy(p -> p.getName())
                .thenBy(p -> p.getAge())
                .select();

        for (var person : result) {
            System.out.println(person.getName() + "\t" + person.getAge());
        }
    }
}
