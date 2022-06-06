package UI;

import UI.Enemy;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

//import bean.Bullet;
//import bean.Enemy;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    private int mScreenWidth = 320; // 窗口宽度
    private int mScreenHeight = 480; // 窗口高度
    private static final int STATE_GAME = 0;
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
    private boolean mIsRunning = false; // 游戏在运行
    public int mAirPosX = 0; // 飞机坐标
    public int mAirPosY = 0;// 飞机坐标
    Enemy mEnemy[] = null; // 一组敌人
    Bullet mBuilet[] = null; // 一组子弹
    public int mSendId = 0; // 当前使用的子弹
    public Long mSendTime = 0L; // 发射时间
    Image myPlanePic[]; // 飞机状态（六张图片）
    public int myPlaneID = 0; // 当前飞机状态

    public GamePanel() {
        setPreferredSize(new Dimension(mScreenWidth, mScreenHeight));
        setFocusable(true);
        addKeyListener(this); // 事件作为当前对象处理
        init(); // 配置
        setGameState(STATE_GAME); //
        mIsRunning = true;
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
        }
    }

    // 配置
    private void init() {

        try {
            // 收入上下背景
            mBitMenuBG0 = Toolkit.getDefaultToolkit().getImage("images\\map_0.png");
            mBitMenuBG1 = Toolkit.getDefaultToolkit().getImage("images\\map_1.png");
            ImageIO.read(new File("images/map_1.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 上下怎么接都是一样的，这里把下图片位置定为程序上部
        mBitposY0 = 0;
        mBitposY1 = -mScreenHeight;
        mAirPosX = 150; // 飞机默认位置
        mAirPosY = 400; // 在400Y轴
        myPlanePic = new Image[6]; // 飞机状态
        for (int i = 0; i < 6; i++)
            myPlanePic[i] = Toolkit.getDefaultToolkit().getImage(
                    "images\\plan_" + i + ".png");
        mEnemy = new Enemy[ENEMY_POOL_COUNT];
        for (int i = 0; i < ENEMY_POOL_COUNT; i++) {
            mEnemy[i] = new Enemy();
            // 初次配置，分散一些
            mEnemy[i].init(i * ENEMY_POS_OFF, i * ENEMY_POS_OFF - 300);
        }

        mBuilet = new Bullet[BULLET_POOL_COUNT];
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
        for (int i = 0; i < BULLET_POOL_COUNT; i++)
            mBuilet[i].DrawBullet(g, this);
        for (int i = 0; i < ENEMY_POOL_COUNT; i++)
            mEnemy[i].DrawEnemy(g, this);
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

    public void Collision() {
        for (int i = 0; i < BULLET_POOL_COUNT; i++) {
            for (int j = 0; j < ENEMY_POOL_COUNT; j++) {
                if (mBuilet[i].m_posX >= mEnemy[j].m_posX
                        && mBuilet[i].m_posX <= mEnemy[j].m_posX + 30
                        && mBuilet[i].m_posY >= mEnemy[j].m_posY
                        && mBuilet[i].m_posY <= mEnemy[j].m_posY + 30
                ) {
                    mEnemy[j].mAnimState = Enemy.ENEMY_DEATH_STATE;
                }
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
                Thread.sleep(100);
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

        if(key == KeyEvent.VK_SPACE){

        }

    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }
}