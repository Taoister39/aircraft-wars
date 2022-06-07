package assets;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class Aircraft {
    public final int PLAN_STEP = 10; // 飞机移动速度
    public final int PLAN_TIME = 10; // 安排子弹发射时间
    public int mAirPosX = 0; // 飞机坐标
    public int mAirPosY = 0; // 飞机坐标
    private int myPlaneID = 0; // 当前飞机状态
    private Image pic[] = null; //飞机状态
    public int lifeCount = 5; // 生命次数
    private long invincibleTime = new Date().getTime(); // 无敌时间

    public Aircraft() {
        pic = new Image[6]; // 飞机状态
        for (int i = 0; i < 6; i++)
            pic[i] = Toolkit.getDefaultToolkit().getImage(
                    "images\\plan_" + i + ".png");
    }

    // 配置位置
    public void init(int x, int y) {
        mAirPosX = x;
        mAirPosY = y;
    }

    // 渲染螺旋桨
    public void renderPropeller() {
        // 飞机的螺旋桨切换
        myPlaneID++;
        if (myPlaneID == 6)
            myPlaneID = 0;
    }

    // 往左
    public void goLeft() {
        mAirPosX -= PLAN_STEP;
        if (mAirPosX < 0)
            mAirPosX = 0;
    }

    // 往右
    public void goRight(int boundary) { // 边界
        mAirPosX += PLAN_STEP;
        if (mAirPosX > boundary - 30)
            mAirPosX = boundary - 30;
    }

    // 获取当前状态
    public Image getCurrentState() {
        return pic[myPlaneID];
    }

    // 是否为无敌
    public Boolean isInvincible(long now) {
        // 无敌时间 -- 3s
        if (now - invincibleTime < 3000) {
            return true;
        }
        return false;
    }
    // 战机受到伤害
    public void inHurt(long nowTime){
        changeInvincible(nowTime);
        JOptionPane.showMessageDialog(null, "你损失了一条命", "不好 ", 0);
        lifeCount--;
    }

    // 更新为无敌状态
    public void changeInvincible(long now) {
        invincibleTime = now;
    }

    public void drawAircraft(Graphics g, JPanel p) {
        g.drawImage(getCurrentState(), mAirPosX, mAirPosY, p);
    }
}
