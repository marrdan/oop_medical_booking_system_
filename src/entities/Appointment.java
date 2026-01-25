package entities;

import java.time.LocalDateTime;

public class Appointment {

    private int id;
    private int patientId;
    private int doctorId;
    private LocalDateTime time;
    private String status;

    public Appointment(int patientId, int doctorId, LocalDateTime time) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.time = time;
        this.status = "BOOKED";
    }

    public Appointment(int id, int patientId, int doctorId,
                       LocalDateTime time, String status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.time = time;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public LocalDateTime getTime() { return time; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Appointment {" + "id=" + id + ", patientId=" + patientId + ", doctorId=" + doctorId + ", time=" +
                time + ", status='" + status + '\'' + '}';
    }

}