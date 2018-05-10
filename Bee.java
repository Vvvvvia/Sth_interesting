package JavaTest.Flyer;

import java.util.Random;

public class Bee extends Flyer {
    private int xspeed = 1; //水平速度
    private int yspeed = 2; //垂直速度
    private int awardType;//奖励类型：两种
//    奖励类型如下
    public static final int DOUBLE_FIRE = 0; //奖励双倍分值
    public static final int LIFE = 1; //奖励生命值加1
//对外提供的读取蜜粉的奖励类型的方法
    public int getAwardType() {
        return awardType;
    }
    public Bee(){
//        无参构造器
//        step1:从主程序获取蜜蜂图片的静态变量
        image = GameStart.bee;
//        step2:使用图片的宽高设置对象的宽高
        width = image.getWidth();
        height = image.getHeight();
//        step3:蜜蜂对象开始下落的位置是 -height
        y = -height;
//        step4:蜜蜂对象开始下落的x坐标在0到（界面宽度-蜜蜂宽度）之内随机
        Random random = new Random();
        x = random.nextInt(GameStart.WIDTH-width);
//        step5:随机选取一种奖励
        awardType=random.nextInt(2);
    }

    @Override
    public void step() {
        x += xspeed;
        y += yspeed;
//        如果蜜蜂超出边界就让xspeed*-1，相当于反向移动
        if(x<0||x>(GameStart.WIDTH-width)){
            xspeed *= -1;
        }

    }
    @Override
    public boolean outOfBounds() {
        return y>GameStart.HEIGHT;
    }
}
