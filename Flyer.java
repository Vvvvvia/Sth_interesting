package JavaTest.Flyer;

import java.awt.image.BufferedImage;

/**
 *父类Flyer：把对象都设计为飞行物，拥有五个共同的属性
 * @param : width：宽度
 * @param : height：高度
 * @param : x:横坐标
 * @param : y:纵坐标
 * @param : image:图形
 */
public abstract class Flyer {
    protected int width;
    protected int height;
    protected int x;
    protected int y;
    protected BufferedImage image;

    public Flyer() {
    }
//要求所有的飞行物都能移动，但是移动的具体方式由子类自己实现
    public abstract void step();
//检查子类的对象是否越界
    public abstract boolean outOfBounds();
//检测是否碰撞
    public static boolean bang(Flyer f1,Flyer f2){
//        检测两个矩形飞行物是否碰撞的工具方法
//        1.求出两个矩形的中心点
        int f1x = f1.x+f1.width/2;
        int f1y = f1.y+f1.height/2;
        int f2x = f2.x+f2.width/2;
        int f2y = f2.y+f2.height/2;
//        2.横向和纵向碰撞检测
        boolean H = Math.abs(f1x - f2x)<(f1.width+f2.width)/2;
        boolean V = Math.abs(f1y-f2y)<(f1.height+f2.height)/2;
//        必须两个方向都碰撞了才能说明真的碰撞
        return H&V;
    }
}
