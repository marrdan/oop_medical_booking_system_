package entities;

import java.time.LocalDateTime;

public class Appointment {

    private int id;
    private int patientId;
    private int doctorId;
    private LocalDateTime appointmentTime;

    // используется при CREATE (id генерирует БД)
    public Appointment(int patientId, int doctorId, LocalDateTime appointmentTime) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentTime = appointmentTime;
    }

    // используется при READ (findById, findAll)
    public Appointment(int id, int patientId, int doctorId, LocalDateTime appointmentTime) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentTime = appointmentTime;
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", appointmentTime=" + appointmentTime +
                '}';
    }

}
