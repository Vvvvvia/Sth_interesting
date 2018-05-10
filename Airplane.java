package JavaTest.Flyer;

import java.util.Random;

public class Airplane extends Flyer{
    private int speed = 2; //下落速度
    private int score = 1; //被击毁的分值

    public Airplane(){
        image = GameStart.airplane;
        width = image.getWidth();
        height = image.getHeight();
        y = -height;
        Random random = new Random();
        x = random.nextInt(GameStart.WIDTH-width);
    }

    public int getScore() {
        return score;
    }
    @Override
    public void step() {
//    每次向下移动一个speed
        y += speed;
    }
    @Override
    public boolean outOfBounds() {
//        敌机y坐标大于游戏界面高度
        return y>GameStart.HEIGHT;
    }


}
