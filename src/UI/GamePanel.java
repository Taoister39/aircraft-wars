package UI;

import DB.ScoreData;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.Random;

import javax.swing.*;

//import bean.Bullet;
//import bean.Enemy;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    private int mScreenWidth = 320; // 窗口宽度
    private int mScreenHeight = 480; // 窗口高度
    private static final int STATE_GAME = 0; // 游戏正常
    private static final int GAME_OVER = 1;
    private int mState = STATE_GAME; // 当前游戏状态
    private Image mBitMenuBG0 = null; // 上背景
    private Image mBitMenuBG1 = null; // 下背景
    private int mBitposY0 = 0; // 上背景坐标
    private int mBitposY1 = 0; // 下背景坐标
    final static int BULLET_POOL_COUNT = 15; // 生成子弹数量
    final static int PLAN_STEP = 10; // 飞机移动速度
    final static int PLAN_TIME = 500; // 安排子弹发射时间
    final static int ENEMY_POOL_COUNT = 5; // 生成敌人数量
    final static int ENEMY_POS_OFF = 65; // 初次间隔
    private Thread mThread = null; // 线程
    private boolean mIsRunning = false; // 线程运行状态
    public int mAirPosX = 0; // 飞机坐标
    public int mAirPosY = 0;// 飞机坐标
    Enemy mEnemy[] = null; // 一组敌人
    Bullet mBuilet[] = null; // 一组子弹
    public int mSendId = 0; // 当前使用的子弹
    public Long mSendTime = 0L; // 发射时间
    Image myPlanePic[]; // 飞机状态（六张图片）
    public int myPlaneID = 0; // 当前飞机状态
    public static int score = -BULLET_POOL_COUNT + 1; // 分数统计
    public static int lifeCount = 5; // 生命次数
    public static long firstGameStart = new Date().getTime();

    public GamePanel() {
        setPreferredSize(new Dimension(mScreenWidth, mScreenHeight));
        setFocusable(true);
        addKeyListener(this); // 事件作为当前对象处理
        init(); // 配置
        setGameState(STATE_GAME); // 初始游戏状态为活的
        mIsRunning = true; // 线程状态
        // 开启一个线程
        mThread = new Thread(this);
        mThread.start();
    }

    // 游戏没有结束就继续画
    protected void Draw() {
        switch (mState) {
            case STATE_GAME:
                renderBg();
                updateBg();
                break;
            case GAME_OVER:
                showGameOver();
                setGameState(-1);
                break;
        }
        if(lifeCount < 0 && mState == STATE_GAME){
            setGameState(GAME_OVER);
        }
    }
    private void showGameOver(){
        ScoreData.appendToScore(Login.username,Login.password,score,new Date().getTime() - firstGameStart);
        JOptionPane.showMessageDialog(null, "游戏结束", "失败 ", 0);

    }

    // 配置
    private void init() {
        // 收入上下背景
        mBitMenuBG0 = Toolkit.getDefaultToolkit().getImage("images\\map_0.png");
        mBitMenuBG1 = Toolkit.getDefaultToolkit().getImage("images\\map_1.png");
        // 上下怎么接都是一样的，这里把下图片位置定为程序上部
        mBitposY0 = 0;
        mBitposY1 = -mScreenHeight;
        mAirPosX = 150; // 飞机默认位置
        mAirPosY = 400; // 在400Y轴
        myPlanePic = new Image[6]; // 飞机状态
        for (int i = 0; i < 6; i++)
            myPlanePic[i] = Toolkit.getDefaultToolkit().getImage(
                    "images\\plan_" + i + ".png");
        mEnemy = new Enemy[ENEMY_POOL_COUNT]; // 五个敌人
        for (int i = 0; i < ENEMY_POOL_COUNT; i++) {
            mEnemy[i] = new Enemy();
            // 初次配置，分散一些
            mEnemy[i].init(i * ENEMY_POS_OFF, i * ENEMY_POS_OFF - 300);
        }

        mBuilet = new Bullet[BULLET_POOL_COUNT]; // 15各子弹
        for (int i = 0; i < BULLET_POOL_COUNT; i++) {
            mBuilet[i] = new Bullet();
        }
        mSendTime = System.currentTimeMillis(); // 当前时间为发射时间
    }

    // 设置游戏状态，是否结束游戏
    private void setGameState(int newState) {
        mState = newState;
    }

    // 渲染背景
    public void renderBg() {
        // 飞机的螺旋桨切换
        myPlaneID++;
        if (myPlaneID == 6)
            myPlaneID = 0;
        repaint(); // 重新绘制
    }

    // 绘制
    public void paint(Graphics g) {
        g.drawImage(mBitMenuBG0, 0, mBitposY0, this);
        g.drawImage(mBitMenuBG1, 0, mBitposY1, this);
        g.drawImage(myPlanePic[myPlaneID], mAirPosX, mAirPosY, this);
        // 绘制子弹
        for (int i = 0; i < BULLET_POOL_COUNT; i++)
            mBuilet[i].DrawBullet(g, this);
        // 绘制敌人
        for (int i = 0; i < ENEMY_POOL_COUNT; i++)
            mEnemy[i].DrawEnemy(g, this);

        g.drawString("生命: " + lifeCount, 0, 390);
        g.drawString("分数: " + score, 0, 410);
        g.drawString("时间: " + (new Date().getTime() - firstGameStart), 0, 430);
    }

    // 更新背景图片
    private void updateBg() {
        /* 上背景和下背景各自都向下 */
        mBitposY0 += 10;
        mBitposY1 += 10;
        /* 如果超出了窗口宽度 */
        if (mBitposY0 == mScreenHeight) {
            mBitposY0 = -mScreenHeight;
        }
        if (mBitposY1 == mScreenHeight) {
            mBitposY1 = -mScreenHeight;
        }
        for (int i = 0; i < BULLET_POOL_COUNT; i++) {
            mBuilet[i].UpdateBullet();
        }
        for (int i = 0; i < ENEMY_POOL_COUNT; i++) {
            mEnemy[i].UpdateEnemy();
            if (mEnemy[i].mAnimState == Enemy.ENEMY_DEATH_STATE
                    && mEnemy[i].mPlayID == 6
                    || mEnemy[i].m_posY >= mScreenHeight) {
                // 敌人战机随机出现
                mEnemy[i].init(UtilRandom(0, ENEMY_POOL_COUNT) * ENEMY_POS_OFF,
                        0);
            }
        }
        if (mSendId < BULLET_POOL_COUNT) {
            long now = System.currentTimeMillis();
            if (now - mSendTime >= PLAN_TIME) { // 间隔时间到了
                mBuilet[mSendId].init(mAirPosX - 5, mAirPosY - 40);
                mSendTime = now;
                mSendId++; // 刷新当前使用的子弹
            }
        } else {
            mSendId = 0;
        }
        Collision();
    }

    // 碰撞
    public void Collision() {
        // 当子弹打中敌人时
        for (int i = 0; i < BULLET_POOL_COUNT; i++) {
            for (int j = 0; j < ENEMY_POOL_COUNT; j++) {
                if (mBuilet[i].mFacus && mBuilet[i].m_posX >= mEnemy[j].m_posX - 30
                        && mBuilet[i].m_posX <= mEnemy[j].m_posX + 30
                        && mBuilet[i].m_posY >= mEnemy[j].m_posY
                        && mBuilet[i].m_posY <= mEnemy[j].m_posY + 50
                ) {
                    mEnemy[j].mAnimState = Enemy.ENEMY_DEATH_STATE;
                    mBuilet[i].mFacus = false; // 子弹打中敌人消失
                    score++;
                }
            }
        }
        // 当飞机撞到敌人时
        for (int i = 0; i < ENEMY_POOL_COUNT; i++) {
            if (mEnemy[i].m_posX >= mAirPosX - 10
                    && mEnemy[i].m_posX <= mAirPosX + 10
                    && mEnemy[i].m_posY + 35 == mAirPosY) {
                JOptionPane.showMessageDialog(null, "你损失了一条命", "不好 ", 0);
                lifeCount--;
            }
        }
    }

    private int UtilRandom(int botton, int top) {
        return ((Math.abs(new Random().nextInt()) % (top - botton)) + botton);
    }

    // 开启线程
    public void run() {
        while (mIsRunning) {
            Draw();
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 按键事件
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
//        System.out.println(key);
//        不可以上下移动
//        if (key == KeyEvent.VK_UP)
//            mAirPosY -= PLAN_STEP;
//        if (key == KeyEvent.VK_DOWN)
//            mAirPosY += PLAN_STEP;
        // 飞机向左
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            mAirPosX -= PLAN_STEP;
            if (mAirPosX < 0)
                mAirPosX = 0;
        }
        // 飞机向右
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            mAirPosX += PLAN_STEP;
            if (mAirPosX > mScreenWidth - 30)
                mAirPosX = mScreenWidth - 30;
        }

        if (key == KeyEvent.VK_SPACE) {

        }

    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }
}