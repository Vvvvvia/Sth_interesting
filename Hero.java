package JavaTest.Flyer;

import java.util.Random;

public class Hero extends Flyer {
    //    双倍火力
    private int doubleFire;
    private int life;
    private int score;

    public Hero(){
        image = GameStart.hero0;
        width = image.getWidth();
        height = image.getHeight();
        x = 150;
        y = 450;
        doubleFire = 0;
        life = 3;
        score = 0;
    }

//对外提供的读取生命值的方法
    public int getLife() {
        return this.life;
    }
//对外提供的读取得分的方法
    public int getScore() {
        return this.score;
    }


    @Override
    public void step() {
        Random random = new Random();
        if(random.nextInt(2)==0){
            image = GameStart.hero0;
        }
        else {
            image = GameStart.hero1;
        }

    }
    @Override
    public boolean outOfBounds() {
        return false;
    }
//    使英雄机移动
    void move(int x,int y){
//        传入的x和y是鼠标的坐标
//        move方法就是为了让英雄机的中心店和鼠标的位置一致
        this.x = x-width/2;
        this.y = y-height/2;
    }
//    获得分数和奖励的方法
    public void getScore_Award(Flyer f){   //upcast
//        先判断敌人对象的类型
        if(f instanceof Airplane){
            score+=((Airplane) f).getScore();
        }
        else {
            if (((Bee)f).getAwardType() == Bee.DOUBLE_FIRE){
                doubleFire+=40;
            }
            else {
                life+=1;
            }
        }
    }

//    发射子弹
    public Bullet[] shoot(){  //可以创建1发子弹或2发,所以用数组类型
        Bullet[] bullets = null;

        if (doubleFire!=0){
//            双倍火力
            bullets = new Bullet[2];
            Bullet b1 = new Bullet(x+width*1/7-GameStart.bullet.getWidth()/2
                    ,y-GameStart.bullet.getHeight());
            Bullet b2 = new Bullet(x+width*6/7-GameStart.bullet.getWidth()/2
                    ,y-GameStart.bullet.getHeight());
            bullets[0] = b1;
            bullets[1] = b2;

            doubleFire -=2;  //每创建一次双倍火力，让其子弹数减2
        }
        else {
            //        单倍火力
            bullets = new Bullet[1];
            bullets[0] = new Bullet(x+width/2-GameStart.bullet.getWidth()/2
                    ,y-GameStart.bullet.getHeight());
        }


        return bullets;
    }
//    碰撞检测方法,返回true说明碰撞
    public boolean hit(Flyer f){
        boolean r  = Flyer.bang(this,f);//检测是否碰撞
//        如果碰撞
        if(r){
            life--;
            doubleFire=0;
        }

        return r;
    }

}
