package entities;

public class Patient {
    private String name;
    private int PacientId;
    private String phone;

    public Patient(String name, int PacientId, String phone) {
        setName(name);
        setPacientId(PacientId);
        setPhone(phone);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPacientId(int PacientId) {
        this.PacientId = PacientId;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }
    public int getPacientId() {
        return PacientId;
    }
    public String getPhone() {
        return phone;
    }
}
