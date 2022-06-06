package UI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import javax.swing.JPanel;

//子弹类
public class Bullet {
    //    static final int BULLET_STEP_X = 3;
//    static final int BULLET_WIDTH = 40;
    static final int BULLET_STEP_Y = 15; // 子弹在Y轴速度
    public int m_posX = 0; // 子弹X坐标
    public int m_posY = 0; // 子弹Y坐标
    boolean mFacus = true; // 被发出
    private Image pic[] = null; // 状态（图片）数据
    private int mPlayID = 0; // 当前状态

    public Bullet() {
        pic = new Image[4];
        for (int i = 0; i < 4; i++)  // 只有四个状态
            pic[i] = Toolkit.getDefaultToolkit().getImage(
                    "images\\bullet_" + i + ".png");
    }

    // 初始化
    public void init(int x, int y) {
        m_posX = x;
        m_posY = y;
        mFacus = true; // 默认发射成功
    }

    // 渲染子弹
    public void DrawBullet(Graphics g, JPanel i) {
        // 打中了敌人，不需要渲染
        if(!mFacus){
            return;
        }
        g.drawImage(pic[mPlayID++], m_posX, m_posY, (ImageObserver) i);
        if (mPlayID == 4) mPlayID = 0;
    }

    // 更新子弹数据
    public void UpdateBullet() {
        if (mFacus) {
            m_posY -= BULLET_STEP_Y; // 子弹向上移动
        }

    }

}
