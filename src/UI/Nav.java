package UI;

import javax.swing.*;
import java.awt.event.*;
// 导航栏
public class Nav extends JMenuBar {
    public static JMenu ranking; // 排行榜
    public static JMenu repeat; // 重玩

    public Nav() {
        ranking = new JMenu("排行榜");
        repeat = new JMenu("重玩");

        this.add(ranking);
        this.add(repeat);

        repeat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PlaneFrame.repeatGame();
                super.mouseClicked(e);
            }
        });
        ranking.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ScoreFrame(); // 单独作为窗口
                super.mouseClicked(e);
            }
        });
    }
}
