package repositories;

import db.IDB;
import entities.Appointment;
import entities.Doctor;
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

            ps.setInt(1, a.getPacientId());
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
        return List.of();
    }

    @Override
    public Appointment findById(int id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int patientId = rs.getInt("patient_id");
                    int doctorId = rs.getInt("doctor_id");
                    LocalDateTime time = rs.getTimestamp("appointment_time").toLocalDateTime();


                    return new Appointment(id, patientId, time);
                } else {
                    throw new NotFoundException("Appointment with ID " + id + " not found");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}