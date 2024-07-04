package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


// jdbc -> connect java to mysql

public class DBService {
    private static String url = "jdbc:mysql://localhost:3306/recipes";
    private static String user = "root";
    private static String password = "admin";

    private Connection connection;
    private Statement statement;
    private String username;
    private String email;

    //method to grab the user from db for "Login" option
    public static User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = DBService.connect();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getString("username"), rs.getString("email"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public void init() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, password);
        statement = connection.createStatement();
    }

    public List<User> getAllUser() throws SQLException, ClassNotFoundException {
        init();

        ResultSet resultSet = statement.executeQuery("select * from users");

        List<User> users = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String username = resultSet.getString("username");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            User user = new User(username, email, password);
            System.out.println(user);
            users.add(user);
        }

        connection.close();

        return users;
    }

    //method to save the users in db
    public boolean save(String username, String email, String password) {
        String sql = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";

        try (Connection connection = DBService.connect();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }}

    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean loginUser(String email, String password) {
        return true;
    }
}


