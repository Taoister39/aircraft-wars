package assets;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BigEnemy extends Enemy {
    public static int bigEnemyWidth;
    public static int bigEnemyHeight;
    static final int ENEMY_STEP_Y = 1; // 敌人在Y轴移动速度
    private Image enemyExplorePic[] = new Image[6]; // 敌人被打中时的状态（图片）
    public int hp = 10; // 生命值
    public BigEnemy(){
        for (int i = 0; i < 6; i++)
            enemyExplorePic[i] = Toolkit.getDefaultToolkit().getImage(
                    "images\\bomb_enemy_" + i + ".png");
    }
    public void UpdateEnemy() {
        m_posY += ENEMY_STEP_Y;
    }

    @Override
    public void init(int x, int y) {
        super.init(x, y);
        hp = 10;
    }

    @Override
    public void DrawEnemy(Graphics g, JPanel i) {
        // 敌人被子弹打中了
        if (mAnimState == ENEMY_DEATH_STATE && mPlayID < 6) {
            System.out.println("ok");
            g.drawImage(enemyExplorePic[mPlayID], m_posX, m_posY,bigEnemyWidth,bigEnemyHeight, i);
            mPlayID++;
            return;
        }
        // 如果没有被打中的话，渲染完整的面貌
        Image pic = Toolkit.getDefaultToolkit().getImage("images/e1_0.png");
        bigEnemyWidth = pic.getWidth(null) * 4;
        bigEnemyHeight = pic.getHeight(null) * 4;
        g.drawImage(pic, m_posX, m_posY, bigEnemyWidth, bigEnemyHeight, i);
    }


}
