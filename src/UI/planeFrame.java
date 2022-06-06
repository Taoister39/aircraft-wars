package UI;

import java.awt.*;
//import java.sql.SQLException;
//import java.time.LocalDate;
import javax.swing.*;

public class planeFrame extends JFrame {
    public static Boolean mStartState = false;
    public static Container contentPane = null;

    // 登录成功，加载游戏
    public static void renderGameState() {
        if (!mStartState) {
            JOptionPane.showMessageDialog(null, "用户名或密码错误！", "错误 ", 0);

        } else {
            JOptionPane.showMessageDialog(null, "登录成功！", "成功", JOptionPane.PLAIN_MESSAGE);
            repeatGame(); // 复用重复开始
        }
    }
    // 重新开始游戏
    public static void repeatGame() {
        GamePanel panel = new GamePanel();
        contentPane.removeAll(); // 移除所有子组件
        contentPane.repaint();
        contentPane.add(panel);
        contentPane.revalidate();

        contentPane.add(new Nav(), BorderLayout.NORTH);

    }

    // 提示注册状态
    public static void renderRegister(Boolean state) {
        if (!state) {
            JOptionPane.showMessageDialog(null, "注册失败！", "错误 ", 0);
        } else {
            JOptionPane.showMessageDialog(null, "注册成功！", "成功", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public planeFrame() {

        setTitle("飞行射击类游戏");

        contentPane = getContentPane();

        pack();
    }

    public static void main(String[] args) {
        Login login = new Login(); // 注册页面

        planeFrame e1 = new planeFrame();
        contentPane.add(login.loginBox);

        e1.setSize(320, 500); // 固定高宽

        e1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        e1.setVisible(true);
    }
}
