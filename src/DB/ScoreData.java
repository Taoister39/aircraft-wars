package DB;

import javax.swing.*;
import java.sql.*;

public class ScoreData extends DataBaseInformation {
    public static final int MAX_QUERY_ROWS = 100; // 最大查询数量行
    public static final int MAX_QUERY_COLS = 4; // 最大查询数量列
    public static int resultCount = 0; // 查询结果有多少纪录

    public static String[][] ResultQueryAll() {
        String queryArray[][] = new String[MAX_QUERY_ROWS][MAX_QUERY_COLS];
        int index = 0;
        String sql = "SELECT username,password,score,game_time FROM login ORDER BY score DESC";
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // 连结对象
            statement = connection.createStatement(); // 创建语句对象
            result = statement.executeQuery(sql); // 结果集
            while (result.next()) {
                queryArray[index][0] = result.getString("username");
                queryArray[index][1] = result.getString("password");
                queryArray[index][2] = String.valueOf(result.getInt("score"));
                queryArray[index++][3] = String.valueOf(result.getInt("game_time"));
            }
            resultCount = index;
            result.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return queryArray;
    }

    public static void appendToScore(String username, String password, int socre, Long game_time) {

        String sql = "UPDATE login SET score = " + socre + ",game_time = " + game_time + " WHERE username = '" +
                username + "' AND password = '" + password + "'";
//        System.out.println(sql);
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS); // 连结对象
            statement = connection.createStatement(); // 创建语句对象
            int state = statement.executeUpdate(sql); // 结果集
            if (state == 0) JOptionPane.showMessageDialog(null, "记录分数失败", "错误 ", 0);

            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

