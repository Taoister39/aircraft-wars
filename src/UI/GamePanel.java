package UI;

import DB.ScoreData;
import assets.Aircraft;
import assets.BigEnemy;
import assets.Bullet;
import assets.Enemy;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.MalformedURLException;
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
    public static int mState = STATE_GAME; // 当前游戏状态
    private Image mBitMenuBG0 = null; // 上背景
    private Image mBitMenuBG1 = null; // 下背景
    private int mBitposY0 = 0; // 上背景坐标
    private int mBitposY1 = 0; // 下背景坐标
    final static int BULLET_POOL_COUNT = 15; // 生成子弹数量
    final static int ENEMY_POOL_COUNT = 10; // 生成小飞机敌人数量
    final static int ENEMY_POS_OFF = 65; // 初次间隔
    public static Thread mThread = null; // 线程
    public static boolean mIsRunning = false; // 线程运行状态
    Enemy mEnemy[] = null; // 一组敌人
    Bullet mBuilet[] = null; // 一组子弹
    public int mSendId = 0; // 当前使用的子弹
    public Long mSendTime = 0L; // 发射时间
    Aircraft mAircraf = null;
    public static int score; // 分数统计
    public static long firstGameStart; // 开始时间
    BigEnemy mBigEnemy[] = null; // 大敌机
    final static int BIG_ENEMY_POOL_COUNT = 2; // 大敌机数量
    AudioClip attack = null; // 攻击声音
    AudioClip collision = null; // 碰撞声音

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

        try {
            File explosion = new File("audio/explosion.wav");
            File shot1 = new File("audio/shot1.wav");
            collision = Applet.newAudioClip(explosion.toURL());
            attack = Applet.newAudioClip(shot1.toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    // 游戏没有结束就继续画
    protected void Draw() {
        switch (mState) {
            case STATE_GAME:
                renderBg();
                updateBg();
                mState = mAircraf.lifeCount < 0 ? GAME_OVER : STATE_GAME;
                repaint(); // 重新绘制
                break;
            case GAME_OVER:
                showGameOver();
                setGameState(-1);
                break;
        }
    }

    // 显示失败
    private void showGameOver() {
        ScoreData.appendToScore(Login.username, Login.password, score, new Date().getTime() - firstGameStart);
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

        mAircraf = new Aircraft();
        mAircraf.init(150, 400); // 配置位置

        mBigEnemy = new BigEnemy[BIG_ENEMY_POOL_COUNT];
        for (int i = 0; i < BIG_ENEMY_POOL_COUNT; i++) {
            mBigEnemy[i] = new BigEnemy();
            mBigEnemy[i].init(i * ENEMY_POS_OFF, i * ENEMY_POS_OFF - 170);
            mBigEnemy[i].bullet.init(mBigEnemy[i].m_posX + 5, mBigEnemy[i].m_posY + 40);
        }

        mEnemy = new Enemy[ENEMY_POOL_COUNT]; // 五个敌人
        for (int i = 0; i < ENEMY_POOL_COUNT; i++) {
            mEnemy[i] = new Enemy();
            // 初次配置，分散一些
            mEnemy[i].init(i * ENEMY_POS_OFF, i * ENEMY_POS_OFF - 10);
            mEnemy[i].bullet.init(mEnemy[i].m_posX + 5, mEnemy[i].m_posY + 40);
        }

        mBuilet = new Bullet[BULLET_POOL_COUNT]; // 15个子弹
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
        mAircraf.renderPropeller();
    }

    // 绘制
    public void paint(Graphics g) {
        g.drawImage(mBitMenuBG0, 0, mBitposY0, this);
        g.drawImage(mBitMenuBG1, 0, mBitposY1, this);


        mAircraf.drawAircraft(g, this); // 绘制飞机
        // 绘制子弹
        for (int i = 0; i < BULLET_POOL_COUNT; i++)
            mBuilet[i].DrawBullet(g, this);
        // 绘制敌人
        for (int i = 0; i < ENEMY_POOL_COUNT; i++) {
            mEnemy[i].DrawEnemy(g, this);
            mEnemy[i].bullet.DrawBullet(g, this);
        }
        // 大敌机
        for (int i = 0; i < BIG_ENEMY_POOL_COUNT; i++) {
            mBigEnemy[i].DrawEnemy(g, this);
            mBigEnemy[i].bullet.DrawBullet(g, this);
        }
        g.drawString("生命: " + mAircraf.lifeCount, 0, 390);
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
            mBuilet[i].updateBullet();
        }
        for (int i = 0; i < ENEMY_POOL_COUNT; i++) {
            mEnemy[i].UpdateEnemy();
            mEnemy[i].bullet.updateBullet();
            mEnemy[i].bulletContinueSend(mScreenHeight);
            // 如果敌人被击败了，且过度动画都完毕。或是战机到最底处。就进行再次配置
            if (mEnemy[i].mAnimState == Enemy.ENEMY_DEATH_STATE
                    && mEnemy[i].mPlayID == 6
                    || mEnemy[i].m_posY >= mScreenHeight) {
                // 敌人战机随机出现
                mEnemy[i].init(UtilRandom(0, ENEMY_POOL_COUNT) * ENEMY_POS_OFF,
                        0);
                mEnemy[i].bullet.init(mEnemy[i].m_posX - 5, mEnemy[i].m_posY + 40);
            }
        }
        // 处理大敌机，逻辑相同
        for (int i = 0; i < BIG_ENEMY_POOL_COUNT; i++) {
            mBigEnemy[i].UpdateEnemy();
            mBigEnemy[i].bullet.updateBullet();
            mBigEnemy[i].bulletContinueSend(mScreenHeight);
            if (mBigEnemy[i].mAnimState == BigEnemy.ENEMY_DEATH_STATE
                    && mBigEnemy[i].mPlayID == 6
                    || mBigEnemy[i].m_posY >= mScreenHeight) {
                // 敌人战机随机出现
                mBigEnemy[i].init(UtilRandom(0, BIG_ENEMY_POOL_COUNT) * ENEMY_POS_OFF, -150);
                mBigEnemy[i].bullet.init(mBigEnemy[i].m_posX - 5, mBigEnemy[i].m_posY + 40);
            }
        }

        long now = System.currentTimeMillis();
        if (now - mSendTime >= mAircraf.PLAN_TIME && mBuilet[mSendId].mFacus) { // 间隔时间到了
            mBuilet[mSendId].init(mAircraf.mAirPosX - 5, mAircraf.mAirPosY - 40);
            mSendTime = now;
            mSendId++; // 刷新当前使用的子弹
        }
        if (mSendId >= BULLET_POOL_COUNT) mSendId = 0;
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
                    collision.play();
                    mEnemy[j].mAnimState = Enemy.ENEMY_DEATH_STATE;
                    mBuilet[i].mFacus = false; // 子弹打中敌人消失
                    score++;
                }
            }
            // 打中大敌机
            for (int j = 0; j < BIG_ENEMY_POOL_COUNT; j++) {
                if (mBuilet[i].mFacus && mBuilet[i].m_posX >= mBigEnemy[j].m_posX - 300
                        && mBuilet[i].m_posX <= mBigEnemy[j].m_posX + 300
                        && mBuilet[i].m_posY >= mBigEnemy[j].m_posY
                        && mBuilet[i].m_posY <= mBigEnemy[j].m_posY + 150) {
                    mBuilet[i].mFacus = false; // 子弹打中敌人消失
                    mBigEnemy[j].hp--;
                    // 当敌机血量为0
                    if (mBigEnemy[j].hp == 0) {
                        mBigEnemy[j].mAnimState = BigEnemy.ENEMY_DEATH_STATE;
                        score += 18;
                        collision.play();
                    }
                }
            }
        }
        // 无敌时间 -- 3s
        long nowTime = new Date().getTime();
        if (mAircraf.isInvincible(nowTime)) return;
        bumpIntoTheEnemy(nowTime);
        meWasHit(nowTime);
    }
    // 飞机被敌机打中
    public void meWasHit(long nowTime){
        for(int i = 0;i < ENEMY_POOL_COUNT;i++){
            if(mAircraf.mAirPosX >= mEnemy[i].bullet.m_posX - 30
                    && mAircraf.mAirPosX <= mEnemy[i].bullet.m_posX + 30
                    && mAircraf.mAirPosY == mEnemy[i].bullet.m_posY){
                mAircraf.inHurt(nowTime);
                mEnemy[i].bullet.init(mEnemy[i].m_posX + 5,mEnemy[i].m_posY + 40);
            }
        }
        for(int i = 0;i < BIG_ENEMY_POOL_COUNT;i++){
            if(mAircraf.mAirPosX >= mBigEnemy[i].bullet.m_posX - 30
                    && mAircraf.mAirPosX <= mBigEnemy[i].bullet.m_posX + 30
                    && mAircraf.mAirPosY == mBigEnemy[i].bullet.m_posY){
                mAircraf.inHurt(nowTime);
                mBigEnemy[i].bullet.init(mBigEnemy[i].m_posX + 5,mBigEnemy[i].m_posY + 40);
            }
        }
    }

    // 撞到了敌人
    public void bumpIntoTheEnemy(long nowTime) {
        // 当飞机撞到敌人时
        for (int i = 0; i < ENEMY_POOL_COUNT; i++) {
            if (mEnemy[i].m_posX >= mAircraf.mAirPosX - 10
                    && mEnemy[i].m_posX <= mAircraf.mAirPosX + 10
                    && mEnemy[i].m_posY + 35 >= mAircraf.mAirPosY
                    && mEnemy[i].m_posY - 35 <= mAircraf.mAirPosY
            ) {
                mAircraf.inHurt(nowTime);
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
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 按键事件
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        // 飞机向左
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            mAircraf.goLeft();
        }
        // 飞机向右
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            mAircraf.goRight(mScreenWidth);
        }

        if (key == KeyEvent.VK_SPACE && !mBuilet[mSendId].mFacus) {
            attack.play();
            mBuilet[mSendId].mFacus = true;
        }

    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }
}