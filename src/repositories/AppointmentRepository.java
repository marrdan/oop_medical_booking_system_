package repositories;

import entities.Appointment;

import java.util.List;

public interface AppointmentRepository {
    boolean create(Appointment appointment);
    List<Appointment> findAll();
    Appointment findById(int id);

}
