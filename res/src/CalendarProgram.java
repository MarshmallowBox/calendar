package src;

import java.awt.event.*;
import javax.swing.*;

import java.util.*;


public class CalendarProgram extends JFrame {
	//ProgramData
	Calendar now, sDay, bDay;	//����, selectDay, birthDay
	String[] weekdays = { "", "��", "��", "ȭ", "��", "��", "��", "��" };
	
	//GUI Data
	JTextField yearTextField, monthTextField;
	JLabel yearText, monthText;		//��, ��
	JLabel dayOfWeekDescriptionLabel;	// YYYY�� MM�� DD���� N�����Դϴ�. (ū �۾�)
	JLabel[] dayOfWeekArr;				// ��, ��, ȭ , �� , ��, �� , ��
	JLabel[] dayLabelArr;	// �Ϸ� �� JLabel ��ü: blank Label / 1~31�� Label = ����� ���� �� 42��
	JLabel dayAfterBirthDescriptionLabel;	//�¾ �� YYYY�� MM���� DD���� �Դϴ�.
	JLabel[] monthImageLabel = new JLabel[12];
	JButton refreshCalendarButton;	//�޷� �������� (getText/setText �̿��ϴ� Refresh() �̿��� ��)
	
	JPanel topPanel, centerPanel, bottomPanel;
	JPanel topPanelSub1, topPanelSub2, topPanelSub3;

	Font bigFont;		//ū �۾�
	
	//Event Listener
	MouseListener onClick;
	
	CalendarProgram() {
		//Calendar Variable �ʱ�ȭ
		now = Calendar.getInstance();
		sDay = Calendar.getInstance();
		bDay = Calendar.getInstance();
		
		//Event Listener �ʱ�ȭ
		onClick = new MouseOnClickListener();
		
		//GUI �ʱ�ȭ
		
		yearTextField = new JTextField(6);
		monthTextField = new JTextField(3);
		yearText = new JLabel("��");
		monthText = new JLabel("��");
		dayOfWeekDescriptionLabel = new JLabel(GetCalY(now) + "�� " + (GetCalM(now) + 1) + "�� " + GetCalD(now) + "���� " + GetCalW_S(now) + "�����Դϴ�.");
		dayOfWeekArr = new JLabel[7];
		for (int i = 0; i < 7; i++)	dayOfWeekArr[i] = new JLabel(weekdays[i + 1]);	//��, ��, ȭ, ��, ��, ��, �� �� �ʱ�ȭ
		dayLabelArr = new JLabel[42];
		dayLabelArr = GetCalendarPage(GetCalY(now), GetCalM(now) + 1);	
		dayAfterBirthDescriptionLabel = new JLabel("�¾ �� YYYY�� MM���� DD���� �Դϴ�.");
		for (int i = 0; i < 12; i++) monthImageLabel[i] = new JLabel(new ImageIcon("res/img/" + (i+1) + ".png"));
		refreshCalendarButton = new JButton("�� �޷� ����");
		refreshCalendarButton.addMouseListener(onClick);
		
		topPanel = new JPanel();
		topPanelSub1 = new JPanel();
		topPanelSub2 = new JPanel();
		topPanelSub3 = new JPanel();
		
		centerPanel = new JPanel();
		
		bottomPanel = new JPanel();
		
		bigFont = new Font("Gothic", Font.BOLD, 20);
		
		
		
		
		//Draw Calendar
		
		//test:�ӽ� ������
		//topPanelSub1.setBackground(new Color(255, 100, 100));
		//topPanelSub2.setBackground(new Color(100, 255, 100));
		//centerPanel.setBackground(new Color(100, 100, 255));
		//bottomPanel.setBackground(new Color(100, 255, 255));
		
		//topPanel
		topPanel.setLayout(new GridLayout(3,1));	//topPanel: topPanelSub1 + topPabelSub2
		topPanel.add(topPanelSub1);
		topPanel.add(topPanelSub2);
		topPanel.add(topPanelSub3);
		//	topPanelSub1
		topPanelSub1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		topPanelSub1.add(yearTextField);
		topPanelSub1.add(yearText);
		topPanelSub1.add(monthTextField);
		topPanelSub1.add(monthText);
		topPanelSub1.add(refreshCalendarButton);
		//	topPanelSub2
		topPanelSub2.setLayout(new FlowLayout());
		dayOfWeekDescriptionLabel.setFont(bigFont);	//�۾�ü�� bigFont��ü�� ��� �۾�ü�� ����
		topPanelSub2.add(dayOfWeekDescriptionLabel);
		//	topPanelSub3
		topPanelSub3.setLayout(new FlowLayout());
		topPanelSub3.add(monthImageLabel[GetCalM(now)]);
		
		//centerPanel
		centerPanel.setLayout(new GridLayout(7, 7));
		for (int i = 0; i < 7; i++) {
			dayOfWeekArr[i].setFont(bigFont);
			centerPanel.add(dayOfWeekArr[i]);
		}
		for(int i = 0; i < 42; i++) {
			dayLabelArr[i].setFont(bigFont);
			centerPanel.add(dayLabelArr[i]);
		}
		
		//bottomPanel
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(dayAfterBirthDescriptionLabel);
		
		this.setLayout(new BorderLayout()); 	//CalendarProgram(JFrame) ��ü�� ���̾ƿ�: BorderLayout
		this.add(topPanel, BorderLayout.NORTH);	//top ��ġ
		this.add(centerPanel, BorderLayout.CENTER);	//center ��ġ
		this.add(bottomPanel, BorderLayout.SOUTH);	//bottom ��ġ
		
		
	}
	
	
	
	int GetCalY(Calendar c) { return c.get(Calendar.YEAR); }
	int GetCalM(Calendar c) { return c.get(Calendar.MONTH); }		//0~11
	int GetCalD(Calendar c) { return c.get(Calendar.DATE); }
	int GetCalW(Calendar c) { return c.get(Calendar.DAY_OF_WEEK); }		//1234567 �Ͽ�ȭ�������
	String GetCalW_S(Calendar c) {
		String[] s = weekdays;
		return s[GetCalW(c)];
	}
	int GetFirstW(Calendar c) {	//c�� �� 1���� ������ ��ȯ
		Calendar c2 = Calendar.getInstance();		
		c2.set(GetCalY(c), GetCalM(c), 1);	//c2 = c�� ����� ���� 1�� Calendar
		return GetCalW(c2);
	}
	
	JLabel[] GetCalendarPage(int year, int month) {	//��, ���� �Է¹޾� �� ���� ������� ����Ͽ� �Բ� ��ȯ
		JLabel[] res = new JLabel[42];
		//���� ù���� ����, ���� ������ ��¥ �ʿ�
		int firstDatePos;	//���� 1���� ��ġ(���� - 1)
		int lastDate;	//���� ������ ��(��¥)
		int argCount = 0;	//arg�߰� ī��Ʈ
		Calendar c = Calendar.getInstance();
		
		c.set(year, month - 1, 1);	//Calendar�� �˰���� ���� 1�Ϸ� ����
		firstDatePos = GetCalW(c) - 1;
		c.set(year, month, 0);	//calendar�� �˰���� ���� ���Ϸ� ����
		lastDate = GetCalD(c);
		
		//res�� ��ġ
		for (int i = 0; i < firstDatePos; i++) {
			res[i] = new JLabel("");	//�� Label �߰�
			argCount++;
		}
		for (int i = 1; i <= lastDate; i++) {
			res[argCount] = new JLabel(Integer.toString(i));	//Label���� "i"�� Label �߰�
			argCount++;
		}
		for (; argCount < 42; argCount++) 
			res[argCount] = new JLabel("");	//�� Label �߰�
		
		
		System.out.println("Year="+year+",Month="+month+",firstDatePos="+firstDatePos+",lastDate="+lastDate+",resSize="+res.length);
		return res;
	}
	
	void UpdateCalendarPage() {
		int year, month;
		JLabel[] calLabel;
		
		year = Integer.parseInt(yearTextField.getText());		//year�� �Էµ� �� int������ �޾ƿ�
		month = Integer.parseInt(monthTextField.getText());		//month�� �Էµ� �� int������ �޾ƿ�
		calLabel = GetCalendarPage(year, month);	//calLabel[]�� Update�� ��¥�� �޾ƿ�
		
		//dayLabelArr[i]�� text�� calLabel�� text�� ��ü
		for(int i = 0; i < 42; i++)	dayLabelArr[i].setText(calLabel[i].getText());		
	}
	void UpdateDayOfWeekDescriptionLabel() {
		int year, month;		//*date�� ����
		year = Integer.parseInt(yearTextField.getText());		//year�� �Էµ� �� int������ �޾ƿ�
		month = Integer.parseInt(monthTextField.getText());		//month�� �Էµ� �� int������ �޾ƿ�
		sDay.set(year, month - 1, 1);	//*date�� 1�� ġȯ
		dayOfWeekDescriptionLabel.setText(GetCalY(sDay) + "�� " + (GetCalM(sDay)+1) + "�� " + GetCalD(sDay) + "���� " + GetCalW_S(sDay) + "�����Դϴ�.");
	}
	
	void UpdateCalendarImage() {
		remove(topPanelSub3);
		repaint();
		revalidate();
		topPanelSub3.add(monthImageLabel[GetCalM(sDay)]);
		repaint();
		revalidate();
		System.out.println("s");
	}
	
	
	public static void main(String[] args) {
		JFrame f = new CalendarProgram();
		f.setTitle("Calendar2_15050032");
		f.setSize(500, 500);
		//f.pack();
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		f.setVisible(true);
		
	}
	
	//�̺�Ʈ������ Inner class
	class MouseOnClickListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			System.out.println("Mouse Clicked");
			UpdateCalendarPage();
			UpdateDayOfWeekDescriptionLabel();
			UpdateCalendarImage();
		}
		
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) { }
		public void mousePressed(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
	}
	
}

