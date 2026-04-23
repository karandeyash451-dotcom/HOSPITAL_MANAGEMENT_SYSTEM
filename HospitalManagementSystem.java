package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Nayan@280705"; // ⚠️ change this

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found!");
        }

        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            while (true) {
                System.out.println("\n===== HOSPITAL MANAGEMENT SYSTEM =====");
                System.out.println("1. ADD PATIENT");
                System.out.println("2. VIEW PATIENTS");
                System.out.println("3. VIEW DOCTORS");
                System.out.println("4. BOOK APPOINTMENT");
                System.out.println("5. EXIT");
                System.out.print("ENTER YOUR CHOICE: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        patient.addPatient();
                        break;

                    case 2:
                        patient.viewPatients();
                        break;

                    case 3:
                        doctor.viewDoctors();
                        break;

                    case 4:
                        bookAppointment(patient, doctor, connection, scanner);
                        break;

                    case 5:
                        System.out.println("THANK YOU!");
                        return;

                    default:
                        System.out.println("INVALID CHOICE!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= BOOK APPOINTMENT =================
    public static void bookAppointment(Patient patient, Doctor doctor,
                                       Connection connection, Scanner scanner) {

        System.out.print("ENTER PATIENT ID: ");
        int patientId = scanner.nextInt();

        System.out.print("ENTER DOCTOR ID: ");
        int doctorId = scanner.nextInt();

        System.out.print("ENTER DATE (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {

            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {

                String query = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?,?,?)";

                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1, patientId);
                    ps.setInt(2, doctorId);
                    ps.setString(3, appointmentDate);

                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        System.out.println("✅ APPOINTMENT BOOKED");
                    } else {
                        System.out.println("❌ FAILED");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("❌ DOCTOR NOT AVAILABLE ON THIS DATE");
            }

        } else {
            System.out.println("❌ INVALID PATIENT OR DOCTOR ID");
        }
    }

    // ================= CHECK DOCTOR =================
    public static boolean checkDoctorAvailability(int doctorId, String date, Connection connection) {

        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, doctorId);
            ps.setString(2, date);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}