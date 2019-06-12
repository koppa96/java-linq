package data;

public class OwnerData {
    public String name;
    public int age;
    public Gender gender;
    public String licensePlate;

    public OwnerData() {

    }

    public OwnerData(String name, int age, Gender gender, String licensePlate) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.licensePlate = licensePlate;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OwnerData)) {
            return false;
        }

        var other = (OwnerData) obj;
        return name.equals(other.name) && age == other.age && gender == other.gender && licensePlate.equals(other.licensePlate);
    }
}
