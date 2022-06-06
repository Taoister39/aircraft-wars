package UI;

import DB.LoginData;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login {

    private JLabel userLabel = new JLabel("用 户:");
    private JTextField usernameField = new JTextField(10);
    private JLabel passLabel = new JLabel("密 码:");
    private JPasswordField passwordField = new JPasswordField(10);
    private JButton okBtn = new JButton("确定");
    private JButton registerBtn = new JButton("注册");
    public Box loginBox = null;
    public static String password;
    public static String username;

    public Login() {

        //定义面板封装文本框和标签
        JPanel panel01 = new JPanel(new FlowLayout(FlowLayout.CENTER));  //居中面板
        panel01.add(this.userLabel);
        panel01.add(this.usernameField);

        JPanel panel02 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel02.add(this.passLabel);
        panel02.add(this.passwordField);

        //定义面板封装按钮
        JPanel panel03 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel03.add(this.okBtn);
        panel03.add(this.registerBtn);

        //箱式布局装入三个面板
        this.loginBox = Box.createVerticalBox();
        this.loginBox.add(panel01);
        this.loginBox.add(panel02);
        this.loginBox.add(panel03);

        this.okBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                password = passwordField.getText();
                username = usernameField.getText();
                // 是否登入成功
                Boolean state = LoginData.loginSuccessful(username, password);
                planeFrame.mStartState = state;
                planeFrame.renderGameState(); // 更新组件（进入游戏界面）
                super.mouseClicked(e);
            }
        });
        this.registerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Boolean state = LoginData.registeredUser(usernameField.getText(), passwordField.getText());
                planeFrame.renderRegister(state);
                super.mouseClicked(e);
            }
        });
    }
}