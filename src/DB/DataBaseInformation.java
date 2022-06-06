package DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBaseInformation {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; // JDBC驱动名
    static final String DB_URL = "jdbc:mysql://localhost:3306/test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"; // 数据库URL

    static final String USER = "root";
    static final String PASS = "liutianhua";
    /* 复用 */
    public static Connection connection = null;
    public static Statement statement = null;
    public static ResultSet result = null;
}
