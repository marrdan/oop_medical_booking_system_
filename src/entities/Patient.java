package entities;

public class Patient {
    private int id;
    private String name;
    private String phone;

    public Patient(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId() { return id; }

    @Override
    public String toString() {
        return id + " " + name + " " + phone;
    }
}
