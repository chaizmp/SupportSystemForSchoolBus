package Project.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by User on 14/10/2558.
 */

/**
 * Created by User on 12/10/2558.
 */
public class ConnectionConfiguration {


    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/SupportSystemForSchoolBus", "root","password");
            System.out.println("hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(ResultSet rs, Statement st, Connection conn) {
        try {
            rs.close();
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
