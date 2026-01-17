package entities;

public class Doctor {
    private int DoctorId;
    private String name;
    private String specialization;

    public Doctor(int DoctorId, String name, String specialization) {
        setDoctorId(DoctorId);
        setName(name);
        setSpecialization(specialization);
    }

    public void setDoctorId(int  DoctorId) {
        this.DoctorId = DoctorId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public int getDoctorId() {
        return DoctorId;

    }
    public String getName() {
        return name;
    }
    public String getSpecialization() {
        return specialization;

    }
}
