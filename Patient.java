package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Patient {

    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {

        System.out.print("ENTER NAME: ");
        String name = scanner.next();

        System.out.print("ENTER AGE: ");
        int age = scanner.nextInt();

        System.out.print("ENTER GENDER: ");
        String gender = scanner.next();

        try {
            String query = "INSERT INTO patients(name, age, gender) VALUES(?,?,?)";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ PATIENT ADDED");
            } else {
                System.out.println("❌ FAILED");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {

        String query = "SELECT * FROM patients";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n---- PATIENT LIST ----");
            System.out.printf("|%-5s|%-15s|%-5s|%-10s|\n", "ID", "NAME", "AGE", "GENDER");

            while (rs.next()) {
                System.out.printf("|%-5d|%-15s|%-5d|%-10s|\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {

        String query = "SELECT * FROM patients WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}