package JavaTest.Flyer;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Java版飞机大战
 * */
public class GameStart extends JPanel {
    //游戏界面大小固定:400*700
    public static final int WIDTH = 400;
    public static final int HEIGHT = 700;

    //游戏启动的第一件事是，要从硬盘加载所有要用到的资源到内存中
    //而且仅在启动时加载一次（静态块）
    //缓存在程序中的所有资源，都会反复使用，仅保存一份（静态变量）

    public static BufferedImage start; //开始图片

    public static BufferedImage background; //背景图片

    public static BufferedImage airplane;

    public static BufferedImage bee;

    public static BufferedImage bullet;

    public static BufferedImage hero0;

    public static BufferedImage hero1;

    public static BufferedImage pause;

    public static BufferedImage over;

    static{//静态代码块，仅在类首次加载到方法区时执行一次，专门加载静态资源

        /*
         *Java中从硬盘中加载图片到内存中的方法:
         * ImageIO.read方法：专门从硬盘中加载图片的静态方法
         * 从当前类所在的路径下加载指定文件到程序中：
         * GameStart.class.getResource("文件名")
         * */
        try {
            background = ImageIO.read(GameStart.class.getResource("background.jpg"));
            airplane = ImageIO.read(GameStart.class.getResource("airplane.jpg"));
            bee = ImageIO.read(GameStart.class.getResource("bee.jpg"));
            bullet = ImageIO.read(GameStart.class.getResource("bullet.jpg"));
            hero0 = ImageIO.read(GameStart.class.getResource("hero0.jpg"));
            hero1 = ImageIO.read(GameStart.class.getResource("hero1.jpg"));
            pause = ImageIO.read(GameStart.class.getResource("pause.jpg"));
            over = ImageIO.read(GameStart.class.getResource("over.jpg"));
            start = ImageIO.read(GameStart.class.getResource("start.jpg"));

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 为游戏中的角色对象定义数据结构，包括
     * 1个英雄机对象
     * 1个储存所有敌人的对象的数组
     * 1个储存所有子弹的对象的数组
     * */
    public Hero hero = new Hero();
    /*
    * 问题：一个数组纪要敌机低级类型对象，又要保存蜜蜂类型对象
    * 解决：用父类型数组
    * */
    public Flyer[] flyers = {};
    public Bullet[] bullets = {};

//    定义游戏状态：当前状态是开始状态
    private int state = START;
//    定义游戏状态的备选项常量
    public static final int START = 0;
    public static final int RUNNING = 1;
    public static final int PAUSR = 2;
    public static final int OVER = 3;


//    重写绘制方法
    @Override
    public void paint(Graphics g) {
//        g.drawLine(10,10,100,100);//画一个坐标（10,10）到（100,100）的直线
//        g.drawRect(10,10,100,100);//画一个以坐标(10,10)为左上角，宽100，高100的矩形
//        g.drawOval(10,10,100,100);//画一个以坐标（10,10）为左上角，在100*100范围内画一个圆
//      绘制背景图片
        g.drawImage(background,0,0,null);
//        绘制英雄机
        paintHero(g);
//        批量绘制敌人
        painFlyers(g);
//        批量绘制子弹
        painBullets(g);
//        绘制分数和生命值
        paintScoreAndLife(g);

//        根据游戏的状态绘制不同的图片
        if(state == START){
            g.drawImage(start,WIDTH/2-start.getWidth()/2,(HEIGHT-start.getHeight())/2-100,null);
        }
        else if(state == PAUSR){
            g.drawImage(pause,WIDTH/2-pause.getWidth()/2,(HEIGHT-pause.getHeight())/2-100,null);
        }
        else if(state == OVER){
            g.drawImage(over,100,300,null);

        }

    }

//    绘制英雄机对象的方法
    public void paintHero(Graphics g){
        g.drawImage(hero.image,hero.x,hero.y,null);
    }
//    批量绘制所有的敌人机的方法
    public void painFlyers(Graphics g){
        for(int i =0;i<flyers.length;i++){
            g.drawImage(flyers[i].image,flyers[i].x,flyers[i].y,null);
        }
    }
//    批量绘制所有子弹对象的方法
    public void painBullets(Graphics g){
        for(int i =0;i<bullets.length;i++){
            g.drawImage(bullets[i].image,bullets[i].x,bullets[i].y,null);
        }
    }

//   动态随机生成敌人对象，每生成一个新敌人，flyers数组就要扩容1，然后将敌人放入数组的最后一个元素
    public void nextOne(){
        Random random = new Random();
        Flyer f = null;
        if(random.nextInt(20)==0){
//            当只有随机数取0时才创建一个蜜蜂
            f = new Bee();
        }
        else
            f = new Airplane();
//        先对flyers数组扩容1
        flyers = Arrays.copyOf(flyers,flyers.length+1);
//        将新的敌人放入数组末尾
        flyers[flyers.length-1] = f;

    }


//    获得英雄机对象发射的子弹对象，把新的子弹对象保存到子弹数组中统一管理
    public void shoot(){
        Bullet[] newBullets = hero.shoot();//获得英雄机返回的新子弹数组

//       根据返回新子弹的数量扩容子弹数量
        bullets = Arrays.copyOf(bullets,bullets.length+newBullets.length);

//        从newBullets数组中拷贝所有元素到bullets数组末尾
        System.arraycopy(newBullets,0,bullets
                ,bullets.length-newBullets.length,newBullets.length);
    }

//    遍历子弹和敌人进行碰撞检测，一但发生碰撞，敌人和子弹就都减少1
    public void bang(){
        for(int i = 0;i<bullets.length;i++){ //取出每一颗子弹
            for(int j = 0;j<flyers.length;j++){  //和每个敌人做检测

                if(Flyer.bang(bullets[i],flyers[j])){
                    //                    增加奖励
                    hero.getScore_Award(flyers[j]);
//                    1.如果发生碰撞，从敌人数组中删除删除被击中的敌机，并且使用数组最后一个
                    flyers[j] = flyers[flyers.length-1];
//                2.压缩数组，元素个数-1
                    flyers = Arrays.copyOf(flyers,flyers.length-1);
//                从子弹数组中删除击中敌人的子弹
                    bullets[i] = bullets[bullets.length-1];
                    bullets = Arrays.copyOf(bullets,bullets.length-1);
                    i--;//每发现一次碰撞，子弹就退一个元素，重新检测当前位置的新子弹

//                只要有敌人被击中，就要退出循环
                    break;
                }

            }
        }
    }

    public void paintScoreAndLife(Graphics g){
        int x = 10;
        int y = 15;
        Font font = new Font(Font.SANS_SERIF,Font.BOLD,14);
        g.setFont(font);
        g.drawString("Score:"+hero.getScore(),x,y);
        y += 20;
        g.drawString("Life:"+hero.getLife(),x,y);
    }



    //        游戏启动时要做的事
    public void action() {
        /*游戏开始时要定义鼠标的监听
        *
        * */
//        1.创建MouseAdapter匿名内部类
        MouseAdapter l = new MouseAdapter() {
            @Override
//            2.希望重写的鼠标新位置的x和y
            public void mouseMoved(MouseEvent e) {
                if(state==RUNNING){
//                    3.获得鼠标的新位置
                    int x = e.getX();
                    int y = e.getY();
//                4.将鼠标的位置传递给英雄机对象的move方法
                    hero.move(x,y);
                }

            }
//            鼠标单击的响应
            @Override
            public void mouseClicked(MouseEvent e) {
                if(state==START){ //只有处于START的状态下才能改为running状态
                    state = RUNNING;
                }else if(state==OVER){
                    state = START;
//                    从over到start状态要初始化游戏的数据
                    flyers = new Flyer[0];
                    bullets = new Bullet[0];
                    hero = new Hero();
                }
            }

//            鼠标移出的响应

            @Override
            public void mouseExited(MouseEvent e) {
                if(state == RUNNING){//仅当游戏处于running状态时鼠标移出才能响应
                    state = PAUSR;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(state==PAUSR){
                    state=RUNNING;
                }
            }
        };
//        5 要响应鼠标事件，必须将鼠标事件添加到程序的监听器
        this.addMouseMotionListener(l);//支持鼠标移动的事件 监听器

        this.addMouseListener(l);//支持鼠标单击

//        创建定时器对象
        Timer timer = new Timer();
//        调用定时器对象的schedule方法
        timer.schedule(new TimerTask() {
            private int runTimes = 0;
            @Override
            public void run() {
                if(state == RUNNING){
//                    自动创建对象
                    runTimes++;
                    if (runTimes % 50 == 0) {
                        nextOne();
                    }

//                遍历对象，调用每个对象的step方法，移动一次对象的位置
                    for (int i = 0; i < flyers.length; i++) {
                        flyers[i].step();
                    }

//                每300毫秒创建一次子弹
                    if (runTimes % 30 == 0) {
                        shoot();
                    }

//                遍历子弹数组中的每个子弹对象，移动位置
                    for (int i = 0; i < bullets.length; i++) {
                        bullets[i].step();
                    }
//                英雄机动画效果
                    hero.step();
//                碰撞检测
                    bang();
//                敌人和英雄机碰撞检测
                    hit();
//                越界检测
                    outOfBounds();

                }


                repaint();//强调，只要界面发生任何改变，必须调用repaint方法，重新绘制图形
            }

        }, 10, 10);
    }

    public static void main(String[] args){

        /**
         * Java中绘制窗体：JFrame对象
         * 绘制内容还需要嵌入背景面板：JPanel
         * */
        JFrame frame = new JFrame("Fly");

        frame.setSize(WIDTH,HEIGHT);

        frame.setAlwaysOnTop(true);//设置窗体总在最上方，不被其他窗体挡住

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置窗体关闭的同时，退出程序

        frame.setLocationRelativeTo(null);//设置窗体的初始位置，null为默认居中

        GameStart gameStart = new GameStart();//创建背景面板对象

        frame.add(gameStart);//讲背景面板对象嵌入到窗体对象中
//        窗体默认不可见 必须显示调用setVisible才能显示窗体
        frame.setVisible(true);//自动调用窗体的paint方法
        gameStart.action();

    }

    public void hit(){
        Flyer[] lives = new Flyer[flyers.length];
        //记录存活的敌人
        int index = 0;
        for (int i = 0;i<flyers.length;i++){
            if(!hero.hit(flyers[i])){
                lives[index] = flyers[i];
                index++;
            }
        }
        //压缩存活的敌人的数组，并替换敌人数组
        flyers = Arrays.copyOf(lives,index);

        if(hero.getLife()<=0){
            state = OVER;
        }
    }


//检查所有飞行物是否越界
    public void outOfBounds(){
        Flyer[] Flives = new Flyer[flyers.length];
//        设计Flives数组的计数器index:标记下一个存活对象的位置，统计lives数组中总共有多少存活对象
        int index = 0;
        for(int i = 0;i<flyers.length;i++){
            if(!flyers[i].outOfBounds()){
                Flives[index] = flyers[i];
                index++;
            }
        }//遍历结束后：
//        index中保存的是当前存活对象的个数
//        lives数组中保存的是当前存活的对象
//        对lives数组按照index中的个数进行压缩
//        压缩后的新数组要替换回flyers数组
        flyers = Arrays.copyOf(Flives,index);

//        检测所有子弹是否越界
        Bullet[] Blives = new Bullet[bullets.length];
        index = 0;
        for(int i = 0;i<bullets.length;i++){
            if(!bullets[i].outOfBounds()){
                Blives[index] = bullets[i];
                index++;
            }
        }
        bullets = Arrays.copyOf(Blives,index);
    }
}
