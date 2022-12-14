package network5;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
public class arrowgame {
	public arrowgame()
    {
        Ex5 ex5 = new Ex5();
        ex5.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ex5.getContentPane().setLayout(null);
    }
}
class Ex5 extends JFrame{
    Ex5(){
        this.setTitle("���ּ��� ������");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GamePanel p = new GamePanel();
        this.add(p);
        
        this.setLocationRelativeTo(null);
        this.setSize(500,500);
        this.setResizable(false);
        this.setVisible(true);
        p.startGame();
    }
}

class GamePanel extends JPanel{
    TargetThread targetThread;
    JLabel base = new JLabel();
    JLabel bullet;
    JLabel target;
    //AudioClip sound;
    GamePanel(){
        this.setLayout(null);
        base.setSize(50,40);
        base.setOpaque(true);
        base.setBackground(Color.black);
        
        ImageIcon img = new ImageIcon("���ּ�.png");
        target = new JLabel(img);
        //�̹��� ũ�⸸ŭ ���̺� ũ�� ����
        target.setSize(img.getIconWidth(),img.getIconHeight());
        ImageIcon arrow = new ImageIcon("ȭ��.png");
        bullet =  new JLabel(arrow);
        bullet.setSize(arrow.getIconWidth(),arrow.getIconHeight());
        bullet.setOpaque(true);
        this.add(base);
        this.add(target);
        this.add(bullet);
        
    }
    public void startGame(){
        base.setLocation(this.getWidth()/2-20, this.getHeight()-40);
        bullet.setLocation(this.getWidth()/2-5, this.getHeight()-120);
        target.setLocation(0, 0);
        
        //Ÿ���� �����̴� ������
        targetThread = new TargetThread(target);
        targetThread.start();
        
        //���̽��� ������ �ΰ� ����Ű �Է¿� ���� bullet������ ����
        base.requestFocus();
        base.addKeyListener(new KeyListener(){
            BulletThread bulletThread = null;
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if(ke.getKeyChar()==KeyEvent.VK_ENTER){
                    //�����尡 �׾��ִ� �������� Ȯ��
                    if(bulletThread==null || !bulletThread.isAlive()){
                        //sound.play();
                        //�Ѿ˷� Ÿ���� ������� Ȯ���ϱ� ���� 2���� ���̺�� Ÿ�ٽ����带 �Ѱ��ش�.
                        bulletThread = new BulletThread(bullet,target,targetThread);
                        bulletThread.start();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
            
        });
    }
    
    class TargetThread extends Thread{
        JLabel target;
        TargetThread(JLabel target){
            this.target=target;
            target.setLocation(0, 0);
        }
        public void run(){
            while(true){
                int x=target.getX()+9;//9�ȼ��� �̵�
                int y=target.getY();
                
                //������ ������ �������
                if(x>GamePanel.this.getWidth())
                    target.setLocation(0, 0);
                //������ �ȿ� ������� 5�ȼ��� �̵�
                else
                    target.setLocation(x, y);
                
                //0.02�ʸ��� �̵�
                try{
                    sleep(20);
                }
                //�����尡 �װԵǸ� �ʱ� ��ġ�� ��ġ�ϰ�, 0.5�ʸ� ��ٸ���.
                catch(Exception e){
                    target.setLocation(0, 0);
                    try{
                        sleep(500);
                    }
                    catch(Exception e2){}
                }
            }
        }
    }
    
    class BulletThread extends Thread{
        JLabel bullet,target;
        Thread targetThread;
        
        public BulletThread(JLabel bullet, JLabel target, Thread targetThread){
            this.bullet=bullet;
            this.target=target;
            this.targetThread=targetThread;
        }
        
        public void run(){
            while(true){
                if(hit()){//Ÿ���� �¾Ҵٸ�
                    targetThread.interrupt();//Ÿ�� �����带 ���δ�.
                    //�Ѿ��� ���� ��ġ�� �̵�
                    bullet.setLocation(bullet.getParent().getWidth()/2-5, bullet.getParent().getHeight()-120);
                    return;//�Ѿ� �����嵵 ���δ�.
                }
                else{
                    int x=bullet.getX();
                    int y=bullet.getY()-9;//9�ȼ��� ���� �̵��Ѵ�.=�Ѿ� �ӵ��� 5�ȼ�
                    //�Ѿ��� ������ ������ ������ ��
                    if(y<0){
                        //�Ѿ� ���� ��ġ�� �̵�
                        bullet.setLocation(bullet.getParent().getWidth()/2-5, bullet.getParent().getHeight()-120);
                        return;//�Ѿ� �����带 ���δ�.
                    }
                    //�Ѿ��� ������ �ȿ� ������ 9�ȼ��� �̵��Ѵ�.
                    else
                        bullet.setLocation(x, y);
                }
                //0.02�ʸ��� 5�ȼ��� �̵�
                try{
                    sleep(20);
                }
                
                catch(Exception e){}
            }
        }
        
        private boolean hit(){
            int x=bullet.getX();
            int y=bullet.getY();
            int w=bullet.getWidth();
            int h=bullet.getHeight();
            
            if(targetContains(x,y)
                    ||targetContains(x+w-1,y)
                    ||targetContains(x+w-1,y+h-1)
                    ||targetContains(x,y+h-1))
                return true;
            else
                return false;
        }
        private boolean targetContains(int x, int y){
            //Ÿ���� x��ǥ�� �Ѿ� x��ǥ���� �۰ų� ������ �Ѿ� x��ǥ���� Ÿ�� x��ǥ + Ÿ���� ���� ���̰� ũ�� 
            if(((target.getX()<=x)&&(x<target.getX()+target.getWidth()))   
                    //Ÿ���� y��ǥ�� �Ѿ� y��ǥ���� �۰ų� ������ �Ѿ� y��ǥ���� Ÿ�� y��ǥ + Ÿ���� ���� ���̰� ũ��
                    &&((target.getY()<=y)&&(y<target.getY()+target.getHeight())))
                return true;
            
            else
                return false;
        }
    }
}