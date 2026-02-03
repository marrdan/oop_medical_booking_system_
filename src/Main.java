import db.PostegresDB;
import entities.Appointment;
import exceptions.AppointmentNotFoundException;
import exceptions.DoctorUnavailableException;
import exceptions.TimeSlotAlreadyBookedException;
import factories.AppointmentFactory;
import factories.BookedAppointmentFactory;
import factories.UrgentAppointmentFactory;
import repositories.AppointmentRepository;
import repositories.AppointmentRepositoryImpl;
import services.AppointmentService;
import services.DoctorAvailabilityService;
import services.UrgentBookingService;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        AppointmentRepository repo = new AppointmentRepositoryImpl(PostegresDB.getInstance());
        DoctorAvailabilityService availability = new DoctorAvailabilityService(repo);
        AppointmentService service = new AppointmentService(repo, availability);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    1. Book appointment
                    2. Cancel appointment
                    3. Doctor schedule
                    4. Patient visits
                    5. Book urgent appointment (next available slot)
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
                        System.out.println("Select appointment type: ");
                        System.out.println("1. Normal");
                        System.out.println("2. Urgent");
                        int typeChoice = Integer.parseInt(sc.nextLine());

                        AppointmentFactory factory;



                        if (typeChoice == 1) {

                            factory = new BookedAppointmentFactory();
                        } else if (typeChoice == 2) {

                            factory = new UrgentAppointmentFactory();

                        } else {
                            System.out.println("Unknown type! Defaulting to Normal.");
                            factory = new BookedAppointmentFactory();
                        }


                        Appointment appointment = factory.create(p, d,
                                typeChoice == 1 ? LocalDateTime.now().plusDays(1) : LocalDateTime.now().plusHours(1));


                        service.book(appointment);
                        System.out.println("Appointment booked successfully! Type: " + appointment.getStatus());

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number.");
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


                            schedule.stream()
                                    .filter(a -> "BOOKED".equals(a.getStatus()))
                                    .sorted((a1, a2) -> a1.getTime().compareTo(a2.getTime()))
                                    .forEach(System.out::println);
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
                case 5 -> {
                    try {
                        System.out.print("Patient ID: ");
                        int p = Integer.parseInt(sc.nextLine());
                        System.out.print("Doctor ID: ");
                        int d = Integer.parseInt(sc.nextLine());

                        UrgentBookingService urgentService = new UrgentBookingService(
                                repo,
                                availability,
                                new UrgentAppointmentFactory()
                        );

                        Appointment urgentAppointment = urgentService.bookNextAvailable(p, d);
                        System.out.println("Urgent appointment booked at: " + urgentAppointment.getTime());

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number.");
                    } catch (TimeSlotAlreadyBookedException | DoctorUnavailableException e) {
                        System.out.println("Error booking appointment: " + e.getMessage());
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
