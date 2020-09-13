package lastproject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.PriorityQueue;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Mini_Game extends JFrame
{
   private static final long serialVersionUID = 1L;
   // 유저아이디
   String ID;
   // 저장
   String[] TableModel_Criteria = { "user", "point" };
   String[][] TableModel_List = { { "", "" }, { "", "" }, { "", "" }, { "", "" }, { "", "" }, { "", "" }, { "", "" }, { "", "" },{ "", "" }, { "", "" } }; 
   // 게임관련 숫자
   int POINT = 0;
   int score = 0;
   int count = 0;
   int gametime = 2000;
   // 각종 component
   JButton Start_button = new JButton("Start");
   JButton Rank_button = new JButton("순위보기");
   JButton Replay_button = new JButton("다시하기");
   JButton Save_button = new JButton("저장하기");
   JLabel TopLable = new JLabel("0");
   JLabel Point = new JLabel();
   // 패널 및 프레임
   frist_panel panel1 = new frist_panel();
   second_panel panel2 = new second_panel();
   last_panel panel3 = new last_panel();
   Container Main; // = this.getContentPane();
   // 쓰레드
   Bubble game = new Bubble();
   Start_time time = new Start_time();
   // 다이얼로그
   DefaultTableModel model = new DefaultTableModel(TableModel_List, TableModel_Criteria);
   JTable table = new JTable(model);
   // 저장,순위 다이얼로그
   Result Rank = new Result(this, "Rank");
   Save save = new Save(this, "Save");
   //자료구조
   PriorityQueue<Player_Point> queue = new PriorityQueue<Player_Point>();
   Stack<Player_Point> stack = new Stack<>();
   //순위표 테이블
   DefaultTableModel m;
   //이름,점수 객체
   Player_Point c;
   
   public Mini_Game() {
      this.setTitle("Bubble Pang");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      table.setPreferredScrollableViewportSize(new Dimension(200, 700));
      Main = this.getContentPane();
      Main.add(panel1);

      this.setSize(400, 600);
      this.setVisible(true);
   }

   class frist_panel extends JPanel // 첫번째 화면 구성
   {
      private static final long serialVersionUID = 1L;

      public frist_panel() {
         this.setLayout(null);
         MyAdapter myadapter = new MyAdapter();
         Start_button.addMouseListener(myadapter);
         this.add(Start_button);
         this.setBackground(Color.BLACK);
      }

      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         g.setColor(Color.RED);
         g.setFont(new Font("고딕체", Font.BOLD, 20));
         g.drawString("Bubble Pang", this.getWidth() / 3, this.getHeight() / 4);
         g.drawString(" Start Click the button to play the game.", this.getWidth() / 100, this.getHeight() / 3);
         g.drawString("       When the number of balloons", this.getWidth() / 100, (this.getHeight() / 3) + 20);
         g.drawString("           reaches three,  Gameover!!", this.getWidth() / 100, (this.getHeight() / 3) + 40);
         Start_button.setBounds((this.getWidth() / 8) * 3, (this.getHeight() / 4) * 2, 80, 30);
      }
   }

   class second_panel extends JPanel // 두번째 화면구성
   {
      private static final long serialVersionUID = 1L;

      public second_panel() {
         this.setLayout(null);
         this.add(TopLable);
         this.setBackground(Color.black);
      }

      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         TopLable.setForeground(Color.RED);
         TopLable.setBounds(this.getWidth() / 2, 0, 60, 60);
      }
   }

   class last_panel extends JPanel // 첫번째 화면 구성
   {
      private static final long serialVersionUID = 1L;

      public last_panel() {
         this.setLayout(null);
         MyAdapter ad = new MyAdapter();
         Rank_button.addMouseListener(ad);
         Replay_button.addMouseListener(ad);
         Save_button.addMouseListener(ad);

         this.add(Rank_button);
         this.add(Replay_button);
         this.add(Save_button);
         this.add(Point);
         this.setBackground(Color.black);
      }

      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         g.setColor(Color.RED);
         g.setFont(new Font("고딕체", Font.BOLD, 40));
         g.drawString(" gameover", this.getWidth() / 4, this.getHeight() / 2);
         Point.setForeground(Color.RED);
         Point.setBounds(this.getWidth() / 2, this.getHeight() / 4, 60, 60);
         Rank_button.setBounds((this.getWidth() / 3) - 85, this.getHeight() / 5 * 4, 100, 40);
         Replay_button.setBounds((this.getWidth() / 3) + 15, this.getHeight() / 5 * 4, 100, 40);
         Save_button.setBounds((this.getWidth() / 3) + 115, this.getHeight() / 5 * 4, 100, 40);
      }
   }

   class MyAdapter extends MouseAdapter // 마우스클릭 이벤트 매소드
   {
      public void mouseClicked(MouseEvent e) {
         JButton a = (JButton) e.getSource();
         if (a.getText().equals("Start") || (a.getText().equals("다시하기"))) {
            count = 0;
            gametime = 2000;
            score = 0;

            time = new Start_time();
            Main.removeAll();
            time.start();
            Main.add(panel2);
            Main.revalidate();
            Main.repaint();
         } else if (a.getText().equals("순위보기")) {
            Rank.setVisible(true);
         } else if (a.getText().equals("저장하기")) {
            save.setVisible(true);
         }
      }
   }

   class Start_time extends Thread // 게임시작 스레드
   {
      int i = 5;

      public void run() {

         while (true) {
            try {
            	TopLable.setText(Integer.toString(i));
               Thread.sleep(1000);
               i--;
               repaint();
               if (i == 0) {
                  panel2.remove(TopLable);
                  panel2.revalidate();
                  panel2.repaint();
                  game = new Bubble();
                  game.start();
                  return;
               }
            } catch (InterruptedException e) {
               return;
            }
         }
      }
   }

   class Bubble extends Thread // 게임 스레드
   {
      ImageIcon bubble = new ImageIcon("images/bubble.jpg");

      public void run() {
    	  TopLable.setBounds(180, 0, 60, 60);
         panel2.add(TopLable);
         TopLable.setText(Integer.toString(score) + "점");
         panel2.repaint();

         while (true) {
            int x = 0;
            int y = 0;
            x = ((int) (Math.random() * Main.getWidth()));
            y = ((int) (Math.random() * Main.getHeight()));
            if (Main.getWidth() - 30 < x && x <= Main.getWidth()) {
               x = x - 30;
            } else if (Main.getHeight() - 30 < y && y <= Main.getHeight()) {
               y = y - 30;
            }
            JLabel bub = new JLabel(bubble);
            bub.setBounds(x, y, 30, 30);
            panel2.add(bub); // 이 부분 때문에 2번째 패널의 점수 바로 옆 오른쪽에 버블 그림이 항상 표현됨.
                           // 그래서 버블이 FlowLayout으로 계속 표시되게 되어 있음.
            bub.addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent e) {
                  score++;
                  count--;
                  Component com = (Component) e.getSource();
                  panel2.remove(com);
                  panel2.revalidate();
                  panel2.repaint();
                  TopLable.setText(Integer.toString(score) + "점");
               }
            });
            try {
               count++;
               if (count == 3) {
                  panel2.removeAll();
                  TopLable.setBounds(180, 0, 60, 60);
                  TopLable.setText(Integer.toString(score) + "점");
                  panel2.add(TopLable);
                  panel2.repaint();
                  
                  Point.setText(Integer.toString(score) + "점");
                  
                  Main.removeAll();
                  Main.add(panel3);
                  Main.revalidate();
                  Main.repaint();
                  return;
               }
               repaint();

               Thread.sleep(gametime);
               if (gametime != 600) {
                  gametime = gametime - 200;
               }
            } catch (InterruptedException e) {
               return;
            }
         }
      }
   }

   class Result extends JDialog // 순위보기 다이얼로그
   {
      private static final long serialVersionUID = 1L;

      public Result(JFrame frame, String title) {
         super(frame, title);
         this.setLayout(new FlowLayout());
         table.setPreferredScrollableViewportSize(new Dimension(100, 250));
         JScrollPane sc = new JScrollPane(table);
         this.add(sc);
         this.setSize(250, 230);
      }
   }

   class Save extends JDialog // 저장하기 다이얼로그
   {
      private static final long serialVersionUID = 1L;

      private JTextField Input_TextField = new JTextField(10);
      private JButton Ok_Button = new JButton("OK");
      private JLabel Input_ID = new JLabel("ID");

      public Save(JFrame frame, String title) {
         super(frame, title, true); // true는 모달 타입을 만들도록 지시
         m = (DefaultTableModel) table.getModel();

         this.setLayout(new FlowLayout());
         this.add(Input_ID);
         this.add(Input_TextField);
         this.add(Ok_Button);
         
         Ok_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

               ID = Input_TextField.getText();
               queue.offer(new Player_Point(ID, score));
               for (int i = 0; i < queue.size(); i++) {
                  m.removeRow(0);
               }
               int i = 0;
               while(queue.size() != 0) {
                  ID = queue.peek().ID;
                  POINT = queue.peek().POINT;
                  m.insertRow(i, new Object[] { ID, POINT });
                  stack.push(queue.peek());
                  queue.poll();
                  i++;
               }
               while (!stack.isEmpty()) {
            	   queue.offer(stack.pop());
               }
               table.updateUI();
               Input_TextField.setText("");
               save.setVisible(false);
            }
         });
       
         this.setSize(200, 100);
      }
   }

   public class Player_Point implements Comparable<Player_Point>
   {
      String ID;
      int POINT;

      public Player_Point(String ID, int POINT) {
         super();
         this.ID = ID;
         this.POINT = POINT;
      }

      public int compareTo(Player_Point target) {
          return this.POINT <= target.POINT ? 1 : - 1;
      }
   }

   public static void main(String[] args)
   {
      new Mini_Game();
   }
}