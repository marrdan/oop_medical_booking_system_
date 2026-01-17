package repositories;

import db.IDB;
import entities.Appointment;
import exceptions.NotFoundException;

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
    public boolean create(Appointment a) {
        String sql = "INSERT INTO appointments(patient_id, doctor_id, appointment_time) VALUES (?, ?, ?)";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());
            ps.setTimestamp(3, Timestamp.valueOf(a.getAppointmentTime()));
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean exists(String table, int id) throws SQLException {
        String sql = "SELECT 1 FROM " + table + " WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        }
    }


    @Override
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();

        String sql = "SELECT * FROM appointments";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int patientId = rs.getInt("patient_id");
                int doctorId = rs.getInt("doctor_id");
                LocalDateTime time =
                        rs.getTimestamp("appointment_time").toLocalDateTime();

                appointments.add(
                        new Appointment(id, patientId, doctorId, time)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    @Override
    public Appointment findById(int id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new NotFoundException(
                            "Appointment with ID " + id + " not found"
                    );
                }

                int patientId = rs.getInt("patient_id");
                int doctorId = rs.getInt("doctor_id");
                LocalDateTime time =
                        rs.getTimestamp("appointment_time").toLocalDateTime();

                return new Appointment(id, patientId, doctorId, time);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointment by id", e);
        }
    }



}