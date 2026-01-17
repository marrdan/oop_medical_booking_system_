import db.IDB;
import db.PostegresDB;
import repositories.AppointmentRepository;
import repositories.AppointmentRepositoryImpl;
import entities.Appointment;
import exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Connecting to Supabase...");
        IDB db = new PostegresDB();
        System.out.println("Connected successfully!");

        AppointmentRepository repo = new AppointmentRepositoryImpl(db);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Medical Booking System ---");
            System.out.println("1. Create appointment");
            System.out.println("2. List all appointments");
            System.out.println("3. Find appointment by ID");
            System.out.println("0. Exit");

            int choice = sc.nextInt();

            try {
                switch (choice) {

                    case 1 -> {
                        System.out.print("Patient ID: ");
                        int patientId = sc.nextInt();

                        System.out.print("Doctor ID: ");
                        int doctorId = sc.nextInt();

                        LocalDateTime time = LocalDateTime.now().plusDays(1);

                        Appointment a =
                                new Appointment(patientId, doctorId, time);

                        repo.create(a);
                        System.out.println("Appointment created successfully");
                    }

                    case 2 -> {
                        List<Appointment> list = repo.findAll();
                        list.forEach(System.out::println);
                    }

                    case 3 -> {
                        System.out.print("Enter appointment ID: ");
                        int id = sc.nextInt();
                        System.out.println(repo.findById(id));
                    }

                    case 0 -> {
                        System.out.println("Goodbye!");
                        return;
                    }

                    default -> System.out.println("Invalid option");
                }

            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
