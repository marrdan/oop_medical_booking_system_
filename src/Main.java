import db.PostegresDB;
import entities.Appointment;
import exceptions.AppointmentNotFoundException;
import exceptions.DoctorUnavailableException;
import exceptions.TimeSlotAlreadyBookedException;
import repositories.AppointmentRepository;
import repositories.AppointmentRepositoryImpl;
import services.AppointmentService;
import services.DoctorAvailabilityService;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        AppointmentRepository repo = new AppointmentRepositoryImpl(new PostegresDB());
        DoctorAvailabilityService availability = new DoctorAvailabilityService(repo);
        AppointmentService service = new AppointmentService(repo, availability);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    1. Book appointment
                    2. Cancel appointment
                    3. Doctor schedule
                    4. Patient visits
                    0. Exit
                    """);
            System.out.print("Enter your choice: ");
            String input = sc.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    try {
                        System.out.print("Patient ID: ");
                        int p = Integer.parseInt(sc.nextLine());
                        System.out.print("Doctor ID: ");
                        int d = Integer.parseInt(sc.nextLine());

                        service.book(
                                new Appointment(p, d, LocalDateTime.now().plusDays(1))
                        );
                        System.out.println("Booked successfully!");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID! Please enter a number.");
                    } catch (TimeSlotAlreadyBookedException | DoctorUnavailableException e) {
                        System.out.println("Error booking appointment: " + e.getMessage());
                    }
                }

                case 2 -> {
                    try {
                        System.out.print("Appointment ID: ");
                        int id = Integer.parseInt(sc.nextLine());
                        service.cancel(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID! Please enter a number.");
                    } catch (AppointmentNotFoundException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }

                case 3 -> {
                    try {
                        System.out.print("Doctor ID: ");
                        int d = Integer.parseInt(sc.nextLine());

                        List<Appointment> schedule = service.doctorSchedule(d);

                        if (schedule.isEmpty()) {
                            System.out.println("No appointments found for doctor with ID " + d);
                        } else {
                            schedule.forEach(System.out::println);
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID! Please enter a number.");
                    } catch (DoctorUnavailableException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }

                case 4 -> {
                    try {
                        System.out.print("Patient ID: ");
                        int p = Integer.parseInt(sc.nextLine());
                        List<Appointment> visits = service.patientVisits(p);

                        if (visits.isEmpty()) {
                            System.out.println("No appointments found for patient with ID " + p);
                        } else {
                            visits.forEach(System.out::println);
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID! Please enter a number.");
                    }
                }

                case 0 -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }

                default -> System.out.println("Unknown option! Please choose 0-4.");
            }
        }
    }
}
