package com.zkteco.biometric;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import lixco.com.entity.FoodNhaAn;
import lixco.com.entity.OrderFood;
import lixco.com.staticentity.DateUtil;
import lixco.com.staticentity.FoodCustom;
import lixco.com.staticentity.Shifts;
import lixco.com.staticentity.URL;
import lixco.com.trong.DepartmentData;
import lixco.com.trong.DepartmentDataService;
import lixco.com.trong.TimekeepingData;
import lixco.com.trong.TimekeepingDataService;
import lixco.com.vantay.LoadVanTay;
import lixco.com.vantay.Template;

public class ZKFPDemo extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int TIME_VISIBLE = 3000;
	private static boolean ON_ALLOW_EXCEPTION = false;

	private static BufferedImage bimgNoImageNV = null;
	private static BufferedImage bimgTuchon = null;
	private static BufferedImage bimgNotImage = null;

	JButton btnOpen = null;
	JButton btnEnroll = null;
	JButton btnVerify = null;
	JButton btnIdentify = null;
	JButton btnRegImg = null;
	JButton btnIdentImg = null;
	JButton btnClose = null;
	JButton btnImg = null;
	JRadioButton radioISO = null;
	JRadioButton radioANSI = null;
	JRadioButton radioZK = null;
	JButton btnRefesh = null;
	// JButton btnhinh = null;

	JTextField textFieldMaTheTu;

	JLabel labelMaNV;
	JTextArea textAreaMaNV;
	JLabel labelTenNV;
	JTextArea textAreaTenNV;
	JPanel jpanel;
	JLabel labelPhongban;
	JTextArea textAreaPhongban;
	JLabel labelNgay;
	JTextArea textAreaNgay;
	JLabel labelMonAn;
	JTextArea textAreaMonAn;
	JLabel labelThongbao;
	JTextArea textAreaTenMonAn;
	JLabel labelTenMonAnLarge;
	JTextArea textAreaTenMonAnLarge;

	JLabel labelName1;
	JLabel labelName2;
	JLabel labelName3;

	JLabel labelHinhNV1;
	JLabel labelHinhNV2;
	JLabel labelHinhNV3;

	// JLabel labelTenMonAn1;
	// JLabel labelTenMonAn2;
	// JLabel labelTenMonAn3;
	JTextArea textAreaTenMonAn1;
	JTextArea textAreaTenMonAn2;
	JTextArea textAreaTenMonAn3;

	JPanel panelHinhNV1;
	JPanel panelHinhNV2;
	JPanel panelHinhNV3;

	JButton btnImage1;
	JButton btnImage2;
	JButton btnImage3;
	JToggleButton togglebtnKhongQuetVT;

	JTextArea textAreaTheTu;
	JLabel labelMaThe;

	private JTextArea textArea;

	List<Template> listl = new ArrayList<>();
	// the width of fingerprint image
	int fpWidth = 0;
	// the height of fingerprint image
	int fpHeight = 0;
	// for verify test
	private byte[] lastRegTemp = new byte[2048];
	// the length of lastRegTemp
	private int cbRegTemp = 0;
	// pre-register template
	private byte[][] regtemparray = new byte[3][2048];
	// Register
	private boolean bRegister = false;
	// Identify
	private boolean bIdentify = true;
	// finger id
	private int iFid = 1;

	private int nFakeFunOn = 1;
	// must be 3
	static final int enroll_cnt = 3;
	// the index of pre-register function
	private int enroll_idx = 0;

	private byte[] imgbuf = null;
	private byte[] template = new byte[2048];
	private int[] templateLen = new int[1];
	// private int[] thamchieu = null;
	String[] tennv = null;
	private boolean mbStop = true;
	private long mhDevice = 0;
	private long mhDB = 0;
	private WorkThread workThread = null;

	Connection con = null;
	Statement statement = null;
	List<OrderFood> orderFoods = new ArrayList<>();
	private OrderFood orderFoodCurrent;
	PreparedStatement preStatement = null;
	PreparedStatement preStatementTop3 = null;
	int widthJComponentLeft = 210;
	int heightJComponentLeft = 40;
	int sizeTextJComponentLeft = 20;
	int widthHinhLon = 650;
	int heightHinhLon = 450;

	public static List<TimekeepingData> listDataVerify;
	public static int shiftsCurrent = 0;
	public static String codeBranch = "";
	// Date current
	private Date dateCurrent = null;

	public void launchFrame() {

		dateCurrent = DateUtil.DATE_WITHOUT_TIME(new Date());

		this.setLayout(null);
		btnOpen = new JButton("Bắt đầu");
		this.add(btnOpen);
		int nRsize = 30;
		btnOpen.setBounds(30, 20, widthJComponentLeft, 50);
		btnOpen.setFont(new Font("Times New Roman", Font.BOLD, 30));
		btnOpen.setBackground(new Color(41, 143, 72));
		btnOpen.setForeground(Color.WHITE);

		btnClose = new JButton("Đóng");
		this.add(btnClose);
		btnClose.setBounds(30, 50 + nRsize, widthJComponentLeft, 50);
		btnClose.setFont(new Font("Times New Roman", Font.BOLD, 30));
		btnClose.setBackground(new Color(182, 46, 46));
		btnClose.setForeground(Color.WHITE);

		btnRefesh = new JButton("Sử dụng thẻ");
		this.add(btnRefesh);
		btnRefesh.setBounds(30, 110 + nRsize, widthJComponentLeft, 50);
		btnRefesh.setFont(new Font("Times New Roman", Font.BOLD, 30));
		btnRefesh.setBackground(new Color(234, 151, 62));
		btnRefesh.setForeground(Color.WHITE);

		// Tao chieu cao ban dau // chi can chinh o day cho tat ca cac the
		int heightFirst = 160;

		labelMaThe = new JLabel("Mã thẻ", JLabel.CENTER);
		this.add(labelMaThe);
		labelMaThe.setBounds(30, heightFirst + nRsize, widthJComponentLeft, heightJComponentLeft);
		// labelMaThe.setFont(new Font("Times New Roman", Font.BOLD,
		// sizeTextJComponentLeft));

		// textAreaTheTu = new JTextArea();
		// this.add(textAreaTheTu);
		// textAreaTheTu.setBounds(30, heightFirst + heightJComponentLeft +
		// nRsize, widthJComponentLeft,
		// heightJComponentLeft);
		// textAreaTheTu.setFont(new Font("Times New Roman", Font.BOLD,
		// sizeTextJComponentLeft));

		textFieldMaTheTu = new JTextField();
		this.add(textFieldMaTheTu);
		textFieldMaTheTu.setBounds(30, heightFirst + heightJComponentLeft + nRsize, widthJComponentLeft,
				heightJComponentLeft);
		textFieldMaTheTu.setFont(new Font("Times New Roman", Font.BOLD, sizeTextJComponentLeft));

		// textFieldMaTheTu.addAncestorListener(new AncestorListener() {
		// @Override
		// public void ancestorRemoved(AncestorEvent pEvent) {
		// System.out.println("Thai");
		// }
		//
		// @Override
		// public void ancestorMoved(AncestorEvent pEvent) {
		//
		// }
		//
		// @Override
		// public void ancestorAdded(AncestorEvent pEvent) {
		// // TextField is added to its parent => request focus in Event
		// Dispatch Thread
		// SwingUtilities.invokeLater(new Runnable() {
		// @Override
		// public void run() {
		// textFieldMaTheTu.requestFocusInWindow();
		// }
		// });
		// }
		// });

		// textAreaTheTu.getDocument().addDocumentListener(new
		// DocumentListener() {
		//
		// @Override
		// public void removeUpdate(DocumentEvent e) {
		// System.out.println("Thai da change 1");
		// }
		//
		// @Override
		// public void insertUpdate(DocumentEvent e) {
		// System.out.println("Thai da change 2");
		// int count = 0;
		// for (int i = 0; i < textAreaTheTu.getText().length(); i++) {
		// count++;
		// }
		// }
		//
		// @Override
		// public void changedUpdate(DocumentEvent e) {
		// System.out.println("Thai da change 3");
		// }
		//
		// public void handleChange() {
		// System.out.println("Thai da change");
		// }
		// });

		labelThongbao = new JLabel("Thông báo", JLabel.CENTER);
		this.add(labelThongbao);
		labelThongbao.setBounds(30, heightFirst + heightJComponentLeft * 2 + nRsize, widthJComponentLeft,
				heightJComponentLeft);
		// labelThongbao.setFont(new Font("Times New Roman", Font.BOLD,
		// sizeTextJComponentLeft));
		// thong bao
		textArea = new JTextArea();
		this.add(textArea);
		textArea.setBounds(30, heightFirst + heightJComponentLeft * 3 + nRsize, widthJComponentLeft,
				heightJComponentLeft);
		textArea.setLineWrap(true);
		textArea.setSelectedTextColor(Color.RED);

		labelMaNV = new JLabel("Mã nhân viên", JLabel.CENTER);
		this.add(labelMaNV);
		labelMaNV.setBounds(30, heightFirst + heightJComponentLeft * 4 + nRsize, widthJComponentLeft,
				heightJComponentLeft);
		// labelMaNV.setFont(new Font("Times New Roman", Font.BOLD,
		// sizeTextJComponentLeft));

		textAreaMaNV = new JTextArea();
		this.add(textAreaMaNV);
		textAreaMaNV.setBounds(30, heightFirst + heightJComponentLeft * 5 + nRsize, widthJComponentLeft,
				heightJComponentLeft);
		textAreaMaNV.setFont(new Font("Times New Roman", Font.BOLD, sizeTextJComponentLeft));

		labelTenNV = new JLabel("Tên nhân viên", JLabel.CENTER);
		this.add(labelTenNV);
		labelTenNV.setBounds(30, heightFirst + heightJComponentLeft * 6 + nRsize, widthJComponentLeft,
				heightJComponentLeft);
		// labelTenNV.setFont(new Font("Times New Roman", Font.BOLD,
		// sizeTextJComponentLeft));

		textAreaTenNV = new JTextArea();
		this.add(textAreaTenNV);
		textAreaTenNV.setBounds(30, heightFirst + heightJComponentLeft * 7 + nRsize, widthJComponentLeft,
				heightJComponentLeft);
		textAreaTenNV.setFont(new Font("Times New Roman", Font.BOLD, sizeTextJComponentLeft));

		labelPhongban = new JLabel("Phòng ban", JLabel.CENTER);
		this.add(labelPhongban);
		labelPhongban.setBounds(30, heightFirst + heightJComponentLeft * 8 + nRsize, widthJComponentLeft,
				heightJComponentLeft);
		// labelPhongban.setFont(new Font("Times New Roman", Font.BOLD,
		// sizeTextJComponentLeft));

		textAreaPhongban = new JTextArea();
		this.add(textAreaPhongban);
		textAreaPhongban.setBounds(30, heightFirst + heightJComponentLeft * 9 + nRsize, widthJComponentLeft,
				heightJComponentLeft);
		textAreaPhongban.setFont(new Font("Times New Roman", Font.BOLD, sizeTextJComponentLeft));

		labelNgay = new JLabel("Ngày", JLabel.CENTER);
		this.add(labelNgay);
		labelNgay.setBounds(30, heightFirst + heightJComponentLeft * 10 + nRsize, widthJComponentLeft,
				heightJComponentLeft);
		// labelNgay.setFont(new Font("Times New Roman", Font.BOLD,
		// sizeTextJComponentLeft));

		textAreaNgay = new JTextArea();
		this.add(textAreaNgay);
		textAreaNgay.setBounds(30, heightFirst + heightJComponentLeft * 11 + nRsize, widthJComponentLeft,
				heightJComponentLeft);
		textAreaNgay.setFont(new Font("Times New Roman", Font.BOLD, sizeTextJComponentLeft));

		togglebtnKhongQuetVT = new JToggleButton("CÓ Đ.KÝ");
		this.add(togglebtnKhongQuetVT);
		togglebtnKhongQuetVT.setBounds(30, 700 + nRsize, widthJComponentLeft, 30);
		togglebtnKhongQuetVT.setSize(widthJComponentLeft, 100);
		togglebtnKhongQuetVT.setFont(new Font("Times New Roman", Font.BOLD, 22));
		// togglebtnKhongQuetVT.setBackground(new Color(180, 147, 54));
		// togglebtnKhongQuetVT.setForeground(Color.WHITE);
		// togglebtnKhongQuetVT.setBackground(Color.green);

		int marginLeftJComponentHinhNho = 340;
		int heightFirstJComponentHinhNho = 450 + nRsize + heightJComponentLeft + 50;
		int sizeTextTenMonAn = 26;

		labelName1 = new JLabel();
		this.add(labelName1);
		labelName1.setBounds(marginLeftJComponentHinhNho, heightFirstJComponentHinhNho, 190, heightJComponentLeft);
		labelName1.setFont(new Font("Times New Roman", Font.PLAIN, sizeTextJComponentLeft));

		// height hinh nhan vien;
		int heightHinhNhanVien = 140;
		int widthHinhNhanVien = 140;
		labelHinhNV1 = new JLabel("", JLabel.CENTER);
		this.add(labelHinhNV1);
		labelHinhNV1.setBounds(marginLeftJComponentHinhNho, heightFirstJComponentHinhNho + heightJComponentLeft,
				heightHinhNhanVien, widthHinhNhanVien);

		int heightHinhMonAn = 190;
		int widthHinhMonAn = 160;
		btnImage1 = new JButton();
		this.add(btnImage1);
		btnImage1.setBounds(marginLeftJComponentHinhNho - 10,
				heightFirstJComponentHinhNho + heightJComponentLeft + 5 + heightHinhNhanVien, heightHinhMonAn,
				widthHinhMonAn);

		textAreaTenMonAn1 = new JTextArea("");
		this.add(textAreaTenMonAn1);
		textAreaTenMonAn1.setBounds(marginLeftJComponentHinhNho - 10,
				heightFirstJComponentHinhNho + heightJComponentLeft + 20 + heightHinhNhanVien + heightHinhMonAn - 43,
				heightHinhMonAn + 100, 70);
		textAreaTenMonAn1.setLineWrap(true);
		textAreaTenMonAn1.setWrapStyleWord(true);
		textAreaTenMonAn1.setOpaque(false);
		textAreaTenMonAn1.setEditable(false);
		textAreaTenMonAn1.setFont(new Font("Calibri", Font.BOLD, sizeTextTenMonAn));

		// width giua cac hinh nhan vien
		int widthGiuaHinhNhanVien = 170;
		labelName2 = new JLabel();
		this.add(labelName2);
		labelName2.setBounds(marginLeftJComponentHinhNho + widthHinhNhanVien + widthGiuaHinhNhanVien + 20,
				heightFirstJComponentHinhNho, 190, heightJComponentLeft);
		labelName2.setFont(new Font("Times New Roman", Font.PLAIN, sizeTextJComponentLeft));

		labelHinhNV2 = new JLabel("", JLabel.CENTER);
		this.add(labelHinhNV2);
		labelHinhNV2.setBounds(marginLeftJComponentHinhNho + widthHinhNhanVien + widthGiuaHinhNhanVien + 25,
				heightFirstJComponentHinhNho + heightJComponentLeft, heightHinhNhanVien, widthHinhNhanVien);

		btnImage2 = new JButton();
		this.add(btnImage2);
		btnImage2.setBounds(marginLeftJComponentHinhNho + widthHinhMonAn + widthGiuaHinhNhanVien,
				heightFirstJComponentHinhNho + heightJComponentLeft + 5 + heightHinhNhanVien, heightHinhMonAn,
				widthHinhMonAn);

		textAreaTenMonAn2 = new JTextArea("");
		this.add(textAreaTenMonAn2);
		textAreaTenMonAn2.setBounds(marginLeftJComponentHinhNho + widthHinhMonAn + widthGiuaHinhNhanVien,
				heightFirstJComponentHinhNho + heightJComponentLeft + 20 + heightHinhNhanVien + heightHinhMonAn - 43,
				heightHinhMonAn + 100, 70);
		textAreaTenMonAn2.setLineWrap(true);
		textAreaTenMonAn2.setWrapStyleWord(true);
		textAreaTenMonAn2.setOpaque(false);
		textAreaTenMonAn2.setEditable(false);
		textAreaTenMonAn2.setFont(new Font("Calibri", Font.BOLD, sizeTextTenMonAn));

		labelName3 = new JLabel();
		this.add(labelName3);
		labelName3.setBounds(marginLeftJComponentHinhNho + widthHinhNhanVien * 2 + widthGiuaHinhNhanVien * 2 + 45,
				heightFirstJComponentHinhNho, 190, heightJComponentLeft);
		labelName3.setFont(new Font("Times New Roman", Font.PLAIN, sizeTextJComponentLeft));

		labelHinhNV3 = new JLabel("", JLabel.CENTER);
		this.add(labelHinhNV3);
		labelHinhNV3.setBounds(marginLeftJComponentHinhNho + widthHinhNhanVien * 2 + widthGiuaHinhNhanVien * 2 + 45,
				heightFirstJComponentHinhNho + heightJComponentLeft, heightHinhNhanVien, widthHinhNhanVien);

		btnImage3 = new JButton();
		this.add(btnImage3);
		btnImage3.setBounds(marginLeftJComponentHinhNho + widthHinhMonAn * 2 + widthGiuaHinhNhanVien * 2,
				heightFirstJComponentHinhNho + heightJComponentLeft + 5 + heightHinhNhanVien, heightHinhMonAn,
				widthHinhMonAn);
		textAreaTenMonAn3 = new JTextArea("");
		this.add(textAreaTenMonAn3);
		textAreaTenMonAn3.setBounds(marginLeftJComponentHinhNho + widthHinhMonAn * 2 + widthGiuaHinhNhanVien * 2,
				heightFirstJComponentHinhNho + heightJComponentLeft + 20 + heightHinhNhanVien + heightHinhMonAn - 43,
				heightHinhMonAn + 70, 70);
		textAreaTenMonAn3.setLineWrap(true);
		textAreaTenMonAn3.setWrapStyleWord(true);
		textAreaTenMonAn3.setOpaque(false);
		textAreaTenMonAn3.setEditable(false);
		textAreaTenMonAn3.setFont(new Font("Calibri", Font.BOLD, sizeTextTenMonAn));

		// khoi tao chieu cao tu hinh lon
		int heightImageLarge = 450;

		btnImg = new JButton();
		btnImg.setBounds(420, nRsize + 80, widthHinhLon, heightHinhLon);
		// btnImg.setDefaultCapable(false);
		this.add(btnImg);

		// textAreaTenMonAn = new JTextArea();
		// textAreaTenMonAn.setBounds(330, 360, 500, 30);
		// this.add(textAreaTenMonAn);

		// labelTenMonAnLarge = new JLabel("");
		// labelTenMonAnLarge.setBounds(420, 9, 900, 60);
		// labelTenMonAnLarge.setFont(new Font("Times New Roman", Font.BOLD,
		// 38));
		// labelTenMonAnLarge.setForeground(new Color(186, 10, 10));

		textAreaTenMonAnLarge = new JTextArea("");
		textAreaTenMonAnLarge.setBounds(420, 2, 750, 105);
		textAreaTenMonAnLarge.setLineWrap(true);
		textAreaTenMonAnLarge.setWrapStyleWord(true);
		textAreaTenMonAnLarge.setOpaque(false);
		textAreaTenMonAnLarge.setEditable(false);
		textAreaTenMonAnLarge.setFont(new Font("Calibri", Font.BOLD, 43));
		// textAreaTenMonAnLarge.setForeground(new Color(186, 10, 10));
		textAreaTenMonAnLarge.setForeground(Color.BLACK);
		this.add(textAreaTenMonAnLarge);

		// image he thong chua san sang
		String pathImageTest = "imagesSystem/image-unsuccess.png";
		// String pathImageTest = "imagesSystem/image-danhansuatan.png";
		File fileTest = new File(pathImageTest);
		BufferedImage bimgTest = null;
		try {
			bimgTest = ImageIO.read(fileTest);
		} catch (IOException e) {
		}
		if (bimgTest == null) {
			btnImg.setIcon(null);
		}
		if (bimgTest != null) {
			Image scaledTest = bimgTest.getScaledInstance(widthHinhLon, heightHinhLon, Image.SCALE_SMOOTH);
			ImageIcon imageTest = new ImageIcon(scaledTest);
			btnImg.setIcon(imageTest);
		}
		// end image he thong

		// btnhinh = new JButton();
		// btnhinh.setBounds(370, 420, 140, 150);
		// btnhinh.setDefaultCapable(false);
		// this.add(btnhinh);

		// set size new
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// this.setUndecorated(true);
		// this.setVisible(true);
		// end size new
		// sie old
		this.setSize(1280, 1024);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		// end old

		this.setTitle("QUẢN LÝ NHÀ ĂN");
		this.setResizable(false);
//		listl = loadvantay.findAll();
//		System.out.println(listl.size());
//		for (template t : listl) {
//			if (t.getMaNhanVien().equals("Ti0017") || t.getMaNhanVien().equals("IT0017")) {
//				System.out.println("Tien");
//			}
//		}

		togglebtnKhongQuetVT.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				int state = e.getStateChange();
				if (state == ItemEvent.SELECTED) {
					togglebtnKhongQuetVT.setText("CHƯA Đ.KÝ");
					ZKFPDemo.ON_ALLOW_EXCEPTION = true;

				} else {
					togglebtnKhongQuetVT.setText("CÓ Đ.KÝ");
					ZKFPDemo.ON_ALLOW_EXCEPTION = false;
				}
			}
		});

		// button su dung the
		btnRefesh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textFieldMaTheTu.setText("");
				textFieldMaTheTu.requestFocusInWindow();
			}
		});

		// button bat dau
		btnOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textFieldMaTheTu.requestFocusInWindow();
				try {
					ZKFPDemo.codeBranch = readFile("configs/code_chinhanh.txt").trim();
					String connect = readFile("configs/url_db_datcom.txt");

					String[] connects = connect.split("__");
					URL.LINK_QUANLYDATCOM_JDBC = connects[0].trim();
					URL.LINK_DULIEUTRUNGTAM_JDBC = connects[1].trim();

//					con = ZKFPDemo.getConnectionMySQL(
//							"jdbc:mysql://192.168.10.252:3306/quanlydatcom_bd?useUnicode=yes&characterEncoding=UTF-8");
					con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);

					// query link jdbc
					String queryLinkQuanLyDatCom = "SELECT * FROM link";
					preStatement = con.prepareStatement(queryLinkQuanLyDatCom);
					ResultSet resultSet = preStatement.executeQuery();
					while (resultSet.next()) {
						String linkName = resultSet.getString("link_name");
						if (linkName.equals("ChamCong")) {
							URL.LINK_CHAMCONG = resultSet.getString("link_jdbc");
						}
					}
					// end query link

					// query va set ca lam viec
					String queryTimeforShift = "SELECT * FROM shifts";
					preStatement = con.prepareStatement(queryTimeforShift);
					resultSet = preStatement.executeQuery();
					while (resultSet.next()) {
						String shifts = resultSet.getString("name");
						if (shifts.equals("Ca 1")) {
							Shifts.SHIFTS_1_ID = resultSet.getInt("id");
							Shifts.SHIFTS_1_HOUR = resultSet.getInt("time");
							Shifts.SHIFTS_1_MINUTES = resultSet.getInt("minutes");

							Shifts.TIME_START_SHIFTS_1 = DateUtil.HANDLE_START_TIME(Shifts.SHIFTS_1_HOUR,
									Shifts.SHIFTS_1_MINUTES, 0);
							// System.out.println("Time start shifts 1: " +
							// Shifts.TIME_START_SHIFTS_1);
							Shifts.TIME_END_SHIFTS_1 = DateUtil.HANDLE_END_TIME(Shifts.SHIFTS_1_HOUR,
									Shifts.SHIFTS_1_MINUTES, 0);
							// System.out.println("Time end shifts 1: " +
							// Shifts.TIME_END_SHIFTS_1);
						}
						if (shifts.equals("Ca 2")) {
							Shifts.SHIFTS_2_ID = resultSet.getInt("id");
							Shifts.SHIFTS_2_HOUR = resultSet.getInt("time");
							Shifts.SHIFTS_2_MINUTES = resultSet.getInt("minutes");

							Shifts.TIME_START_SHIFTS_2 = DateUtil.HANDLE_START_TIME(Shifts.SHIFTS_2_HOUR,
									Shifts.SHIFTS_2_MINUTES, 0);
							// System.out.println("Time start shifts 2: " +
							// Shifts.TIME_START_SHIFTS_2);
							Shifts.TIME_END_SHIFTS_2 = DateUtil.HANDLE_END_TIME(Shifts.SHIFTS_2_HOUR,
									Shifts.SHIFTS_2_MINUTES, 0);
							// System.out.println("Time end shifts 2: " +
							// Shifts.TIME_END_SHIFTS_2);
						}
						if (shifts.equals("Ca 3")) {
							Shifts.SHIFTS_3_ID = resultSet.getInt("id");
							Shifts.SHIFTS_3_HOUR = resultSet.getInt("time");
							Shifts.SHIFTS_3_MINUTES = resultSet.getInt("minutes");

							Shifts.TIME_START_SHIFTS_3 = DateUtil.HANDLE_START_TIME(Shifts.SHIFTS_3_HOUR,
									Shifts.SHIFTS_3_MINUTES, 3);
							// System.out.println("Time start shifts 3: " +
							// Shifts.TIME_START_SHIFTS_3);
							Shifts.TIME_END_SHIFTS_3 = DateUtil.HANDLE_END_TIME(Shifts.SHIFTS_3_HOUR,
									Shifts.SHIFTS_3_MINUTES, 3);
							// System.out.println("Time end shifts 3: " +
							// Shifts.TIME_END_SHIFTS_3);
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} finally {
					try {
						ZKFPDemo.closeConnectionPre(con, preStatement);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}

				// HANDLE GET LIST VERIFY
				// NEU THANH CONG HANDLE GET DU LIEU TU CHUONG TRINH CHAM CONG
				DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				String dateSearchString = formatter1.format(new Date());
				TimekeepingData[] arr = null;
				// set ca hien tai
				// get date time current
				dateCurrent = new Date();
				if (!Shifts.TIME_START_SHIFTS_1.after(dateCurrent) && !Shifts.TIME_END_SHIFTS_1.before(dateCurrent)) {
					shiftsCurrent = Shifts.SHIFTS_1_ID;
					Shifts.NGAY_CUA_CA = DateUtil.DATE_WITHOUT_TIME(Shifts.TIME_START_SHIFTS_1);
					// bao gom nhan vien van phong + nhan vien ca 12 tieng
					arr = TimekeepingDataService.timtheongay(dateSearchString);
					// arr = TimekeepingDataService.timtheongay("13/10/2020");
				}
				if (!Shifts.TIME_START_SHIFTS_2.after(dateCurrent) && !Shifts.TIME_END_SHIFTS_2.before(dateCurrent)) {
					shiftsCurrent = Shifts.SHIFTS_2_ID;
					Shifts.NGAY_CUA_CA = DateUtil.DATE_WITHOUT_TIME(Shifts.TIME_START_SHIFTS_2);
					arr = TimekeepingDataService.searchByDateAndWorkTemp(dateSearchString, "8C");
					List<TimekeepingData> arrListTemp = new ArrayList<>();
					if (arr != null) {
						for (int i = 0; i < arr.length; i++) {
							// check dieu kien co di ca hay khong
							if (arr[i].isWorkShift()) {
								arrListTemp.add(arr[i]);
							}
						}
						arr = arrListTemp.toArray(new TimekeepingData[arrListTemp.size()]);
						// test
						// arr =
						// TimekeepingDataService.searchByDateAndWorkTemp("18/09/2020",
						// "8C");
					}
				}
				if (!Shifts.TIME_START_SHIFTS_3.after(dateCurrent) && !Shifts.TIME_END_SHIFTS_3.before(dateCurrent)) {
					shiftsCurrent = Shifts.SHIFTS_3_ID;
					Shifts.NGAY_CUA_CA = DateUtil.DATE_WITHOUT_TIME(Shifts.TIME_START_SHIFTS_3);
					arr = TimekeepingDataService.searchByDateAndWorkTemp(dateSearchString, "CD");
					// test
					// arr =
					// TimekeepingDataService.searchByDateAndWorkTemp("19/09/2020",
					// "CD");
				}
				if (arr != null) {
					ZKFPDemo.listDataVerify = Arrays.asList(arr);
				}
				// END

				// TODO Auto-generated method stub
				if (0 != mhDevice) {
					// already inited
					textArea.setText("Please close device first!\n");
					// image he thong chua san sang
					String pathImageTest = "imagesSystem/testClose.png";
					File fileTest = new File(pathImageTest);
					BufferedImage bimgTest = null;
					try {
						bimgTest = ImageIO.read(fileTest);
					} catch (IOException e2) {
					}
					if (bimgTest == null) {
						btnImg.setIcon(null);
					}
					if (bimgTest != null) {
						Image scaledTest = bimgTest.getScaledInstance(widthHinhLon, heightHinhLon, Image.SCALE_SMOOTH);
						ImageIcon imageTest = new ImageIcon(scaledTest);
						btnImg.setIcon(null);
						btnImg.setIcon(imageTest);
					}
					// end image he thong
					return;
				}
				int ret = FingerprintSensorErrorCode.ZKFP_ERR_OK;
				// Initialize
				cbRegTemp = 0;
				bRegister = false;
				bIdentify = false;
				iFid = 1;
				enroll_idx = 0;
				if (FingerprintSensorErrorCode.ZKFP_ERR_OK != FingerprintSensorEx.Init()) {
					textArea.setText("Init failed!\n");
					return;
				}
				ret = FingerprintSensorEx.GetDeviceCount();
				if (ret < 0) {
					textArea.setText("No devices connected!\n");
					FreeSensor();
					return;
				}
				if (0 == (mhDevice = FingerprintSensorEx.OpenDevice(0))) {
					textArea.setText("Open device fail, ret = " + ret + "!\n");
					FreeSensor();
					return;
				}
				if (0 == (mhDB = FingerprintSensorEx.DBInit())) {
					textArea.setText("Init DB fail, ret = " + ret + "!\n");
					FreeSensor();
					return;
				}

				// For ISO/Ansi
				int nFmt = 0; // Ansi
				// if (radioISO.isSelected()) {
				// nFmt = 1; // ISO
				// }
				FingerprintSensorEx.DBSetParameter(mhDB, 5010, nFmt);
				// For ISO/Ansi End

				// set fakefun off
				// FingerprintSensorEx.SetParameter(mhDevice, 2002,
				// changeByte(nFakeFunOn), 4);

				byte[] paramValue = new byte[4];
				int[] size = new int[1];
				// GetFakeOn
				// size[0] = 4;
				// FingerprintSensorEx.GetParameters(mhDevice, 2002, paramValue,
				// size);
				// nFakeFunOn = byteArrayToInt(paramValue);

				size[0] = 4;
				FingerprintSensorEx.GetParameters(mhDevice, 1, paramValue, size);
				fpWidth = byteArrayToInt(paramValue);
				size[0] = 4;
				FingerprintSensorEx.GetParameters(mhDevice, 2, paramValue, size);
				fpHeight = byteArrayToInt(paramValue);

				imgbuf = new byte[fpWidth * fpHeight];
				// btnImg.resize(fpWidth, fpHeight);
				mbStop = false;
				workThread = new WorkThread();
				workThread.start();// 线程启动

				// load data finger -- btn enroll
				String url_vantay = readFile("configs/url_ds_vantay.txt");
				String[] parts = url_vantay.split("_");
				String urlDB = parts[0];
				String usernameRemote = parts[1];
				String passRemote = parts[2];
				listl = LoadVanTay.findAll(urlDB.trim(), usernameRemote.trim(), passRemote.trim());
				for (int i = 0; i < listl.size(); i++) {
					Template tl = new Template(listl.get(i).getMaChamCong(), listl.get(i).getFingerID(),
							listl.get(i).getFlag(), listl.get(i).getFingerTemplate());
					String cmau = tl.getFingerTemplate();
					int[] sokt = new int[1];
					sokt[0] = 2048;
					byte[] mau = new byte[sokt[0]];
					FingerprintSensor.Base64ToBlob(cmau, mau, sokt[0]);
					if (0 == (ret = FingerprintSensorEx.DBAdd(mhDB, i + 1, mau))) {
						textArea.setText("Đang load" + mau);
					} else {
						textArea.setText("Bị lỗi" + ret + "\n");
					}
				}

				textArea.setText("Open succ! Finger Image Width:" + fpWidth + ",Height:" + fpHeight + "\n");
				// thong bao da san san
				String pathImageTestSucc = "imagesSystem/image-success.png";
				File fileTestSucc = new File(pathImageTestSucc);
				BufferedImage bimgTestSucc = null;
				try {
					bimgTestSucc = ImageIO.read(fileTestSucc);
				} catch (IOException e5) {
				}
				if (bimgTestSucc == null) {
					btnImg.setIcon(null);
				}
				if (bimgTestSucc != null) {
					Image scaledTestSucc = bimgTestSucc.getScaledInstance(widthHinhLon, heightHinhLon,
							Image.SCALE_SMOOTH);
					ImageIcon imageTestSucc = new ImageIcon(scaledTestSucc);
					btnImg.setIcon(null);
					btnImg.setIcon(imageTestSucc);
				}
				// end thong bao

				// turn on identify -- btn identify
				if (0 == mhDevice) {
					textArea.setText("Please Open device first!\n");
					return;
				}
				if (bRegister) {
					enroll_idx = 0;
					bRegister = false;
				}
				if (!bIdentify) {
					bIdentify = true;
				}
			}
		});

		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// kiem tra co la nhan vien cong ty hay khong
					boolean isEmployee = false;
					java.sql.Date ngay_cua_ca_SQL = new java.sql.Date(Shifts.NGAY_CUA_CA.getTime());
					// Tai sao khong lay list verify ra so sanh truoc ->
					// Du lieu a hong dua qua khong co truong ma the nen khong
					// the so sanh dc
					for (int indexMathe = 0; indexMathe < listl.size(); indexMathe++) {
						BigInteger inum = new BigInteger(listl.get(indexMathe).getMaThe());
						BigInteger inum1 = new BigInteger("1");
						if (!textFieldMaTheTu.getText().isEmpty()) {
							inum1 = new BigInteger(textFieldMaTheTu.getText());
						}
						if (inum.compareTo(inum1) == 0) {
							textFieldMaTheTu.setText("");
							isEmployee = true;
							if (ZKFPDemo.listDataVerify != null) {
								// check xem nhan vien co cat com hay khong
								// nho check cho nay listDataVerify bi null
								boolean isEat = false;
								for (TimekeepingData t : ZKFPDemo.listDataVerify) {
									if (t.getCodeOld().equals(listl.get(indexMathe).getMaNhanVien())) {
										isEat = true;
										break;
									}
								}
								// neu khong cat com ->
								if (isEat) {
									List<FoodNhaAn> foodNhaAnTop4 = new ArrayList<>();
									orderFoodCurrent = new OrderFood();
									// chua handle cho nay
									// kiem tra xem hien tai co dang thuoc ca
									// nao hay khong
									if (shiftsCurrent != 0) {
										// thai
										// query by date and employee
										String queryFood = "";
										queryFood = "SELECT ofbd.id as order_food_by_day_id, of.employee_code, of.employee_id, of.employee_name, of.department_code, of.department_name, cf.name,cf.image,cf.id as category_food_id,fbd.shifts_id, of.registration_date\r\n"
												+ "FROM order_food_by_day as ofbd, order_food as of, food_by_day fbd, category_food as cf\r\n"
												+ "WHERE of.employee_id = ? AND of.registration_date = ? AND fbd.shifts_id = ? AND ofbd.order_food_id = of.id AND ofbd.food_by_day_id = fbd.id AND cf.id = fbd.category_food_id;	";

										try {
											con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
											// kiem tra co du lieu ca do duoi DB
											// chua
											String queryChecked = "SELECT * FROM food_nha_an WHERE employee_id = ? and shifts_id = ? and food_date = ?";
											PreparedStatement preStatementChecked = null;
											preStatementChecked = con.prepareStatement(queryChecked);
											// check bang ma nhan vien cu
											preStatementChecked.setString(1, listl.get(indexMathe).getMaNhanVien());
											preStatementChecked.setInt(2, shiftsCurrent);
											preStatementChecked.setDate(3, ngay_cua_ca_SQL);
											ResultSet resultSetChecked = preStatementChecked.executeQuery();
											boolean checkedExist = false;
											if (resultSetChecked.next()) {
												checkedExist = true;
												String pathImageDaNhanSuatAn = "imagesSystem/image-danhansuatan650x450.png";
												File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
												BufferedImage bimgDaNhanSuatAn = null;
												try {
													bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
												} catch (IOException e1) {
													e1.printStackTrace();
												}
												if (bimgDaNhanSuatAn == null) {
													btnImg.setIcon(null);
												}
												if (bimgDaNhanSuatAn != null) {
													// Image scaledTest =
													// bimgDaNhanSuatAn.getScaledInstance(widthHinhLon,
													// heightHinhLon,
													// Image.SCALE_SMOOTH);
													// ImageIcon imageTest = new
													// ImageIcon(scaledTest);
													ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
													btnImg.setIcon(imageTest);
												}
												return;
											}
											// neu chua co thi query
											preStatement = con.prepareStatement(queryFood);
											// pass id employee
											preStatement.setString(1, listl.get(indexMathe).getMaNhanVien());
											// date param
											preStatement.setDate(2, ngay_cua_ca_SQL);
											preStatement.setInt(3, shiftsCurrent);
											ResultSet resultSet = preStatement.executeQuery();

											// Neu chua an se them du lieu vao
											// DB -> 2 truong hop: 1 la co dang
											// ky , 2
											// la khong dang ky
											// Co dang ky
											while (resultSet.next()) {
												// neu nhan vien do da an ca do
												// roi - > k cho them
												if (!checkedExist) {
													orderFoodCurrent.setDepartmentName(
															resultSet.getString("of.department_name"));
													orderFoodCurrent.setDepartmentCode(
															resultSet.getString("of.department_code"));
													orderFoodCurrent
															.setEmployeeCode(resultSet.getString("of.employee_code"));
													orderFoodCurrent
															.setEmployeeName(resultSet.getString("of.employee_name"));
													orderFoodCurrent.setFoodName(resultSet.getString("cf.name"));
													orderFoodCurrent.setImage(resultSet.getBytes("cf.image"));
													orderFoodCurrent
															.setFood_date(resultSet.getDate("of.registration_date"));
													orderFoodCurrent
															.setCategory_food_id(resultSet.getInt("category_food_id"));
													orderFoodCurrent
															.setEmployeeId(resultSet.getString("of.employee_id"));
													orderFoodCurrent.setShifts_id(resultSet.getInt("fbd.shifts_id"));
													ZKFPDemo.addOne(orderFoodCurrent, orderFoodCurrent.getShifts_id());
												}
											}
											// chua dang ky
											// handle cho phan mon an tu chon
											if (!resultSet.first()) {
												DepartmentData[] departmentHCMArray = DepartmentDataService
														.timtheophongquanly(ZKFPDemo.codeBranch);

												List<DepartmentData> departmentHCM = new ArrayList<>(
														Arrays.asList(departmentHCMArray));

//												DepartmentData[] departmentBDArray = DepartmentDataService
//														.timtheophongquanly("20002");
//												List<DepartmentData> departmentBD = new ArrayList<>(
//														Arrays.asList(departmentBDArray));
//
//												departmentHCM.addAll(departmentBD);
												// HANDLE -> Tao list id
												// department
												List<Long> listDepartmentCodeHCM = new ArrayList<Long>();
												for (DepartmentData de : departmentHCM) {
													listDepartmentCodeHCM.add(de.getId());
												}

												StringBuilder builder = new StringBuilder();
												for (int i = 0; i < listDepartmentCodeHCM.size(); i++) {
													builder.append("?,");
												}
												// query nhan vien tu du lieu
												// trung tam
												String queryEmployee = "SELECT empl.workShift, empl.layOff as da_nghi_viec,empl.id as employee_id,empl.name as employee_name,empl.code as employee_code,empl.codeOld as employee_code_old,depart.name as department_name,depart.code as department_code \r\n"
														+ "FROM dulieutrungtam.employee as empl, dulieutrungtam.department as depart\r\n"
														+ "WHERE empl.codeOld = ? AND empl.department_id IN ("
														+ builder.deleteCharAt(builder.length() - 1).toString()
														+ ") AND empl.department_id = depart.id;";
												PreparedStatement preStatementEmployee = null;
												Connection conDulieutrungtam = null;

												try {
													conDulieutrungtam = ZKFPDemo
															.getConnectionMySQL(URL.LINK_DULIEUTRUNGTAM_JDBC);
													preStatementEmployee = conDulieutrungtam
															.prepareStatement(queryEmployee);
													preStatementEmployee.setString(1,
															listl.get(indexMathe).getMaNhanVien());
													// handle param IN operator
													int index = 2;
													for (Long id : listDepartmentCodeHCM) {
														preStatementEmployee.setObject(index++, id); // or
																										// whatever
																										// it
																										// applies
													}
													ResultSet resultSet1 = preStatementEmployee.executeQuery();
													while (resultSet1.next()) {
														boolean daNghiViec = resultSet1.getBoolean("da_nghi_viec");
														// neu chua nghi viec
														// moi duoc luu
														if (!daNghiViec) {
															// orderFoodCurrent.setDepartmentName(
															// resultSet1.getString("department_name"));
															// orderFoodCurrent.setDepartmentCode(
															// resultSet1.getString("department_code"));
															// orderFoodCurrent.setEmployeeCode(
															// resultSet1.getString("employee_code"));
															// orderFoodCurrent.setEmployeeName(
															// resultSet1.getString("employee_name"));
															// orderFoodCurrent.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
															// orderFoodCurrent.setFood_date(
															// DateUtil.DATE_WITHOUT_TIME(new
															// Date()));
															// // gan thang id
															// cua category food
															// tu chon
															// orderFoodCurrent
															// .setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
															// orderFoodCurrent.setEmployeeId(
															// resultSet1.getString("employee_code_old"));
															// orderFoodCurrent.setShifts_id(shiftsCurrent);
															// ZKFPDemo.addOne(orderFoodCurrent,
															// orderFoodCurrent.getShifts_id());

															boolean workShift = resultSet1.getBoolean("empl.workShift");
															String employeeCode = resultSet1.getString("employee_code");
															// nhan vien van
															// phong an ca 2
															if (!workShift && shiftsCurrent == Shifts.SHIFTS_2_ID) {
																try {
																	con = ZKFPDemo.getConnectionMySQL(
																			URL.LINK_QUANLYDATCOM_JDBC);
																	// kiem tra
																	// co dang
																	// ky tang
																	// ca hay
																	// khong
																	String query = "SELECT food_ot.employee_name, food_ot.employee_code, ot.department_code,ot.department_name,ot.food_date, ot.shifts_id, food_ot.employee_code_old "
																			+ "FROM food_over_time as food_ot, over_time as ot "
																			+ "WHERE food_ot.employee_code = ? AND ot.shifts_id = ? AND ot.food_date = ? AND "
																			+ "ot.id = food_ot.over_time_id";
																	preStatementChecked = con.prepareStatement(query);
																	// check
																	// bang ma
																	// nhan vien
																	// cu
																	preStatementChecked.setString(1, employeeCode);
																	preStatementChecked.setInt(2, shiftsCurrent);
																	preStatementChecked.setDate(3, ngay_cua_ca_SQL);
																	ResultSet rs = preStatementChecked.executeQuery();
																	if (rs.next()) {
																		orderFoodCurrent.setDepartmentName(
																				rs.getString("ot.department_name"));
																		orderFoodCurrent.setDepartmentCode(
																				rs.getString("ot.department_code"));
																		orderFoodCurrent.setEmployeeCode(employeeCode);
																		orderFoodCurrent.setEmployeeName(
																				rs.getString("food_ot.employee_name"));
																		orderFoodCurrent.setFoodName(
																				FoodCustom.FOOD_CUSTOM_NAME);
																		orderFoodCurrent.setFood_date(
																				DateUtil.DATE_WITHOUT_TIME(
																						rs.getDate("ot.food_date")));
																		// gan
																		// thang
																		// id
																		// cua
																		// category
																		// food
																		// tu
																		// chon
																		orderFoodCurrent.setCategory_food_id(
																				FoodCustom.FOOD_CUSTOM_ID);
																		if (rs.getString("employee_code_old") != null) {
																			orderFoodCurrent.setEmployeeId(
																					rs.getString("employee_code_old"));
																		}
																		orderFoodCurrent.setShifts_id(shiftsCurrent);
																		orderFoodCurrent.setOverTime(true);
																		ZKFPDemo.addOne(orderFoodCurrent,
																				orderFoodCurrent.getShifts_id());
																	}
																	if (!rs.first()) {
																		String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
																		File fileKCoSuatAn = new File(
																				pathImageKhongCoSuatAn);
																		BufferedImage bimgKCoSuatAn = null;
																		try {
																			bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
																		} catch (IOException e1) {
																			e1.printStackTrace();
																		}
																		if (bimgKCoSuatAn == null) {
																			btnImg.setIcon(null);
																		}
																		if (bimgKCoSuatAn != null) {
																			ImageIcon imageTest = new ImageIcon(
																					bimgKCoSuatAn);
																			btnImg.setIcon(imageTest);
																		}
																		return;
																	}
																} catch (Exception e1) {
																	// TODO:
																	// handle
																	// exception
																} finally {
																	try {
																		ZKFPDemo.closeConnectionPre(con,
																				preStatementChecked);
																	} catch (Exception e2) {
																		e2.printStackTrace();
																		;
																	}
																}
															}
															// di ca hoac nhan
															// vien van phong an
															// ca 1
															if (workShift || !workShift
																	&& shiftsCurrent == Shifts.SHIFTS_1_ID) {
																orderFoodCurrent.setDepartmentName(
																		resultSet1.getString("department_name"));
																orderFoodCurrent.setDepartmentCode(
																		resultSet1.getString("department_code"));
																orderFoodCurrent.setEmployeeCode(employeeCode);
																orderFoodCurrent.setEmployeeName(
																		resultSet1.getString("employee_name"));
																orderFoodCurrent
																		.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
																orderFoodCurrent.setFood_date(
																		DateUtil.DATE_WITHOUT_TIME(Shifts.NGAY_CUA_CA));
																// gan thang id
																// cua category
																// food tu chon
																orderFoodCurrent
																		.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
																orderFoodCurrent.setEmployeeId(
																		resultSet1.getString("employee_code_old"));
																orderFoodCurrent.setShifts_id(shiftsCurrent);
																ZKFPDemo.addOne(orderFoodCurrent,
																		orderFoodCurrent.getShifts_id());
															}
															// nhan vien van
															// phong an ca 3
															if (!workShift && shiftsCurrent == Shifts.SHIFTS_3_ID) {
																String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
																File fileKCoSuatAn = new File(pathImageKhongCoSuatAn);
																BufferedImage bimgKCoSuatAn = null;
																try {
																	bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
																} catch (IOException e1) {
																	e1.printStackTrace();
																}
																if (bimgKCoSuatAn == null) {
																	btnImg.setIcon(null);
																}
																if (bimgKCoSuatAn != null) {
																	ImageIcon imageTest = new ImageIcon(bimgKCoSuatAn);
																	btnImg.setIcon(imageTest);
																}
																return;
															}
															// END KIEM TRA CA 2
														}
													}
													if (!resultSet1.first()) {
														String pathImageDaNhanSuatAn = "imagesSystem/image-kcttnhanvien650x450.png";
														File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
														BufferedImage bimgDaNhanSuatAn = null;
														try {
															bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
														} catch (IOException e1) {
															e1.printStackTrace();
														}
														if (bimgDaNhanSuatAn == null) {
															btnImg.setIcon(null);
														}
														if (bimgDaNhanSuatAn != null) {
															ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
															btnImg.setIcon(imageTest);
														}
														return;
													}
												} catch (Exception e1) {
													// TODO: handle exception
												} finally {
													try {
														ZKFPDemo.closeConnectionPre(conDulieutrungtam,
																preStatementEmployee);
													} catch (Exception e2) {
														e2.printStackTrace();
														;
													}
												}
											}
											// end mon an tu chon
										} catch (Exception e1) {
											// TODO: handle exception
										} finally {
											try {
												ZKFPDemo.closeConnectionPre(con, preStatement);
											} catch (Exception e2) {
												e2.printStackTrace();
											}
										}
										// da dang ky mon an
										if (orderFoodCurrent.getEmployeeName() != null) {
											textAreaMaNV.setText(orderFoodCurrent.getEmployeeCode());
											textAreaTenNV.setText(orderFoodCurrent.getEmployeeName());
											textAreaPhongban.setText(orderFoodCurrent.getDepartmentName());
											// convert date to string
											String pattern = "dd-MM-yyyy";
											DateFormat df = new SimpleDateFormat(pattern);
											String foodDate = df.format(orderFoodCurrent.getFood_date());
											textAreaNgay.setText(foodDate);
											// labelTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());
											textAreaTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());
											// set hinh
											// check neu hinh bi null
											if (orderFoodCurrent.getImage() == null) {
												// neu la mon an tu chon thi
												// show hinh tu chon
												if (orderFoodCurrent
														.getCategory_food_id() == FoodCustom.FOOD_CUSTOM_ID) {
													if (bimgTuchon == null) {
														btnImg.setIcon(null);
													}
													if (bimgTuchon != null) {
														Image scaledTuchon = bimgTuchon.getScaledInstance(widthHinhLon,
																heightHinhLon, Image.SCALE_SMOOTH);
														ImageIcon imageTuchon = new ImageIcon(scaledTuchon);
														btnImg.setIcon(null);
														btnImg.setIcon(imageTuchon);
													}
												} else {
													// khong co hinh mon an
													if (bimgNotImage == null) {
														btnImg.setIcon(null);
													}
													if (bimgNotImage != null) {
														ImageIcon imageNotImage = new ImageIcon(bimgNotImage);
														btnImg.setIcon(imageNotImage);
													}
												}
											} else {
												Image img = Toolkit.getDefaultToolkit()
														.createImage(orderFoodCurrent.getImage());
												ImageIcon icon = new ImageIcon(img.getScaledInstance(widthHinhLon,
														heightHinhLon, Image.SCALE_SMOOTH));
												btnImg.setIcon(icon);
											}
										}

										// handle show 3 o nho cho 4 nguoi quet
										// gan nhat
										String queryTop4 = "SELECT FNA.employee_name,CF.name, FNA.employee_id, CF.image\r\n"
												+ "FROM food_nha_an as FNA, category_food as CF\r\n"
												+ "WHERE FNA.category_food_id = CF.id and FNA.shifts_id = ? and FNA.food_date = ?\r\n"
												+ "ORDER BY FNA.id DESC\r\n" + "LIMIT 3;";

										try {
											con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
											preStatementTop3 = con.prepareStatement(queryTop4);
											preStatementTop3.setInt(1, shiftsCurrent);
											preStatementTop3.setDate(2, ngay_cua_ca_SQL);
											ResultSet resultSet = preStatementTop3.executeQuery();
											while (resultSet.next()) {
												FoodNhaAn foodNhaAn = new FoodNhaAn();
												foodNhaAn.setEmployeeName(resultSet.getString("FNA.employee_name"));
												foodNhaAn.setImageFood(resultSet.getBytes("CF.image"));
												foodNhaAn.setEmployeeIdOld(resultSet.getString("FNA.employee_id"));
												foodNhaAn.setFoodName(resultSet.getString("CF.name"));
												foodNhaAnTop4.add(foodNhaAn);
											}
										} catch (Exception e1) {
											// TODO: handle exception
										} finally {
											try {
												ZKFPDemo.closeConnectionPre(con, preStatementTop3);
											} catch (Exception e2) {
												e2.printStackTrace();
											}
										}
										// set hinh
										// query co bi rong hay khong
										if (!foodNhaAnTop4.isEmpty()) {
											// query co 2 phan tu
											if (foodNhaAnTop4.size() == 1) {

												// image nhan vien
												String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
														+ ".bmp";
												File file = new File(pathImage);
												BufferedImage bimg = null;
												try {
													bimg = ImageIO.read(file);
												} catch (IOException e1) {
												}
												if (bimg == null) {
													// image he thong nhan vien
													// khong co hinh
													if (bimgNoImageNV == null) {
														labelHinhNV1.setIcon(null);
													}
													if (bimgNoImageNV != null) {
														ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
														labelHinhNV1.setIcon(imageNoImageNV);
													}
												}
												if (bimg != null) {
													Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
													ImageIcon image = new ImageIcon(scaled);
													labelHinhNV1.setIcon(image);
												}
												textAreaTenMonAn1
														.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
												// image mon an 1
												if (foodNhaAnTop4.get(0).getImageFood() == null) {
													btnImage1.setIcon(null);
												} else {
													Image img1 = Toolkit.getDefaultToolkit()
															.createImage(foodNhaAnTop4.get(0).getImageFood());
													ImageIcon icon1 = new ImageIcon(
															img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
													btnImage1.setIcon(icon1);
												}
												labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());
											}
											if (foodNhaAnTop4.size() == 2) {
												// image nhan vien 1
												String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
														+ ".bmp";
												File file = new File(pathImage);
												BufferedImage bimg = null;
												try {
													bimg = ImageIO.read(file);
												} catch (IOException e1) {
												}

												if (bimg == null) {
													if (bimgNoImageNV == null) {
														labelHinhNV1.setIcon(null);
													}
													if (bimgNoImageNV != null) {
														ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
														labelHinhNV1.setIcon(imageNoImageNV);
													}
												}
												if (bimg != null) {
													Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
													ImageIcon image = new ImageIcon(scaled);
													labelHinhNV1.setIcon(image);
												}
												textAreaTenMonAn1
														.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
												// check neu image null
												if (foodNhaAnTop4.get(0).getImageFood() == null) {
													btnImage1.setIcon(null);
												} else {
													Image img1 = Toolkit.getDefaultToolkit()
															.createImage(foodNhaAnTop4.get(0).getImageFood());
													ImageIcon icon1 = new ImageIcon(
															img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
													btnImage1.setIcon(icon1);
												}

												labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

												// image nhan vien 2
												String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
														+ ".bmp";
												File file2 = new File(pathImage2);
												BufferedImage bimg2 = null;
												try {
													bimg2 = ImageIO.read(file2);
												} catch (IOException e1) {
												}
												if (bimg2 == null) {
													// image he thong nhan vien
													// khong co hinh
													if (bimgNoImageNV == null) {
														labelHinhNV2.setIcon(null);
													}
													if (bimgNoImageNV != null) {
														ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
														labelHinhNV2.setIcon(imageNoImageNV);
													}
												}
												if (bimg2 != null) {
													Image scaled2 = bimg2.getScaledInstance(140, 140,
															Image.SCALE_SMOOTH);
													ImageIcon image2 = new ImageIcon(scaled2);
													labelHinhNV2.setIcon(image2);
												}
												textAreaTenMonAn2
														.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
												// check neu image mon an 2 null
												if (foodNhaAnTop4.get(1).getImageFood() == null) {
													btnImage2.setIcon(null);
												} else {
													Image img2 = Toolkit.getDefaultToolkit()
															.createImage(foodNhaAnTop4.get(1).getImageFood());
													ImageIcon icon2 = new ImageIcon(
															img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
													btnImage2.setIcon(icon2);
												}
												labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());
											}
											if (foodNhaAnTop4.size() == 3) {
												// image nhan vien 1
												String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
														+ ".bmp";
												File file = new File(pathImage);
												BufferedImage bimg = null;
												try {
													bimg = ImageIO.read(file);
												} catch (IOException e1) {
												}
												if (bimg == null) {
													// image he thong nhan vien
													// khong co hinh
													if (bimgNoImageNV == null) {
														labelHinhNV1.setIcon(null);
													}
													if (bimgNoImageNV != null) {
														ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
														labelHinhNV1.setIcon(imageNoImageNV);
													}
												}
												if (bimg != null) {
													Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
													ImageIcon image = new ImageIcon(scaled);
													labelHinhNV1.setIcon(image);
												}

												// hinh mon an 1
												// check neu image null
												textAreaTenMonAn1
														.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
												if (foodNhaAnTop4.get(0).getImageFood() == null) {
													btnImage1.setIcon(null);
												} else {
													Image img1 = Toolkit.getDefaultToolkit()
															.createImage(foodNhaAnTop4.get(0).getImageFood());
													ImageIcon icon1 = new ImageIcon(
															img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
													btnImage1.setIcon(icon1);
												}
												labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

												// image nhan vien 2
												String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
														+ ".bmp";
												File file2 = new File(pathImage2);
												BufferedImage bimg2 = null;
												try {
													bimg2 = ImageIO.read(file2);
												} catch (IOException e1) {
												}
												if (bimg2 == null) {
													// image he thong nhan vien
													// khong co hinh
													if (bimgNoImageNV == null) {
														labelHinhNV2.setIcon(null);
													}
													if (bimgNoImageNV != null) {
														ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
														labelHinhNV2.setIcon(imageNoImageNV);
													}
												}
												if (bimg2 != null) {
													Image scaled2 = bimg2.getScaledInstance(140, 140,
															Image.SCALE_SMOOTH);
													ImageIcon image2 = new ImageIcon(scaled2);
													labelHinhNV2.setIcon(image2);
												}

												// image mon an 2
												// check neu image mon an 2 null
												textAreaTenMonAn2
														.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
												if (foodNhaAnTop4.get(1).getImageFood() == null) {
													btnImage2.setIcon(null);
												} else {
													Image img2 = Toolkit.getDefaultToolkit()
															.createImage(foodNhaAnTop4.get(1).getImageFood());
													ImageIcon icon2 = new ImageIcon(
															img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
													btnImage2.setIcon(icon2);
												}
												labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());

												// image nhan vien 3
												String pathImage3 = "images/" + foodNhaAnTop4.get(2).getEmployeeIdOld()
														+ ".bmp";
												File file3 = new File(pathImage3);
												BufferedImage bimg3 = null;
												try {
													bimg3 = ImageIO.read(file3);
												} catch (IOException e1) {
												}
												if (bimg3 == null) {
													// image he thong nhan vien
													// khong co hinh
													if (bimgNoImageNV == null) {
														labelHinhNV3.setIcon(null);
													}
													if (bimgNoImageNV != null) {
														ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
														labelHinhNV3.setIcon(imageNoImageNV);
													}
												}
												if (bimg3 != null) {
													Image scaled3 = bimg3.getScaledInstance(140, 140,
															Image.SCALE_SMOOTH);
													ImageIcon image3 = new ImageIcon(scaled3);
													labelHinhNV3.setIcon(image3);
												}

												// image mon an 3
												textAreaTenMonAn3
														.setText(foodNhaAnTop4.get(2).getFoodName().toUpperCase());
												if (foodNhaAnTop4.get(2).getImageFood() == null) {
													btnImage3.setIcon(null);
												} else {
													Image img3 = Toolkit.getDefaultToolkit()
															.createImage(foodNhaAnTop4.get(2).getImageFood());
													ImageIcon icon3 = new ImageIcon(
															img3.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
													btnImage3.setIcon(icon3);
												}
												labelName3.setText(foodNhaAnTop4.get(2).getEmployeeName());
											}
										}
										textArea.setText("Thành công");
										// end thai
									}
									// Khong co gio an phu hop
									else {
										// image he thong chua san sang
										String pathImageTest = "imagesSystem/image-khongcogioanphuhop.png";
										File fileTest = new File(pathImageTest);
										BufferedImage bimgTest = null;
										try {
											bimgTest = ImageIO.read(fileTest);
										} catch (IOException e2) {
										}
										if (bimgTest == null) {
											btnImg.setIcon(null);
										}
										if (bimgTest != null) {
											Image scaledTest = bimgTest.getScaledInstance(widthHinhLon, heightHinhLon,
													Image.SCALE_SMOOTH);
											ImageIcon imageTest = new ImageIcon(scaledTest);
											btnImg.setIcon(null);
											btnImg.setIcon(imageTest);
										}
										// end image he thong
									}
								}
								// neu cat com
								if (!isEat && ZKFPDemo.ON_ALLOW_EXCEPTION == false) {
									// CODE CHINH THUC
									// // image he thong chua san sang
									// String pathImageTest =
									// "imagesSystem/image-khongdangkysuatan.png";
									// File fileTest = new File(pathImageTest);
									// BufferedImage bimgTest = null;
									// try {
									// bimgTest = ImageIO.read(fileTest);
									// } catch (IOException e2) {
									// }
									// if (bimgTest == null) {
									// btnImg.setIcon(null);
									// }
									// if (bimgTest != null) {
									// Image scaledTest =
									// bimgTest.getScaledInstance(widthHinhLon,
									// heightHinhLon,
									// Image.SCALE_SMOOTH);
									// ImageIcon imageTest = new
									// ImageIcon(scaledTest);
									// btnImg.setIcon(null);
									// btnImg.setIcon(imageTest);
									// }
									// // end image he thong
									// END CODE CHINH THUC
									// CODE TAM THOI

									// check xem nhan vien co cat com hay khong
									// nho check cho nay listDataVerify bi null
									// neu khong cat com ->
									List<FoodNhaAn> foodNhaAnTop4 = new ArrayList<>();
									orderFoodCurrent = new OrderFood();
									// chua handle cho nay

									// kiem tra xem hien tai co dang thuoc ca
									// nao hay khong
									if (shiftsCurrent != 0) {
										// thai
										// java.sql.Date dateCurrentSQL = new
										// java.sql.Date(dateCurrent.getTime());
										// query by date and employee
										String queryFood = "";
										queryFood = "SELECT ofbd.id as order_food_by_day_id, of.employee_code, of.employee_id, of.employee_name, of.department_code, of.department_name, cf.name,cf.image,cf.id as category_food_id,fbd.shifts_id, of.registration_date\r\n"
												+ "FROM order_food_by_day as ofbd, order_food as of, food_by_day fbd, category_food as cf\r\n"
												+ "WHERE of.employee_id = ? AND of.registration_date = ? AND fbd.shifts_id = ? AND ofbd.order_food_id = of.id AND ofbd.food_by_day_id = fbd.id AND cf.id = fbd.category_food_id;	";

										try {
											con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
											// kiem tra co du lieu ca do duoi DB
											// chua
											String queryChecked = "SELECT * FROM food_nha_an WHERE employee_id = ? and shifts_id = ? and food_date = ?";
											PreparedStatement preStatementChecked = null;
											preStatementChecked = con.prepareStatement(queryChecked);
											// check bang ma nhan vien cu
											preStatementChecked.setString(1, listl.get(indexMathe).getMaNhanVien());
											preStatementChecked.setInt(2, shiftsCurrent);
											preStatementChecked.setDate(3, ngay_cua_ca_SQL);
											ResultSet resultSetChecked = preStatementChecked.executeQuery();
											boolean checkedExist = false;
											if (resultSetChecked.next()) {
												checkedExist = true;
												// image he thong da nhan suat
												// an
												String pathImageDaNhanSuatAn = "imagesSystem/image-danhansuatan650x450.png";
												File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
												BufferedImage bimgDaNhanSuatAn = null;
												try {
													bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
												} catch (IOException e1) {
												}
												if (bimgDaNhanSuatAn == null) {
													btnImg.setIcon(null);
												}
												if (bimgDaNhanSuatAn != null) {
													ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
													btnImg.setIcon(imageTest);
												}
												if (preStatementChecked != null) {
													preStatementChecked.close();
												}
												// end image he thong
												return;
											}
											// Neu chua an se them du lieu vao
											// DB -> 2 truong hop: 1 la co dang
											// ky , 2
											// la khong dang ky
											// Co dang ky
											preStatement = con.prepareStatement(queryFood);
											// pass id employee
											preStatement.setString(1, listl.get(indexMathe).getMaNhanVien());
											// date param
											preStatement.setDate(2, ngay_cua_ca_SQL);
											preStatement.setInt(3, shiftsCurrent);
											ResultSet resultSet = preStatement.executeQuery();

											while (resultSet.next()) {
												// neu nhan vien do da an ca do
												// roi - > k cho them
												if (!checkedExist) {
													orderFoodCurrent.setDepartmentName(
															resultSet.getString("of.department_name"));
													orderFoodCurrent.setDepartmentCode(
															resultSet.getString("of.department_code"));
													orderFoodCurrent
															.setEmployeeCode(resultSet.getString("of.employee_code"));
													orderFoodCurrent
															.setEmployeeName(resultSet.getString("of.employee_name"));
													orderFoodCurrent.setFoodName(resultSet.getString("cf.name"));
													orderFoodCurrent.setImage(resultSet.getBytes("cf.image"));
													orderFoodCurrent
															.setFood_date(resultSet.getDate("of.registration_date"));
													orderFoodCurrent
															.setCategory_food_id(resultSet.getInt("category_food_id"));
													orderFoodCurrent
															.setEmployeeId(resultSet.getString("of.employee_id"));
													orderFoodCurrent.setShifts_id(resultSet.getInt("fbd.shifts_id"));
													orderFoodCurrent.setNotRegFood(true);
													ZKFPDemo.addOne(orderFoodCurrent, orderFoodCurrent.getShifts_id());
												}
											}
											// chua dang ky
											// handle cho phan mon an tu chon
											if (!resultSet.first()) {
												DepartmentData[] departmentHCMArray = DepartmentDataService
														.timtheophongquanly(ZKFPDemo.codeBranch);
												List<DepartmentData> departmentHCM = new ArrayList<>(
														Arrays.asList(departmentHCMArray));

//												DepartmentData[] departmentBDArray = DepartmentDataService
//														.timtheophongquanly("20002");
//												List<DepartmentData> departmentBD = new ArrayList<>(
//														Arrays.asList(departmentBDArray));
//
//												departmentHCM.addAll(departmentBD);

												// HANDLE -> Tao list id
												// department
												List<Long> listDepartmentCodeHCM = new ArrayList<Long>();
												for (DepartmentData de : departmentHCM) {
													listDepartmentCodeHCM.add(de.getId());
												}

												StringBuilder builder = new StringBuilder();
												for (int i = 0; i < listDepartmentCodeHCM.size(); i++) {
													builder.append("?,");
												}
												// query nhan vien tu du lieu
												// trung tam
												String queryEmployee = "SELECT empl.workShift, empl.layOff as da_nghi_viec,empl.id as employee_id,empl.name as employee_name,empl.code as employee_code,empl.codeOld as employee_code_old,depart.name as department_name,depart.code as department_code \r\n"
														+ "FROM dulieutrungtam.employee as empl, dulieutrungtam.department as depart\r\n"
														+ "WHERE empl.codeOld = ? AND empl.department_id IN ("
														+ builder.deleteCharAt(builder.length() - 1).toString()
														+ ") AND empl.department_id = depart.id;";
												PreparedStatement preStatementEmployee = null;
												Connection conDulieutrungtam = null;

												try {
													conDulieutrungtam = ZKFPDemo
															.getConnectionMySQL(URL.LINK_DULIEUTRUNGTAM_JDBC);
													preStatementEmployee = conDulieutrungtam
															.prepareStatement(queryEmployee);
													preStatementEmployee.setString(1,
															listl.get(indexMathe).getMaNhanVien());
													// handle param IN operator
													int index = 2;
													for (Long id : listDepartmentCodeHCM) {
														preStatementEmployee.setObject(index++, id); // or
																										// whatever
																										// it
																										// applies
													}
													ResultSet resultSet1 = preStatementEmployee.executeQuery();
													while (resultSet1.next()) {
														boolean daNghiViec = resultSet1.getBoolean("da_nghi_viec");
														// neu chua nghi viec
														// moi duoc luu
														if (!daNghiViec) {
															// orderFoodCurrent.setDepartmentName(
															// resultSet1.getString("department_name"));
															// orderFoodCurrent.setDepartmentCode(
															// resultSet1.getString("department_code"));
															// orderFoodCurrent.setEmployeeCode(
															// resultSet1.getString("employee_code"));
															// orderFoodCurrent.setEmployeeName(
															// resultSet1.getString("employee_name"));
															// orderFoodCurrent.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
															// orderFoodCurrent.setFood_date(
															// DateUtil.DATE_WITHOUT_TIME(new
															// Date()));
															// // gan thang id
															// cua category food
															// tu chon
															// orderFoodCurrent
															// .setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
															// orderFoodCurrent.setEmployeeId(
															// resultSet1.getString("employee_code_old"));
															// orderFoodCurrent.setShifts_id(shiftsCurrent);
															// orderFoodCurrent.setNotRegFood(true);
															// ZKFPDemo.addOne(orderFoodCurrent,
															// orderFoodCurrent.getShifts_id());

															boolean workShift = resultSet1.getBoolean("empl.workShift");
															String employeeCode = resultSet1.getString("employee_code");
															// nhan vien van
															// phong an ca 2
															if (!workShift && shiftsCurrent == Shifts.SHIFTS_2_ID) {
																try {
																	con = ZKFPDemo.getConnectionMySQL(
																			URL.LINK_QUANLYDATCOM_JDBC);
																	// kiem tra
																	// co dang
																	// ky tang
																	// ca hay
																	// khong
																	String query = "SELECT food_ot.employee_name, food_ot.employee_code, ot.department_code,ot.department_name,ot.food_date, ot.shifts_id, food_ot.employee_code_old "
																			+ "FROM food_over_time as food_ot, over_time as ot "
																			+ "WHERE food_ot.employee_code = ? AND ot.shifts_id = ? AND ot.food_date = ? AND "
																			+ "ot.id = food_ot.over_time_id";
																	preStatementChecked = con.prepareStatement(query);
																	// check
																	// bang ma
																	// nhan vien
																	// cu
																	preStatementChecked.setString(1, employeeCode);
																	preStatementChecked.setInt(2, shiftsCurrent);
																	preStatementChecked.setDate(3, ngay_cua_ca_SQL);
																	ResultSet rs = preStatementChecked.executeQuery();
																	if (rs.next()) {
																		orderFoodCurrent.setDepartmentName(
																				rs.getString("ot.department_name"));
																		orderFoodCurrent.setDepartmentCode(
																				rs.getString("ot.department_code"));
																		orderFoodCurrent.setEmployeeCode(employeeCode);
																		orderFoodCurrent.setEmployeeName(
																				rs.getString("food_ot.employee_name"));
																		orderFoodCurrent.setFoodName(
																				FoodCustom.FOOD_CUSTOM_NAME);
																		orderFoodCurrent.setFood_date(
																				DateUtil.DATE_WITHOUT_TIME(
																						rs.getDate("ot.food_date")));
																		// gan
																		// thang
																		// id
																		// cua
																		// category
																		// food
																		// tu
																		// chon
																		orderFoodCurrent.setCategory_food_id(
																				FoodCustom.FOOD_CUSTOM_ID);
																		if (rs.getString("employee_code_old") != null) {
																			orderFoodCurrent.setEmployeeId(
																					rs.getString("employee_code_old"));
																		}
																		orderFoodCurrent.setShifts_id(shiftsCurrent);
																		orderFoodCurrent.setOverTime(true);
																		ZKFPDemo.addOne(orderFoodCurrent,
																				orderFoodCurrent.getShifts_id());
																	}
																	if (!rs.first()) {
																		String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
																		File fileKCoSuatAn = new File(
																				pathImageKhongCoSuatAn);
																		BufferedImage bimgKCoSuatAn = null;
																		try {
																			bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
																		} catch (IOException e1) {
																			e1.printStackTrace();
																		}
																		if (bimgKCoSuatAn == null) {
																			btnImg.setIcon(null);
																		}
																		if (bimgKCoSuatAn != null) {
																			ImageIcon imageTest = new ImageIcon(
																					bimgKCoSuatAn);
																			btnImg.setIcon(imageTest);
																		}
																		return;
																	}
																} catch (Exception e1) {
																	// TODO:
																	// handle
																	// exception
																} finally {
																	try {
																		ZKFPDemo.closeConnectionPre(con,
																				preStatementChecked);
																	} catch (Exception e2) {
																		e2.printStackTrace();
																		;
																	}
																}
															}
															// di ca hoac nhan
															// vien van phong an
															// ca 1
															if (workShift || !workShift
																	&& shiftsCurrent == Shifts.SHIFTS_1_ID) {
																orderFoodCurrent.setDepartmentName(
																		resultSet1.getString("department_name"));
																orderFoodCurrent.setDepartmentCode(
																		resultSet1.getString("department_code"));
																orderFoodCurrent.setEmployeeCode(employeeCode);
																orderFoodCurrent.setEmployeeName(
																		resultSet1.getString("employee_name"));
																orderFoodCurrent
																		.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
																orderFoodCurrent.setFood_date(
																		DateUtil.DATE_WITHOUT_TIME(Shifts.NGAY_CUA_CA));
																// gan thang id
																// cua category
																// food tu chon
																orderFoodCurrent
																		.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
																orderFoodCurrent.setEmployeeId(
																		resultSet1.getString("employee_code_old"));
																orderFoodCurrent.setShifts_id(shiftsCurrent);
																ZKFPDemo.addOne(orderFoodCurrent,
																		orderFoodCurrent.getShifts_id());
															}
															// nhan vien van
															// phong an ca 3
															if (!workShift && shiftsCurrent == Shifts.SHIFTS_3_ID) {
																String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
																File fileKCoSuatAn = new File(pathImageKhongCoSuatAn);
																BufferedImage bimgKCoSuatAn = null;
																try {
																	bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
																} catch (IOException e1) {
																	e1.printStackTrace();
																}
																if (bimgKCoSuatAn == null) {
																	btnImg.setIcon(null);
																}
																if (bimgKCoSuatAn != null) {
																	ImageIcon imageTest = new ImageIcon(bimgKCoSuatAn);
																	btnImg.setIcon(imageTest);
																}
																return;
															}
															// END KIEM TRA CA 2
														}
													}
													if (!resultSet1.first()) {
														String pathImageDaNhanSuatAn = "imagesSystem/image-kcttnhanvien650x450.png";
														File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
														BufferedImage bimgDaNhanSuatAn = null;
														try {
															bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
														} catch (IOException e1) {
															e1.printStackTrace();
														}
														if (bimgDaNhanSuatAn == null) {
															btnImg.setIcon(null);
														}
														if (bimgDaNhanSuatAn != null) {
															ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
															btnImg.setIcon(imageTest);
														}
														return;
													}
												} catch (Exception e1) {
													// TODO: handle exception
												} finally {
													try {
														ZKFPDemo.closeConnectionPre(conDulieutrungtam,
																preStatementEmployee);
													} catch (Exception e2) {
														e2.printStackTrace();
														;
													}
												}
											}
											// end mon an tu chon
										} catch (Exception e1) {
											// TODO: handle exception
										} finally {
											try {
												ZKFPDemo.closeConnectionPre(con, preStatement);
											} catch (Exception e2) {
												e2.printStackTrace();
											}
										}
										// da dang ky mon an
										if (orderFoodCurrent.getEmployeeName() != null) {
											textAreaMaNV.setText(orderFoodCurrent.getEmployeeCode());
											textAreaTenNV.setText(orderFoodCurrent.getEmployeeName());
											textAreaPhongban.setText(orderFoodCurrent.getDepartmentName());
											// convert date to string
											String pattern = "dd-MM-yyyy";
											DateFormat df = new SimpleDateFormat(pattern);
											String foodDate = df.format(orderFoodCurrent.getFood_date());
											textAreaNgay.setText(foodDate);
											// labelTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());a
											textAreaTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());

											// set hinh
											// check neu hinh bi null
											if (orderFoodCurrent.getImage() == null) {
												if (orderFoodCurrent
														.getCategory_food_id() == FoodCustom.FOOD_CUSTOM_ID) {
													if (bimgTuchon == null) {
														btnImg.setIcon(null);
													}
													if (bimgTuchon != null) {
														Image scaledTuchon = bimgTuchon.getScaledInstance(widthHinhLon,
																heightHinhLon, Image.SCALE_SMOOTH);
														ImageIcon imageTuchon = new ImageIcon(scaledTuchon);
														btnImg.setIcon(null);
														btnImg.setIcon(imageTuchon);
													}
												} else {
													// khong co hinh mon an
													if (bimgNotImage == null) {
														btnImg.setIcon(null);
													}
													if (bimgNotImage != null) {
														ImageIcon imageNotImage = new ImageIcon(bimgNotImage);
														btnImg.setIcon(imageNotImage);
													}
												}
											} else {
												Image img = Toolkit.getDefaultToolkit()
														.createImage(orderFoodCurrent.getImage());
												ImageIcon icon = new ImageIcon(img.getScaledInstance(widthHinhLon,
														heightHinhLon, Image.SCALE_SMOOTH));
												btnImg.setIcon(icon);
											}
										}

										// handle show 3 o nho cho 4 nguoi quet
										// gan nhat
										String queryTop4 = "SELECT FNA.employee_name,CF.name, FNA.employee_id, CF.image\r\n"
												+ "FROM food_nha_an as FNA, category_food as CF\r\n"
												+ "WHERE FNA.category_food_id = CF.id and FNA.shifts_id = ? and FNA.food_date = ?\r\n"
												+ "ORDER BY FNA.id DESC\r\n" + "LIMIT 3;";

										try {
											con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
											preStatementTop3 = con.prepareStatement(queryTop4);
											preStatementTop3.setInt(1, shiftsCurrent);
											preStatementTop3.setDate(2, ngay_cua_ca_SQL);
											ResultSet resultSet = preStatementTop3.executeQuery();
											while (resultSet.next()) {
												FoodNhaAn foodNhaAn = new FoodNhaAn();
												foodNhaAn.setEmployeeName(resultSet.getString("FNA.employee_name"));
												foodNhaAn.setImageFood(resultSet.getBytes("CF.image"));
												foodNhaAn.setEmployeeIdOld(resultSet.getString("FNA.employee_id"));
												foodNhaAn.setFoodName(resultSet.getString("CF.name"));
												foodNhaAnTop4.add(foodNhaAn);
											}
										} catch (Exception e1) {
											// TODO: handle exception
										} finally {
											try {
												ZKFPDemo.closeConnectionPre(con, preStatementTop3);
											} catch (Exception e2) {
												e2.printStackTrace();
												;
											}
										}
										// set hinh
										// query co bi rong hay khong
										if (!foodNhaAnTop4.isEmpty()) {
											// query co 2 phan tu
											if (foodNhaAnTop4.size() == 1) {

												// image nhan vien
												String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
														+ ".bmp";
												File file = new File(pathImage);
												BufferedImage bimg = null;
												try {
													bimg = ImageIO.read(file);
												} catch (IOException e1) {
												}
												if (bimg == null) {
													// image he thong nhan vien
													// khong co hinh
													if (bimgNoImageNV == null) {
														labelHinhNV1.setIcon(null);
													}
													if (bimgNoImageNV != null) {
														ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
														labelHinhNV1.setIcon(imageNoImageNV);
													}
												}
												if (bimg != null) {
													Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
													ImageIcon image = new ImageIcon(scaled);
													labelHinhNV1.setIcon(image);
												}
												textAreaTenMonAn1
														.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
												// image mon an 1
												if (foodNhaAnTop4.get(0).getImageFood() == null) {
													btnImage1.setIcon(null);
												} else {
													Image img1 = Toolkit.getDefaultToolkit()
															.createImage(foodNhaAnTop4.get(0).getImageFood());
													ImageIcon icon1 = new ImageIcon(
															img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
													btnImage1.setIcon(icon1);
												}
												labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());
											}
											if (foodNhaAnTop4.size() == 2) {
												// image nhan vien 1
												String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
														+ ".bmp";
												File file = new File(pathImage);
												BufferedImage bimg = null;
												try {
													bimg = ImageIO.read(file);
												} catch (IOException e1) {
												}

												if (bimg == null) {
													// image he thong nhan vien
													// khong co hinh
													if (bimgNoImageNV == null) {
														labelHinhNV1.setIcon(null);
													}
													if (bimgNoImageNV != null) {
														ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
														labelHinhNV1.setIcon(imageNoImageNV);
													}
												}
												if (bimg != null) {
													Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
													ImageIcon image = new ImageIcon(scaled);
													labelHinhNV1.setIcon(image);
												}
												textAreaTenMonAn1
														.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
												// check neu image null
												if (foodNhaAnTop4.get(0).getImageFood() == null) {
													btnImage1.setIcon(null);
												} else {
													Image img1 = Toolkit.getDefaultToolkit()
															.createImage(foodNhaAnTop4.get(0).getImageFood());
													ImageIcon icon1 = new ImageIcon(
															img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
													btnImage1.setIcon(icon1);
												}

												labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

												// image nhan vien 2
												String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
														+ ".bmp";
												File file2 = new File(pathImage2);
												BufferedImage bimg2 = null;
												try {
													bimg2 = ImageIO.read(file2);
												} catch (IOException e1) {
												}
												if (bimg2 == null) {
													// image he thong nhan vien
													// khong co hinh
													if (bimgNoImageNV == null) {
														labelHinhNV2.setIcon(null);
													}
													if (bimgNoImageNV != null) {
														ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
														labelHinhNV2.setIcon(imageNoImageNV);
													}
												}
												if (bimg2 != null) {
													Image scaled2 = bimg2.getScaledInstance(140, 140,
															Image.SCALE_SMOOTH);
													ImageIcon image2 = new ImageIcon(scaled2);
													labelHinhNV2.setIcon(image2);
												}
												textAreaTenMonAn2
														.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
												// check neu image mon an 2 null
												if (foodNhaAnTop4.get(1).getImageFood() == null) {
													btnImage2.setIcon(null);
												} else {
													Image img2 = Toolkit.getDefaultToolkit()
															.createImage(foodNhaAnTop4.get(1).getImageFood());
													ImageIcon icon2 = new ImageIcon(
															img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
													btnImage2.setIcon(icon2);
												}
												labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());
											}
											if (foodNhaAnTop4.size() == 3) {
												// image nhan vien 1
												String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
														+ ".bmp";
												File file = new File(pathImage);
												BufferedImage bimg = null;
												try {
													bimg = ImageIO.read(file);
												} catch (IOException e1) {
												}
												if (bimg == null) {
													// image he thong nhan vien
													// khong co hinh
													if (bimgNoImageNV == null) {
														labelHinhNV1.setIcon(null);
													}
													if (bimgNoImageNV != null) {
														ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
														labelHinhNV1.setIcon(imageNoImageNV);
													}
												}
												if (bimg != null) {
													Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
													ImageIcon image = new ImageIcon(scaled);
													labelHinhNV1.setIcon(image);
												}
												// hinh mon an 1
												// check neu image null
												textAreaTenMonAn1
														.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
												if (foodNhaAnTop4.get(0).getImageFood() == null) {
													btnImage1.setIcon(null);
												} else {
													Image img1 = Toolkit.getDefaultToolkit()
															.createImage(foodNhaAnTop4.get(0).getImageFood());
													ImageIcon icon1 = new ImageIcon(
															img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
													btnImage1.setIcon(icon1);
												}
												labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

												// image nhan vien 2
												String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
														+ ".bmp";
												File file2 = new File(pathImage2);
												BufferedImage bimg2 = null;
												try {
													bimg2 = ImageIO.read(file2);
												} catch (IOException e1) {
												}
												if (bimg2 == null) {
													// image he thong nhan vien
													// khong co hinh
													if (bimgNoImageNV == null) {
														labelHinhNV2.setIcon(null);
													}
													if (bimgNoImageNV != null) {
														ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
														labelHinhNV2.setIcon(imageNoImageNV);
													}
												}
												if (bimg2 != null) {
													Image scaled2 = bimg2.getScaledInstance(140, 140,
															Image.SCALE_SMOOTH);
													ImageIcon image2 = new ImageIcon(scaled2);
													labelHinhNV2.setIcon(image2);
												}

												// image mon an 2
												// check neu image mon an 2 null
												textAreaTenMonAn2
														.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
												if (foodNhaAnTop4.get(1).getImageFood() == null) {
													btnImage2.setIcon(null);
												} else {
													Image img2 = Toolkit.getDefaultToolkit()
															.createImage(foodNhaAnTop4.get(1).getImageFood());
													ImageIcon icon2 = new ImageIcon(
															img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
													btnImage2.setIcon(icon2);
												}
												labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());

												// image nhan vien 3
												String pathImage3 = "images/" + foodNhaAnTop4.get(2).getEmployeeIdOld()
														+ ".bmp";
												File file3 = new File(pathImage3);
												BufferedImage bimg3 = null;
												try {
													bimg3 = ImageIO.read(file3);
												} catch (IOException e1) {
												}
												if (bimg3 == null) {
													if (bimgNoImageNV == null) {
														labelHinhNV3.setIcon(null);
													}
													if (bimgNoImageNV != null) {
														ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
														labelHinhNV3.setIcon(imageNoImageNV);
													}
												}
												if (bimg3 != null) {
													Image scaled3 = bimg3.getScaledInstance(140, 140,
															Image.SCALE_SMOOTH);
													ImageIcon image3 = new ImageIcon(scaled3);
													labelHinhNV3.setIcon(image3);
												}

												// image mon an 3
												textAreaTenMonAn3
														.setText(foodNhaAnTop4.get(2).getFoodName().toUpperCase());
												if (foodNhaAnTop4.get(2).getImageFood() == null) {
													btnImage3.setIcon(null);
												} else {
													Image img3 = Toolkit.getDefaultToolkit()
															.createImage(foodNhaAnTop4.get(2).getImageFood());
													ImageIcon icon3 = new ImageIcon(
															img3.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
													btnImage3.setIcon(icon3);
												}
												labelName3.setText(foodNhaAnTop4.get(2).getEmployeeName());
											}
										}
										textArea.setText("Thành công");
										// end thai
									}
									// Khong co gio an phu hop
									else {
										// image he thong chua san sang
										String pathImageTest = "imagesSystem/image-khongcogioanphuhop.png";
										File fileTest = new File(pathImageTest);
										BufferedImage bimgTest = null;
										try {
											bimgTest = ImageIO.read(fileTest);
										} catch (IOException e2) {
										}
										if (bimgTest == null) {
											btnImg.setIcon(null);
										}
										if (bimgTest != null) {
											Image scaledTest = bimgTest.getScaledInstance(widthHinhLon, heightHinhLon,
													Image.SCALE_SMOOTH);
											ImageIcon imageTest = new ImageIcon(scaledTest);
											btnImg.setIcon(null);
											btnImg.setIcon(imageTest);
										}
										// end image he thong
									}
									// END CODE TAM THOI

								}
								// CAT COM VA VAN CHO LUU
								if (!isEat && ZKFPDemo.ON_ALLOW_EXCEPTION) {
									// co gio an
									List<FoodNhaAn> foodNhaAnTop4 = new ArrayList<>();
									orderFoodCurrent = new OrderFood();
									// handle khong co gio an va da an roi
									if (shiftsCurrent == 0) {
										// image he thong chua san sang
										String pathImageTest = "imagesSystem/image-khongcogioanphuhop.png";
										File fileTest = new File(pathImageTest);
										BufferedImage bimgTest = null;
										try {
											bimgTest = ImageIO.read(fileTest);
										} catch (IOException e2) {
										}
										if (bimgTest == null) {
											btnImg.setIcon(null);
										}
										if (bimgTest != null) {
											Image scaledTest = bimgTest.getScaledInstance(widthHinhLon, heightHinhLon,
													Image.SCALE_SMOOTH);
											ImageIcon imageTest = new ImageIcon(scaledTest);
											btnImg.setIcon(null);
											btnImg.setIcon(imageTest);
										}
										return;
									}
									// chua handle cho nay

									// kiem tra xem hien tai co dang thuoc ca
									// nao hay khong
									if (shiftsCurrent != 0) {
										// thai
										// java.sql.Date dateCurrentSQL = new
										// java.sql.Date(dateCurrent.getTime());
										// query by date and employee
										String queryFood = "";
										queryFood = "SELECT ofbd.id as order_food_by_day_id, of.employee_code, of.employee_id, of.employee_name, of.department_code, of.department_name, cf.name,cf.image,cf.id as category_food_id,fbd.shifts_id, of.registration_date\r\n"
												+ "FROM order_food_by_day as ofbd, order_food as of, food_by_day fbd, category_food as cf\r\n"
												+ "WHERE of.employee_id = ? AND of.registration_date = ? AND fbd.shifts_id = ? AND ofbd.order_food_id = of.id AND ofbd.food_by_day_id = fbd.id AND cf.id = fbd.category_food_id;	";

										try {
											con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
											// kiem tra co du lieu ca do duoi DB
											// chua
											String queryChecked = "SELECT * FROM food_nha_an WHERE employee_id = ? and shifts_id = ? and food_date = ?";
											PreparedStatement preStatementChecked = null;
											preStatementChecked = con.prepareStatement(queryChecked);
											// check bang ma nhan vien cu
											preStatementChecked.setString(1, listl.get(indexMathe).getMaNhanVien());
											preStatementChecked.setInt(2, shiftsCurrent);
											preStatementChecked.setDate(3, ngay_cua_ca_SQL);
											ResultSet resultSetChecked = preStatementChecked.executeQuery();
											boolean checkedExist = false;
											if (resultSetChecked.next()) {
												checkedExist = true;
												// image he thong da nhan suat
												// an
												String pathImageDaNhanSuatAn = "imagesSystem/image-danhansuatan650x450.png";
												File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
												BufferedImage bimgDaNhanSuatAn = null;
												try {
													bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
												} catch (IOException e1) {
												}
												if (bimgDaNhanSuatAn == null) {
													btnImg.setIcon(null);
												}
												if (bimgDaNhanSuatAn != null) {
													ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
													btnImg.setIcon(imageTest);
												}
												// end image he thong
												if (preStatementChecked != null) {
													preStatementChecked.close();
												}
												return;
											}
											// Neu chua an se them du lieu vao
											// DB -> 2 truong hop: 1 la co dang
											// ky , 2
											// la khong dang ky
											// Co dang ky
											preStatement = con.prepareStatement(queryFood);
											// pass id employee
											preStatement.setString(1, listl.get(indexMathe).getMaNhanVien());
											// date param
											preStatement.setDate(2, ngay_cua_ca_SQL);
											preStatement.setInt(3, shiftsCurrent);
											ResultSet resultSet = preStatement.executeQuery();

											while (resultSet.next()) {
												// neu nhan vien do da an ca do
												// roi - > k cho them
												if (!checkedExist) {
													orderFoodCurrent.setDepartmentName(
															resultSet.getString("of.department_name"));
													orderFoodCurrent.setDepartmentCode(
															resultSet.getString("of.department_code"));
													orderFoodCurrent
															.setEmployeeCode(resultSet.getString("of.employee_code"));
													orderFoodCurrent
															.setEmployeeName(resultSet.getString("of.employee_name"));
													orderFoodCurrent.setFoodName(resultSet.getString("cf.name"));
													orderFoodCurrent.setImage(resultSet.getBytes("cf.image"));
													orderFoodCurrent
															.setFood_date(resultSet.getDate("of.registration_date"));
													orderFoodCurrent
															.setCategory_food_id(resultSet.getInt("category_food_id"));
													orderFoodCurrent
															.setEmployeeId(resultSet.getString("of.employee_id"));
													orderFoodCurrent.setShifts_id(resultSet.getInt("fbd.shifts_id"));
													orderFoodCurrent.setNotRegFood(true);
													ZKFPDemo.addOne(orderFoodCurrent, orderFoodCurrent.getShifts_id());
												}
												togglebtnKhongQuetVT.setText("CÓ Đ.KÝ");
												ZKFPDemo.ON_ALLOW_EXCEPTION = false;
												return;
											}
											// NEU CHUA AN THI CHO THEM SUAT AN
											// VAO DB
											if (!checkedExist) {
												orderFoodCurrent = new OrderFood();
												DepartmentData[] departmentHCMArray = DepartmentDataService
														.timtheophongquanly(ZKFPDemo.codeBranch);
												List<DepartmentData> departmentHCM = new ArrayList<>(
														Arrays.asList(departmentHCMArray));

//												DepartmentData[] departmentBDArray = DepartmentDataService
//														.timtheophongquanly("20002");
//												List<DepartmentData> departmentBD = new ArrayList<>(
//														Arrays.asList(departmentBDArray));
//
//												departmentHCM.addAll(departmentBD);

												// HANDLE -> Tao list id
												// department
												List<Long> listDepartmentCodeHCM = new ArrayList<Long>();
												for (DepartmentData de : departmentHCM) {
													listDepartmentCodeHCM.add(de.getId());
												}

												StringBuilder builder = new StringBuilder();
												for (int i = 0; i < listDepartmentCodeHCM.size(); i++) {
													builder.append("?,");
												}
												// query nhan vien tu du lieu
												// trung tam
												String queryEmployee = "SELECT empl.workShift, empl.layOff as da_nghi_viec,empl.id as employee_id,empl.name as employee_name,empl.code as employee_code,empl.codeOld as employee_code_old,depart.name as department_name,depart.code as department_code \r\n"
														+ "FROM dulieutrungtam.employee as empl, dulieutrungtam.department as depart\r\n"
														+ "WHERE empl.codeOld = ? AND empl.department_id IN ("
														+ builder.deleteCharAt(builder.length() - 1).toString()
														+ ") AND empl.department_id = depart.id;";
												PreparedStatement preStatementEmployee = null;
												Connection conDulieutrungtam = null;

												try {
													conDulieutrungtam = ZKFPDemo
															.getConnectionMySQL(URL.LINK_DULIEUTRUNGTAM_JDBC);
													preStatementEmployee = conDulieutrungtam
															.prepareStatement(queryEmployee);
													preStatementEmployee.setString(1,
															listl.get(indexMathe).getMaNhanVien());
													// handle param IN operator
													int index = 2;
													for (Long id : listDepartmentCodeHCM) {
														preStatementEmployee.setObject(index++, id); // or
																										// whatever
																										// it
																										// applies
													}
													ResultSet resultSet1 = preStatementEmployee.executeQuery();
													while (resultSet1.next()) {
														boolean daNghiViec = resultSet1.getBoolean("da_nghi_viec");
														// neu chua nghi viec
														// moi duoc luu
														if (!daNghiViec) {
															// orderFoodCurrent.setDepartmentName(
															// resultSet1.getString("department_name"));
															// orderFoodCurrent.setDepartmentCode(
															// resultSet1.getString("department_code"));
															// orderFoodCurrent.setEmployeeCode(
															// resultSet1.getString("employee_code"));
															// orderFoodCurrent.setEmployeeName(
															// resultSet1.getString("employee_name"));
															// orderFoodCurrent.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
															// orderFoodCurrent.setFood_date(
															// DateUtil.DATE_WITHOUT_TIME(new
															// Date()));
															// // gan thang id
															// cua category food
															// tu chon
															// orderFoodCurrent
															// .setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
															// orderFoodCurrent.setEmployeeId(
															// resultSet1.getString("employee_code_old"));
															// orderFoodCurrent.setShifts_id(shiftsCurrent);
															// ZKFPDemo.addOneException(orderFoodCurrent,
															// orderFoodCurrent.getShifts_id());

															boolean workShift = resultSet1.getBoolean("empl.workShift");
															String employeeCode = resultSet1.getString("employee_code");
															// nhan vien van
															// phong an ca 2
															if (!workShift && shiftsCurrent == Shifts.SHIFTS_2_ID) {
																try {
																	con = ZKFPDemo.getConnectionMySQL(
																			URL.LINK_QUANLYDATCOM_JDBC);
																	// kiem tra
																	// co dang
																	// ky tang
																	// ca hay
																	// khong
																	String query = "SELECT food_ot.employee_name, food_ot.employee_code, ot.department_code,ot.department_name,ot.food_date, ot.shifts_id, food_ot.employee_code_old "
																			+ "FROM food_over_time as food_ot, over_time as ot "
																			+ "WHERE food_ot.employee_code = ? AND ot.shifts_id = ? AND ot.food_date = ? AND "
																			+ "ot.id = food_ot.over_time_id";
																	preStatementChecked = con.prepareStatement(query);
																	// check
																	// bang ma
																	// nhan vien
																	// cu
																	preStatementChecked.setString(1, employeeCode);
																	preStatementChecked.setInt(2, shiftsCurrent);
																	preStatementChecked.setDate(3, ngay_cua_ca_SQL);
																	ResultSet rs = preStatementChecked.executeQuery();
																	if (rs.next()) {
																		orderFoodCurrent.setDepartmentName(
																				rs.getString("ot.department_name"));
																		orderFoodCurrent.setDepartmentCode(
																				rs.getString("ot.department_code"));
																		orderFoodCurrent.setEmployeeCode(employeeCode);
																		orderFoodCurrent.setEmployeeName(
																				rs.getString("food_ot.employee_name"));
																		orderFoodCurrent.setFoodName(
																				FoodCustom.FOOD_CUSTOM_NAME);
																		orderFoodCurrent.setFood_date(
																				DateUtil.DATE_WITHOUT_TIME(
																						rs.getDate("ot.food_date")));
																		// gan
																		// thang
																		// id
																		// cua
																		// category
																		// food
																		// tu
																		// chon
																		orderFoodCurrent.setCategory_food_id(
																				FoodCustom.FOOD_CUSTOM_ID);
																		if (rs.getString("employee_code_old") != null) {
																			orderFoodCurrent.setEmployeeId(
																					rs.getString("employee_code_old"));
																		}
																		orderFoodCurrent.setShifts_id(shiftsCurrent);
																		orderFoodCurrent.setOverTime(true);
																		ZKFPDemo.addOne(orderFoodCurrent,
																				orderFoodCurrent.getShifts_id());
																	}
																	if (!rs.first()) {
																		String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
																		File fileKCoSuatAn = new File(
																				pathImageKhongCoSuatAn);
																		BufferedImage bimgKCoSuatAn = null;
																		try {
																			bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
																		} catch (IOException e1) {
																			e1.printStackTrace();
																		}
																		if (bimgKCoSuatAn == null) {
																			btnImg.setIcon(null);
																		}
																		if (bimgKCoSuatAn != null) {
																			ImageIcon imageTest = new ImageIcon(
																					bimgKCoSuatAn);
																			btnImg.setIcon(imageTest);
																		}
																		return;
																	}
																} catch (Exception e1) {
																	// TODO:
																	// handle
																	// exception
																} finally {
																	try {
																		ZKFPDemo.closeConnectionPre(con,
																				preStatementChecked);
																	} catch (Exception e2) {
																		e2.printStackTrace();
																		;
																	}
																}
															}
															// di ca hoac nhan
															// vien van phong an
															// ca 1
															if (workShift || !workShift
																	&& shiftsCurrent == Shifts.SHIFTS_1_ID) {
																orderFoodCurrent.setDepartmentName(
																		resultSet1.getString("department_name"));
																orderFoodCurrent.setDepartmentCode(
																		resultSet1.getString("department_code"));
																orderFoodCurrent.setEmployeeCode(employeeCode);
																orderFoodCurrent.setEmployeeName(
																		resultSet1.getString("employee_name"));
																orderFoodCurrent
																		.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
																orderFoodCurrent.setFood_date(
																		DateUtil.DATE_WITHOUT_TIME(Shifts.NGAY_CUA_CA));
																// gan thang id
																// cua category
																// food tu chon
																orderFoodCurrent
																		.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
																orderFoodCurrent.setEmployeeId(
																		resultSet1.getString("employee_code_old"));
																orderFoodCurrent.setShifts_id(shiftsCurrent);
																ZKFPDemo.addOne(orderFoodCurrent,
																		orderFoodCurrent.getShifts_id());
															}
															// nhan vien van
															// phong an ca 3
															if (!workShift && shiftsCurrent == Shifts.SHIFTS_3_ID) {
																String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
																File fileKCoSuatAn = new File(pathImageKhongCoSuatAn);
																BufferedImage bimgKCoSuatAn = null;
																try {
																	bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
																} catch (IOException e1) {
																	e1.printStackTrace();
																}
																if (bimgKCoSuatAn == null) {
																	btnImg.setIcon(null);
																}
																if (bimgKCoSuatAn != null) {
																	ImageIcon imageTest = new ImageIcon(bimgKCoSuatAn);
																	btnImg.setIcon(imageTest);
																}
																return;
															}
															// END KIEM TRA CA 2
														}
													}
													if (!resultSet1.first()) {
														String pathImageDaNhanSuatAn = "imagesSystem/image-kcttnhanvien650x450.png";
														File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
														BufferedImage bimgDaNhanSuatAn = null;
														try {
															bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
														} catch (IOException e1) {
															e1.printStackTrace();
														}
														if (bimgDaNhanSuatAn == null) {
															btnImg.setIcon(null);
														}
														if (bimgDaNhanSuatAn != null) {
															ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
															btnImg.setIcon(imageTest);
														}
														return;
													}
												} catch (Exception e1) {
													// TODO: handle exception
												} finally {
													try {
														ZKFPDemo.closeConnectionPre(conDulieutrungtam,
																preStatementEmployee);
													} catch (Exception e2) {
														e2.printStackTrace();
														;
													}
												}
											}
										} catch (Exception e1) {
											// TODO: handle exception
										} finally {
											try {
												ZKFPDemo.closeConnectionPre(con, preStatement);
											} catch (Exception e2) {
												e2.printStackTrace();
											}
										}
										// mon an da luu xuong db -> handle hien
										// ra view
										if (orderFoodCurrent.getEmployeeName() != null) {
											textAreaMaNV.setText(orderFoodCurrent.getEmployeeCode());
											textAreaTenNV.setText(orderFoodCurrent.getEmployeeName());
											textAreaPhongban.setText(orderFoodCurrent.getDepartmentName());
											// convert date to string
											String pattern = "dd-MM-yyyy";
											DateFormat df = new SimpleDateFormat(pattern);
											String foodDate = df.format(orderFoodCurrent.getFood_date());
											textAreaNgay.setText(foodDate);
											textAreaMonAn.setText(orderFoodCurrent.getFoodName());
											// set hinh
											// check neu hinh bi null
											if (orderFoodCurrent.getImage() == null) {
												if (orderFoodCurrent
														.getCategory_food_id() == FoodCustom.FOOD_CUSTOM_ID) {
													if (bimgTuchon == null) {
														btnImg.setIcon(null);
													}
													if (bimgTuchon != null) {
														Image scaledTuchon = bimgTuchon.getScaledInstance(widthHinhLon,
																heightHinhLon, Image.SCALE_SMOOTH);
														ImageIcon imageTuchon = new ImageIcon(scaledTuchon);
														btnImg.setIcon(null);
														btnImg.setIcon(imageTuchon);
													}
												} else {
													// khong co hinh mon an
													if (bimgNotImage == null) {
														btnImg.setIcon(null);
													}
													if (bimgNotImage != null) {
														ImageIcon imageNotImage = new ImageIcon(bimgNotImage);
														btnImg.setIcon(imageNotImage);
													}
												}
											} else {
												Image img = Toolkit.getDefaultToolkit()
														.createImage(orderFoodCurrent.getImage());
												ImageIcon icon = new ImageIcon(
														img.getScaledInstance(650, 450, Image.SCALE_SMOOTH));
												btnImg.setIcon(icon);
											}
										}
										textArea.setText("Thành công");
										togglebtnKhongQuetVT.setText("CÓ Đ.KÝ");
										ZKFPDemo.ON_ALLOW_EXCEPTION = false;
									}
									// end
								}
							}
							// nguoc lai khong co data
							if (ZKFPDemo.listDataVerify == null) {
								// // image he thong chua san sang
								// String pathImageNull =
								// "imagesSystem/image-nulldatachamcong.png";
								// File fileNull = new File(pathImageNull);
								// BufferedImage bimgNull = null;
								// try {
								// bimgNull = ImageIO.read(fileNull);
								// } catch (IOException e2) {
								// }
								// if (bimgNull == null) {
								// btnImg.setIcon(null);
								// }
								// if (bimgNull != null) {
								// Image scaledTest =
								// bimgNull.getScaledInstance(widthHinhLon,
								// heightHinhLon,
								// Image.SCALE_SMOOTH);
								// ImageIcon imageTest = new
								// ImageIcon(scaledTest);
								// btnImg.setIcon(null);
								// btnImg.setIcon(imageTest);
								// }
								// // end image he thong

								// CODE CHO NHAN VIEN CA 3 LUU DU LIEU
								// check xem nhan vien co cat com hay khong
								// nho check cho nay listDataVerify bi null
								// neu khong cat com ->
								List<FoodNhaAn> foodNhaAnTop4 = new ArrayList<>();
								orderFoodCurrent = new OrderFood();
								// chua handle cho nay

								// kiem tra xem hien tai co dang thuoc ca nao
								// hay khong
								if (shiftsCurrent != 0) {
									// query by date and employee
									String queryFood = "";
									queryFood = "SELECT ofbd.id as order_food_by_day_id, of.employee_code, of.employee_id, of.employee_name, of.department_code, of.department_name, cf.name,cf.image,cf.id as category_food_id,fbd.shifts_id, of.registration_date\r\n"
											+ "FROM order_food_by_day as ofbd, order_food as of, food_by_day fbd, category_food as cf\r\n"
											+ "WHERE of.employee_id = ? AND of.registration_date = ? AND fbd.shifts_id = ? AND ofbd.order_food_id = of.id AND ofbd.food_by_day_id = fbd.id AND cf.id = fbd.category_food_id;	";

									try {
										con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
										// kiem tra co du lieu ca do duoi DB
										// chua
										String queryChecked = "SELECT * FROM food_nha_an WHERE employee_id = ? and shifts_id = ? and food_date = ?";
										PreparedStatement preStatementChecked = null;
										preStatementChecked = con.prepareStatement(queryChecked);
										// check bang ma nhan vien cu
										preStatementChecked.setString(1, listl.get(indexMathe).getMaNhanVien());
										preStatementChecked.setInt(2, shiftsCurrent);
										preStatementChecked.setDate(3, ngay_cua_ca_SQL);
										ResultSet resultSetChecked = preStatementChecked.executeQuery();
										boolean checkedExist = false;
										if (resultSetChecked.next()) {
											checkedExist = true;
											// image he thong da nhan suat an
											String pathImageDaNhanSuatAn = "imagesSystem/image-danhansuatan650x450.png";
											File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
											BufferedImage bimgDaNhanSuatAn = null;
											try {
												bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
											} catch (IOException e1) {
											}
											if (bimgDaNhanSuatAn == null) {
												btnImg.setIcon(null);
											}
											if (bimgDaNhanSuatAn != null) {
												ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
												btnImg.setIcon(imageTest);
											}
											if (preStatementChecked != null) {
												preStatementChecked.close();
											}
											// end image he thong
											return;
										}
										// Neu chua an se them du lieu vao DB ->
										// 2 truong hop: 1 la co dang ky , 2
										// la khong dang ky
										preStatement = con.prepareStatement(queryFood);
										// pass id employee
										preStatement.setString(1, listl.get(indexMathe).getMaNhanVien());
										// date param
										preStatement.setDate(2, ngay_cua_ca_SQL);
										preStatement.setInt(3, shiftsCurrent);
										ResultSet resultSet = preStatement.executeQuery();
										// Co dang ky
										while (resultSet.next()) {
											// neu nhan vien do da an ca do roi
											// - > k cho them
											if (!checkedExist) {
												orderFoodCurrent
														.setDepartmentName(resultSet.getString("of.department_name"));
												orderFoodCurrent
														.setDepartmentCode(resultSet.getString("of.department_code"));
												orderFoodCurrent
														.setEmployeeCode(resultSet.getString("of.employee_code"));
												orderFoodCurrent
														.setEmployeeName(resultSet.getString("of.employee_name"));
												orderFoodCurrent.setFoodName(resultSet.getString("cf.name"));
												orderFoodCurrent.setImage(resultSet.getBytes("cf.image"));
												orderFoodCurrent
														.setFood_date(resultSet.getDate("of.registration_date"));
												orderFoodCurrent
														.setCategory_food_id(resultSet.getInt("category_food_id"));
												orderFoodCurrent.setEmployeeId(resultSet.getString("of.employee_id"));
												orderFoodCurrent.setShifts_id(resultSet.getInt("fbd.shifts_id"));
												orderFoodCurrent.setNotRegFood(true);
												ZKFPDemo.addOne(orderFoodCurrent, orderFoodCurrent.getShifts_id());
											}
										}
										// chua dang ky
										// handle cho phan mon an tu chon
										if (!resultSet.first()) {
											DepartmentData[] departmentHCMArray = DepartmentDataService
													.timtheophongquanly(ZKFPDemo.codeBranch);
											List<DepartmentData> departmentHCM = new ArrayList<>(
													Arrays.asList(departmentHCMArray));

//											DepartmentData[] departmentBDArray = DepartmentDataService
//													.timtheophongquanly("20002");
//											List<DepartmentData> departmentBD = new ArrayList<>(
//													Arrays.asList(departmentBDArray));
//
//											departmentHCM.addAll(departmentBD);

											// HANDLE -> Tao list id department
											List<Long> listDepartmentCodeHCM = new ArrayList<Long>();
											for (DepartmentData de : departmentHCM) {
												listDepartmentCodeHCM.add(de.getId());
											}

											StringBuilder builder = new StringBuilder();
											for (int i = 0; i < listDepartmentCodeHCM.size(); i++) {
												builder.append("?,");
											}
											// query nhan vien tu du lieu trung
											// tam
											String queryEmployee = "SELECT empl.workShift, empl.layOff as da_nghi_viec,empl.id as employee_id,empl.name as employee_name,empl.code as employee_code,empl.codeOld as employee_code_old,depart.name as department_name,depart.code as department_code \r\n"
													+ "FROM dulieutrungtam.employee as empl, dulieutrungtam.department as depart\r\n"
													+ "WHERE empl.codeOld = ? AND empl.department_id IN ("
													+ builder.deleteCharAt(builder.length() - 1).toString()
													+ ") AND empl.department_id = depart.id;";
											PreparedStatement preStatementEmployee = null;
											Connection conDulieutrungtam = null;

											try {
												conDulieutrungtam = ZKFPDemo
														.getConnectionMySQL(URL.LINK_DULIEUTRUNGTAM_JDBC);
												preStatementEmployee = conDulieutrungtam
														.prepareStatement(queryEmployee);
												preStatementEmployee.setString(1,
														listl.get(indexMathe).getMaNhanVien());
												// handle param IN operator
												int index = 2;
												for (Long id : listDepartmentCodeHCM) {
													preStatementEmployee.setObject(index++, id); // or
																									// whatever
																									// it
																									// applies
												}
												ResultSet resultSet1 = preStatementEmployee.executeQuery();
												while (resultSet1.next()) {
													boolean daNghiViec = resultSet1.getBoolean("da_nghi_viec");
													// neu chua nghi viec moi
													// duoc luu
													if (!daNghiViec) {
														// orderFoodCurrent.setDepartmentName(
														// resultSet1.getString("department_name"));
														// orderFoodCurrent.setDepartmentCode(
														// resultSet1.getString("department_code"));
														// orderFoodCurrent
														// .setEmployeeCode(resultSet1.getString("employee_code"));
														// orderFoodCurrent
														// .setEmployeeName(resultSet1.getString("employee_name"));
														// orderFoodCurrent.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
														// orderFoodCurrent
														// .setFood_date(DateUtil.DATE_WITHOUT_TIME(new
														// Date()));
														// // gan thang id cua
														// category food tu chon
														// orderFoodCurrent.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
														// orderFoodCurrent.setEmployeeId(
														// resultSet1.getString("employee_code_old"));
														// orderFoodCurrent.setShifts_id(shiftsCurrent);
														// orderFoodCurrent.setNotRegFood(true);
														// ZKFPDemo.addOne(orderFoodCurrent,
														// orderFoodCurrent.getShifts_id());

														boolean workShift = resultSet1.getBoolean("empl.workShift");
														String employeeCode = resultSet1.getString("employee_code");
														// nhan vien van phong
														// an ca 2
														if (!workShift && shiftsCurrent == Shifts.SHIFTS_2_ID) {
															try {
																con = ZKFPDemo
																		.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
																// kiem tra co
																// dang ky tang
																// ca hay khong
																String query = "SELECT food_ot.employee_name, food_ot.employee_code, ot.department_code,ot.department_name,ot.food_date, ot.shifts_id, food_ot.employee_code_old "
																		+ "FROM food_over_time as food_ot, over_time as ot "
																		+ "WHERE food_ot.employee_code = ? AND ot.shifts_id = ? AND ot.food_date = ? AND "
																		+ "ot.id = food_ot.over_time_id";
																preStatementChecked = con.prepareStatement(query);
																// check bang ma
																// nhan vien cu
																preStatementChecked.setString(1, employeeCode);
																preStatementChecked.setInt(2, shiftsCurrent);
																preStatementChecked.setDate(3, ngay_cua_ca_SQL);
																ResultSet rs = preStatementChecked.executeQuery();
																if (rs.next()) {
																	orderFoodCurrent.setDepartmentName(
																			rs.getString("ot.department_name"));
																	orderFoodCurrent.setDepartmentCode(
																			rs.getString("ot.department_code"));
																	orderFoodCurrent.setEmployeeCode(employeeCode);
																	orderFoodCurrent.setEmployeeName(
																			rs.getString("food_ot.employee_name"));
																	orderFoodCurrent
																			.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
																	orderFoodCurrent
																			.setFood_date(DateUtil.DATE_WITHOUT_TIME(
																					rs.getDate("ot.food_date")));
																	// gan thang
																	// id cua
																	// category
																	// food tu
																	// chon
																	orderFoodCurrent.setCategory_food_id(
																			FoodCustom.FOOD_CUSTOM_ID);
																	if (rs.getString("employee_code_old") != null) {
																		orderFoodCurrent.setEmployeeId(
																				rs.getString("employee_code_old"));
																	}
																	orderFoodCurrent.setShifts_id(shiftsCurrent);
																	orderFoodCurrent.setOverTime(true);
																	orderFoodCurrent.setNotRegFood(true);
																	ZKFPDemo.addOne(orderFoodCurrent,
																			orderFoodCurrent.getShifts_id());
																}
																if (!rs.first()) {
																	String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
																	File fileKCoSuatAn = new File(
																			pathImageKhongCoSuatAn);
																	BufferedImage bimgKCoSuatAn = null;
																	try {
																		bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
																	} catch (IOException e1) {
																		e1.printStackTrace();
																	}
																	if (bimgKCoSuatAn == null) {
																		btnImg.setIcon(null);
																	}
																	if (bimgKCoSuatAn != null) {
																		ImageIcon imageTest = new ImageIcon(
																				bimgKCoSuatAn);
																		btnImg.setIcon(imageTest);
																	}
																	return;
																}
															} catch (Exception e1) {
																// TODO: handle
																// exception
															} finally {
																try {
																	ZKFPDemo.closeConnectionPre(con,
																			preStatementChecked);
																} catch (Exception e2) {
																	e2.printStackTrace();
																	;
																}
															}
														}
														// di ca hoac nhan vien
														// van phong an ca 1
														if (workShift
																|| !workShift && shiftsCurrent == Shifts.SHIFTS_1_ID) {
															orderFoodCurrent.setDepartmentName(
																	resultSet1.getString("department_name"));
															orderFoodCurrent.setDepartmentCode(
																	resultSet1.getString("department_code"));
															orderFoodCurrent.setEmployeeCode(employeeCode);
															orderFoodCurrent.setEmployeeName(
																	resultSet1.getString("employee_name"));
															orderFoodCurrent.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
															orderFoodCurrent.setFood_date(
																	DateUtil.DATE_WITHOUT_TIME(Shifts.NGAY_CUA_CA));
															// gan thang id cua
															// category food tu
															// chon
															orderFoodCurrent
																	.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
															orderFoodCurrent.setEmployeeId(
																	resultSet1.getString("employee_code_old"));
															orderFoodCurrent.setShifts_id(shiftsCurrent);
															orderFoodCurrent.setNotRegFood(true);
															ZKFPDemo.addOne(orderFoodCurrent,
																	orderFoodCurrent.getShifts_id());
														}
														// nhan vien van phong
														// an ca 3
														if (!workShift && shiftsCurrent == Shifts.SHIFTS_3_ID) {
															String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
															File fileKCoSuatAn = new File(pathImageKhongCoSuatAn);
															BufferedImage bimgKCoSuatAn = null;
															try {
																bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
															} catch (IOException e1) {
																e1.printStackTrace();
															}
															if (bimgKCoSuatAn == null) {
																btnImg.setIcon(null);
															}
															if (bimgKCoSuatAn != null) {
																ImageIcon imageTest = new ImageIcon(bimgKCoSuatAn);
																btnImg.setIcon(imageTest);
															}
															return;
														}
														// END KIEM TRA CA 2
													}
												}
												// k co du lieu trung tam
												if (!resultSet1.first()) {
													String pathImageDaNhanSuatAn = "imagesSystem/image-kcttnhanvien650x450.png";
													File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
													BufferedImage bimgDaNhanSuatAn = null;
													try {
														bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
													} catch (IOException e1) {
														e1.printStackTrace();
													}
													if (bimgDaNhanSuatAn == null) {
														btnImg.setIcon(null);
													}
													if (bimgDaNhanSuatAn != null) {
														ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
														btnImg.setIcon(imageTest);
													}
													return;
												}
											} catch (Exception e1) {
												// TODO: handle exception
											} finally {
												try {
													ZKFPDemo.closeConnectionPre(conDulieutrungtam,
															preStatementEmployee);
												} catch (Exception e2) {
													e2.printStackTrace();
													;
												}
											}
										}
										// end mon an tu chon
									} catch (Exception e1) {
										// TODO: handle exception
									} finally {
										try {
											ZKFPDemo.closeConnectionPre(con, preStatement);
										} catch (Exception e2) {
											e2.printStackTrace();
										}
									}
									// da dang ky mon an
									if (orderFoodCurrent.getEmployeeName() != null) {
										textAreaMaNV.setText(orderFoodCurrent.getEmployeeCode());
										textAreaTenNV.setText(orderFoodCurrent.getEmployeeName());
										textAreaPhongban.setText(orderFoodCurrent.getDepartmentName());
										// convert date to string
										String pattern = "dd-MM-yyyy";
										DateFormat df = new SimpleDateFormat(pattern);
										String foodDate = df.format(orderFoodCurrent.getFood_date());
										textAreaNgay.setText(foodDate);
										// labelTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());a
										textAreaTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());

										// set hinh
										// check neu hinh bi null
										if (orderFoodCurrent.getImage() == null) {
											if (orderFoodCurrent.getCategory_food_id() == FoodCustom.FOOD_CUSTOM_ID) {
												if (bimgTuchon == null) {
													btnImg.setIcon(null);
												}
												if (bimgTuchon != null) {
													Image scaledTuchon = bimgTuchon.getScaledInstance(widthHinhLon,
															heightHinhLon, Image.SCALE_SMOOTH);
													ImageIcon imageTuchon = new ImageIcon(scaledTuchon);
													btnImg.setIcon(null);
													btnImg.setIcon(imageTuchon);
												}
											} else {
												// khong co hinh mon an
												if (bimgNotImage == null) {
													btnImg.setIcon(null);
												}
												if (bimgNotImage != null) {
													ImageIcon imageNotImage = new ImageIcon(bimgNotImage);
													btnImg.setIcon(imageNotImage);
												}
											}
										} else {
											Image img = Toolkit.getDefaultToolkit()
													.createImage(orderFoodCurrent.getImage());
											ImageIcon icon = new ImageIcon(img.getScaledInstance(widthHinhLon,
													heightHinhLon, Image.SCALE_SMOOTH));
											btnImg.setIcon(icon);
										}
									}

									// handle show 3 o nho cho 4 nguoi quet gan
									// nhat
									String queryTop4 = "SELECT FNA.employee_name,CF.name, FNA.employee_id, CF.image\r\n"
											+ "FROM food_nha_an as FNA, category_food as CF\r\n"
											+ "WHERE FNA.category_food_id = CF.id and FNA.shifts_id = ? and FNA.food_date = ?\r\n"
											+ "ORDER BY FNA.id DESC\r\n" + "LIMIT 3;";

									try {
										con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
										preStatementTop3 = con.prepareStatement(queryTop4);
										preStatementTop3.setInt(1, shiftsCurrent);
										preStatementTop3.setDate(2, ngay_cua_ca_SQL);
										ResultSet resultSet = preStatementTop3.executeQuery();
										while (resultSet.next()) {
											FoodNhaAn foodNhaAn = new FoodNhaAn();
											foodNhaAn.setEmployeeName(resultSet.getString("FNA.employee_name"));
											foodNhaAn.setImageFood(resultSet.getBytes("CF.image"));
											foodNhaAn.setEmployeeIdOld(resultSet.getString("FNA.employee_id"));
											foodNhaAn.setFoodName(resultSet.getString("CF.name"));
											foodNhaAnTop4.add(foodNhaAn);
										}
									} catch (Exception e1) {
										// TODO: handle exception
									} finally {
										try {
											ZKFPDemo.closeConnectionPre(con, preStatementTop3);
										} catch (Exception e2) {
											e2.printStackTrace();
											;
										}
									}
									// set hinh
									// query co bi rong hay khong
									if (!foodNhaAnTop4.isEmpty()) {
										// query co 2 phan tu
										if (foodNhaAnTop4.size() == 1) {

											// image nhan vien
											String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
													+ ".bmp";
											File file = new File(pathImage);
											BufferedImage bimg = null;
											try {
												bimg = ImageIO.read(file);
											} catch (IOException e1) {
											}
											if (bimg == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV1.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV1.setIcon(imageNoImageNV);
												}
											}
											if (bimg != null) {
												Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image = new ImageIcon(scaled);
												labelHinhNV1.setIcon(image);
											}
											textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
											// image mon an 1
											if (foodNhaAnTop4.get(0).getImageFood() == null) {
												btnImage1.setIcon(null);
											} else {
												Image img1 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(0).getImageFood());
												ImageIcon icon1 = new ImageIcon(
														img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage1.setIcon(icon1);
											}
											labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());
										}
										if (foodNhaAnTop4.size() == 2) {
											// image nhan vien 1
											String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
													+ ".bmp";
											File file = new File(pathImage);
											BufferedImage bimg = null;
											try {
												bimg = ImageIO.read(file);
											} catch (IOException e1) {
											}

											if (bimg == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV1.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV1.setIcon(imageNoImageNV);
												}
											}
											if (bimg != null) {
												Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image = new ImageIcon(scaled);
												labelHinhNV1.setIcon(image);
											}
											textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
											// check neu image null
											if (foodNhaAnTop4.get(0).getImageFood() == null) {
												btnImage1.setIcon(null);
											} else {
												Image img1 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(0).getImageFood());
												ImageIcon icon1 = new ImageIcon(
														img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage1.setIcon(icon1);
											}

											labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

											// image nhan vien 2
											String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
													+ ".bmp";
											File file2 = new File(pathImage2);
											BufferedImage bimg2 = null;
											try {
												bimg2 = ImageIO.read(file2);
											} catch (IOException e1) {
											}
											if (bimg2 == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV2.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV2.setIcon(imageNoImageNV);
												}
											}
											if (bimg2 != null) {
												Image scaled2 = bimg2.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image2 = new ImageIcon(scaled2);
												labelHinhNV2.setIcon(image2);
											}
											textAreaTenMonAn2.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
											// check neu image mon an 2 null
											if (foodNhaAnTop4.get(1).getImageFood() == null) {
												btnImage2.setIcon(null);
											} else {
												Image img2 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(1).getImageFood());
												ImageIcon icon2 = new ImageIcon(
														img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage2.setIcon(icon2);
											}
											labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());
										}
										if (foodNhaAnTop4.size() == 3) {
											// image nhan vien 1
											String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
													+ ".bmp";
											File file = new File(pathImage);
											BufferedImage bimg = null;
											try {
												bimg = ImageIO.read(file);
											} catch (IOException e1) {
											}
											if (bimg == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV1.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV1.setIcon(imageNoImageNV);
												}
											}
											if (bimg != null) {
												Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image = new ImageIcon(scaled);
												labelHinhNV1.setIcon(image);
											}
											// hinh mon an 1
											// check neu image null
											textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
											if (foodNhaAnTop4.get(0).getImageFood() == null) {
												btnImage1.setIcon(null);
											} else {
												Image img1 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(0).getImageFood());
												ImageIcon icon1 = new ImageIcon(
														img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage1.setIcon(icon1);
											}
											labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

											// image nhan vien 2
											String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
													+ ".bmp";
											File file2 = new File(pathImage2);
											BufferedImage bimg2 = null;
											try {
												bimg2 = ImageIO.read(file2);
											} catch (IOException e1) {
											}
											if (bimg2 == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV2.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV2.setIcon(imageNoImageNV);
												}
											}
											if (bimg2 != null) {
												Image scaled2 = bimg2.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image2 = new ImageIcon(scaled2);
												labelHinhNV2.setIcon(image2);
											}

											// image mon an 2
											// check neu image mon an 2 null
											textAreaTenMonAn2.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
											if (foodNhaAnTop4.get(1).getImageFood() == null) {
												btnImage2.setIcon(null);
											} else {
												Image img2 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(1).getImageFood());
												ImageIcon icon2 = new ImageIcon(
														img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage2.setIcon(icon2);
											}
											labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());

											// image nhan vien 3
											String pathImage3 = "images/" + foodNhaAnTop4.get(2).getEmployeeIdOld()
													+ ".bmp";
											File file3 = new File(pathImage3);
											BufferedImage bimg3 = null;
											try {
												bimg3 = ImageIO.read(file3);
											} catch (IOException e1) {
											}
											if (bimg3 == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV3.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV3.setIcon(imageNoImageNV);
												}
											}
											if (bimg3 != null) {
												Image scaled3 = bimg3.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image3 = new ImageIcon(scaled3);
												labelHinhNV3.setIcon(image3);
											}

											// image mon an 3
											textAreaTenMonAn3.setText(foodNhaAnTop4.get(2).getFoodName().toUpperCase());
											if (foodNhaAnTop4.get(2).getImageFood() == null) {
												btnImage3.setIcon(null);
											} else {
												Image img3 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(2).getImageFood());
												ImageIcon icon3 = new ImageIcon(
														img3.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage3.setIcon(icon3);
											}
											labelName3.setText(foodNhaAnTop4.get(2).getEmployeeName());
										}
									}
									textArea.setText("Thành công");
									// end thai
								}
								// Khong co gio an phu hop
								else {
									// image he thong chua san sang
									String pathImageTest = "imagesSystem/image-khongcogioanphuhop.png";
									File fileTest = new File(pathImageTest);
									BufferedImage bimgTest = null;
									try {
										bimgTest = ImageIO.read(fileTest);
									} catch (IOException e2) {
									}
									if (bimgTest == null) {
										btnImg.setIcon(null);
									}
									if (bimgTest != null) {
										Image scaledTest = bimgTest.getScaledInstance(widthHinhLon, heightHinhLon,
												Image.SCALE_SMOOTH);
										ImageIcon imageTest = new ImageIcon(scaledTest);
										btnImg.setIcon(null);
										btnImg.setIcon(imageTest);
									}
									// end image he thong
								}
								// END CODE
							}
						}
					}
					if (!isEmployee) {
						// String cardId = textFieldMaTheTu.getText();
						textFieldMaTheTu.setText("");
						// System.out.println("Trường hợp ngoại lệ");
						// the quet khong chinh xac
						textArea.setText("Mã thẻ không chính xác" + "\n");
						// van tay sai
						String pathImageTest = "imagesSystem/image-error650x450.png";
						File fileTest = new File(pathImageTest);
						BufferedImage bimgTest = null;
						try {
							bimgTest = ImageIO.read(fileTest);
						} catch (IOException e2) {
						}
						if (bimgTest == null) {
							btnImg.setIcon(null);
						}
						if (bimgTest != null) {
							// Image scaledTest =
							// bimgTest.getScaledInstance(widthHinhLon,
							// heightHinhLon,
							// Image.SCALE_SMOOTH);
							ImageIcon imageTest = new ImageIcon(bimgTest);
							btnImg.setIcon(null);
							btnImg.setIcon(imageTest);
						}
					}

				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		};
		textFieldMaTheTu.addActionListener(action);

		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				FreeSensor();
				// JOptionPane.showMessageDialog(btnImg, "BẠN ĐÃ NHẬN PHẦN ĂN
				// TRƯỚC ĐÓ!");
				textArea.setText("Close succ!\n");

				// image he thong chua san sang
				String pathImageTest = "imagesSystem/image-unsuccess.png";
				File fileTest = new File(pathImageTest);
				BufferedImage bimgTest = null;
				try {
					bimgTest = ImageIO.read(fileTest);
				} catch (IOException e2) {
				}
				if (bimgTest == null) {
					btnImg.setIcon(null);
				}
				if (bimgTest != null) {
					Image scaledTest = bimgTest.getScaledInstance(widthHinhLon, heightHinhLon, Image.SCALE_SMOOTH);
					ImageIcon imageTest = new ImageIcon(scaledTest);
					btnImg.setIcon(null);
					btnImg.setIcon(imageTest);
				}
				// end image he thong
				togglebtnKhongQuetVT.setText("CÓ Đ.KÝ");
				ZKFPDemo.ON_ALLOW_EXCEPTION = false;
			}
		});

		// btnEnroll.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// if (0 == mhDevice) {
		// textArea.setText("Please Open device first!\n");
		// return;
		// }
		//// if (!bRegister) {
		//// enroll_idx = 0;
		//// bRegister = true;
		//// textArea.setText("Vui long quet 3 lan!\n");
		//// }
		// listl = loadvantay.findAll();
		// for (int i = 0; i < listl.size(); i++) {
		// template tl = new template(listl.get(i).getMaChamCong(),
		// listl.get(i).getFingerID(),
		// listl.get(i).getFlag(), listl.get(i).getFingerTemplate());
		// String cmau = tl.getFingerTemplate();
		// int[] sokt = new int[1];
		// sokt[0] = 2048;
		// byte[] mau = new byte[sokt[0]];
		// int ret;
		// FingerprintSensor.Base64ToBlob(cmau, mau, sokt[0]);
		// if (0 == (ret = FingerprintSensorEx.DBAdd(mhDB, i + 1, mau))) {
		// textArea.setText("Đang load" + mau);
		// } else {
		// textArea.setText("Bị lỗi" + ret + "\n");
		// }
		// }
		// textArea.setText("Đã load vân tay vào hệ thống với " + listl.size());
		// }
		// });
		//
		// btnVerify.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// if (0 == mhDevice) {
		// textArea.setText("Please Open device first!\n");
		// return;
		// }
		// if (bRegister) {
		// enroll_idx = 0;
		// bRegister = false;
		// }
		// if (bIdentify) {
		// bIdentify = false;
		// }
		// }
		// });
		//
		// btnIdentify.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// if (0 == mhDevice) {
		// textArea.setText("Please Open device first!\n");
		// return;
		// }
		// if (bRegister) {
		// enroll_idx = 0;
		// bRegister = false;
		// }
		// if (!bIdentify) {
		// bIdentify = true;
		// }
		// }
		// });
		//
		// btnRegImg.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// if (0 == mhDB) {
		// textArea.setText("Please open device first!\n");
		// }
		// String path = "d:\\test\\fingerprint.bmp";
		// byte[] fpTemplate = new byte[2048];
		// int[] sizeFPTemp = new int[1];
		// sizeFPTemp[0] = 2048;
		// int ret = FingerprintSensorEx.ExtractFromImage(mhDB, path, 500,
		// fpTemplate, sizeFPTemp);
		// if (0 == ret) {
		// ret = FingerprintSensorEx.DBAdd(mhDB, iFid, fpTemplate);
		// if (0 == ret) {
		// // String base64 =
		// // fingerprintSensor.BlobToBase64(fpTemplate,
		// // sizeFPTemp[0]);
		// iFid++;
		// cbRegTemp = sizeFPTemp[0];
		// System.arraycopy(fpTemplate, 0, lastRegTemp, 0, cbRegTemp);
		// // Base64 Template
		// // String strBase64 = Base64.encodeToString(regTemp, 0,
		// // ret, Base64.NO_WRAP);
		// textArea.setText("enroll succ\n");
		// } else {
		// textArea.setText("DBAdd fail, ret=" + ret + "\n");
		// }
		// } else {
		// textArea.setText("ExtractFromImage fail, ret=" + ret + "\n");
		// }
		// }
		// });
		//
		// btnIdentImg.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// if (0 == mhDB) {
		// textArea.setText("Please open device first!\n");
		// }
		// String path = "d:\\test\\fingerprint.bmp";
		// byte[] fpTemplate = new byte[2048];
		// int[] sizeFPTemp = new int[1];
		// sizeFPTemp[0] = 2048;
		// int ret = FingerprintSensorEx.ExtractFromImage(mhDB, path, 500,
		// fpTemplate, sizeFPTemp);
		// if (0 == ret) {
		// if (bIdentify) {
		// int[] fid = new int[1];
		// int[] score = new int[1];
		// ret = FingerprintSensorEx.DBIdentify(mhDB, fpTemplate, fid, score);
		// if (ret == 0) {
		// textArea.setText("Identify succ, fid=" + fid[0] + ",score=" +
		// score[0] + "\n");
		// } else {
		// textArea.setText("Identify fail, errcode=" + ret + "\n");
		// }
		//
		// } else {
		// if (cbRegTemp <= 0) {
		// textArea.setText("Please register first!\n");
		// } else {
		// ret = FingerprintSensorEx.DBMatch(mhDB, lastRegTemp, fpTemplate);
		// if (ret > 0) {
		// textArea.setText("Verify succ, score=" + ret + "\n");
		// } else {
		// textArea.setText("Verify fail, ret=" + ret + "\n");
		// }
		// }
		// }
		// } else {
		// textArea.setText("ExtractFromImage fail, ret=" + ret + "\n");
		// }
		// }
		// });

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				FreeSensor();
			}
		});
	}

	private void FreeSensor() {
		mbStop = true;
		try { // wait for thread stopping
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (0 != mhDB) {
			FingerprintSensorEx.DBFree(mhDB);
			mhDB = 0;
		}
		if (0 != mhDevice) {
			FingerprintSensorEx.CloseDevice(mhDevice);
			mhDevice = 0;
		}
		try {
			FingerprintSensorEx.Terminate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeBitmap(byte[] imageBuf, int nWidth, int nHeight, String path) throws IOException {
		java.io.FileOutputStream fos = new java.io.FileOutputStream(path);
		java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

		int w = (((nWidth + 3) / 4) * 4);
		int bfType = 0x424d; // 位图文件类型（0—1字节）
		int bfSize = 54 + 1024 + w * nHeight;// bmp文件的大小（2—5字节）
		int bfReserved1 = 0;// 位图文件保留字，必须为0（6-7字节）
		int bfReserved2 = 0;// 位图文件保留字，必须为0（8-9字节）
		int bfOffBits = 54 + 1024;// 文件头开始到位图实际数据之间的字节的偏移量（10-13字节）

		dos.writeShort(bfType); // 输入位图文件类型'BM'
		dos.write(changeByte(bfSize), 0, 4); // 输入位图文件大小
		dos.write(changeByte(bfReserved1), 0, 2);// 输入位图文件保留字
		dos.write(changeByte(bfReserved2), 0, 2);// 输入位图文件保留字
		dos.write(changeByte(bfOffBits), 0, 4);// 输入位图文件偏移量

		int biSize = 40;// 信息头所需的字节数（14-17字节）
		int biWidth = nWidth;// 位图的宽（18-21字节）
		int biHeight = nHeight;// 位图的高（22-25字节）
		int biPlanes = 1; // 目标设备的级别，必须是1（26-27字节）
		int biBitcount = 8;// 每个像素所需的位数（28-29字节），必须是1位（双色）、4位（16色）、8位（256色）或者24位（真彩色）之一。
		int biCompression = 0;// 位图压缩类型，必须是0（不压缩）（30-33字节）、1（BI_RLEB压缩类型）或2（BI_RLE4压缩类型）之一。
		int biSizeImage = w * nHeight;// 实际位图图像的大小，即整个实际绘制的图像大小（34-37字节）
		int biXPelsPerMeter = 0;// 位图水平分辨率，每米像素数（38-41字节）这个数是系统默认值
		int biYPelsPerMeter = 0;// 位图垂直分辨率，每米像素数（42-45字节）这个数是系统默认值
		int biClrUsed = 0;// 位图实际使用的颜色表中的颜色数（46-49字节），如果为0的话，说明全部使用了
		int biClrImportant = 0;// 位图显示过程中重要的颜色数(50-53字节)，如果为0的话，说明全部重要

		dos.write(changeByte(biSize), 0, 4);// 输入信息头数据的总字节数
		dos.write(changeByte(biWidth), 0, 4);// 输入位图的宽
		dos.write(changeByte(biHeight), 0, 4);// 输入位图的高
		dos.write(changeByte(biPlanes), 0, 2);// 输入位图的目标设备级别
		dos.write(changeByte(biBitcount), 0, 2);// 输入每个像素占据的字节数
		dos.write(changeByte(biCompression), 0, 4);// 输入位图的压缩类型
		dos.write(changeByte(biSizeImage), 0, 4);// 输入位图的实际大小
		dos.write(changeByte(biXPelsPerMeter), 0, 4);// 输入位图的水平分辨率
		dos.write(changeByte(biYPelsPerMeter), 0, 4);// 输入位图的垂直分辨率
		dos.write(changeByte(biClrUsed), 0, 4);// 输入位图使用的总颜色数
		dos.write(changeByte(biClrImportant), 0, 4);// 输入位图使用过程中重要的颜色数

		for (int i = 0; i < 256; i++) {
			dos.writeByte(i);
			dos.writeByte(i);
			dos.writeByte(i);
			dos.writeByte(0);
		}

		byte[] filter = null;
		if (w > nWidth) {
			filter = new byte[w - nWidth];
		}

		for (int i = 0; i < nHeight; i++) {
			dos.write(imageBuf, (nHeight - 1 - i) * nWidth, nWidth);
			if (w > nWidth)
				dos.write(filter, 0, w - nWidth);
		}
		dos.flush();
		dos.close();
		fos.close();
	}

	public static byte[] changeByte(int data) {
		return intToByteArray(data);
	}

	public static byte[] intToByteArray(final int number) {
		byte[] abyte = new byte[4];
		// "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
		abyte[0] = (byte) (0xff & number);
		// ">>"右移位，若为正数则高位补0，若为负数则高位补1
		abyte[1] = (byte) ((0xff00 & number) >> 8);
		abyte[2] = (byte) ((0xff0000 & number) >> 16);
		abyte[3] = (byte) ((0xff000000 & number) >> 24);
		return abyte;
	}

	public static int byteArrayToInt(byte[] bytes) {
		int number = bytes[0] & 0xFF;
		// "|="按位或赋值。
		number |= ((bytes[1] << 8) & 0xFF00);
		number |= ((bytes[2] << 16) & 0xFF0000);
		number |= ((bytes[3] << 24) & 0xFF000000);
		return number;
	}

	private class WorkThread extends Thread {
		@Override
		public void run() {
			super.run();
			int ret = 0;
			while (!mbStop) {
				templateLen[0] = 2048;
				if (0 == (ret = FingerprintSensorEx.AcquireFingerprint(mhDevice, imgbuf, template, templateLen))) {
					if (nFakeFunOn == 1) {
						byte[] paramValue = new byte[4];
						int[] size = new int[1];
						size[0] = 4;
						int nFakeStatus = 0;
						// GetFakeStatus
						ret = FingerprintSensorEx.GetParameters(mhDevice, 2004, paramValue, size);
						nFakeStatus = byteArrayToInt(paramValue);
						// System.out.println("ret = " + ret + ",nFakeStatus=" +
						// nFakeStatus);
						if (0 == ret && (byte) (nFakeStatus & 31) != 31) {
							textArea.setText("Is a fake finger?\n");
							return;
						}
					}
					OnCatpureOK(imgbuf);
					OnExtractOK(template, templateLen[0]);
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private void OnCatpureOK(byte[] imgBuf) {
		try {
			writeBitmap(imgBuf, fpWidth, fpHeight, "fingerprint.bmp");
			btnImg.setIcon(new ImageIcon(ImageIO.read(new File("fingerprint.bmp"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void OnExtractOK(byte[] template, int len) {
		try {
			if (bRegister) {
				int[] fid = new int[1];
				int[] score = new int[1];
				int ret = FingerprintSensorEx.DBIdentify(mhDB, template, fid, score);
				if (ret == 0) {
					textArea.setText("the finger already enroll by " + fid[0] + ",cancel enroll\n");
					bRegister = false;
					enroll_idx = 0;
					return;
				}
				if (enroll_idx > 0 && FingerprintSensorEx.DBMatch(mhDB, regtemparray[enroll_idx - 1], template) <= 0) {
					textArea.setText("please press the same finger 3 times for the enrollment\n");
					return;
				}
				System.arraycopy(template, 0, regtemparray[enroll_idx], 0, 2048);
				enroll_idx++;
				if (enroll_idx == 3) {
					int[] _retLen = new int[1];
					_retLen[0] = 2048;
					byte[] regTemp = new byte[_retLen[0]];

					if (0 == (ret = FingerprintSensorEx.DBMerge(mhDB, regtemparray[0], regtemparray[1], regtemparray[2],
							regTemp, _retLen)) && 0 == (ret = FingerprintSensorEx.DBAdd(mhDB, iFid, regTemp))) {
						iFid++;
						cbRegTemp = _retLen[0];
						System.arraycopy(regTemp, 0, lastRegTemp, 0, cbRegTemp);
						// Base64 Template
						textArea.setText("enroll succ:\n");
					} else {
						textArea.setText("enroll fail, error code=" + ret + "\n");
					}
					bRegister = false;
				} else {
					textArea.setText("You need to press the " + (3 - enroll_idx) + " times fingerprint\n");
				}
			} else {
				if (bIdentify) {
					int[] fid = new int[1];
					int[] score = new int[1];
					int ret = FingerprintSensorEx.DBIdentify(mhDB, template, fid, score);

					if (ret == 0) {
						// check neu list data tu cham cong rong
						if (ZKFPDemo.listDataVerify != null) {

							// check xem nhan vien co cat com hay khong
							// nho check cho nay listDataVerify bi null
							boolean isEat = false;
							for (TimekeepingData t : ZKFPDemo.listDataVerify) {
								if (t.getCodeOld().equals(listl.get(fid[0] - 1).getMaNhanVien())) {
									isEat = true;
									break;
								}
							}
							// neu khong cat com ->
							if (isEat) {
								// textArea.setText("Identify succ, fid=" +
								// fid[0] + ", Ten = "
								// + listl.get(fid[0] - 1).getTenNhanVien() +
								// ",score=" + score[0] + "\n");

								List<FoodNhaAn> foodNhaAnTop4 = new ArrayList<>();
								orderFoodCurrent = new OrderFood();

								// chua handle cho nay

								// kiem tra xem hien tai co dang thuoc ca nao
								// hay khong
								if (shiftsCurrent != 0) {
									// thai
									// // Date current
									// Date dateCurrentTemp = new Date();
									// chinh thuc
									java.sql.Date ngay_cua_ca_SQL = new java.sql.Date(Shifts.NGAY_CUA_CA.getTime());
									// end chinh thuc

									// query by date and employee
									String queryFood = "";
									queryFood = "SELECT ofbd.id as order_food_by_day_id, of.employee_code, of.employee_id, of.employee_name, of.department_code, of.department_name, cf.name,cf.image,cf.id as category_food_id,fbd.shifts_id, of.registration_date\r\n"
											+ "FROM order_food_by_day as ofbd, order_food as of, food_by_day fbd, category_food as cf\r\n"
											+ "WHERE of.employee_id = ? AND of.registration_date = ? AND fbd.shifts_id = ? AND ofbd.order_food_id = of.id AND ofbd.food_by_day_id = fbd.id AND cf.id = fbd.category_food_id;";

									try {
										con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
										// kiem tra co du lieu ca do duoi DB
										// chua
										String queryChecked = "SELECT * FROM food_nha_an WHERE employee_id = ? and shifts_id = ? and food_date = ?";
										PreparedStatement preStatementChecked = null;
										preStatementChecked = con.prepareStatement(queryChecked);
										// check bang ma nhan vien cu
										preStatementChecked.setString(1, listl.get(fid[0] - 1).getMaNhanVien());
										preStatementChecked.setInt(2, shiftsCurrent);
										preStatementChecked.setDate(3, ngay_cua_ca_SQL);
										ResultSet resultSetChecked = preStatementChecked.executeQuery();
										boolean checkedExist = false;
										if (resultSetChecked.next()) {
											checkedExist = true;
											try {

												String pathImageDaNhanSuatAn = "imagesSystem/image-danhansuatan650x450.png";
												File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
												BufferedImage bimgDaNhanSuatAn = null;
												try {
													bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
												} catch (IOException e) {
												}
												if (bimgDaNhanSuatAn == null) {
													btnImg.setIcon(null);
												}
												if (bimgDaNhanSuatAn != null) {
													// Image scaledTest =
													// bimgDaNhanSuatAn.getScaledInstance(widthHinhLon,
													// heightHinhLon,
													// Image.SCALE_SMOOTH);

													ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
													btnImg.setIcon(imageTest);
												}
											} catch (Exception ex) {
												System.out.println(ex);
											}
											if (preStatementChecked != null) {
												preStatementChecked.close();
											}
											return;
										}
										// co dang ky
										preStatement = con.prepareStatement(queryFood);
										// pass id employee
										preStatement.setString(1, listl.get(fid[0] - 1).getMaNhanVien());
										// date param
										preStatement.setDate(2, ngay_cua_ca_SQL);
										preStatement.setInt(3, shiftsCurrent);
										ResultSet resultSet = preStatement.executeQuery();

										// Neu chua an se them du lieu vao DB ->
										// 2 truong hop: 1 la co dang ky , 2
										// la khong dang ky
										// Co dang ky
										while (resultSet.next()) {
											if (!checkedExist) {
												orderFoodCurrent
														.setDepartmentName(resultSet.getString("of.department_name"));
												orderFoodCurrent
														.setDepartmentCode(resultSet.getString("of.department_code"));
												orderFoodCurrent
														.setEmployeeCode(resultSet.getString("of.employee_code"));
												orderFoodCurrent
														.setEmployeeName(resultSet.getString("of.employee_name"));
												orderFoodCurrent.setFoodName(resultSet.getString("cf.name"));
												orderFoodCurrent.setImage(resultSet.getBytes("cf.image"));
												orderFoodCurrent
														.setFood_date(resultSet.getDate("of.registration_date"));
												orderFoodCurrent
														.setCategory_food_id(resultSet.getInt("category_food_id"));
												orderFoodCurrent.setEmployeeId(resultSet.getString("of.employee_id"));
												orderFoodCurrent.setShifts_id(resultSet.getInt("fbd.shifts_id"));
												ZKFPDemo.addOne(orderFoodCurrent, orderFoodCurrent.getShifts_id());
											}
										}
										// chua dang ky
										// handle cho phan mon an tu chon
										if (!resultSet.first()) {
											DepartmentData[] departmentHCMArray = DepartmentDataService
													.timtheophongquanly(ZKFPDemo.codeBranch);
											List<DepartmentData> departmentHCM = new ArrayList<>(
													Arrays.asList(departmentHCMArray));

//											DepartmentData[] departmentBDArray = DepartmentDataService
//													.timtheophongquanly("20002");
//											List<DepartmentData> departmentBD = new ArrayList<>(
//													Arrays.asList(departmentBDArray));
//
//											departmentHCM.addAll(departmentBD);

											// HANDLE -> Tao list id department
											List<Long> listDepartmentCodeHCM = new ArrayList<Long>();
											for (DepartmentData de : departmentHCM) {
												listDepartmentCodeHCM.add(de.getId());
											}

											StringBuilder builder = new StringBuilder();
											for (int i = 0; i < listDepartmentCodeHCM.size(); i++) {
												builder.append("?,");
											}
											// query nhan vien tu du lieu trung
											// tam
											String queryEmployee = "SELECT empl.workShift, empl.layOff as da_nghi_viec,empl.id as employee_id,empl.name as employee_name,empl.code as employee_code,empl.codeOld as employee_code_old,depart.name as department_name,depart.code as department_code \r\n"
													+ "FROM dulieutrungtam.employee as empl, dulieutrungtam.department as depart\r\n"
													+ "WHERE empl.codeOld = ? AND empl.department_id IN ("
													+ builder.deleteCharAt(builder.length() - 1).toString()
													+ ") AND empl.department_id = depart.id;";
											PreparedStatement preStatementEmployee = null;
											Connection conDulieutrungtam = null;

											try {
												conDulieutrungtam = ZKFPDemo
														.getConnectionMySQL(URL.LINK_DULIEUTRUNGTAM_JDBC);
												preStatementEmployee = conDulieutrungtam
														.prepareStatement(queryEmployee);
												preStatementEmployee.setString(1,
														listl.get(fid[0] - 1).getMaNhanVien());
												// handle param IN operator
												int index = 2;
												for (Long id : listDepartmentCodeHCM) {
													preStatementEmployee.setObject(index++, id); // or
																									// whatever
																									// it
																									// applies
												}
												ResultSet resultSet1 = preStatementEmployee.executeQuery();
												while (resultSet1.next()) {
													boolean daNghiViec = resultSet1.getBoolean("da_nghi_viec");
													// neu chua nghi viec moi
													// duoc luu
													if (!daNghiViec) {
														boolean workShift = resultSet1.getBoolean("empl.workShift");
														String employeeCode = resultSet1.getString("employee_code");
														// nhan vien van phong
														// an ca 2
														if (!workShift && shiftsCurrent == Shifts.SHIFTS_2_ID) {
															try {
																con = ZKFPDemo
																		.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
																// kiem tra co
																// dang ky tang
																// ca hay khong
																String query = "SELECT food_ot.employee_name, food_ot.employee_code, ot.department_code,ot.department_name,ot.food_date, ot.shifts_id, food_ot.employee_code_old "
																		+ "FROM food_over_time as food_ot, over_time as ot "
																		+ "WHERE food_ot.employee_code = ? AND ot.shifts_id = ? AND ot.food_date = ? AND "
																		+ "ot.id = food_ot.over_time_id";
																preStatementChecked = con.prepareStatement(query);
																// check bang ma
																// nhan vien cu
																preStatementChecked.setString(1, employeeCode);
																preStatementChecked.setInt(2, shiftsCurrent);
																preStatementChecked.setDate(3, ngay_cua_ca_SQL);
																ResultSet rs = preStatementChecked.executeQuery();
																if (rs.next()) {
																	orderFoodCurrent.setDepartmentName(
																			rs.getString("ot.department_name"));
																	orderFoodCurrent.setDepartmentCode(
																			rs.getString("ot.department_code"));
																	orderFoodCurrent.setEmployeeCode(employeeCode);
																	orderFoodCurrent.setEmployeeName(
																			rs.getString("food_ot.employee_name"));
																	orderFoodCurrent
																			.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
																	orderFoodCurrent
																			.setFood_date(DateUtil.DATE_WITHOUT_TIME(
																					rs.getDate("ot.food_date")));
																	// gan thang
																	// id cua
																	// category
																	// food tu
																	// chon
																	orderFoodCurrent.setCategory_food_id(
																			FoodCustom.FOOD_CUSTOM_ID);
																	if (rs.getString("employee_code_old") != null) {
																		orderFoodCurrent.setEmployeeId(
																				rs.getString("employee_code_old"));
																	}
																	orderFoodCurrent.setShifts_id(shiftsCurrent);
																	orderFoodCurrent.setOverTime(true);
																	ZKFPDemo.addOne(orderFoodCurrent,
																			orderFoodCurrent.getShifts_id());
																}
																if (!rs.first()) {
																	String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
																	File fileKCoSuatAn = new File(
																			pathImageKhongCoSuatAn);
																	BufferedImage bimgKCoSuatAn = null;
																	try {
																		bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
																	} catch (IOException e1) {
																		e1.printStackTrace();
																	}
																	if (bimgKCoSuatAn == null) {
																		btnImg.setIcon(null);
																	}
																	if (bimgKCoSuatAn != null) {
																		ImageIcon imageTest = new ImageIcon(
																				bimgKCoSuatAn);
																		btnImg.setIcon(imageTest);
																	}
																	return;
																}
															} catch (Exception e1) {
																// TODO: handle
																// exception
															} finally {
																try {
																	ZKFPDemo.closeConnectionPre(con,
																			preStatementChecked);
																} catch (Exception e2) {
																	e2.printStackTrace();
																	;
																}
															}
														}
														// di ca hoac nhan vien
														// van phong an ca 1
														if (workShift
																|| !workShift && shiftsCurrent == Shifts.SHIFTS_1_ID) {
															orderFoodCurrent.setDepartmentName(
																	resultSet1.getString("department_name"));
															orderFoodCurrent.setDepartmentCode(
																	resultSet1.getString("department_code"));
															orderFoodCurrent.setEmployeeCode(employeeCode);
															orderFoodCurrent.setEmployeeName(
																	resultSet1.getString("employee_name"));
															orderFoodCurrent.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
															orderFoodCurrent.setFood_date(
																	DateUtil.DATE_WITHOUT_TIME(Shifts.NGAY_CUA_CA));
															// gan thang id cua
															// category food tu
															// chon
															orderFoodCurrent
																	.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
															orderFoodCurrent.setEmployeeId(
																	resultSet1.getString("employee_code_old"));
															orderFoodCurrent.setShifts_id(shiftsCurrent);
															ZKFPDemo.addOne(orderFoodCurrent,
																	orderFoodCurrent.getShifts_id());
														}
														// nhan vien van phong
														// an ca 3
														if (!workShift && shiftsCurrent == Shifts.SHIFTS_3_ID) {
															String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
															File fileKCoSuatAn = new File(pathImageKhongCoSuatAn);
															BufferedImage bimgKCoSuatAn = null;
															try {
																bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
															} catch (IOException e1) {
																e1.printStackTrace();
															}
															if (bimgKCoSuatAn == null) {
																btnImg.setIcon(null);
															}
															if (bimgKCoSuatAn != null) {
																ImageIcon imageTest = new ImageIcon(bimgKCoSuatAn);
																btnImg.setIcon(imageTest);
															}
															return;
														}
														// END KIEM TRA CA 2
													}
												}
												if (!resultSet1.first()) {
													String pathImageDaNhanSuatAn = "imagesSystem/image-kcttnhanvien650x450.png";
													File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
													BufferedImage bimgDaNhanSuatAn = null;
													try {
														bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
													} catch (IOException e1) {
														e1.printStackTrace();
													}
													if (bimgDaNhanSuatAn == null) {
														btnImg.setIcon(null);
													}
													if (bimgDaNhanSuatAn != null) {
														ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
														btnImg.setIcon(imageTest);
													}
													return;
												}
											} catch (Exception e) {
												// TODO: handle exception
											} finally {
												try {
													ZKFPDemo.closeConnectionPre(conDulieutrungtam,
															preStatementEmployee);
												} catch (Exception e2) {
													e2.printStackTrace();
													;
												}
											}
										}
										// end mon an tu chon
									} catch (Exception e) {
										// TODO: handle exception
									} finally {
										try {
											ZKFPDemo.closeConnectionPre(con, preStatement);
										} catch (Exception e2) {
											e2.printStackTrace();
										}
									}
									// da dang ky mon an
									if (orderFoodCurrent.getEmployeeName() != null) {
										textAreaMaNV.setText(orderFoodCurrent.getEmployeeCode());
										textAreaTenNV.setText(orderFoodCurrent.getEmployeeName());
										textAreaPhongban.setText(orderFoodCurrent.getDepartmentName());
										// convert date to string
										String pattern = "dd-MM-yyyy";
										DateFormat df = new SimpleDateFormat(pattern);
										String foodDate = df.format(orderFoodCurrent.getFood_date());
										textAreaNgay.setText(foodDate);
										// labelTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());a
										textAreaTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());

										// set hinh
										// check neu hinh bi null
										if (orderFoodCurrent.getImage() == null) {
											if (orderFoodCurrent.getCategory_food_id() == FoodCustom.FOOD_CUSTOM_ID) {
												if (bimgTuchon == null) {
													btnImg.setIcon(null);
												}
												if (bimgTuchon != null) {
													Image scaledTuchon = bimgTuchon.getScaledInstance(widthHinhLon,
															heightHinhLon, Image.SCALE_SMOOTH);
													ImageIcon imageTuchon = new ImageIcon(scaledTuchon);
													btnImg.setIcon(null);
													btnImg.setIcon(imageTuchon);
												}
											} else {
												// khong co hinh mon an
												if (bimgNotImage == null) {
													btnImg.setIcon(null);
												}
												if (bimgNotImage != null) {
													ImageIcon imageNotImage = new ImageIcon(bimgNotImage);
													btnImg.setIcon(imageNotImage);
												}
											}
										} else {
											Image img = Toolkit.getDefaultToolkit()
													.createImage(orderFoodCurrent.getImage());
											ImageIcon icon = new ImageIcon(img.getScaledInstance(widthHinhLon,
													heightHinhLon, Image.SCALE_SMOOTH));
											btnImg.setIcon(icon);
										}
									}

									// handle show 3 o nho cho 4 nguoi quet gan
									// nhat
									String queryTop4 = "SELECT FNA.employee_name,CF.name, FNA.employee_id, CF.image\r\n"
											+ "FROM food_nha_an as FNA, category_food as CF\r\n"
											+ "WHERE FNA.category_food_id = CF.id and FNA.shifts_id = ? and FNA.food_date = ?\r\n"
											+ "ORDER BY FNA.id DESC\r\n" + "LIMIT 3;";

									try {
										con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
										preStatementTop3 = con.prepareStatement(queryTop4);
										preStatementTop3.setInt(1, shiftsCurrent);
										preStatementTop3.setDate(2, ngay_cua_ca_SQL);
										ResultSet resultSet = preStatementTop3.executeQuery();
										while (resultSet.next()) {
											FoodNhaAn foodNhaAn = new FoodNhaAn();
											foodNhaAn.setEmployeeName(resultSet.getString("FNA.employee_name"));
											foodNhaAn.setImageFood(resultSet.getBytes("CF.image"));
											foodNhaAn.setEmployeeIdOld(resultSet.getString("FNA.employee_id"));
											foodNhaAn.setFoodName(resultSet.getString("CF.name"));
											foodNhaAnTop4.add(foodNhaAn);
										}
									} catch (Exception e) {
										// TODO: handle exception
									} finally {
										try {
											ZKFPDemo.closeConnectionPre(con, preStatementTop3);
										} catch (Exception e2) {
											e2.printStackTrace();
											;
										}
									}
									// set hinh
									// query co bi rong hay khong
									if (!foodNhaAnTop4.isEmpty()) {
										// query co 2 phan tu
										if (foodNhaAnTop4.size() == 1) {

											// image nhan vien
											String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
													+ ".bmp";
											File file = new File(pathImage);
											BufferedImage bimg = null;
											try {
												bimg = ImageIO.read(file);
											} catch (IOException e) {
											}
											if (bimg == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV1.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV1.setIcon(imageNoImageNV);
												}
											}
											if (bimg != null) {
												Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image = new ImageIcon(scaled);
												labelHinhNV1.setIcon(image);
											}
											textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
											// image mon an 1
											if (foodNhaAnTop4.get(0).getImageFood() == null) {
												btnImage1.setIcon(null);
											} else {
												Image img1 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(0).getImageFood());
												ImageIcon icon1 = new ImageIcon(
														img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage1.setIcon(icon1);
											}
											labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());
										}
										if (foodNhaAnTop4.size() == 2) {
											// image nhan vien 1
											String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
													+ ".bmp";
											File file = new File(pathImage);
											BufferedImage bimg = null;
											try {
												bimg = ImageIO.read(file);
											} catch (IOException e) {
											}

											if (bimg == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV1.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV1.setIcon(imageNoImageNV);
												}
											}
											if (bimg != null) {
												Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image = new ImageIcon(scaled);
												labelHinhNV1.setIcon(image);
											}
											textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
											// check neu image null
											if (foodNhaAnTop4.get(0).getImageFood() == null) {
												btnImage1.setIcon(null);
											} else {
												Image img1 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(0).getImageFood());
												ImageIcon icon1 = new ImageIcon(
														img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage1.setIcon(icon1);
											}

											labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

											// image nhan vien 2
											String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
													+ ".bmp";
											File file2 = new File(pathImage2);
											BufferedImage bimg2 = null;
											try {
												bimg2 = ImageIO.read(file2);
											} catch (IOException e) {
											}
											if (bimg2 == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV2.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV2.setIcon(imageNoImageNV);
												}
											}
											if (bimg2 != null) {
												Image scaled2 = bimg2.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image2 = new ImageIcon(scaled2);
												labelHinhNV2.setIcon(image2);
											}
											textAreaTenMonAn2.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
											// check neu image mon an 2 null
											if (foodNhaAnTop4.get(1).getImageFood() == null) {
												btnImage2.setIcon(null);
											} else {
												Image img2 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(1).getImageFood());
												ImageIcon icon2 = new ImageIcon(
														img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage2.setIcon(icon2);
											}
											labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());
										}
										if (foodNhaAnTop4.size() == 3) {
											// image nhan vien 1
											String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
													+ ".bmp";
											File file = new File(pathImage);
											BufferedImage bimg = null;
											try {
												bimg = ImageIO.read(file);
											} catch (IOException e) {
											}
											if (bimg == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV1.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV1.setIcon(imageNoImageNV);
												}
											}
											if (bimg != null) {
												Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image = new ImageIcon(scaled);
												labelHinhNV1.setIcon(image);
											}

											// hinh mon an 1
											// check neu image null
											textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
											if (foodNhaAnTop4.get(0).getImageFood() == null) {
												btnImage1.setIcon(null);
											} else {
												Image img1 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(0).getImageFood());
												ImageIcon icon1 = new ImageIcon(
														img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage1.setIcon(icon1);
											}
											labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

											// image nhan vien 2
											String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
													+ ".bmp";
											File file2 = new File(pathImage2);
											BufferedImage bimg2 = null;
											try {
												bimg2 = ImageIO.read(file2);
											} catch (IOException e) {
											}
											if (bimg2 == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV2.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV2.setIcon(imageNoImageNV);
												}
											}
											if (bimg2 != null) {
												Image scaled2 = bimg2.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image2 = new ImageIcon(scaled2);
												labelHinhNV2.setIcon(image2);
											}

											// image mon an 2
											// check neu image mon an 2 null
											textAreaTenMonAn2.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
											if (foodNhaAnTop4.get(1).getImageFood() == null) {
												btnImage2.setIcon(null);
											} else {
												Image img2 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(1).getImageFood());
												ImageIcon icon2 = new ImageIcon(
														img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage2.setIcon(icon2);
											}
											labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());

											// image nhan vien 3
											String pathImage3 = "images/" + foodNhaAnTop4.get(2).getEmployeeIdOld()
													+ ".bmp";
											File file3 = new File(pathImage3);
											BufferedImage bimg3 = null;
											try {
												bimg3 = ImageIO.read(file3);
											} catch (IOException e) {
											}
											if (bimg3 == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV3.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV3.setIcon(imageNoImageNV);
												}
											}
											if (bimg3 != null) {
												Image scaled3 = bimg3.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image3 = new ImageIcon(scaled3);
												labelHinhNV3.setIcon(image3);
											}

											// image mon an 3
											textAreaTenMonAn3.setText(foodNhaAnTop4.get(2).getFoodName().toUpperCase());
											if (foodNhaAnTop4.get(2).getImageFood() == null) {
												btnImage3.setIcon(null);
											} else {
												Image img3 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(2).getImageFood());
												ImageIcon icon3 = new ImageIcon(
														img3.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage3.setIcon(icon3);
											}
											labelName3.setText(foodNhaAnTop4.get(2).getEmployeeName());
										}
									}
									textArea.setText("Thành công");
									// end thai
								}
								// Khong co gio an phu hop
								else {
									// image he thong chua san sang
									String pathImageTest = "imagesSystem/image-khongcogioanphuhop.png";
									File fileTest = new File(pathImageTest);
									BufferedImage bimgTest = null;
									try {
										bimgTest = ImageIO.read(fileTest);
									} catch (IOException e2) {
									}
									if (bimgTest == null) {
										btnImg.setIcon(null);
									}
									if (bimgTest != null) {
										Image scaledTest = bimgTest.getScaledInstance(widthHinhLon, heightHinhLon,
												Image.SCALE_SMOOTH);
										ImageIcon imageTest = new ImageIcon(scaledTest);
										btnImg.setIcon(null);
										btnImg.setIcon(imageTest);
									}
									// end image he thong
								}
							}
							// neu cat com
							if (!isEat && ZKFPDemo.ON_ALLOW_EXCEPTION == false) {
								// CODE CHINH THUC
								// // image he thong chua san sang
								// String pathImageTest =
								// "imagesSystem/image-khongdangkysuatan.png";
								// File fileTest = new File(pathImageTest);
								// BufferedImage bimgTest = null;
								// try {
								// bimgTest = ImageIO.read(fileTest);
								// } catch (IOException e2) {
								// }
								// if (bimgTest == null) {
								// btnImg.setIcon(null);
								// }
								// if (bimgTest != null) {
								// Image scaledTest =
								// bimgTest.getScaledInstance(widthHinhLon,
								// heightHinhLon,
								// Image.SCALE_SMOOTH);
								// ImageIcon imageTest = new
								// ImageIcon(scaledTest);
								// btnImg.setIcon(null);
								// btnImg.setIcon(imageTest);
								// }
								// // end image he thong
								// END CODE CHINH THUC
								// CODE TAM THOI

								// check xem nhan vien co cat com hay khong
								// nho check cho nay listDataVerify bi null
								// neu khong cat com ->
								List<FoodNhaAn> foodNhaAnTop4 = new ArrayList<>();
								orderFoodCurrent = new OrderFood();
								// chua handle cho nay

								// kiem tra xem hien tai co dang thuoc ca nao
								// hay khong
								if (shiftsCurrent != 0) {
									// thai
									java.sql.Date ngay_cua_ca_SQL = new java.sql.Date(Shifts.NGAY_CUA_CA.getTime());
									// query by date and employee
									String queryFood = "";
									queryFood = "SELECT ofbd.id as order_food_by_day_id, of.employee_code, of.employee_id, of.employee_name, of.department_code, of.department_name, cf.name,cf.image,cf.id as category_food_id,fbd.shifts_id, of.registration_date\r\n"
											+ "FROM order_food_by_day as ofbd, order_food as of, food_by_day fbd, category_food as cf\r\n"
											+ "WHERE of.employee_id = ? AND of.registration_date = ? AND fbd.shifts_id = ? AND ofbd.order_food_id = of.id AND ofbd.food_by_day_id = fbd.id AND cf.id = fbd.category_food_id;	";

									try {
										con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);

										// kiem tra co du lieu ca do duoi DB
										// chua
										String queryChecked = "SELECT * FROM food_nha_an WHERE employee_id = ? and shifts_id = ? and food_date = ?";
										PreparedStatement preStatementChecked = null;
										preStatementChecked = con.prepareStatement(queryChecked);
										// check bang ma nhan vien cu
										preStatementChecked.setString(1, listl.get(fid[0] - 1).getMaNhanVien());
										preStatementChecked.setInt(2, shiftsCurrent);
										preStatementChecked.setDate(3, ngay_cua_ca_SQL);
										ResultSet resultSetChecked = preStatementChecked.executeQuery();
										boolean checkedExist = false;
										if (resultSetChecked.next()) {
											checkedExist = true;
											// image he thong da nhan suat an
											String pathImageDaNhanSuatAn = "imagesSystem/image-danhansuatan650x450.png";
											File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
											BufferedImage bimgDaNhanSuatAn = null;
											try {
												bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
											} catch (IOException e) {
											}
											if (bimgDaNhanSuatAn == null) {
												btnImg.setIcon(null);
											}
											if (bimgDaNhanSuatAn != null) {
												// Image scaledTest =
												// bimgDaNhanSuatAn.getScaledInstance(widthHinhLon,
												// heightHinhLon,
												// Image.SCALE_SMOOTH);
												ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
												btnImg.setIcon(imageTest);
											}
											// end image he thong
											if (preStatementChecked != null) {
												preStatementChecked.close();
											}
											return;
										}
										// Neu chua an se them du lieu vao DB ->
										// 2 truong hop: 1 la co dang ky , 2
										// la khong dang ky
										preStatement = con.prepareStatement(queryFood);
										// pass id employee
										preStatement.setString(1, listl.get(fid[0] - 1).getMaNhanVien());
										// date param
										preStatement.setDate(2, ngay_cua_ca_SQL);
										preStatement.setInt(3, shiftsCurrent);
										ResultSet resultSet = preStatement.executeQuery();

										// Co dang ky
										while (resultSet.next()) {
											// neu nhan vien do da an ca do roi
											// - > k cho them
											if (!checkedExist) {
												orderFoodCurrent
														.setDepartmentName(resultSet.getString("of.department_name"));
												orderFoodCurrent
														.setDepartmentCode(resultSet.getString("of.department_code"));
												orderFoodCurrent
														.setEmployeeCode(resultSet.getString("of.employee_code"));
												orderFoodCurrent
														.setEmployeeName(resultSet.getString("of.employee_name"));
												orderFoodCurrent.setFoodName(resultSet.getString("cf.name"));
												orderFoodCurrent.setImage(resultSet.getBytes("cf.image"));
												orderFoodCurrent
														.setFood_date(resultSet.getDate("of.registration_date"));
												orderFoodCurrent
														.setCategory_food_id(resultSet.getInt("category_food_id"));
												orderFoodCurrent.setEmployeeId(resultSet.getString("of.employee_id"));
												orderFoodCurrent.setShifts_id(resultSet.getInt("fbd.shifts_id"));
												orderFoodCurrent.setNotRegFood(true);
												ZKFPDemo.addOne(orderFoodCurrent, orderFoodCurrent.getShifts_id());
											}
										}
										// chua dang ky
										// handle cho phan mon an tu chon
										if (!resultSet.first()) {
											DepartmentData[] departmentHCMArray = DepartmentDataService
													.timtheophongquanly(ZKFPDemo.codeBranch);
											List<DepartmentData> departmentHCM = new ArrayList<>(
													Arrays.asList(departmentHCMArray));

//											DepartmentData[] departmentBDArray = DepartmentDataService
//													.timtheophongquanly("20002");
//											List<DepartmentData> departmentBD = new ArrayList<>(
//													Arrays.asList(departmentBDArray));
//
//											departmentHCM.addAll(departmentBD);

											// HANDLE -> Tao list id department
											List<Long> listDepartmentCodeHCM = new ArrayList<Long>();
											for (DepartmentData de : departmentHCM) {
												listDepartmentCodeHCM.add(de.getId());
											}

											StringBuilder builder = new StringBuilder();
											for (int i = 0; i < listDepartmentCodeHCM.size(); i++) {
												builder.append("?,");
											}
											// query nhan vien tu du lieu trung
											// tam
											String queryEmployee = "SELECT empl.workShift, empl.layOff as da_nghi_viec,empl.id as employee_id,empl.name as employee_name,empl.code as employee_code,empl.codeOld as employee_code_old,depart.name as department_name,depart.code as department_code \r\n"
													+ "FROM dulieutrungtam.employee as empl, dulieutrungtam.department as depart\r\n"
													+ "WHERE empl.codeOld = ? AND empl.department_id IN ("
													+ builder.deleteCharAt(builder.length() - 1).toString()
													+ ") AND empl.department_id = depart.id;";
											PreparedStatement preStatementEmployee = null;
											Connection conDulieutrungtam = null;

											try {
												conDulieutrungtam = ZKFPDemo
														.getConnectionMySQL(URL.LINK_DULIEUTRUNGTAM_JDBC);
												preStatementEmployee = conDulieutrungtam
														.prepareStatement(queryEmployee);
												preStatementEmployee.setString(1,
														listl.get(fid[0] - 1).getMaNhanVien());
												// handle param IN operator
												int index = 2;
												for (Long id : listDepartmentCodeHCM) {
													preStatementEmployee.setObject(index++, id); // or
																									// whatever
																									// it
																									// applies
												}
												ResultSet resultSet1 = preStatementEmployee.executeQuery();
												while (resultSet1.next()) {
													boolean daNghiViec = resultSet1.getBoolean("da_nghi_viec");
													// neu chua nghi viec moi
													// duoc luu
													if (!daNghiViec) {
														// orderFoodCurrent.setDepartmentName(
														// resultSet1.getString("department_name"));
														// orderFoodCurrent.setDepartmentCode(
														// resultSet1.getString("department_code"));
														// orderFoodCurrent
														// .setEmployeeCode(resultSet1.getString("employee_code"));
														// orderFoodCurrent
														// .setEmployeeName(resultSet1.getString("employee_name"));
														// orderFoodCurrent.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
														// orderFoodCurrent
														// .setFood_date(DateUtil.DATE_WITHOUT_TIME(new
														// Date()));
														// // gan thang id cua
														// category food tu chon
														// orderFoodCurrent.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
														// orderFoodCurrent.setEmployeeId(
														// resultSet1.getString("employee_code_old"));
														// orderFoodCurrent.setShifts_id(shiftsCurrent);
														// orderFoodCurrent.setNotRegFood(true);
														// ZKFPDemo.addOne(orderFoodCurrent,
														// orderFoodCurrent.getShifts_id());

														boolean workShift = resultSet1.getBoolean("empl.workShift");
														String employeeCode = resultSet1.getString("employee_code");
														// nhan vien van phong
														// an ca 2
														if (!workShift && shiftsCurrent == Shifts.SHIFTS_2_ID) {
															try {
																con = ZKFPDemo
																		.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
																// kiem tra co
																// dang ky tang
																// ca hay khong
																String query = "SELECT food_ot.employee_name, food_ot.employee_code, ot.department_code,ot.department_name,ot.food_date, ot.shifts_id, food_ot.employee_code_old "
																		+ "FROM food_over_time as food_ot, over_time as ot "
																		+ "WHERE food_ot.employee_code = ? AND ot.shifts_id = ? AND ot.food_date = ? AND "
																		+ "ot.id = food_ot.over_time_id";
																preStatementChecked = con.prepareStatement(query);
																// check bang ma
																// nhan vien cu
																preStatementChecked.setString(1, employeeCode);
																preStatementChecked.setInt(2, shiftsCurrent);
																preStatementChecked.setDate(3, ngay_cua_ca_SQL);
																ResultSet rs = preStatementChecked.executeQuery();
																if (rs.next()) {
																	orderFoodCurrent.setDepartmentName(
																			rs.getString("ot.department_name"));
																	orderFoodCurrent.setDepartmentCode(
																			rs.getString("ot.department_code"));
																	orderFoodCurrent.setEmployeeCode(employeeCode);
																	orderFoodCurrent.setEmployeeName(
																			rs.getString("food_ot.employee_name"));
																	orderFoodCurrent
																			.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
																	orderFoodCurrent
																			.setFood_date(DateUtil.DATE_WITHOUT_TIME(
																					rs.getDate("ot.food_date")));
																	// gan thang
																	// id cua
																	// category
																	// food tu
																	// chon
																	orderFoodCurrent.setCategory_food_id(
																			FoodCustom.FOOD_CUSTOM_ID);
																	if (rs.getString("employee_code_old") != null) {
																		orderFoodCurrent.setEmployeeId(
																				rs.getString("employee_code_old"));
																	}
																	orderFoodCurrent.setShifts_id(shiftsCurrent);
																	orderFoodCurrent.setOverTime(true);
																	ZKFPDemo.addOne(orderFoodCurrent,
																			orderFoodCurrent.getShifts_id());
																}
																if (!rs.first()) {
																	String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
																	File fileKCoSuatAn = new File(
																			pathImageKhongCoSuatAn);
																	BufferedImage bimgKCoSuatAn = null;
																	try {
																		bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
																	} catch (IOException e1) {
																		e1.printStackTrace();
																	}
																	if (bimgKCoSuatAn == null) {
																		btnImg.setIcon(null);
																	}
																	if (bimgKCoSuatAn != null) {
																		ImageIcon imageTest = new ImageIcon(
																				bimgKCoSuatAn);
																		btnImg.setIcon(imageTest);
																	}
																	return;
																}
															} catch (Exception e1) {
																// TODO: handle
																// exception
															} finally {
																try {
																	ZKFPDemo.closeConnectionPre(con,
																			preStatementChecked);
																} catch (Exception e2) {
																	e2.printStackTrace();
																	;
																}
															}
														}
														// di ca hoac nhan vien
														// van phong an ca 1
														if (workShift
																|| !workShift && shiftsCurrent == Shifts.SHIFTS_1_ID) {
															orderFoodCurrent.setDepartmentName(
																	resultSet1.getString("department_name"));
															orderFoodCurrent.setDepartmentCode(
																	resultSet1.getString("department_code"));
															orderFoodCurrent.setEmployeeCode(employeeCode);
															orderFoodCurrent.setEmployeeName(
																	resultSet1.getString("employee_name"));
															orderFoodCurrent.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
															orderFoodCurrent.setFood_date(
																	DateUtil.DATE_WITHOUT_TIME(Shifts.NGAY_CUA_CA));
															// gan thang id cua
															// category food tu
															// chon
															orderFoodCurrent
																	.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
															orderFoodCurrent.setEmployeeId(
																	resultSet1.getString("employee_code_old"));
															orderFoodCurrent.setShifts_id(shiftsCurrent);
															ZKFPDemo.addOne(orderFoodCurrent,
																	orderFoodCurrent.getShifts_id());
														}
														// nhan vien van phong
														// an ca 3
														if (!workShift && shiftsCurrent == Shifts.SHIFTS_3_ID) {
															String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
															File fileKCoSuatAn = new File(pathImageKhongCoSuatAn);
															BufferedImage bimgKCoSuatAn = null;
															try {
																bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
															} catch (IOException e1) {
																e1.printStackTrace();
															}
															if (bimgKCoSuatAn == null) {
																btnImg.setIcon(null);
															}
															if (bimgKCoSuatAn != null) {
																ImageIcon imageTest = new ImageIcon(bimgKCoSuatAn);
																btnImg.setIcon(imageTest);
															}
															return;
														}
														// END KIEM TRA CA 2
													}
												}
												if (!resultSet1.first()) {
													String pathImageDaNhanSuatAn = "imagesSystem/image-kcttnhanvien650x450.png";
													File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
													BufferedImage bimgDaNhanSuatAn = null;
													try {
														bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
													} catch (IOException e1) {
														e1.printStackTrace();
													}
													if (bimgDaNhanSuatAn == null) {
														btnImg.setIcon(null);
													}
													if (bimgDaNhanSuatAn != null) {
														ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
														btnImg.setIcon(imageTest);
													}
													return;
												}
											} catch (Exception e) {
												// TODO: handle exception
											} finally {
												try {
													ZKFPDemo.closeConnectionPre(conDulieutrungtam,
															preStatementEmployee);
												} catch (Exception e2) {
													e2.printStackTrace();
													;
												}
											}
										}
										// end mon an tu chon
									} catch (Exception e) {
										// TODO: handle exception
									} finally {
										try {
											ZKFPDemo.closeConnectionPre(con, preStatement);
										} catch (Exception e2) {
											e2.printStackTrace();
										}
									}
									// da dang ky mon an
									if (orderFoodCurrent.getEmployeeName() != null) {
										textAreaMaNV.setText(orderFoodCurrent.getEmployeeCode());
										textAreaTenNV.setText(orderFoodCurrent.getEmployeeName());
										textAreaPhongban.setText(orderFoodCurrent.getDepartmentName());
										// convert date to string
										String pattern = "dd-MM-yyyy";
										DateFormat df = new SimpleDateFormat(pattern);
										String foodDate = df.format(orderFoodCurrent.getFood_date());
										textAreaNgay.setText(foodDate);
										// labelTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());a
										textAreaTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());

										// set hinh
										// check neu hinh bi null
										if (orderFoodCurrent.getImage() == null) {
											// khong co hinh mon an
											if (orderFoodCurrent.getCategory_food_id() == FoodCustom.FOOD_CUSTOM_ID) {
												if (bimgTuchon == null) {
													btnImg.setIcon(null);
												}
												if (bimgTuchon != null) {
													Image scaledTuchon = bimgTuchon.getScaledInstance(widthHinhLon,
															heightHinhLon, Image.SCALE_SMOOTH);
													ImageIcon imageTuchon = new ImageIcon(scaledTuchon);
													btnImg.setIcon(null);
													btnImg.setIcon(imageTuchon);
												}
											} else {
												// khong co hinh mon an
												if (bimgNotImage == null) {
													btnImg.setIcon(null);
												}
												if (bimgNotImage != null) {
													ImageIcon imageNotImage = new ImageIcon(bimgNotImage);
													btnImg.setIcon(imageNotImage);
												}
											}
										} else {
											Image img = Toolkit.getDefaultToolkit()
													.createImage(orderFoodCurrent.getImage());
											ImageIcon icon = new ImageIcon(img.getScaledInstance(widthHinhLon,
													heightHinhLon, Image.SCALE_SMOOTH));
											btnImg.setIcon(icon);
										}
									}

									// handle show 3 o nho cho 4 nguoi quet gan
									// nhat
									String queryTop4 = "SELECT FNA.employee_name,CF.name, FNA.employee_id, CF.image\r\n"
											+ "FROM food_nha_an as FNA, category_food as CF\r\n"
											+ "WHERE FNA.category_food_id = CF.id and FNA.shifts_id = ? and FNA.food_date = ?\r\n"
											+ "ORDER BY FNA.id DESC\r\n" + "LIMIT 3;";

									try {
										con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
										preStatementTop3 = con.prepareStatement(queryTop4);
										preStatementTop3.setInt(1, shiftsCurrent);
										preStatementTop3.setDate(2, ngay_cua_ca_SQL);
										ResultSet resultSet = preStatementTop3.executeQuery();
										while (resultSet.next()) {
											FoodNhaAn foodNhaAn = new FoodNhaAn();
											foodNhaAn.setEmployeeName(resultSet.getString("FNA.employee_name"));
											foodNhaAn.setImageFood(resultSet.getBytes("CF.image"));
											foodNhaAn.setEmployeeIdOld(resultSet.getString("FNA.employee_id"));
											foodNhaAn.setFoodName(resultSet.getString("CF.name"));
											foodNhaAnTop4.add(foodNhaAn);
										}
									} catch (Exception e) {
										// TODO: handle exception
									} finally {
										try {
											ZKFPDemo.closeConnectionPre(con, preStatementTop3);
										} catch (Exception e2) {
											e2.printStackTrace();
											;
										}
									}
									// set hinh
									// query co bi rong hay khong
									if (!foodNhaAnTop4.isEmpty()) {
										// query co 2 phan tu
										if (foodNhaAnTop4.size() == 1) {

											// image nhan vien
											String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
													+ ".bmp";
											File file = new File(pathImage);
											BufferedImage bimg = null;
											try {
												bimg = ImageIO.read(file);
											} catch (IOException e) {
											}
											if (bimg == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV1.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV1.setIcon(imageNoImageNV);
												}
											}
											if (bimg != null) {
												Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image = new ImageIcon(scaled);
												labelHinhNV1.setIcon(image);
											}
											textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
											// image mon an 1
											if (foodNhaAnTop4.get(0).getImageFood() == null) {
												btnImage1.setIcon(null);
											} else {
												Image img1 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(0).getImageFood());
												ImageIcon icon1 = new ImageIcon(
														img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage1.setIcon(icon1);
											}
											labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());
										}
										if (foodNhaAnTop4.size() == 2) {
											// image nhan vien 1
											String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
													+ ".bmp";
											File file = new File(pathImage);
											BufferedImage bimg = null;
											try {
												bimg = ImageIO.read(file);
											} catch (IOException e) {
											}

											if (bimg == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV1.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV1.setIcon(imageNoImageNV);
												}
											}
											if (bimg != null) {
												Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image = new ImageIcon(scaled);
												labelHinhNV1.setIcon(image);
											}
											textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
											// check neu image null
											if (foodNhaAnTop4.get(0).getImageFood() == null) {
												btnImage1.setIcon(null);
											} else {
												Image img1 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(0).getImageFood());
												ImageIcon icon1 = new ImageIcon(
														img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage1.setIcon(icon1);
											}

											labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

											// image nhan vien 2
											String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
													+ ".bmp";
											File file2 = new File(pathImage2);
											BufferedImage bimg2 = null;
											try {
												bimg2 = ImageIO.read(file2);
											} catch (IOException e) {
											}
											if (bimg2 == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV2.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV2.setIcon(imageNoImageNV);
												}
											}
											if (bimg2 != null) {
												Image scaled2 = bimg2.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image2 = new ImageIcon(scaled2);
												labelHinhNV2.setIcon(image2);
											}
											textAreaTenMonAn2.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
											// check neu image mon an 2 null
											if (foodNhaAnTop4.get(1).getImageFood() == null) {
												btnImage2.setIcon(null);
											} else {
												Image img2 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(1).getImageFood());
												ImageIcon icon2 = new ImageIcon(
														img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage2.setIcon(icon2);
											}
											labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());
										}
										if (foodNhaAnTop4.size() == 3) {
											// image nhan vien 1
											String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
													+ ".bmp";
											File file = new File(pathImage);
											BufferedImage bimg = null;
											try {
												bimg = ImageIO.read(file);
											} catch (IOException e) {
											}
											if (bimg == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV1.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV1.setIcon(imageNoImageNV);
												}
											}
											if (bimg != null) {
												Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image = new ImageIcon(scaled);
												labelHinhNV1.setIcon(image);
											}
											// hinh mon an 1
											// check neu image null
											textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
											if (foodNhaAnTop4.get(0).getImageFood() == null) {
												btnImage1.setIcon(null);
											} else {
												Image img1 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(0).getImageFood());
												ImageIcon icon1 = new ImageIcon(
														img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage1.setIcon(icon1);
											}
											labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

											// image nhan vien 2
											String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
													+ ".bmp";
											File file2 = new File(pathImage2);
											BufferedImage bimg2 = null;
											try {
												bimg2 = ImageIO.read(file2);
											} catch (IOException e) {
											}
											if (bimg2 == null) {
												if (bimgNoImageNV == null) {
													labelHinhNV2.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV2.setIcon(imageNoImageNV);
												}
											}
											if (bimg2 != null) {
												Image scaled2 = bimg2.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image2 = new ImageIcon(scaled2);
												labelHinhNV2.setIcon(image2);
											}

											// image mon an 2
											// check neu image mon an 2 null
											textAreaTenMonAn2.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
											if (foodNhaAnTop4.get(1).getImageFood() == null) {
												btnImage2.setIcon(null);
											} else {
												Image img2 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(1).getImageFood());
												ImageIcon icon2 = new ImageIcon(
														img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage2.setIcon(icon2);
											}
											labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());

											// image nhan vien 3
											String pathImage3 = "images/" + foodNhaAnTop4.get(2).getEmployeeIdOld()
													+ ".bmp";
											File file3 = new File(pathImage3);
											BufferedImage bimg3 = null;
											try {
												bimg3 = ImageIO.read(file3);
											} catch (IOException e) {
											}
											if (bimg3 == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV3.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV3.setIcon(imageNoImageNV);
												}
											}
											if (bimg3 != null) {
												Image scaled3 = bimg3.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image3 = new ImageIcon(scaled3);
												labelHinhNV3.setIcon(image3);
											}

											// image mon an 3
											textAreaTenMonAn3.setText(foodNhaAnTop4.get(2).getFoodName().toUpperCase());
											if (foodNhaAnTop4.get(2).getImageFood() == null) {
												btnImage3.setIcon(null);
											} else {
												Image img3 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(2).getImageFood());
												ImageIcon icon3 = new ImageIcon(
														img3.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage3.setIcon(icon3);
											}
											labelName3.setText(foodNhaAnTop4.get(2).getEmployeeName());
										}
									}
									textArea.setText("Thành công");
									// end thai
								}
								// Khong co gio an phu hop
								else {
									// image he thong chua san sang
									String pathImageTest = "imagesSystem/image-khongcogioanphuhop.png";
									File fileTest = new File(pathImageTest);
									BufferedImage bimgTest = null;
									try {
										bimgTest = ImageIO.read(fileTest);
									} catch (IOException e2) {
									}
									if (bimgTest == null) {
										btnImg.setIcon(null);
									}
									if (bimgTest != null) {
										Image scaledTest = bimgTest.getScaledInstance(widthHinhLon, heightHinhLon,
												Image.SCALE_SMOOTH);
										ImageIcon imageTest = new ImageIcon(scaledTest);
										btnImg.setIcon(null);
										btnImg.setIcon(imageTest);
									}
									// end image he thong
								}
								// END CODE TAM THOI

							}
							// CAT COM VA VAN CHO LUU
							if (!isEat && ZKFPDemo.ON_ALLOW_EXCEPTION) {
								// CODE TAM THOI

								// check xem nhan vien co cat com hay khong
								// nho check cho nay listDataVerify bi null
								// neu khong cat com ->
								List<FoodNhaAn> foodNhaAnTop4 = new ArrayList<>();
								orderFoodCurrent = new OrderFood();
								// chua handle cho nay

								// kiem tra xem hien tai co dang thuoc ca nao
								// hay khong
								if (shiftsCurrent != 0) {
									// thai
									java.sql.Date ngay_cua_ca_SQL = new java.sql.Date(Shifts.NGAY_CUA_CA.getTime());
									// query by date and employee
									String queryFood = "";
									queryFood = "SELECT ofbd.id as order_food_by_day_id, of.employee_code, of.employee_id, of.employee_name, of.department_code, of.department_name, cf.name,cf.image,cf.id as category_food_id,fbd.shifts_id, of.registration_date\r\n"
											+ "FROM order_food_by_day as ofbd, order_food as of, food_by_day fbd, category_food as cf\r\n"
											+ "WHERE of.employee_id = ? AND of.registration_date = ? AND fbd.shifts_id = ? AND ofbd.order_food_id = of.id AND ofbd.food_by_day_id = fbd.id AND cf.id = fbd.category_food_id;	";

									try {
										con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
										// kiem tra co du lieu ca do duoi DB
										// chua
										String queryChecked = "SELECT * FROM food_nha_an WHERE employee_id = ? and shifts_id = ? and food_date = ?";
										PreparedStatement preStatementChecked = null;
										preStatementChecked = con.prepareStatement(queryChecked);
										// check bang ma nhan vien cu
										preStatementChecked.setString(1, listl.get(fid[0] - 1).getMaNhanVien());
										preStatementChecked.setInt(2, shiftsCurrent);
										preStatementChecked.setDate(3, ngay_cua_ca_SQL);
										ResultSet resultSetChecked = preStatementChecked.executeQuery();
										boolean checkedExist = false;
										if (resultSetChecked.next()) {
											checkedExist = true;
											// image he thong da nhan suat an
											String pathImageDaNhanSuatAn = "imagesSystem/image-danhansuatan650x450.png";
											File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
											BufferedImage bimgDaNhanSuatAn = null;
											try {
												bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
											} catch (IOException e) {
											}
											if (bimgDaNhanSuatAn == null) {
												btnImg.setIcon(null);
											}
											if (bimgDaNhanSuatAn != null) {
												// Image scaledTest =
												// bimgDaNhanSuatAn.getScaledInstance(widthHinhLon,
												// heightHinhLon,
												// Image.SCALE_SMOOTH);
												// ImageIcon imageTest = new
												// ImageIcon(scaledTest);
												ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
												btnImg.setIcon(imageTest);
											}
											// end image he thong
											if (preStatementChecked != null) {
												preStatementChecked.close();
											}
											return;
										}
										// Neu chua an se them du lieu vao DB ->
										// 2 truong hop: 1 la co dang ky , 2
										// la khong dang ky

										preStatement = con.prepareStatement(queryFood);
										// pass id employee
										preStatement.setString(1, listl.get(fid[0] - 1).getMaNhanVien());
										// date param
										preStatement.setDate(2, ngay_cua_ca_SQL);
										preStatement.setInt(3, shiftsCurrent);
										ResultSet resultSet = preStatement.executeQuery();
										// Co dang ky
										while (resultSet.next()) {
											// neu nhan vien do da an ca do roi
											// - > k cho them
											if (!checkedExist) {
												orderFoodCurrent
														.setDepartmentName(resultSet.getString("of.department_name"));
												orderFoodCurrent
														.setDepartmentCode(resultSet.getString("of.department_code"));
												orderFoodCurrent
														.setEmployeeCode(resultSet.getString("of.employee_code"));
												orderFoodCurrent
														.setEmployeeName(resultSet.getString("of.employee_name"));
												orderFoodCurrent.setFoodName(resultSet.getString("cf.name"));
												orderFoodCurrent.setImage(resultSet.getBytes("cf.image"));
												orderFoodCurrent
														.setFood_date(resultSet.getDate("of.registration_date"));
												orderFoodCurrent
														.setCategory_food_id(resultSet.getInt("category_food_id"));
												orderFoodCurrent.setEmployeeId(resultSet.getString("of.employee_id"));
												orderFoodCurrent.setShifts_id(resultSet.getInt("fbd.shifts_id"));
												orderFoodCurrent.setNotRegFood(true);
												ZKFPDemo.addOne(orderFoodCurrent, orderFoodCurrent.getShifts_id());
											}
										}
										// chua dang ky
										// handle cho phan mon an tu chon
										if (!resultSet.first()) {
											DepartmentData[] departmentHCMArray = DepartmentDataService
													.timtheophongquanly(ZKFPDemo.codeBranch);
											List<DepartmentData> departmentHCM = new ArrayList<>(
													Arrays.asList(departmentHCMArray));

//											DepartmentData[] departmentBDArray = DepartmentDataService
//													.timtheophongquanly("20002");
//											List<DepartmentData> departmentBD = new ArrayList<>(
//													Arrays.asList(departmentBDArray));
//
//											departmentHCM.addAll(departmentBD);

											// HANDLE -> Tao list id department
											List<Long> listDepartmentCodeHCM = new ArrayList<Long>();
											for (DepartmentData de : departmentHCM) {
												listDepartmentCodeHCM.add(de.getId());
											}

											StringBuilder builder = new StringBuilder();
											for (int i = 0; i < listDepartmentCodeHCM.size(); i++) {
												builder.append("?,");
											}
											// query nhan vien tu du lieu trung
											// tam
											String queryEmployee = "SELECT empl.workShift, empl.layOff as da_nghi_viec,empl.id as employee_id,empl.name as employee_name,empl.code as employee_code,empl.codeOld as employee_code_old,depart.name as department_name,depart.code as department_code \r\n"
													+ "FROM dulieutrungtam.employee as empl, dulieutrungtam.department as depart\r\n"
													+ "WHERE empl.codeOld = ? AND empl.department_id IN ("
													+ builder.deleteCharAt(builder.length() - 1).toString()
													+ ") AND empl.department_id = depart.id;";
											PreparedStatement preStatementEmployee = null;
											Connection conDulieutrungtam = null;

											try {
												conDulieutrungtam = ZKFPDemo
														.getConnectionMySQL(URL.LINK_DULIEUTRUNGTAM_JDBC);
												preStatementEmployee = conDulieutrungtam
														.prepareStatement(queryEmployee);
												preStatementEmployee.setString(1,
														listl.get(fid[0] - 1).getMaNhanVien());
												// handle param IN operator
												int index = 2;
												for (Long id : listDepartmentCodeHCM) {
													preStatementEmployee.setObject(index++, id); // or
																									// whatever
																									// it
																									// applies
												}
												ResultSet resultSet1 = preStatementEmployee.executeQuery();
												while (resultSet1.next()) {
													boolean daNghiViec = resultSet1.getBoolean("da_nghi_viec");
													// neu chua nghi viec moi
													// duoc luu
													if (!daNghiViec) {
														// orderFoodCurrent.setDepartmentName(
														// resultSet1.getString("department_name"));
														// orderFoodCurrent.setDepartmentCode(
														// resultSet1.getString("department_code"));
														// orderFoodCurrent
														// .setEmployeeCode(resultSet1.getString("employee_code"));
														// orderFoodCurrent
														// .setEmployeeName(resultSet1.getString("employee_name"));
														// orderFoodCurrent.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
														// orderFoodCurrent
														// .setFood_date(DateUtil.DATE_WITHOUT_TIME(new
														// Date()));
														// // gan thang id cua
														// category food tu chon
														// orderFoodCurrent.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
														// orderFoodCurrent.setEmployeeId(
														// resultSet1.getString("employee_code_old"));
														// orderFoodCurrent.setShifts_id(shiftsCurrent);
														// orderFoodCurrent.setNotRegFood(true);
														// ZKFPDemo.addOne(orderFoodCurrent,
														// orderFoodCurrent.getShifts_id());

														boolean workShift = resultSet1.getBoolean("empl.workShift");
														String employeeCode = resultSet1.getString("employee_code");
														// nhan vien van phong
														// an ca 2
														if (!workShift && shiftsCurrent == Shifts.SHIFTS_2_ID) {
															try {
																con = ZKFPDemo
																		.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
																// kiem tra co
																// dang ky tang
																// ca hay khong
																String query = "SELECT food_ot.employee_name, food_ot.employee_code, ot.department_code,ot.department_name,ot.food_date, ot.shifts_id, food_ot.employee_code_old "
																		+ "FROM food_over_time as food_ot, over_time as ot "
																		+ "WHERE food_ot.employee_code = ? AND ot.shifts_id = ? AND ot.food_date = ? AND "
																		+ "ot.id = food_ot.over_time_id";
																preStatementChecked = con.prepareStatement(query);
																// check bang ma
																// nhan vien cu
																preStatementChecked.setString(1, employeeCode);
																preStatementChecked.setInt(2, shiftsCurrent);
																preStatementChecked.setDate(3, ngay_cua_ca_SQL);
																ResultSet rs = preStatementChecked.executeQuery();
																if (rs.next()) {
																	orderFoodCurrent.setDepartmentName(
																			rs.getString("ot.department_name"));
																	orderFoodCurrent.setDepartmentCode(
																			rs.getString("ot.department_code"));
																	orderFoodCurrent.setEmployeeCode(employeeCode);
																	orderFoodCurrent.setEmployeeName(
																			rs.getString("food_ot.employee_name"));
																	orderFoodCurrent
																			.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
																	orderFoodCurrent
																			.setFood_date(DateUtil.DATE_WITHOUT_TIME(
																					rs.getDate("ot.food_date")));
																	// gan thang
																	// id cua
																	// category
																	// food tu
																	// chon
																	orderFoodCurrent.setCategory_food_id(
																			FoodCustom.FOOD_CUSTOM_ID);
																	if (rs.getString("employee_code_old") != null) {
																		orderFoodCurrent.setEmployeeId(
																				rs.getString("employee_code_old"));
																	}
																	orderFoodCurrent.setShifts_id(shiftsCurrent);
																	orderFoodCurrent.setOverTime(true);
																	ZKFPDemo.addOne(orderFoodCurrent,
																			orderFoodCurrent.getShifts_id());
																}
																if (!rs.first()) {
																	String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
																	File fileKCoSuatAn = new File(
																			pathImageKhongCoSuatAn);
																	BufferedImage bimgKCoSuatAn = null;
																	try {
																		bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
																	} catch (IOException e1) {
																		e1.printStackTrace();
																	}
																	if (bimgKCoSuatAn == null) {
																		btnImg.setIcon(null);
																	}
																	if (bimgKCoSuatAn != null) {
																		ImageIcon imageTest = new ImageIcon(
																				bimgKCoSuatAn);
																		btnImg.setIcon(imageTest);
																	}
																	return;
																}
															} catch (Exception e1) {
																// TODO: handle
																// exception
															} finally {
																try {
																	ZKFPDemo.closeConnectionPre(con,
																			preStatementChecked);
																} catch (Exception e2) {
																	e2.printStackTrace();
																	;
																}
															}
														}
														// di ca hoac nhan vien
														// van phong an ca 1
														if (workShift
																|| !workShift && shiftsCurrent == Shifts.SHIFTS_1_ID) {
															orderFoodCurrent.setDepartmentName(
																	resultSet1.getString("department_name"));
															orderFoodCurrent.setDepartmentCode(
																	resultSet1.getString("department_code"));
															orderFoodCurrent.setEmployeeCode(employeeCode);
															orderFoodCurrent.setEmployeeName(
																	resultSet1.getString("employee_name"));
															orderFoodCurrent.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
															orderFoodCurrent.setFood_date(
																	DateUtil.DATE_WITHOUT_TIME(Shifts.NGAY_CUA_CA));
															// gan thang id cua
															// category food tu
															// chon
															orderFoodCurrent
																	.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
															orderFoodCurrent.setEmployeeId(
																	resultSet1.getString("employee_code_old"));
															orderFoodCurrent.setShifts_id(shiftsCurrent);
															ZKFPDemo.addOne(orderFoodCurrent,
																	orderFoodCurrent.getShifts_id());
														}
														// nhan vien van phong
														// an ca 3
														if (!workShift && shiftsCurrent == Shifts.SHIFTS_3_ID) {
															String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
															File fileKCoSuatAn = new File(pathImageKhongCoSuatAn);
															BufferedImage bimgKCoSuatAn = null;
															try {
																bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
															} catch (IOException e1) {
																e1.printStackTrace();
															}
															if (bimgKCoSuatAn == null) {
																btnImg.setIcon(null);
															}
															if (bimgKCoSuatAn != null) {
																ImageIcon imageTest = new ImageIcon(bimgKCoSuatAn);
																btnImg.setIcon(imageTest);
															}
															return;
														}
														// END KIEM TRA CA 2
													}
												}
												if (!resultSet1.first()) {
													String pathImageDaNhanSuatAn = "imagesSystem/image-kcttnhanvien650x450.png";
													File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
													BufferedImage bimgDaNhanSuatAn = null;
													try {
														bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
													} catch (IOException e1) {
														e1.printStackTrace();
													}
													if (bimgDaNhanSuatAn == null) {
														btnImg.setIcon(null);
													}
													if (bimgDaNhanSuatAn != null) {
														ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
														btnImg.setIcon(imageTest);
													}
													return;
												}
											} catch (Exception e) {
												// TODO: handle exception
											} finally {
												try {
													ZKFPDemo.closeConnectionPre(conDulieutrungtam,
															preStatementEmployee);
												} catch (Exception e2) {
													e2.printStackTrace();
													;
												}
											}
										}
										// end mon an tu chon
									} catch (Exception e) {
										// TODO: handle exception
									} finally {
										try {
											ZKFPDemo.closeConnectionPre(con, preStatement);
										} catch (Exception e2) {
											e2.printStackTrace();
										}
									}
									// da dang ky mon an
									if (orderFoodCurrent.getEmployeeName() != null) {
										textAreaMaNV.setText(orderFoodCurrent.getEmployeeCode());
										textAreaTenNV.setText(orderFoodCurrent.getEmployeeName());
										textAreaPhongban.setText(orderFoodCurrent.getDepartmentName());
										// convert date to string
										String pattern = "dd-MM-yyyy";
										DateFormat df = new SimpleDateFormat(pattern);
										String foodDate = df.format(orderFoodCurrent.getFood_date());
										textAreaNgay.setText(foodDate);
										// labelTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());a
										textAreaTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());

										// set hinh
										// check neu hinh bi null
										if (orderFoodCurrent.getImage() == null) {
											if (orderFoodCurrent.getCategory_food_id() == FoodCustom.FOOD_CUSTOM_ID) {
												if (bimgTuchon == null) {
													btnImg.setIcon(null);
												}
												if (bimgTuchon != null) {
													Image scaledTuchon = bimgTuchon.getScaledInstance(widthHinhLon,
															heightHinhLon, Image.SCALE_SMOOTH);
													ImageIcon imageTuchon = new ImageIcon(scaledTuchon);
													btnImg.setIcon(null);
													btnImg.setIcon(imageTuchon);
												}
											} else {
												// khong co hinh mon an
												if (bimgNotImage == null) {
													btnImg.setIcon(null);
												}
												if (bimgNotImage != null) {
													ImageIcon imageNotImage = new ImageIcon(bimgNotImage);
													btnImg.setIcon(imageNotImage);
												}
											}
										} else {
											Image img = Toolkit.getDefaultToolkit()
													.createImage(orderFoodCurrent.getImage());
											ImageIcon icon = new ImageIcon(img.getScaledInstance(widthHinhLon,
													heightHinhLon, Image.SCALE_SMOOTH));
											btnImg.setIcon(icon);
										}
									}

									// handle show 3 o nho cho 4 nguoi quet gan
									// nhat
									String queryTop4 = "SELECT FNA.employee_name,CF.name, FNA.employee_id, CF.image\r\n"
											+ "FROM food_nha_an as FNA, category_food as CF\r\n"
											+ "WHERE FNA.category_food_id = CF.id and FNA.shifts_id = ? and FNA.food_date = ?\r\n"
											+ "ORDER BY FNA.id DESC\r\n" + "LIMIT 3;";

									try {
										con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
										preStatementTop3 = con.prepareStatement(queryTop4);
										preStatementTop3.setInt(1, shiftsCurrent);
										preStatementTop3.setDate(2, ngay_cua_ca_SQL);
										ResultSet resultSet = preStatementTop3.executeQuery();
										while (resultSet.next()) {
											FoodNhaAn foodNhaAn = new FoodNhaAn();
											foodNhaAn.setEmployeeName(resultSet.getString("FNA.employee_name"));
											foodNhaAn.setImageFood(resultSet.getBytes("CF.image"));
											foodNhaAn.setEmployeeIdOld(resultSet.getString("FNA.employee_id"));
											foodNhaAn.setFoodName(resultSet.getString("CF.name"));
											foodNhaAnTop4.add(foodNhaAn);
										}
									} catch (Exception e) {
										// TODO: handle exception
									} finally {
										try {
											ZKFPDemo.closeConnectionPre(con, preStatementTop3);
										} catch (Exception e2) {
											e2.printStackTrace();
											;
										}
									}
									// set hinh
									// query co bi rong hay khong
									if (!foodNhaAnTop4.isEmpty()) {
										// query co 2 phan tu
										if (foodNhaAnTop4.size() == 1) {

											// image nhan vien
											String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
													+ ".bmp";
											File file = new File(pathImage);
											BufferedImage bimg = null;
											try {
												bimg = ImageIO.read(file);
											} catch (IOException e) {
											}
											if (bimg == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV1.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV1.setIcon(imageNoImageNV);
												}
											}
											if (bimg != null) {
												Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image = new ImageIcon(scaled);
												labelHinhNV1.setIcon(image);
											}
											textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
											// image mon an 1
											if (foodNhaAnTop4.get(0).getImageFood() == null) {
												btnImage1.setIcon(null);
											} else {
												Image img1 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(0).getImageFood());
												ImageIcon icon1 = new ImageIcon(
														img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage1.setIcon(icon1);
											}
											labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());
										}
										if (foodNhaAnTop4.size() == 2) {
											// image nhan vien 1
											String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
													+ ".bmp";
											File file = new File(pathImage);
											BufferedImage bimg = null;
											try {
												bimg = ImageIO.read(file);
											} catch (IOException e) {
											}

											if (bimg == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV1.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV1.setIcon(imageNoImageNV);
												}
											}
											if (bimg != null) {
												Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image = new ImageIcon(scaled);
												labelHinhNV1.setIcon(image);
											}
											textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
											// check neu image null
											if (foodNhaAnTop4.get(0).getImageFood() == null) {
												btnImage1.setIcon(null);
											} else {
												Image img1 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(0).getImageFood());
												ImageIcon icon1 = new ImageIcon(
														img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage1.setIcon(icon1);
											}

											labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

											// image nhan vien 2
											String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
													+ ".bmp";
											File file2 = new File(pathImage2);
											BufferedImage bimg2 = null;
											try {
												bimg2 = ImageIO.read(file2);
											} catch (IOException e) {
											}
											if (bimg2 == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV2.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV2.setIcon(imageNoImageNV);
												}
											}
											if (bimg2 != null) {
												Image scaled2 = bimg2.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image2 = new ImageIcon(scaled2);
												labelHinhNV2.setIcon(image2);
											}
											textAreaTenMonAn2.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
											// check neu image mon an 2 null
											if (foodNhaAnTop4.get(1).getImageFood() == null) {
												btnImage2.setIcon(null);
											} else {
												Image img2 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(1).getImageFood());
												ImageIcon icon2 = new ImageIcon(
														img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage2.setIcon(icon2);
											}
											labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());
										}
										if (foodNhaAnTop4.size() == 3) {
											// image nhan vien 1
											String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld()
													+ ".bmp";
											File file = new File(pathImage);
											BufferedImage bimg = null;
											try {
												bimg = ImageIO.read(file);
											} catch (IOException e) {
											}
											if (bimg == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV1.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV1.setIcon(imageNoImageNV);
												}
											}
											if (bimg != null) {
												Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image = new ImageIcon(scaled);
												labelHinhNV1.setIcon(image);
											}
											// hinh mon an 1
											// check neu image null
											textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
											if (foodNhaAnTop4.get(0).getImageFood() == null) {
												btnImage1.setIcon(null);
											} else {
												Image img1 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(0).getImageFood());
												ImageIcon icon1 = new ImageIcon(
														img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage1.setIcon(icon1);
											}
											labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

											// image nhan vien 2
											String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
													+ ".bmp";
											File file2 = new File(pathImage2);
											BufferedImage bimg2 = null;
											try {
												bimg2 = ImageIO.read(file2);
											} catch (IOException e) {
											}
											if (bimg2 == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV2.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV2.setIcon(imageNoImageNV);
												}
											}
											if (bimg2 != null) {
												Image scaled2 = bimg2.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image2 = new ImageIcon(scaled2);
												labelHinhNV2.setIcon(image2);
											}

											// image mon an 2
											// check neu image mon an 2 null
											textAreaTenMonAn2.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
											if (foodNhaAnTop4.get(1).getImageFood() == null) {
												btnImage2.setIcon(null);
											} else {
												Image img2 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(1).getImageFood());
												ImageIcon icon2 = new ImageIcon(
														img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage2.setIcon(icon2);
											}
											labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());

											// image nhan vien 3
											String pathImage3 = "images/" + foodNhaAnTop4.get(2).getEmployeeIdOld()
													+ ".bmp";
											File file3 = new File(pathImage3);
											BufferedImage bimg3 = null;
											try {
												bimg3 = ImageIO.read(file3);
											} catch (IOException e) {
											}
											if (bimg3 == null) {
												// image he thong nhan vien
												// khong co hinh
												if (bimgNoImageNV == null) {
													labelHinhNV3.setIcon(null);
												}
												if (bimgNoImageNV != null) {
													ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
													labelHinhNV3.setIcon(imageNoImageNV);
												}
											}
											if (bimg3 != null) {
												Image scaled3 = bimg3.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
												ImageIcon image3 = new ImageIcon(scaled3);
												labelHinhNV3.setIcon(image3);
											}

											// image mon an 3
											textAreaTenMonAn3.setText(foodNhaAnTop4.get(2).getFoodName().toUpperCase());
											if (foodNhaAnTop4.get(2).getImageFood() == null) {
												btnImage3.setIcon(null);
											} else {
												Image img3 = Toolkit.getDefaultToolkit()
														.createImage(foodNhaAnTop4.get(2).getImageFood());
												ImageIcon icon3 = new ImageIcon(
														img3.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
												btnImage3.setIcon(icon3);
											}
											labelName3.setText(foodNhaAnTop4.get(2).getEmployeeName());
										}
									}
									textArea.setText("Thành công");
									// end thai
								}
								// Khong co gio an phu hop
								else {
									// image he thong chua san sang
									String pathImageTest = "imagesSystem/image-khongcogioanphuhop.png";
									File fileTest = new File(pathImageTest);
									BufferedImage bimgTest = null;
									try {
										bimgTest = ImageIO.read(fileTest);
									} catch (IOException e2) {
									}
									if (bimgTest == null) {
										btnImg.setIcon(null);
									}
									if (bimgTest != null) {
										Image scaledTest = bimgTest.getScaledInstance(widthHinhLon, heightHinhLon,
												Image.SCALE_SMOOTH);
										ImageIcon imageTest = new ImageIcon(scaledTest);
										btnImg.setIcon(null);
										btnImg.setIcon(imageTest);
									}
									// end image he thong
								}
								// END CODE TAM THOI

								// // CODE CHINH THUC: truong hop khong dang ky
								// com va nut duoc bat len
								// // handle khong co gio an va da an roi
								// if (shiftsCurrent == 0) {
								// // image he thong chua san sang
								// String pathImageTest =
								// "imagesSystem/image-khongcogioanphuhop.png";
								// File fileTest = new File(pathImageTest);
								// BufferedImage bimgTest = null;
								// try {
								// bimgTest = ImageIO.read(fileTest);
								// } catch (IOException e2) {
								// }
								// if (bimgTest == null) {
								// btnImg.setIcon(null);
								// }
								// if (bimgTest != null) {
								// Image scaledTest =
								// bimgTest.getScaledInstance(widthHinhLon,
								// heightHinhLon,
								// Image.SCALE_SMOOTH);
								// ImageIcon imageTest = new
								// ImageIcon(scaledTest);
								// btnImg.setIcon(null);
								// btnImg.setIcon(imageTest);
								// }
								// return;
								// }
								// // co gio an
								// if (shiftsCurrent != 0) {
								// Connection conException = null;
								// PreparedStatement psCheckedException = null;
								// try {
								// conException = ZKFPDemo.getConnectionMySQL(
								// "jdbc:mysql://192.168.0.132:3306/quanlydatcom?useUnicode=yes&characterEncoding=UTF-8");
								// // kiem tra co du lieu ca do duoi DB chua
								// String queryChecked = "SELECT * FROM
								// quanlydatcom.food_nha_an_exception WHERE
								// employee_id = ? and shifts_id = ? and
								// food_date = ?";
								// psCheckedException =
								// conException.prepareStatement(queryChecked);
								// // check bang ma nhan vien cu
								// psCheckedException.setString(1,
								// listl.get(fid[0] - 1).getMaNhanVien());
								// psCheckedException.setInt(2, shiftsCurrent);
								// // convert date sql
								// java.sql.Date sqlDateChecked = new
								// java.sql.Date(dateCurrent.getTime());
								// psCheckedException.setDate(3,
								// sqlDateChecked);
								// ResultSet resultSetChecked =
								// psCheckedException.executeQuery();
								// boolean checkedExist = false;
								// if (resultSetChecked.next()) {
								// checkedExist = true;
								//// Toolkit.getDefaultToolkit().beep();
								//// JOptionPane optionPane = new
								// JOptionPane("BẠN ĐÃ NHẬN PHẦN ĂN TRƯỚC ĐÓ!",
								//// JOptionPane.WARNING_MESSAGE);
								//// JDialog dialog =
								// optionPane.createDialog(btnImg, "Chú ý!");
								//// dialog.setAlwaysOnTop(true);
								////// dialog.setVisible(true);
								////
								//// new Thread(new Runnable() {
								//// @Override
								//// public void run() {
								//// try {
								//// Thread.sleep(2000);
								//// } catch (InterruptedException e) {
								//// e.printStackTrace();
								//// }
								//// dialog.setVisible(false);
								//// }
								//// }).start();
								//// dialog.setVisible(true);
								//// btnImg.setIcon(null);
								// String pathImageDaNhanSuatAn =
								// "imagesSystem/image-danhansuatan650x450.png";
								// File fileDaNhanSuatAn = new
								// File(pathImageDaNhanSuatAn);
								// BufferedImage bimgDaNhanSuatAn = null;
								// try {
								// bimgDaNhanSuatAn =
								// ImageIO.read(fileDaNhanSuatAn);
								// } catch (IOException e) {
								// }
								// if (bimgDaNhanSuatAn == null) {
								// btnImg.setIcon(null);
								// }
								// if (bimgDaNhanSuatAn != null) {
								// Image scaledTest =
								// bimgDaNhanSuatAn.getScaledInstance(widthHinhLon,
								// heightHinhLon, Image.SCALE_SMOOTH);
								// ImageIcon imageTest = new
								// ImageIcon(scaledTest);
								// btnImg.setIcon(imageTest);
								// }
								// togglebtnKhongQuetVT.setText("CÓ Đ.KÝ");
								// ZKFPDemo.ON_ALLOW_EXCEPTION = false;
								// return;
								// }
								// // NEU CHUA AN THI CHO THEM SUAT AN VAO DB
								// if (!checkedExist) {
								// orderFoodCurrent = new OrderFood();
								// DepartmentData[] departmentHCMArray =
								// DepartmentDataService
								// .timtheophongquanly("20002");
								// List<DepartmentData> departmentHCM = new
								// ArrayList<>(
								// Arrays.asList(departmentHCMArray));
								// // HANDLE -> Tao list id department
								// List<Long> listDepartmentCodeHCM = new
								// ArrayList<Long>();
								// for (DepartmentData de : departmentHCM) {
								// listDepartmentCodeHCM.add(de.getId());
								// }
								//
								// StringBuilder builder = new StringBuilder();
								// for (int i = 0; i <
								// listDepartmentCodeHCM.size(); i++) {
								// builder.append("?,");
								// }
								// // query nhan vien tu du lieu trung tam
								// String queryEmployee = "SELECT empl.id as
								// employee_id,empl.name as
								// employee_name,empl.code as
								// employee_code,empl.codeOld as
								// employee_code_old,depart.name as
								// department_name,depart.code as
								// department_code \r\n"
								// + "FROM dulieutrungtam.employee as empl,
								// dulieutrungtam.department as depart\r\n"
								// + "WHERE empl.codeOld = ? AND
								// empl.department_id IN ("
								// + builder.deleteCharAt(builder.length() -
								// 1).toString()
								// + ") AND empl.department_id = depart.id;";
								// PreparedStatement preStatementEmployee =
								// null;
								// Connection conDulieutrungtam = null;
								//
								// try {
								// conDulieutrungtam =
								// ZKFPDemo.getConnectionMySQL(
								// "jdbc:mysql://192.168.0.132:3306/dulieutrungtam?useUnicode=yes&characterEncoding=UTF-8");
								// preStatementEmployee = conDulieutrungtam
								// .prepareStatement(queryEmployee);
								// preStatementEmployee.setString(1,
								// listl.get(fid[0] - 1).getMaNhanVien());
								// // handle param IN operator
								// int index = 2;
								// for (Long id : listDepartmentCodeHCM) {
								// preStatementEmployee.setObject(index++, id);
								// // or whatever it
								// // applies
								// }
								// ResultSet resultSet1 =
								// preStatementEmployee.executeQuery();
								// while (resultSet1.next()) {
								// orderFoodCurrent
								// .setDepartmentName(resultSet1.getString("department_name"));
								// orderFoodCurrent
								// .setDepartmentCode(resultSet1.getString("department_code"));
								// orderFoodCurrent
								// .setEmployeeCode(resultSet1.getString("employee_code"));
								// orderFoodCurrent
								// .setEmployeeName(resultSet1.getString("employee_name"));
								// orderFoodCurrent.setFoodName("Tự chọn");
								// orderFoodCurrent.setFood_date(new Date());
								// // gan thang id cua category food tu chon
								// orderFoodCurrent.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
								// orderFoodCurrent
								// .setEmployeeId(resultSet1.getString("employee_code_old"));
								// orderFoodCurrent.setShifts_id(shiftsCurrent);
								// ZKFPDemo.addOneException(orderFoodCurrent,
								// orderFoodCurrent.getShifts_id());
								// }
								// } catch (Exception e) {
								// // TODO: handle exception
								// } finally {
								// try {
								// ZKFPDemo.closeConnectionPre(conDulieutrungtam,
								// preStatementEmployee);
								// } catch (Exception e2) {
								// e2.printStackTrace();
								// ;
								// }
								// }
								// }
								// } catch (Exception e) {
								// // TODO: handle exception
								// } finally {
								// try {
								// ZKFPDemo.closeConnectionPre(conException,
								// psCheckedException);
								// } catch (Exception e2) {
								// e2.printStackTrace();
								// }
								// }
								// // Da luu suat an xuong db -> handle hien ra
								// view
								// if (orderFoodCurrent.getEmployeeName() !=
								// null) {
								// textAreaMaNV.setText(orderFoodCurrent.getEmployeeCode());
								// textAreaTenNV.setText(orderFoodCurrent.getEmployeeName());
								// textAreaPhongban.setText(orderFoodCurrent.getDepartmentName());
								// // convert date to string
								// String pattern = "dd-MM-yyyy";
								// DateFormat df = new
								// SimpleDateFormat(pattern);
								// String foodDate =
								// df.format(orderFoodCurrent.getFood_date());
								// textAreaNgay.setText(foodDate);
								// textAreaMonAn.setText(orderFoodCurrent.getFoodName());
								// // set hinh
								// // check neu hinh bi null
								// if (orderFoodCurrent.getImage() == null) {
								// String pathImageTuchon =
								// "imagesSystem/image-monantuchon.png";
								// File fileTuchon = new File(pathImageTuchon);
								// BufferedImage bimgTuchon = null;
								// try {
								// bimgTuchon = ImageIO.read(fileTuchon);
								// } catch (IOException e2) {
								// }
								// if (bimgTuchon == null) {
								// btnImg.setIcon(null);
								// }
								// if (bimgTuchon != null) {
								// Image scaledTuchon =
								// bimgTuchon.getScaledInstance(widthHinhLon,
								// heightHinhLon, Image.SCALE_SMOOTH);
								// ImageIcon imageTuchon = new
								// ImageIcon(scaledTuchon);
								// btnImg.setIcon(null);
								// btnImg.setIcon(imageTuchon);
								// }
								// } else {
								// Image img = Toolkit.getDefaultToolkit()
								// .createImage(orderFoodCurrent.getImage());
								// ImageIcon icon = new ImageIcon(
								// img.getScaledInstance(500, 350,
								// Image.SCALE_SMOOTH));
								// btnImg.setIcon(icon);
								// }
								// }
								// textArea.setText("Thành công");
								// togglebtnKhongQuetVT.setText("CÓ Đ.KÝ");
								// ZKFPDemo.ON_ALLOW_EXCEPTION = false;
								// }
								// // end
							}
							// END CODE CHINH THUC: truong hop khong dang ky com
							// va nut duoc bat len
						}
						// nguoc lai khong co data
						if (ZKFPDemo.listDataVerify == null) {
							// // image he thong chua san sang
							// String pathImageNull =
							// "imagesSystem/image-nulldatachamcong.png";
							// File fileNull = new File(pathImageNull);
							// BufferedImage bimgNull = null;
							// try {
							// bimgNull = ImageIO.read(fileNull);
							// } catch (IOException e2) {
							// }
							// if (bimgNull == null) {
							// btnImg.setIcon(null);
							// }
							// if (bimgNull != null) {
							// Image scaledTest =
							// bimgNull.getScaledInstance(widthHinhLon,
							// heightHinhLon,
							// Image.SCALE_SMOOTH);
							// ImageIcon imageTest = new ImageIcon(scaledTest);
							// btnImg.setIcon(null);
							// btnImg.setIcon(imageTest);
							// }
							// // end image he thong

							// CODE CHO NHAN VIEN CA 3 LUU DU LIEU

							// check xem nhan vien co cat com hay khong
							// nho check cho nay listDataVerify bi null
							// neu khong cat com ->
							List<FoodNhaAn> foodNhaAnTop4 = new ArrayList<>();
							orderFoodCurrent = new OrderFood();
							// chua handle cho nay

							// kiem tra xem hien tai co dang thuoc ca nao hay
							// khong
							if (shiftsCurrent != 0) {
								// thai
								java.sql.Date ngay_cua_ca_SQL = new java.sql.Date(Shifts.NGAY_CUA_CA.getTime());
								// query by date and employee
								String queryFood = "";
								queryFood = "SELECT ofbd.id as order_food_by_day_id, of.employee_code, of.employee_id, of.employee_name, of.department_code, of.department_name, cf.name,cf.image,cf.id as category_food_id,fbd.shifts_id, of.registration_date\r\n"
										+ "FROM order_food_by_day as ofbd, order_food as of, food_by_day fbd, category_food as cf\r\n"
										+ "WHERE of.employee_id = ? AND of.registration_date = ? AND fbd.shifts_id = ? AND ofbd.order_food_id = of.id AND ofbd.food_by_day_id = fbd.id AND cf.id = fbd.category_food_id;	";

								try {
									con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
									// kiem tra co du lieu ca do duoi DB chua
									String queryChecked = "SELECT * FROM food_nha_an WHERE employee_id = ? and shifts_id = ? and food_date = ?";
									PreparedStatement preStatementChecked = null;
									preStatementChecked = con.prepareStatement(queryChecked);
									// check bang ma nhan vien cu
									preStatementChecked.setString(1, listl.get(fid[0] - 1).getMaNhanVien());
									preStatementChecked.setInt(2, shiftsCurrent);
									preStatementChecked.setDate(3, ngay_cua_ca_SQL);
									ResultSet resultSetChecked = preStatementChecked.executeQuery();
									boolean checkedExist = false;
									if (resultSetChecked.next()) {
										checkedExist = true;
										// image he thong da nhan suat an
										String pathImageDaNhanSuatAn = "imagesSystem/image-danhansuatan650x450.png";
										File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
										BufferedImage bimgDaNhanSuatAn = null;
										try {
											bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
										} catch (IOException e) {
										}
										if (bimgDaNhanSuatAn == null) {
											btnImg.setIcon(null);
										}
										if (bimgDaNhanSuatAn != null) {
											ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
											btnImg.setIcon(imageTest);
										}
										// end image he thong
										return;
									}
									// Neu chua an se them du lieu vao DB -> 2
									// truong hop: 1 la co dang ky , 2
									// la khong dang ky
									preStatement = con.prepareStatement(queryFood);
									// pass id employee
									preStatement.setString(1, listl.get(fid[0] - 1).getMaNhanVien());
									// date param
									preStatement.setDate(2, ngay_cua_ca_SQL);
									preStatement.setInt(3, shiftsCurrent);
									ResultSet resultSet = preStatement.executeQuery();
									// Co dang ky
									while (resultSet.next()) {
										// neu nhan vien do da an ca do roi - >
										// k cho them
										if (!checkedExist) {
											orderFoodCurrent
													.setDepartmentName(resultSet.getString("of.department_name"));
											orderFoodCurrent
													.setDepartmentCode(resultSet.getString("of.department_code"));
											orderFoodCurrent.setEmployeeCode(resultSet.getString("of.employee_code"));
											orderFoodCurrent.setEmployeeName(resultSet.getString("of.employee_name"));
											orderFoodCurrent.setFoodName(resultSet.getString("cf.name"));
											orderFoodCurrent.setImage(resultSet.getBytes("cf.image"));
											orderFoodCurrent.setFood_date(resultSet.getDate("of.registration_date"));
											orderFoodCurrent.setCategory_food_id(resultSet.getInt("category_food_id"));
											orderFoodCurrent.setEmployeeId(resultSet.getString("of.employee_id"));
											orderFoodCurrent.setShifts_id(resultSet.getInt("fbd.shifts_id"));
											orderFoodCurrent.setNotRegFood(true);
											ZKFPDemo.addOne(orderFoodCurrent, orderFoodCurrent.getShifts_id());
										}
									}
									// chua dang ky
									// handle cho phan mon an tu chon
									if (!resultSet.first()) {
										DepartmentData[] departmentHCMArray = DepartmentDataService
												.timtheophongquanly(ZKFPDemo.codeBranch);
										List<DepartmentData> departmentHCM = new ArrayList<>(
												Arrays.asList(departmentHCMArray));

//										DepartmentData[] departmentBDArray = DepartmentDataService
//												.timtheophongquanly("20002");
//										List<DepartmentData> departmentBD = new ArrayList<>(
//												Arrays.asList(departmentBDArray));
//
//										departmentHCM.addAll(departmentBD);

										// HANDLE -> Tao list id department
										List<Long> listDepartmentCodeHCM = new ArrayList<Long>();
										for (DepartmentData de : departmentHCM) {
											listDepartmentCodeHCM.add(de.getId());
										}

										StringBuilder builder = new StringBuilder();
										for (int i = 0; i < listDepartmentCodeHCM.size(); i++) {
											builder.append("?,");
										}
										// query nhan vien tu du lieu trung tam
										String queryEmployee = "SELECT empl.workShift, empl.layOff as da_nghi_viec,empl.id as employee_id,empl.name as employee_name,empl.code as employee_code,empl.codeOld as employee_code_old,depart.name as department_name,depart.code as department_code \r\n"
												+ "FROM dulieutrungtam.employee as empl, dulieutrungtam.department as depart\r\n"
												+ "WHERE empl.codeOld = ? AND empl.department_id IN ("
												+ builder.deleteCharAt(builder.length() - 1).toString()
												+ ") AND empl.department_id = depart.id;";
										PreparedStatement preStatementEmployee = null;
										Connection conDulieutrungtam = null;

										try {
											conDulieutrungtam = ZKFPDemo
													.getConnectionMySQL(URL.LINK_DULIEUTRUNGTAM_JDBC);
											preStatementEmployee = conDulieutrungtam.prepareStatement(queryEmployee);
											preStatementEmployee.setString(1, listl.get(fid[0] - 1).getMaNhanVien());
											// handle param IN operator
											int index = 2;
											for (Long id : listDepartmentCodeHCM) {
												preStatementEmployee.setObject(index++, id); // or
																								// whatever
																								// it
																								// applies
											}
											ResultSet resultSet1 = preStatementEmployee.executeQuery();
											while (resultSet1.next()) {
												boolean daNghiViec = resultSet1.getBoolean("da_nghi_viec");
												// neu chua nghi viec moi duoc
												// luu
												if (!daNghiViec) {
													// orderFoodCurrent
													// .setDepartmentName(resultSet1.getString("department_name"));
													// orderFoodCurrent
													// .setDepartmentCode(resultSet1.getString("department_code"));
													// orderFoodCurrent
													// .setEmployeeCode(resultSet1.getString("employee_code"));
													// orderFoodCurrent
													// .setEmployeeName(resultSet1.getString("employee_name"));
													// orderFoodCurrent.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
													// orderFoodCurrent
													// .setFood_date(DateUtil.DATE_WITHOUT_TIME(new
													// Date()));
													// // gan thang id cua
													// category food tu chon
													// orderFoodCurrent.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
													// orderFoodCurrent
													// .setEmployeeId(resultSet1.getString("employee_code_old"));
													// orderFoodCurrent.setShifts_id(shiftsCurrent);
													// orderFoodCurrent.setNotRegFood(true);
													// ZKFPDemo.addOne(orderFoodCurrent,
													// orderFoodCurrent.getShifts_id());

													boolean workShift = resultSet1.getBoolean("empl.workShift");
													String employeeCode = resultSet1.getString("employee_code");
													// nhan vien van phong an ca
													// 2
													if (!workShift && shiftsCurrent == Shifts.SHIFTS_2_ID) {
														try {
															con = ZKFPDemo
																	.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
															// kiem tra co dang
															// ky tang ca hay
															// khong
															String query = "SELECT food_ot.employee_name, food_ot.employee_code, ot.department_code,ot.department_name,ot.food_date, ot.shifts_id, food_ot.employee_code_old "
																	+ "FROM food_over_time as food_ot, over_time as ot "
																	+ "WHERE food_ot.employee_code = ? AND ot.shifts_id = ? AND ot.food_date = ? AND "
																	+ "ot.id = food_ot.over_time_id";
															preStatementChecked = con.prepareStatement(query);
															// check bang ma
															// nhan vien cu
															preStatementChecked.setString(1, employeeCode);
															preStatementChecked.setInt(2, shiftsCurrent);
															preStatementChecked.setDate(3, ngay_cua_ca_SQL);
															ResultSet rs = preStatementChecked.executeQuery();
															if (rs.next()) {
																orderFoodCurrent.setDepartmentName(
																		rs.getString("ot.department_name"));
																orderFoodCurrent.setDepartmentCode(
																		rs.getString("ot.department_code"));
																orderFoodCurrent.setEmployeeCode(employeeCode);
																orderFoodCurrent.setEmployeeName(
																		rs.getString("food_ot.employee_name"));
																orderFoodCurrent
																		.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
																orderFoodCurrent.setFood_date(DateUtil
																		.DATE_WITHOUT_TIME(rs.getDate("ot.food_date")));
																// gan thang id
																// cua category
																// food tu chon
																orderFoodCurrent
																		.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
																if (rs.getString("employee_code_old") != null) {
																	orderFoodCurrent.setEmployeeId(
																			rs.getString("employee_code_old"));
																}
																orderFoodCurrent.setShifts_id(shiftsCurrent);
																orderFoodCurrent.setOverTime(true);
																orderFoodCurrent.setNotRegFood(true);
																ZKFPDemo.addOne(orderFoodCurrent,
																		orderFoodCurrent.getShifts_id());
															}
															if (!rs.first()) {
																String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
																File fileKCoSuatAn = new File(pathImageKhongCoSuatAn);
																BufferedImage bimgKCoSuatAn = null;
																try {
																	bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
																} catch (IOException e1) {
																	e1.printStackTrace();
																}
																if (bimgKCoSuatAn == null) {
																	btnImg.setIcon(null);
																}
																if (bimgKCoSuatAn != null) {
																	ImageIcon imageTest = new ImageIcon(bimgKCoSuatAn);
																	btnImg.setIcon(imageTest);
																}
																return;
															}
														} catch (Exception e1) {
															// TODO: handle
															// exception
														} finally {
															try {
																ZKFPDemo.closeConnectionPre(con, preStatementChecked);
															} catch (Exception e2) {
																e2.printStackTrace();
																;
															}
														}
													}
													// di ca hoac nhan vien van
													// phong an ca 1
													if (workShift
															|| !workShift && shiftsCurrent == Shifts.SHIFTS_1_ID) {
														orderFoodCurrent.setDepartmentName(
																resultSet1.getString("department_name"));
														orderFoodCurrent.setDepartmentCode(
																resultSet1.getString("department_code"));
														orderFoodCurrent.setEmployeeCode(employeeCode);
														orderFoodCurrent
																.setEmployeeName(resultSet1.getString("employee_name"));
														orderFoodCurrent.setFoodName(FoodCustom.FOOD_CUSTOM_NAME);
														orderFoodCurrent.setFood_date(
																DateUtil.DATE_WITHOUT_TIME(Shifts.NGAY_CUA_CA));
														// gan thang id cua
														// category food tu chon
														orderFoodCurrent.setCategory_food_id(FoodCustom.FOOD_CUSTOM_ID);
														orderFoodCurrent.setEmployeeId(
																resultSet1.getString("employee_code_old"));
														orderFoodCurrent.setShifts_id(shiftsCurrent);
														orderFoodCurrent.setNotRegFood(true);
														ZKFPDemo.addOne(orderFoodCurrent,
																orderFoodCurrent.getShifts_id());
													}
													// nhan vien van phong an ca
													// 3
													if (!workShift && shiftsCurrent == Shifts.SHIFTS_3_ID) {
														String pathImageKhongCoSuatAn = "imagesSystem/image-khongcosuatan-650x450.png";
														File fileKCoSuatAn = new File(pathImageKhongCoSuatAn);
														BufferedImage bimgKCoSuatAn = null;
														try {
															bimgKCoSuatAn = ImageIO.read(fileKCoSuatAn);
														} catch (IOException e1) {
															e1.printStackTrace();
														}
														if (bimgKCoSuatAn == null) {
															btnImg.setIcon(null);
														}
														if (bimgKCoSuatAn != null) {
															ImageIcon imageTest = new ImageIcon(bimgKCoSuatAn);
															btnImg.setIcon(imageTest);
														}
														return;
													}
													// END KIEM TRA CA 2
												}
											}
											// k co du lieu trung tam
											if (!resultSet1.first()) {
												String pathImageDaNhanSuatAn = "imagesSystem/image-kcttnhanvien650x450.png";
												File fileDaNhanSuatAn = new File(pathImageDaNhanSuatAn);
												BufferedImage bimgDaNhanSuatAn = null;
												try {
													bimgDaNhanSuatAn = ImageIO.read(fileDaNhanSuatAn);
												} catch (IOException e1) {
													e1.printStackTrace();
												}
												if (bimgDaNhanSuatAn == null) {
													btnImg.setIcon(null);
												}
												if (bimgDaNhanSuatAn != null) {
													ImageIcon imageTest = new ImageIcon(bimgDaNhanSuatAn);
													btnImg.setIcon(imageTest);
												}
												return;
											}
										} catch (Exception e) {
											// TODO: handle exception
										} finally {
											try {
												ZKFPDemo.closeConnectionPre(conDulieutrungtam, preStatementEmployee);
											} catch (Exception e2) {
												e2.printStackTrace();
												;
											}
										}
									}
									// end mon an tu chon
								} catch (Exception e) {
									// TODO: handle exception
								} finally {
									try {
										ZKFPDemo.closeConnectionPre(con, preStatement);
									} catch (Exception e2) {
										e2.printStackTrace();
									}
								}
								// da dang ky mon an
								if (orderFoodCurrent.getEmployeeName() != null) {
									textAreaMaNV.setText(orderFoodCurrent.getEmployeeCode());
									textAreaTenNV.setText(orderFoodCurrent.getEmployeeName());
									textAreaPhongban.setText(orderFoodCurrent.getDepartmentName());
									// convert date to string
									String pattern = "dd-MM-yyyy";
									DateFormat df = new SimpleDateFormat(pattern);
									String foodDate = df.format(orderFoodCurrent.getFood_date());
									textAreaNgay.setText(foodDate);
									// labelTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());a
									textAreaTenMonAnLarge.setText(orderFoodCurrent.getFoodName().toUpperCase());

									// set hinh
									// check neu hinh bi null
									if (orderFoodCurrent.getImage() == null) {
										if (orderFoodCurrent.getCategory_food_id() == FoodCustom.FOOD_CUSTOM_ID) {
											if (bimgTuchon == null) {
												btnImg.setIcon(null);
											}
											if (bimgTuchon != null) {
												Image scaledTuchon = bimgTuchon.getScaledInstance(widthHinhLon,
														heightHinhLon, Image.SCALE_SMOOTH);
												ImageIcon imageTuchon = new ImageIcon(scaledTuchon);
												btnImg.setIcon(null);
												btnImg.setIcon(imageTuchon);
											}
										} else {
											// khong co hinh mon an
											if (bimgNotImage == null) {
												btnImg.setIcon(null);
											}
											if (bimgNotImage != null) {
												ImageIcon imageNotImage = new ImageIcon(bimgNotImage);
												btnImg.setIcon(imageNotImage);
											}
										}
									} else {
										Image img = Toolkit.getDefaultToolkit()
												.createImage(orderFoodCurrent.getImage());
										ImageIcon icon = new ImageIcon(
												img.getScaledInstance(widthHinhLon, heightHinhLon, Image.SCALE_SMOOTH));
										btnImg.setIcon(icon);
									}
								}

								// handle show 3 o nho cho 4 nguoi quet gan nhat
								String queryTop4 = "SELECT FNA.employee_name,CF.name, FNA.employee_id, CF.image\r\n"
										+ "FROM food_nha_an as FNA, category_food as CF\r\n"
										+ "WHERE FNA.category_food_id = CF.id and FNA.shifts_id = ? and FNA.food_date = ?\r\n"
										+ "ORDER BY FNA.id DESC\r\n" + "LIMIT 3;";

								try {
									con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
									preStatementTop3 = con.prepareStatement(queryTop4);
									preStatementTop3.setInt(1, shiftsCurrent);
									preStatementTop3.setDate(2, ngay_cua_ca_SQL);
									ResultSet resultSet = preStatementTop3.executeQuery();
									while (resultSet.next()) {
										FoodNhaAn foodNhaAn = new FoodNhaAn();
										foodNhaAn.setEmployeeName(resultSet.getString("FNA.employee_name"));
										foodNhaAn.setImageFood(resultSet.getBytes("CF.image"));
										foodNhaAn.setEmployeeIdOld(resultSet.getString("FNA.employee_id"));
										foodNhaAn.setFoodName(resultSet.getString("CF.name"));
										foodNhaAnTop4.add(foodNhaAn);
									}
								} catch (Exception e) {
									// TODO: handle exception
								} finally {
									try {
										ZKFPDemo.closeConnectionPre(con, preStatementTop3);
									} catch (Exception e2) {
										e2.printStackTrace();
										;
									}
								}
								// set hinh
								// query co bi rong hay khong
								if (!foodNhaAnTop4.isEmpty()) {
									// query co 2 phan tu
									if (foodNhaAnTop4.size() == 1) {

										// image nhan vien
										String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld() + ".bmp";
										File file = new File(pathImage);
										BufferedImage bimg = null;
										try {
											bimg = ImageIO.read(file);
										} catch (IOException e) {
										}
										if (bimg == null) {
											// image he thong nhan vien khong co
											// hinh
											if (bimgNoImageNV == null) {
												labelHinhNV1.setIcon(null);
											}
											if (bimgNoImageNV != null) {
												ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
												labelHinhNV1.setIcon(imageNoImageNV);
											}
										}
										if (bimg != null) {
											Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
											ImageIcon image = new ImageIcon(scaled);
											labelHinhNV1.setIcon(image);
										}
										textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
										// image mon an 1
										if (foodNhaAnTop4.get(0).getImageFood() == null) {
											btnImage1.setIcon(null);
										} else {
											Image img1 = Toolkit.getDefaultToolkit()
													.createImage(foodNhaAnTop4.get(0).getImageFood());
											ImageIcon icon1 = new ImageIcon(
													img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
											btnImage1.setIcon(icon1);
										}
										labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());
									}
									if (foodNhaAnTop4.size() == 2) {
										// image nhan vien 1
										String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld() + ".bmp";
										File file = new File(pathImage);
										BufferedImage bimg = null;
										try {
											bimg = ImageIO.read(file);
										} catch (IOException e) {
										}

										if (bimg == null) {
											// image he thong nhan vien khong co
											// hinh
											if (bimgNoImageNV == null) {
												labelHinhNV1.setIcon(null);
											}
											if (bimgNoImageNV != null) {
												ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
												labelHinhNV1.setIcon(imageNoImageNV);
											}
										}
										if (bimg != null) {
											Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
											ImageIcon image = new ImageIcon(scaled);
											labelHinhNV1.setIcon(image);
										}
										textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
										// check neu image null
										if (foodNhaAnTop4.get(0).getImageFood() == null) {
											btnImage1.setIcon(null);
										} else {
											Image img1 = Toolkit.getDefaultToolkit()
													.createImage(foodNhaAnTop4.get(0).getImageFood());
											ImageIcon icon1 = new ImageIcon(
													img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
											btnImage1.setIcon(icon1);
										}

										labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

										// image nhan vien 2
										String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
												+ ".bmp";
										File file2 = new File(pathImage2);
										BufferedImage bimg2 = null;
										try {
											bimg2 = ImageIO.read(file2);
										} catch (IOException e) {
										}
										if (bimg2 == null) {
											// image he thong nhan vien khong co
											// hinh
											if (bimgNoImageNV == null) {
												labelHinhNV2.setIcon(null);
											}
											if (bimgNoImageNV != null) {
												ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
												labelHinhNV2.setIcon(imageNoImageNV);
											}
										}
										if (bimg2 != null) {
											Image scaled2 = bimg2.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
											ImageIcon image2 = new ImageIcon(scaled2);
											labelHinhNV2.setIcon(image2);
										}
										textAreaTenMonAn2.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
										// check neu image mon an 2 null
										if (foodNhaAnTop4.get(1).getImageFood() == null) {
											btnImage2.setIcon(null);
										} else {
											Image img2 = Toolkit.getDefaultToolkit()
													.createImage(foodNhaAnTop4.get(1).getImageFood());
											ImageIcon icon2 = new ImageIcon(
													img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
											btnImage2.setIcon(icon2);
										}
										labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());
									}
									if (foodNhaAnTop4.size() == 3) {
										// image nhan vien 1
										String pathImage = "images/" + foodNhaAnTop4.get(0).getEmployeeIdOld() + ".bmp";
										File file = new File(pathImage);
										BufferedImage bimg = null;
										try {
											bimg = ImageIO.read(file);
										} catch (IOException e) {
										}
										if (bimg == null) {
											// image he thong nhan vien khong co
											// hinh
											if (bimgNoImageNV == null) {
												labelHinhNV1.setIcon(null);
											}
											if (bimgNoImageNV != null) {
												ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
												labelHinhNV1.setIcon(imageNoImageNV);
											}
										}
										if (bimg != null) {
											Image scaled = bimg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
											ImageIcon image = new ImageIcon(scaled);
											labelHinhNV1.setIcon(image);
										}
										// hinh mon an 1
										// check neu image null
										textAreaTenMonAn1.setText(foodNhaAnTop4.get(0).getFoodName().toUpperCase());
										if (foodNhaAnTop4.get(0).getImageFood() == null) {
											btnImage1.setIcon(null);
										} else {
											Image img1 = Toolkit.getDefaultToolkit()
													.createImage(foodNhaAnTop4.get(0).getImageFood());
											ImageIcon icon1 = new ImageIcon(
													img1.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
											btnImage1.setIcon(icon1);
										}
										labelName1.setText(foodNhaAnTop4.get(0).getEmployeeName());

										// image nhan vien 2
										String pathImage2 = "images/" + foodNhaAnTop4.get(1).getEmployeeIdOld()
												+ ".bmp";
										File file2 = new File(pathImage2);
										BufferedImage bimg2 = null;
										try {
											bimg2 = ImageIO.read(file2);
										} catch (IOException e) {
										}
										if (bimg2 == null) {
											// image he thong nhan vien khong co
											// hinh
											if (bimgNoImageNV == null) {
												labelHinhNV2.setIcon(null);
											}
											if (bimgNoImageNV != null) {
												ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
												labelHinhNV2.setIcon(imageNoImageNV);
											}
										}
										if (bimg2 != null) {
											Image scaled2 = bimg2.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
											ImageIcon image2 = new ImageIcon(scaled2);
											labelHinhNV2.setIcon(image2);
										}

										// image mon an 2
										// check neu image mon an 2 null
										textAreaTenMonAn2.setText(foodNhaAnTop4.get(1).getFoodName().toUpperCase());
										if (foodNhaAnTop4.get(1).getImageFood() == null) {
											btnImage2.setIcon(null);
										} else {
											Image img2 = Toolkit.getDefaultToolkit()
													.createImage(foodNhaAnTop4.get(1).getImageFood());
											ImageIcon icon2 = new ImageIcon(
													img2.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
											btnImage2.setIcon(icon2);
										}
										labelName2.setText(foodNhaAnTop4.get(1).getEmployeeName());

										// image nhan vien 3
										String pathImage3 = "images/" + foodNhaAnTop4.get(2).getEmployeeIdOld()
												+ ".bmp";
										File file3 = new File(pathImage3);
										BufferedImage bimg3 = null;
										try {
											bimg3 = ImageIO.read(file3);
										} catch (IOException e) {
										}
										if (bimg3 == null) {
											// image he thong nhan vien khong co
											// hinh
											if (bimgNoImageNV == null) {
												labelHinhNV3.setIcon(null);
											}
											if (bimgNoImageNV != null) {
												ImageIcon imageNoImageNV = new ImageIcon(bimgNoImageNV);
												labelHinhNV3.setIcon(imageNoImageNV);
											}
										}
										if (bimg3 != null) {
											Image scaled3 = bimg3.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
											ImageIcon image3 = new ImageIcon(scaled3);
											labelHinhNV3.setIcon(image3);
										}

										// image mon an 3
										textAreaTenMonAn3.setText(foodNhaAnTop4.get(2).getFoodName().toUpperCase());
										if (foodNhaAnTop4.get(2).getImageFood() == null) {
											btnImage3.setIcon(null);
										} else {
											Image img3 = Toolkit.getDefaultToolkit()
													.createImage(foodNhaAnTop4.get(2).getImageFood());
											ImageIcon icon3 = new ImageIcon(
													img3.getScaledInstance(190, 160, Image.SCALE_SMOOTH));
											btnImage3.setIcon(icon3);
										}
										labelName3.setText(foodNhaAnTop4.get(2).getEmployeeName());
									}
								}
								textArea.setText("Thành công");
								// end thai
							}
							// Khong co gio an phu hop
							else {
								// image he thong chua san sang
								String pathImageTest = "imagesSystem/image-khongcogioanphuhop.png";
								File fileTest = new File(pathImageTest);
								BufferedImage bimgTest = null;
								try {
									bimgTest = ImageIO.read(fileTest);
								} catch (IOException e2) {
								}
								if (bimgTest == null) {
									btnImg.setIcon(null);
								}
								if (bimgTest != null) {
									Image scaledTest = bimgTest.getScaledInstance(widthHinhLon, heightHinhLon,
											Image.SCALE_SMOOTH);
									ImageIcon imageTest = new ImageIcon(scaledTest);
									btnImg.setIcon(null);
									btnImg.setIcon(imageTest);
								}
								// end image he thong
							}
							// END CODE
						}
					} else {

						textArea.setText("Identify fail, errcode=" + ret + "\n");
						// van tay sai
						String pathImageTest = "imagesSystem/image-error650x450.png";
						File fileTest = new File(pathImageTest);
						BufferedImage bimgTest = null;
						try {
							bimgTest = ImageIO.read(fileTest);
						} catch (IOException e2) {
						}
						if (bimgTest == null) {
							btnImg.setIcon(null);
						}
						if (bimgTest != null) {
							// Image scaledTest =
							// bimgTest.getScaledInstance(widthHinhLon,
							// heightHinhLon,
							// Image.SCALE_SMOOTH);
							ImageIcon imageTest = new ImageIcon(bimgTest);
							btnImg.setIcon(null);
							btnImg.setIcon(imageTest);
						}
						// end image he thong
					}
				} else {
					if (cbRegTemp <= 0) {
						textArea.setText("Please register first!\n");
					} else {
						int ret = FingerprintSensorEx.DBMatch(mhDB, lastRegTemp, template);
						if (ret > 0) {
							textArea.setText("Verify succ, score=" + ret + "\n");
						} else {
							textArea.setText("Verify fail, ret=" + ret + "\n");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// lauch frame
		new ZKFPDemo().launchFrame();
		// image he thong nhan vien khong co hinh
		String pathImageNoImageNV = "imagesSystem/image-noimagenv140x140.png";
		File fileNoImageNV = new File(pathImageNoImageNV);
		try {
			bimgNoImageNV = ImageIO.read(fileNoImageNV);
		} catch (IOException e1) {
		}
		// image khong co hinh mon an
		String pathImageNotImage = "imagesSystem/image-notimage.png";
		File fileNotImage = new File(pathImageNotImage);
		bimgNotImage = null;
		try {
			bimgNotImage = ImageIO.read(fileNotImage);
		} catch (IOException e1) {
		}
		// hinh mon an tu chon
		String pathImageTuchon = "imagesSystem/image-monantuchon.png";
		File fileTuchon = new File(pathImageTuchon);
		bimgTuchon = null;
		try {
			bimgTuchon = ImageIO.read(fileTuchon);
		} catch (IOException e2) {
		}

	}

	public static Connection getConnectionMySQL(String url) {
		// Load driver mysql
		try {
			// Class.forName("com.mysql.jdbc.Driver");
			// handle connect mysql
			// String url =
			// "jdbc:mysql://localhost:3306/quanlydatcom?useUnicode=yes&characterEncoding=UTF-8";
			// String user = "remote";
			// String password = "Voquangthai1901";

			String user = "remote";
			String password = "remote2013";
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void addOneException(OrderFood orderFood, int shiftsCurrent) {
		Connection con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
		PreparedStatement statement = null;
		String sql = "INSERT INTO food_nha_an_exception (disable, oldData, employee_code,employee_name,department_code,department_name, food_date, category_food_id , shifts_id,employee_id) VALUES (?,?,?,?,?,?,?,?,?,?)";
		try {
			statement = con.prepareStatement(sql);
			statement.setInt(1, 0);
			statement.setInt(2, 0);
			statement.setString(3, orderFood.getEmployeeCode());
			statement.setString(4, orderFood.getEmployeeName());
			statement.setString(5, orderFood.getDepartmentCode());
			statement.setString(6, orderFood.getDepartmentName());
			// convert date sql
			java.sql.Date sqlDate = new java.sql.Date(orderFood.getFood_date().getTime());
			statement.setDate(7, sqlDate);
			statement.setInt(8, orderFood.getCategory_food_id());
			statement.setInt(9, shiftsCurrent);
			statement.setString(10, orderFood.getEmployeeId());

			statement.execute();
		} catch (SQLException e) {
			// System.out.println(e.getMessage());
		} finally {
			try {
				ZKFPDemo.closeConnectionPre(con, statement);
			} catch (Exception e2) {
				// System.out.println(e2.getMessage());
			}
		}
	}

	public static void addOne(OrderFood orderFood, int shiftsCurrent) {
		Connection con = ZKFPDemo.getConnectionMySQL(URL.LINK_QUANLYDATCOM_JDBC);
		PreparedStatement statement = null;
		String sql = "INSERT INTO food_nha_an (disable, oldData, employee_code,employee_name,department_code,department_name, food_date, category_food_id , shifts_id,employee_id, is_not_reg_food, created_date, is_over_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			statement = con.prepareStatement(sql);
			statement.setInt(1, 0);
			statement.setInt(2, 0);
			statement.setString(3, orderFood.getEmployeeCode());
			statement.setString(4, orderFood.getEmployeeName());
			statement.setString(5, orderFood.getDepartmentCode());
			statement.setString(6, orderFood.getDepartmentName());
			// convert date sql
			java.sql.Date sqlDate = new java.sql.Date(orderFood.getFood_date().getTime());
			statement.setDate(7, sqlDate);
			statement.setInt(8, orderFood.getCategory_food_id());
			statement.setInt(9, shiftsCurrent);
			statement.setString(10, orderFood.getEmployeeId());
			statement.setBoolean(11, orderFood.isNotRegFood());
			// convert date sql
			statement.setTimestamp(12, DateUtil.GET_CURRENT_TIMESTAMP());
			statement.setBoolean(13, orderFood.isOverTime());

			statement.execute();
		} catch (SQLException e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				ZKFPDemo.closeConnectionPre(con, statement);
			} catch (Exception e2) {
				// System.out.println(e2.getMessage());
				e2.printStackTrace();
			}
		}
	}

	public static Connection getConnectionSQLServer() {
		// Load driver mysql
		try {
			// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			// handle connect mysql
			String url = "jdbc:sqlserver://localhost:3306/quanlydatcom";
			String user = "remote";
			String password = "remote2013";
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean closeConnectionPre(Connection con, PreparedStatement ps) {
		try {
			if (con != null) {
				con.close();
			}
			if (ps != null) {
				ps.close();
			}
		} catch (Exception e2) {
			return false;
		}
		return true;
	}

	public static boolean closeConnectionSta(Connection con, Statement ps) {
		try {
			if (con != null) {
				con.close();
			}
			if (ps != null) {
				ps.close();
			}
		} catch (Exception e2) {
			return false;
		}
		return true;
	}

	public static String readFile(String path) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}
				String everything = sb.toString();
				return everything;
			} finally {
				br.close();
			}
		} catch (Exception e) {
			return null;
		}
	}
}
