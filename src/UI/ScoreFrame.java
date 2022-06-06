package UI;

import DB.ScoreData;

import javax.swing.*;
import java.awt.*;
// 排行榜窗口
public class ScoreFrame extends JFrame {
    Container container = null;

    ScoreFrame() {
        this.setBounds(0, 100, 600, 300);
        container = new JPanel();

        this.setVisible(true);

        String[][] result = ScoreData.ResultQueryAll();

        container.setLayout(new GridLayout(10, 4,10,5));
        this.add(container,BorderLayout.CENTER);

        container.add(new JLabel("用户名"));
        container.add(new JLabel("密码"));
        container.add(new JLabel("分数"));
        container.add(new JLabel("时间"));

        for(int i = 0;i < result.length;i++){
            for(int j = 0;j < result[0].length;j++){
//                System.out.println(result[i][j]);
                if(result[i][j] == null){
                    break;
                }
                container.add(new JLabel(result[i][j]));
            }
        }
    }
}
