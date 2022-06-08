package UI;

import java.awt.*;
import java.util.Date;
import javax.swing.*;

public class PlaneFrame extends JFrame {
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
    /*
    * 重新开始游戏（有问题）
    * 2022-6-7
    * Qiacrkeng-Taoer
    * 杀掉线程也依然存在，当前线程的键盘事件监听器无法使用
    * */
    public static void repeatGame() {
        GamePanel.mIsRunning = false;
        if(GamePanel.mThread != null) {
            GamePanel.mThread.stop();
            GamePanel.mState = 1;
        }
        GamePanel panel = new GamePanel();
        GamePanel.firstGameStart = new Date().getTime();
        GamePanel.score = 0;
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

    public PlaneFrame() {

        setTitle("飞行射击类游戏");

        contentPane = getContentPane();

        pack();
    }

    public static void main(String[] args) {
        Login login = new Login(); // 注册页面

        PlaneFrame e1 = new PlaneFrame();
        contentPane.add(login.loginBox,BorderLayout.CENTER);

        e1.setSize(320, 500); // 固定高宽

        e1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        e1.setResizable(false);

        e1.setVisible(true);
    }
}
