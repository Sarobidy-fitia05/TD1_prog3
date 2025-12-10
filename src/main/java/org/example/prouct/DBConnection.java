package org.example.prouct;

import com.sun.jdi.ClassNotLoadedException;

import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.Class.forName;

public class DBConnection {
    private String URL = "jdbc:postgresql://localhost:5432/product_management_db";
    private String USER = "product_manager_user";
    private String PASSWRD = "123456";

    public DBConnection(String URL, String USER, String PASSWRD) {
        this.URL = URL;
        this.USER = USER;
        this.PASSWRD = PASSWRD;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    public void setPASSWRD(String PASSWRD) {
        this.PASSWRD = PASSWRD;
    }

    public String getURL() {
        return URL;
    }

    public String getUSER() {
        return USER;
    }

    public String getPASSWRD() {
        return PASSWRD;
    }

    public <connection> connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found." , e);
        }
        return (connection) DriverManager.getConnection(URL , USER , PASSWRD);
    }
}
