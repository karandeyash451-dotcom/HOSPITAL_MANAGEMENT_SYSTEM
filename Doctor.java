package HospitalManagementSystem;

import java.sql.*;

public class Doctor {

    private Connection connection;

    public Doctor(Connection connection) {
        this.connection = connection;
    }

    public void viewDoctors() {

        String query = "SELECT * FROM doctors";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n---- DOCTOR LIST ----");
            System.out.printf("|%-5s|%-15s|%-15s|\n", "ID", "NAME", "SPECIALIZATION");

            while (rs.next()) {
                System.out.printf("|%-5d|%-15s|%-15s|\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("specialization"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id) {

        String query = "SELECT * FROM doctors WHERE id = ?";

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