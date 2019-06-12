package data;

public class Car {
    private String ownerName;
    private String licensePlate;

    public Car(String ownerName, String licensePlate) {
        this.ownerName = ownerName;
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getOwnerName() {
        return ownerName;
    }
}
