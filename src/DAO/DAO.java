package DAO;

import model.User;

import java.sql.*;
import java.util.ArrayList;

public class DAO {
    private Connection conn;

    public DAO() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/javachatappdb";
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection (url, "root", "1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> ls = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String id = rs.getString("id");
                String nameLogin = rs.getString("NameLogin");
                String name = rs.getString("Name");
                String password = rs.getString("Password");
                User user = new User(id, nameLogin, name, password);
                ls.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ls;
    }

    public User getUserInfo(String namelogin) {
        String sql = "SELECT * FROM User where NameLogin = ?";
        User user = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namelogin);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String id = rs.getString("id");
                String nameLogin = rs.getString("NameLogin");
                String name = rs.getString("Name");
                String password = rs.getString("Password");
                user = new User(id, nameLogin, name, password);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public User getUserById(String id) {
        String sql = "SELECT * FROM User where id = ?";
        User user = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String userid = rs.getString("id");
                String nameLogin = rs.getString("NameLogin");
                String name = rs.getString("Name");
                String password = rs.getString("Password");
                user = new User(userid, nameLogin, name, password);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public boolean addNewUser(User s) {
        String sql = "INSERT INTO user(NameLogin, Name, Password) VALUES(?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getNameLogin());
            ps.setString(2, s.getName());
            ps.setString(3, s.getPassword());
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
}
