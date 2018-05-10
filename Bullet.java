package JavaTest.Flyer;

public class Bullet extends Flyer {
    private int speed = 3;//子弹每次上升3个单位

    public Bullet(int x,int y){
        this.x = x;
        this.y = y;
        image = GameStart.bullet;
        width = image.getWidth();
        height = image.getHeight();
    }

    @Override
    public void step() {
        y -= speed;

    }
    @Override
    public boolean outOfBounds() {
//        子弹的y坐标加上子弹的高度<0
        return y+height<0;
    }
}
