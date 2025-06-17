import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HotelReservation {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/hotel_db";
        String username = "root";
        String password = "amam";


        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded successfully!!");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection con = DriverManager.getConnection(url,username,password);
            System.out.println("Connection established successfully!!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}