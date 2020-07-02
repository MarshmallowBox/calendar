package src;

import java.awt.event.*;
import javax.swing.*;

import java.util.*;


public class CalendarProgram extends JFrame {
	//ProgramData
	Calendar now, sDay, bDay;	//오늘, selectDay, birthDay
	String[] weekdays = { "", "일", "월", "화", "수", "목", "금", "토" };
	
	//GUI Data
	JTextField yearTextField, monthTextField;
	JLabel yearText, monthText;		//년, 월
	JLabel dayOfWeekDescriptionLabel;	// YYYY년 MM월 DD일은 N요일입니다. (큰 글씨)
	JLabel[] dayOfWeekArr;				// 일, 월, 화 , 수 , 목, 금 , 토
	JLabel[] dayLabelArr;	// 하루 의 JLabel 객체: blank Label / 1~31일 Label = 빈공간 포함 총 42개
	JLabel dayAfterBirthDescriptionLabel;	//태어난 지 YYYY년 MM개월 DD일차 입니다.
	JLabel[] monthImageLabel = new JLabel[12];
	JButton refreshCalendarButton;	//달력 리프레시 (getText/setText 이용하는 Refresh() 이용할 것)
	
	JPanel topPanel, centerPanel, bottomPanel;
	JPanel topPanelSub1, topPanelSub2, topPanelSub3;

	Font bigFont;		//큰 글씨
	
	//Event Listener
	MouseListener onClick;
	
	CalendarProgram() {
		//Calendar Variable 초기화
		now = Calendar.getInstance();
		sDay = Calendar.getInstance();
		bDay = Calendar.getInstance();
		
		//Event Listener 초기화
		onClick = new MouseOnClickListener();
		
		//GUI 초기화
		
		yearTextField = new JTextField(6);
		monthTextField = new JTextField(3);
		yearText = new JLabel("년");
		monthText = new JLabel("월");
		dayOfWeekDescriptionLabel = new JLabel(GetCalY(now) + "년 " + (GetCalM(now) + 1) + "월 " + GetCalD(now) + "일은 " + GetCalW_S(now) + "요일입니다.");
		dayOfWeekArr = new JLabel[7];
		for (int i = 0; i < 7; i++)	dayOfWeekArr[i] = new JLabel(weekdays[i + 1]);	//일, 월, 화, 수, 목, 금, 토 로 초기화
		dayLabelArr = new JLabel[42];
		dayLabelArr = GetCalendarPage(GetCalY(now), GetCalM(now) + 1);	
		dayAfterBirthDescriptionLabel = new JLabel("태어난 지 YYYY년 MM개월 DD일차 입니다.");
		for (int i = 0; i < 12; i++) monthImageLabel[i] = new JLabel(new ImageIcon("res/img/" + (i+1) + ".png"));
		refreshCalendarButton = new JButton("의 달력 보기");
		refreshCalendarButton.addMouseListener(onClick);
		
		topPanel = new JPanel();
		topPanelSub1 = new JPanel();
		topPanelSub2 = new JPanel();
		topPanelSub3 = new JPanel();
		
		centerPanel = new JPanel();
		
		bottomPanel = new JPanel();
		
		bigFont = new Font("Gothic", Font.BOLD, 20);
		
		
		
		
		//Draw Calendar
		
		//test:임시 색설정
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
		dayOfWeekDescriptionLabel.setFont(bigFont);	//글씨체를 bigFont객체에 담긴 글씨체로 설정
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
		
		this.setLayout(new BorderLayout()); 	//CalendarProgram(JFrame) 객체의 레이아웃: BorderLayout
		this.add(topPanel, BorderLayout.NORTH);	//top 배치
		this.add(centerPanel, BorderLayout.CENTER);	//center 배치
		this.add(bottomPanel, BorderLayout.SOUTH);	//bottom 배치
		
		
	}
	
	
	
	int GetCalY(Calendar c) { return c.get(Calendar.YEAR); }
	int GetCalM(Calendar c) { return c.get(Calendar.MONTH); }		//0~11
	int GetCalD(Calendar c) { return c.get(Calendar.DATE); }
	int GetCalW(Calendar c) { return c.get(Calendar.DAY_OF_WEEK); }		//1234567 일월화수목금토
	String GetCalW_S(Calendar c) {
		String[] s = weekdays;
		return s[GetCalW(c)];
	}
	int GetFirstW(Calendar c) {	//c의 월 1일의 요일을 반환
		Calendar c2 = Calendar.getInstance();		
		c2.set(GetCalY(c), GetCalM(c), 1);	//c2 = c의 년월을 가진 1일 Calendar
		return GetCalW(c2);
	}
	
	JLabel[] GetCalendarPage(int year, int month) {	//년, 월을 입력받아 그 달의 빈공간을 계산하여 함께 반환
		JLabel[] res = new JLabel[42];
		//달의 첫날의 요일, 달의 끝날의 날짜 필요
		int firstDatePos;	//달의 1일의 위치(요일 - 1)
		int lastDate;	//달의 말일의 값(날짜)
		int argCount = 0;	//arg추가 카운트
		Calendar c = Calendar.getInstance();
		
		c.set(year, month - 1, 1);	//Calendar를 알고싶은 달의 1일로 설정
		firstDatePos = GetCalW(c) - 1;
		c.set(year, month, 0);	//calendar를 알고싶은 달의 말일로 설정
		lastDate = GetCalD(c);
		
		//res에 배치
		for (int i = 0; i < firstDatePos; i++) {
			res[i] = new JLabel("");	//빈 Label 추가
			argCount++;
		}
		for (int i = 1; i <= lastDate; i++) {
			res[argCount] = new JLabel(Integer.toString(i));	//Label내용 "i"로 Label 추가
			argCount++;
		}
		for (; argCount < 42; argCount++) 
			res[argCount] = new JLabel("");	//빈 Label 추가
		
		
		System.out.println("Year="+year+",Month="+month+",firstDatePos="+firstDatePos+",lastDate="+lastDate+",resSize="+res.length);
		return res;
	}
	
	void UpdateCalendarPage() {
		int year, month;
		JLabel[] calLabel;
		
		year = Integer.parseInt(yearTextField.getText());		//year에 입력된 값 int형으로 받아옴
		month = Integer.parseInt(monthTextField.getText());		//month에 입력된 값 int형으로 받아옴
		calLabel = GetCalendarPage(year, month);	//calLabel[]에 Update할 날짜를 받아옴
		
		//dayLabelArr[i]의 text를 calLabel의 text로 대체
		for(int i = 0; i < 42; i++)	dayLabelArr[i].setText(calLabel[i].getText());		
	}
	void UpdateDayOfWeekDescriptionLabel() {
		int year, month;		//*date는 생략
		year = Integer.parseInt(yearTextField.getText());		//year에 입력된 값 int형으로 받아옴
		month = Integer.parseInt(monthTextField.getText());		//month에 입력된 값 int형으로 받아옴
		sDay.set(year, month - 1, 1);	//*date는 1로 치환
		dayOfWeekDescriptionLabel.setText(GetCalY(sDay) + "년 " + (GetCalM(sDay)+1) + "월 " + GetCalD(sDay) + "일은 " + GetCalW_S(sDay) + "요일입니다.");
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
	
	//이벤트리스너 Inner class
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

