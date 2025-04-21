import java.sql.*;
import java.util.Scanner;

public class EventManagementSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/event_management";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nEvent Management System");
            System.out.println("1. Create Event");
            System.out.println("2. Register Attendee");
            System.out.println("3. List Events");
            System.out.println("4. View Attendees");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    createEvent();
                    break;
                case 2:
                    registerAttendee();
                    break;
                case 3:
                    listEvents();
                    break;
                case 4:
                    viewAttendees();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Create Event
    private static void createEvent() {
        System.out.print("Enter event name: ");
        String name = scanner.nextLine();
        System.out.print("Enter event date (yyyy-MM-dd): ");
        String date = scanner.nextLine();
        System.out.print("Enter event time (HH:mm:ss): ");
        String time = scanner.nextLine();
        System.out.print("Enter event location: ");
        String location = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "INSERT INTO events (name, date, time, location) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, date);
            pstmt.setString(3, time);
            pstmt.setString(4, location);
            pstmt.executeUpdate();
            System.out.println("Event created successfully");
        } catch (SQLException e) {
            System.out.println("Error creating event: " + e.getMessage());
        }
    }

    // Register Attendee
    private static void registerAttendee() {
        System.out.print("Enter attendee name: ");
        String name = scanner.nextLine();
        System.out.print("Enter attendee email: ");
        String email = scanner.nextLine();
        System.out.print("Enter event ID: ");
        int eventId = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "INSERT INTO attendees (name, email, event_id) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, eventId);
            pstmt.executeUpdate();
            System.out.println("Attendee registered successfully");
        } catch (SQLException e) {
            System.out.println("Error registering attendee: " + e.getMessage());
        }
    }

    // List Events
    private static void listEvents() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM events";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println("Event ID: " + rs.getInt("id"));
                System.out.println("Event Name: " + rs.getString("name"));
                System.out.println("Event Date: " + rs.getString("date"));
                System.out.println("Event Time: " + rs.getString("time"));
                System.out.println("Event Location: " + rs.getString("location"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error listing events: " + e.getMessage());
        }
    }

    // View Attendees
    private static void viewAttendees() {
        System.out.print("Enter event ID: ");
        int eventId = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM attendees WHERE event_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();

            boolean attendeesFound = false;
            while (rs.next()) {
                System.out.println("Attendee Name: " + rs.getString("name"));
                System.out.println("Attendee Email: " + rs.getString("email"));
                System.out.println();
                attendeesFound = true;
            }

            if (!attendeesFound) {
                System.out.println("No attendees found for this event.");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing attendees: " + e.getMessage());
        }
    }
}