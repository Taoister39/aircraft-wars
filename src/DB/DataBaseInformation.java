package DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBaseInformation {
    public static String connectionDatabase = "test"; // 需要连接的数据库库名
    public static String connectionTableName = "login"; // 需要连接的数据库的表名

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; // JDBC驱动名
    static String DB_URL = "jdbc:mysql://localhost:3306/" + connectionDatabase + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"; // 数据库URL

    static final String USER = "root"; // 数据库用户名
    static final String PASS = "liutianhua"; // 数据库密码
    /* 复用 */
    public static Connection connection = null;
    public static Statement statement = null;
    public static ResultSet result = null;
    // 刷新DB_URL
    public static void refreshDB_URL() {
        DB_URL = "jdbc:mysql://localhost:3306/" + connectionDatabase + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"; // 数据库URL
    }
}
