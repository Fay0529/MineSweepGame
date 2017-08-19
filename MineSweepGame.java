package mineSweepGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MineSweepGame {
//	������Ҫ�����
	private JFrame frame;
	private Container contentPane;
	private JPanel timePanel,gamePanel,menuPanel;
	private JLabel timeLabel,resultLabel,mineCountLabel;
	private JMenuItem menuItem1,menuItem2,menuItem3,menuItem4;
	private JButton [][] buttons;
	private int [][] buttonValues;
	private boolean [][]buttonFlag;
	private int timeLength=0;//ʱ��
	private int col=9,row=9;//��������
	private int mineCount=10;//ʣ�������
	private int mineRealCount=10;//��ʵʣ�������
	private boolean winGame=false;//״̬��ʱ��Ӯ���ⳡ��Ϸ
	private int  gameStatus=0;//״̬����Ϸ�Ƿ�ʼ��0��ʾδ��ʼ��1��ʾ��ʼ
	private Timer timer;//��ʱ��
	private int level=1;//��Ϸ�Ѷȣ�����1���м�2
	public MineSweepGame() {
//		���캯��
		frame=new JFrame("Fay��ɨ����Ϸ");
		contentPane=frame.getContentPane();
		timePanel=new JPanel();
		menuPanel=new JPanel();
		gamePanel=new JPanel();
		menuItem1=new JMenuItem("����");
		menuItem2=new JMenuItem("�м�");
		menuItem3=new JMenuItem("�߼�");
		menuItem4=new JMenuItem("����һ��");
		timeLabel=new JLabel("  ��Ϸʱ�䣺 "+timeLength+" �� ");
		resultLabel=new JLabel(" ״̬�������ʼ��Ϸ��");
		mineCountLabel=new JLabel("  ʣ��������� "+mineCount);
		this.initButtonsAllValues();
		timer =new Timer(1000,new TimeActionListener());
	}
	//��buttons���г�ʼ���ķ���
	public  void initButtonsAllValues() {
		buttons=new JButton[row+2][col+2];
		buttonValues= new int[row+2][col+2];
		buttonFlag= new boolean[row+2][col+2];
		
		for(int i=0;i<row+2;i++) {
			for(int j=0;j<col+2;j++) {
				buttons[i][j]=new JButton();
				buttons[i][j].setMargin(new Insets(0,0,0,0));
				buttons[i][j].setFont(new Font(null,Font.BOLD,25));
				buttons[i][j].setText("");
				buttonValues[i][j]=0;
			}
		}
		
	}
//	��������в���
	public void initGame() {
		JMenuBar menuBar=new JMenuBar();
		JMenu menu = new JMenu("��Ϸ����");
		menuBar.add(menu);
		menu.add(menuItem1);
		menu.add(menuItem2);
		menu.add(menuItem3);
		menu.add(menuItem4);
		menuPanel.add(menuBar);
		
		timePanel.add(timeLabel);
		timePanel.add(mineCountLabel);
		timePanel.add(resultLabel);
		
		gamePanel.setLayout(new GridLayout(row, col,0,0));
		for(int i=1;i<=row;i++)
			for(int j=1;j<=col;j++) {
				gamePanel.add(buttons[i][j]);
				
			}
		JPanel panel=new JPanel(new BorderLayout());
		panel.add(menuPanel,BorderLayout.NORTH);
		panel.add(timePanel,BorderLayout.SOUTH);
		contentPane.add(panel,BorderLayout.NORTH);
		contentPane.add(gamePanel,BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(297,377);
		frame.setBounds(400,100,400,500);
		frame.setVisible(true);
		setMines(mineRealCount);
		setButtonValue();
		addListener();
		
	}
	
//	���õ���,10��ʾ���ף�0-8��ʾ��λ�ø����ĵ�����
	public void setMines(int mineCount) {
		this.mineCount=mineCount;
//		���������
		int[] randomValues=new int[mineCount];
		for(int i=0;i<mineCount;i++) {
			int temp= (int)(Math.random()*col*row);
			for(int j=0;j<randomValues.length;j++) {
				if(temp==randomValues[j]) {
					temp=(int)(Math.random()*col*row);
				}
			}
			randomValues[i]=temp;
			int x=temp/col+1;
			int y=temp%col+1;
			buttonValues[x][y]=10;
//			buttons[x][y].setText(" ");
		}
		
		
	}
//	������Χ������
	public void setButtonValue() {
		for(int i=1;i<=row;i++) {
			for(int j=1;j<=col;j++) {
				if(buttonValues[i][j]!=10) {
					for(int x=j-1;x<=j+1;x++) {
						if(buttonValues[i-1][x]==10) {
							buttonValues[i][j]++;
						}
						if(buttonValues[i+1][x]==10) {
							buttonValues[i][j]++;
						}
						
					}
					if(buttonValues[i][j-1]==10)
						buttonValues[i][j]++;
					if(buttonValues[i][j+1]==10)
						buttonValues[i][j]++;
//					buttons[i][j].setText(new Integer(buttonValues[i][j]).toString());
//					buttons[i][j].setText("");
				}
			
			}
		}
	}
	
//	������֡����ס��հ�
	public void markNumber(int i,int j) {
		buttons[i][j].setText(new Integer(buttonValues[i][j]).toString());
		buttons[i][j].setEnabled(false);
		buttonFlag[i][j]=true;
	}
	public void markMine(int i,int j) {
		buttons[i][j].setText("Q");
		buttons[i][j].setEnabled(false);
		buttonFlag[i][j]=true;
	}
	public void markZero(int i,int j) {
		
		if(buttonFlag[i][j]==true) {
			return ;
		}
		buttons[i][j].setEnabled(false);
		buttonFlag[i][j]=true;
		if(buttonValues[i][j]!=10&&buttonValues[i][j]!=0) {
			markNumber(i,j);
		}else if(buttonValues[i][j]==0) {
			buttons[i][j].setText("");
			for(int x=i-1;x>=0&&x<i+2&&x<=row;x++) {
				for(int y=j-1;y>=0&&y<j+2&&y<=col;y++) {
					markZero(x,y);
				}
			}
		}
		

	}
//	�����������ť�����¼�
	class ButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(gameStatus==0) {
				timer.start();
				resultLabel.setText(" ״̬����Ϸ��  ");
				gameStatus=1;
			}
			
			for(int i=1;i<=row;i++) {
				for(int j=1;j<=col;j++)
				{
					if(arg0.getSource()==buttons[i][j]) {
						if(buttons[i][j].getText().equals("Q")) {
							if(buttonValues[i][j]==10) {
								markMine(i,j);
								stopGame();
								return;
							}
							else if(buttonValues[i][j]==0) {
								markZero(i,j);
								mineCount++;
								mineCountLabel.setText("  ʣ��������� "+mineCount);
								isWinner();
							}
							else{
								markNumber(i,j);
								mineCount++;
								mineCountLabel.setText("  ʣ��������� "+mineCount);
								isWinner();
							}
							
						}
						else {
							if(buttonValues[i][j]==10) {
								markMine(i,j);
								stopGame();
								return;
							}
							else if(buttonValues[i][j]==0) {
								markZero(i,j);
							}
							else{
								markNumber(i,j);
							}
						}
					return;
					}
				}
			}
		}
		
	}
	//�����¼�������
	public void addListener() 
	{
//	Ϊ��ť����¼�������
		for(int i=1;i<=row;i++) {
			for(int j=1;j<=col;j++) {
				buttons[i][j].addActionListener(new ButtonActionListener());
				buttons[i][j].addMouseListener(new FindMineMouseListener());
			}
		}
//		Ϊ�˵�������¼�������
		menuItem1.addActionListener(new MenuItemListener());
		menuItem2.addActionListener(new MenuItemListener());
		menuItem3.addActionListener(new MenuItemListener());
	}
//	�Ҽ���ǵ���
	public void findMine(int i,int j) {
		if(buttonFlag[i][j]==false) {
			buttonFlag[i][j]=true;
			buttons[i][j].setForeground(Color.red);
			buttons[i][j].setText("Q");
			mineCount--;
			if(buttonValues[i][j]==10)
				mineRealCount--;
			mineCountLabel.setText("  ʣ��������� "+mineCount);
			isWinner();
		}
		
	}
	class FindMineMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if(gameStatus==0) {
				timer.start();
				resultLabel.setText(" ״̬����Ϸ��  ");
				gameStatus=1;
			}
			
			for(int i=1;i<=row;i++) {
				for(int j=1;j<=col;j++) {
					if(e.getSource()==buttons[i][j]&&e.getButton()==MouseEvent.BUTTON3) {
						findMine(i,j);
						return;
					}
				}
			}
			}
		
	}
	//������������Ϸ
	public void  stopGame() {
		for(int i=1;i<=row;i++) {
			for(int j=1;j<=col;j++) {
//				�����е�δ����İ�ť��ʾ����
				if(buttonFlag[i][j]==false) {
					if(buttonValues[i][j]==10) {
						buttons[i][j].setText("Q");
					}
					else if(buttonValues[i][j]==0) {
						buttons[i][j].setText("");
					}
					else{
						buttons[i][j].setText(new Integer(buttonValues[i][j]).toString());
					}					
				}
				buttons[i][j].setEnabled(false);
				
			}
			
		}
		if(winGame) {
//			resultLabel.setText("	״̬����ϲ�㣬Ӯ�ˣ�  ");
			JOptionPane.showMessageDialog(null,"��ϲ��Ӯ�ˣ�"); 
		}
		else {
//			resultLabel.setText("	״̬����ȵ������ˣ����ˣ�  ");
			JOptionPane.showMessageDialog(null,"��ȵ������ˣ����ˣ�"); 
		}
		timer.stop();
		
	}
//	�жϵ�ǰ��û�л�ʤ
	public void isWinner() {
		if(mineRealCount==0&&mineCount==0)
		{	winGame=true;
			stopGame();
		}
	}
//	��ʱ����������
	class TimeActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			timeLength++;
			timeLabel.setText("  ��Ϸʱ�䣺 "+new Integer(timeLength).toString()+" �� ");
		}
		
	}
//	ˢ����Ϸ����
	public void refreshGamePanel() {
		contentPane.remove(gamePanel);
		gamePanel=new JPanel();
		this.initButtonsAllValues();
		gamePanel.setLayout(new GridLayout(row, col,0,0));
		for(int i=1;i<=row;i++)
			for(int j=1;j<=col;j++) {
				gamePanel.add(buttons[i][j]);
				
			}
		timeLabel.setText("  ��Ϸʱ�䣺 "+timeLength+" �� ");
		resultLabel.setText("	״̬�������ʼ��Ϸ��  ");
		mineCountLabel.setText("  ʣ��������� "+mineCount);
		contentPane.add(gamePanel,BorderLayout.CENTER);
		
		setMines(mineRealCount);
		setButtonValue();
		addListener();
	}
//	���¿�ʼ��Ϸ
	public void relayoutGame(int level) {
		timer.stop();
		gameStatus=0;
		timeLength=0;
		winGame=false;
		if(level==1) {
			this.mineCount=10;
			this.mineRealCount=10;
			this.row=9;
			this.col=9;
			refreshGamePanel();
		}
		else if(level==2) {
			this.mineCount=20;
			this.mineRealCount=20;
			this.row=12;
			this.col=12;
			refreshGamePanel();
		}
		else {
			this.mineCount=30;
			this.mineRealCount=30;
			this.row=15;
			this.col=15;
			refreshGamePanel();
		}
	}
	
	class MenuItemListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.getSource()==menuItem1) {
				
				level=1;
				relayoutGame(1);
			}
			else if(arg0.getSource()==menuItem2) {
				level=2;
				relayoutGame(2);
			}
			else if(arg0.getSource()==menuItem3){
				level=3;
				relayoutGame(3);
			}
			else {
				relayoutGame(level);
			}
			
		}
		
	}
}
