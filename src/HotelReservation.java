import java.sql.*;
import java.util.Scanner;

public class HotelReservation {


    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "amam";
    private static int id;


    public static void main(String[] args) {

        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded successfully!!");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            while (true){
                System.out.println("Hotel Reservation System");
                System.out.println("1. View Reservations");
                System.out.println("2. Add Reservation");
                System.out.println("3. Update Reservation");
                System.out.println("4. Delete Reservation");
                System.out.println("5. Check Room No");
                System.out.println("0. Exit");
                System.out.print("Enter your choice : ");
                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        viewReservations(connection);
                        break;
                    case 2:
                        addReservation(connection,scanner);
                        break;
                    case 3:
                        updateReservation(connection,scanner);
                        break;
                    case 4:
                        deleteReservation(connection,scanner);
                        break;
                    case 5:
                        checkRoomNo(connection,scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice, Try again.");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void viewReservations(Connection connection){

        String query = "SELECT * FROM reservations";
       try{
           Statement stmt = connection.createStatement();
           ResultSet rs = stmt.executeQuery(query);
           while(rs.next()){

               int id = rs.getInt("reservation_id");
               String name = rs.getString("guest_name");
               int roomNo = rs.getInt("room_no");
               String contactNo = rs.getString("contact_no");
               String reservationTime = rs.getTimestamp("reservation_date").toString();

               System.out.println("=====================");
               System.out.println("id: "+id);
               System.out.println("Guest Name: "+name);
               System.out.println("Room No: "+roomNo);
               System.out.println("Contact No: "+contactNo);
               System.out.println("Reservation Date: "+reservationTime);
               System.out.println();

           }
       }catch (SQLException e){
           System.out.println(e.getMessage());
       }
    }

    public static void addReservation(Connection connection,Scanner scanner){

       try{
           System.out.println("Enter guest name: ");
           String guestName = scanner.next();
           scanner.nextLine();
           System.out.println("Enter room number: ");
           int roomNumber = scanner.nextInt();
           System.out.println("Enter contact number: ");
           String contactNumber = scanner.next();
           String insertQuery = "INSERT INTO reservations (guest_name,room_no,contact_no) VALUES ('"+guestName+"',"+roomNumber+",'"+contactNumber+"')";

           try(Statement stmt = connection.createStatement()) {
               int rowsAffected = stmt.executeUpdate(insertQuery);
               if (rowsAffected > 0) {
                   System.out.println("Reservation added successfully\n");
               } else {
                   System.out.println("Reservation Failed\n");
               }
           }
       }catch (SQLException e){
           System.out.println(e.getMessage());
       }
    }

    public static void updateReservation(Connection connection,Scanner scanner){

        System.out.println("Enter reservation ID: ");
        id = scanner.nextInt();
        scanner.nextLine();
        if (!idExist(connection,id)){
            System.out.println("Reservation does not exist with id "+id);
        }else{
            System.out.println("Enter new guest name: ");
            String name = scanner.nextLine();
            System.out.println("Enter new room number: ");
            int roomNo = scanner.nextInt();
            System.out.println("Enter new contact number: ");
            String contactNo = scanner.next();
            try{
                String updateQuery = "update reservations set guest_name = '"+name+"', room_no = "+roomNo+", contact_no = '"+contactNo+
                                    "' where reservation_id = "+id;
                Statement stmt = connection.createStatement();
                int rowsAffected = stmt.executeUpdate(updateQuery);
                if (rowsAffected>0){
                    System.out.println("Reservation with ID "+id+" is successfully updated.\n");
                }else {
                    System.out.println("Reservation is not updated!\n");
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void deleteReservation(Connection connection,Scanner scanner){

        System.out.println("Enter reservation id to remove: ");
        id = scanner.nextInt();
        scanner.nextLine();
        if (!idExist(connection,id)){
            System.out.println("Reservation does not exist with ID "+id+".");
        }else {
            try {
                String deleteQuery = "delete from reservations where reservation_id = "+id;
                Statement stmt = connection.createStatement();
                int rowsAffected = stmt.executeUpdate(deleteQuery);
                if (rowsAffected>0){
                    System.out.println("Reservation with ID "+id+" deleted successfully\n");
                }else {
                    System.out.println("Reservation is not deleted\n");
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void checkRoomNo(Connection connection,Scanner scanner){

        System.out.print("Enter reservation id: ");
        id = scanner.nextInt();
        scanner.nextLine();
        if (!idExist(connection,id)){
            System.out.println("Reservation with ID "+id+" does not exist");
        }else{
            try {
                String query = "select room_no from reservations where reservation_id = "+id;
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()){
                    System.out.println("Room Number for reservation ID "+id+" is: "+rs.getInt("room_no")+"\n");
                }else {
                    System.out.println("Reservation does not exist with ID "+id+"\n");
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void exit(){
        int i = 5;
        System.out.print("Existing");
        try{
        while (i>0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
            System.out.println("\nThanks for using our system!!");
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

    public static boolean idExist(Connection connection,int id){

        try{
            String query = "SELECT reservation_id FROM reservations where reservation_id = "+id;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}