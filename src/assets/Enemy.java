package assets;

import UI.GamePanel;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

// 敌人
public class Enemy {
    /* 敌人是死是活 */
    public static final int ENEMY_ALIVE_STATE = 0;
    public static final int ENEMY_DEATH_STATE = 1;
    static final int ENEMY_STEP_Y = 5; // 敌人在Y轴移动速度
    public int m_posX = -100; // 敌人坐标
    public int m_posY = -100; // 敌人坐标
    public int mAnimState = ENEMY_ALIVE_STATE;   // 默认当前状态为存活
    private Image enemyExplorePic[] = new Image[6]; // 敌人被打中时的状态（图片）
    public int mPlayID = 0;
    public Bullet bullet = null;

    public Enemy() {
        for (int i = 0; i < 6; i++)
            enemyExplorePic[i] = Toolkit.getDefaultToolkit().getImage(
                    "images\\bomb_enemy_" + i + ".png");
    }

    // 配置
    public void init(int x, int y) {
        m_posX = x;
        m_posY = y;
        mAnimState = ENEMY_ALIVE_STATE;
        mPlayID = 0;
        bullet = new Bullet();
        bullet.BULLET_STEP_Y = -15;
    }
    // 渲染敌人
    public void DrawEnemy(Graphics g, JPanel i) {
        // 敌人被子弹打中了
        if (mAnimState == ENEMY_DEATH_STATE && mPlayID < 6) {
            g.drawImage(enemyExplorePic[mPlayID], m_posX, m_posY, i);
            mPlayID++;
            return;
        }
        // 如果没有被打中的话，渲染完整的面貌
        Image pic = Toolkit.getDefaultToolkit().getImage("images/e1_0.png");
        g.drawImage(pic, m_posX, m_posY, (ImageObserver) i);
    }
    // 敌人向下移动
    public void UpdateEnemy() {
        m_posY += ENEMY_STEP_Y;
    }
    // 子弹继续发送
    public void bulletContinueSend(int boundary){
        if(bullet.m_posY > boundary){
            bullet.init(m_posX + 5,m_posY + 40);
        }
    }
}
