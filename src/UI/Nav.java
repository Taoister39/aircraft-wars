package UI;

import DB.ScoreData;

import javax.swing.*;
import java.awt.event.*;

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
                planeFrame.repeatGame();
                super.mouseClicked(e);
            }
        });
        ranking.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ScoreFrame();
                super.mouseClicked(e);
            }
        });
    }
}
