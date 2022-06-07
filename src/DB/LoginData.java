package DB;

import java.sql.*;

// 数据库API
public class LoginData extends DataBaseInformation {
    // 用户名是否被使用了
    public static boolean isLogin(String username) {
        try {

            String sql = "SELECT username FROM " + DataBaseInformation.connectionTableName +
                    " WHERE username = " + "'" + username + "'";
            resultQuerySQL(sql); // 结果集

            if (result.next()) {
                return true;
            }
            result.close();
            connection.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // 复用查询
    private static ResultSet resultQuerySQL(String sql) {
        try {
            // 注册JDBC驱动
            Class.forName(JDBC_DRIVER);

//            System.out.println("连接数据库");
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // 连结对象

//            System.out.println("实例化Statement对象");
            statement = connection.createStatement(); // 创建语句对象
            result = statement.executeQuery(sql); // 结果集

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // 登录是否成功
    public static Boolean loginSuccessful(String username, String password) {
        try {
            String sql = "SELECT username FROM " + DataBaseInformation.connectionTableName +
                    " WHERE username = " + "'" + username + "'" + "AND password = " + "'" + password + "'";
            resultQuerySQL(sql); // 结果集

            if (result.next()) {
                return true;
            }
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // 注册用户
    public static Boolean registeredUser(String username, String password) {
        if (isLogin(username)) {
            return false;
        }
        String sql = "INSERT INTO " + DataBaseInformation.connectionTableName +
                " (username,password) VALUES('" + username + "','" + password + "')";
        try {
            // 注册JDBC驱动
            Class.forName(JDBC_DRIVER);

//            System.out.println("连接数据库");
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // 连结对象

//            System.out.println("实例化Statement对象");
            statement = connection.createStatement(); // 创建语句对象
            if (statement.executeUpdate(sql) != 0) {
                return true;
            }
            ;

            connection.close();
            statement.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
