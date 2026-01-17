package entities;

import java.time.LocalDateTime;

public class Appointment {
    private int pacientId;
    private int appointmentId;
    private int doctorId;
    private LocalDateTime appointmentTime;

    public Appointment(int pacientId, int appointmentId, LocalDateTime appointmentTime) {
        setPacientId(pacientId);
        setAppointmentId(appointmentId);
        setDoctorId(doctorId);
        setAppointmentTime(appointmentTime);

    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setPacientId(int pacientId) {
        this.pacientId = pacientId;
    }
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
    public int getPacientId() {
        return pacientId;
    }
    public int getAppointmentId() {
        return appointmentId;
    }
    public int getDoctorId() {
        return doctorId;
    }
    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }


}
