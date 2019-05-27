package data;

import java.util.Calendar;

public class Person {
    private String name;
    private Gender gender;
    private int age;
    private CustomDate birthDate;

    public Person() {

    }

    public Person(String name, Gender gender, int age) {
        this.name = name;
        this.gender = gender;
        this.age = age;

        var calendar = Calendar.getInstance();
        birthDate = new CustomDate(calendar.get(Calendar.YEAR) - age, 1, 1);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public CustomDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(CustomDate birthDate) {
        this.birthDate = birthDate;
    }
}
