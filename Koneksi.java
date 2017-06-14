package TUBES;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static Connection conn;
    public static Connection getKoneksi()
    {
        String host = "jdbc:mysql://localhost/puzzlegames", user = "root", pass = "";
        try
        {
            conn = (Connection) DriverManager.getConnection(host, user, pass);
        }

        catch(SQLException exc)
        {
            System.out.println(exc.getMessage());
        }
        return conn;
    }
}
