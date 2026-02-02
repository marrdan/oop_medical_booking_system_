package repositories;

import db.IDB;
import entities.Appointment;
import exceptions.AppointmentNotFoundException;
import exceptions.DoctorUnavailableException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final IDB db;

    public AppointmentRepositoryImpl(IDB db) {
        this.db = db;
    }


    @Override
    public void save(Appointment a) {

        String sql = """
        INSERT INTO appointments(patient_id, doctor_id, appointment_time, status)
        VALUES (?, ?, ?, ?)
    """;
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());
            ps.setTimestamp(3, Timestamp.valueOf(a.getTime()));
            ps.setString(4, a.getStatus());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                a.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean exists(int doctorId, LocalDateTime time) {
        String sql = """
    SELECT 1 FROM appointments
    WHERE doctor_id=? AND appointment_time=? AND status='BOOKED'
""";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setTimestamp(2, Timestamp.valueOf(time));
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancel(int id) {
        String sql = "UPDATE appointments SET status='CANCELLED' WHERE id=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            if (ps.executeUpdate() == 0)
                throw new AppointmentNotFoundException("Appointment not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Appointment findById(int id) {
        String sql = "SELECT * FROM appointments WHERE id=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                throw new AppointmentNotFoundException("Appointment not found");

            return new Appointment(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getTimestamp("appointment_time").toLocalDateTime(),
                    rs.getString("status")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Appointment> findByDoctor(int doctorId) {
        List<Appointment> list = find("doctor_id", doctorId);

        if (list.isEmpty()) {
            throw new DoctorUnavailableException("No appointments found for doctor with id " + doctorId);
        }

        return list;
    }

    @Override
    public List<Appointment> findByPatient(int patientId) {
        return find("patient_id", patientId);
    }

    private List<Appointment> find(String field, int id) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE " + field + "=?";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getTimestamp("appointment_time").toLocalDateTime(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
