package UI;

import DB.ScoreData;

import javax.swing.*;
import java.awt.*;
// 排行榜窗口
public class ScoreFrame extends JFrame {
    Container container = null;

    ScoreFrame() {
        this.setBounds(0, 100, 600, 300);

        this.setVisible(true);

        String[][] result = ScoreData.ResultQueryAll();
        // 查询有多少行，再加上标题一行
        container = new JPanel(new GridLayout(ScoreData.resultCount + 1,result[0].length,10,5));
        this.add(container,BorderLayout.CENTER);

        container.add(new JLabel("用户名"));
        container.add(new JLabel("密码"));
        container.add(new JLabel("分数"));
        container.add(new JLabel("时间"));

        for(int i = 0;i < ScoreData.resultCount;i++){
            for(int j = 0;j < result[0].length;j++){
                container.add(new JLabel(result[i][j]));
            }
        }
    }
}
