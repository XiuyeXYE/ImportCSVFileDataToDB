package com.xiuye.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import com.xiuye.handler.CsvFileHandler;
import com.xiuye.handler.TimeHandler;
import com.xiuye.jdbc.ColumnFormatCondition;
import com.xiuye.jdbc.Connections;
import com.xiuye.util.DBConfigUtil;
import com.xiuye.util.JDBCConnectionUtil;

public class OpenFileFrame extends JFrame {

	private JMenuBar menuBar;
	private JTextArea area;
	private JLabel fileNameLabel;
	private JTextField fileNameTextField;
	private JFileChooser fileChooser;
	private JProgressBar progressBar;
	private JPanel southPanel;
	private JComboBox<String> comboBox;
	private JLabel time;

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 700;

	private CurrentControlState state;

	/**
	 *
	 */
	private static final long serialVersionUID = 4296680701211544402L;

	private static final String BEGINNING = "1";
	private static final String PAUSE = "2";
	private static final String END = "3";
	private static final String CONTINUE = "4";
	private static final String RUNNING = "5";

	class CurrentControlState {
		private Object control;
		private String state;

		private Thread t;

		public Thread getT() {
			return t;
		}

		public void setT(Thread t) {
			this.t = t;
		}

		public CurrentControlState() {
		}

		public CurrentControlState(Object control, String state) {
			this.control = control;
			this.state = state;
		}

		public Object getControl() {
			return control;
		}

		public void setControl(Object control) {
			this.control = control;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		@Override
		public String toString() {
			return "CurrentControlState [control=" + control + ", state=" + state + ", t=" + t + "]";
		}

	}

	public OpenFileFrame() {
		this.init();
	}

	private void init() {
		this.initLookAndFeel();
		this.initMenu();
		this.initFileChooser();
		this.initTextArea();
		this.initLabels();
		this.initSouthPanel();
		this.initFrame();
	}

	private void initSouthPanel() {
		this.southPanel = new JPanel();
		this.initProgressBar();
		this.initButtons();
		this.getContentPane().add(this.southPanel, BorderLayout.CENTER);
	}

	private void initLabels() {

		this.fileNameLabel = new JLabel("文件名字:");
		this.fileNameTextField = new JTextField();
		this.fileNameTextField.setEditable(false);
		this.fileNameTextField.setColumns(100);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(fileNameLabel);
		panel.add(fileNameTextField);
		this.getContentPane().add(panel, BorderLayout.NORTH);

	}

	private void initProgressBar() {
		this.progressBar = new JProgressBar();
		this.progressBar.setOrientation(JProgressBar.HORIZONTAL);
		this.progressBar.setMinimum(0);
		this.progressBar.setMaximum(100);
		this.progressBar.setValue(0);
		this.progressBar.setStringPainted(true);// 显示进度字符串
		this.progressBar.setPreferredSize(new Dimension(WIDTH, 30));
		this.southPanel.add(progressBar, BorderLayout.NORTH);

	}

	private static final String commandImportDataStart = "开始导入数据";
	private static final String commandImportDataPause = "暂停导入数据";
	private static final String commandImportDataContinue = "继续导入数据";
	private static final String commandImportDataEnd = "结束导入数据";
	private static final String commandImportDataExit = "退出程序";
	private static final String commandImportDataClear = "清理显示";
	private static final String commandImportDataClearTable = "清空数据表数据";

	private void initButtons() {

		JButton start = createButton(commandImportDataStart);
		start.setActionCommand(BEGINNING);
		start.setForeground(Color.BLUE);
		start.setBackground(Color.BLUE);
		JButton end = createButton(commandImportDataEnd);
		end.setForeground(Color.RED);
		end.setEnabled(false);
		JButton exit = createButton(commandImportDataExit);
		exit.setForeground(Color.GREEN);
		JButton clearUIControlData = createButton(commandImportDataClear);
		JButton truncateTables = createButton(commandImportDataClearTable);
		truncateTables.setForeground(Color.RED);
		JButton about = this.createButton("关于");
		about.setForeground(Color.DARK_GRAY);

		this.buttonAddListener(about, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(OpenFileFrame.this,
						"1.本”软件”的一切知识产权，\n" + "以及与”软件”相关的所有信息内容，\n" + "包括但不限于：文字表述及其组合、\n" + "图标、图饰、图像、图表、色彩、\n"
								+ "界面设计、版面框架、有关数据、附加程序、\n" + "印刷材料或电子文档等均为作者所有，\n" + "受著作权法和国际著作权条约以及其他知识产\n"
								+ "权法律法规的保护。\n" + "2.禁止反向工程、反向编译和反向汇编：\n" + "用户不得对本软件产品进行反向工程(Reverse Engineer)、\n"
								+ "反向编译(Decompile)或反向汇编(Disassemble)，\n" + "同时不得改动编译在程序文件内部的任何资源。\n"
								+ "除法律、法规明文规定允许上述活动外，\n" + "用户必须遵守此协议限制。\n" + "3.授权: 如需进行商业性的销售、复制、分发，\n"
								+ "包括但不限于软件销售、预装、捆绑等，必须获得\n" + "作者的授权和许可。\n" + "4.保留权利：本协议未明示授权的其他一切权利仍归作者所有，\n"
								+ "用户使用其他权利时必须获得作者的书面同意。\n" + "5.修改和升级：不升级和维护!\n" + "6.禁止用于非法活动,如触犯任何法律,由当事人自己承担!\n",
						"协议", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		this.buttonAddListener(start, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JButton b = (JButton) e.getSource();

				String command = b.getActionCommand();

				String path = fileNameTextField.getText();

				// check file exist?
				File f = new File(path);
				if (!f.exists()) {
					JOptionPane.showMessageDialog(OpenFileFrame.this, "文件已经不存在了!请重新选择!");
					return;
				}
				String tableName = getTableName();
				if (tableName == null) {
					return;
				}
				if (!checkTableValid(tableName)) {
					JOptionPane.showMessageDialog(null, "没有此表的配置文件,请配置...!", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// validate state?
				if (state == null) {
					state = createCurrentControlState(b, command);
				}
				switch (command) {
				case BEGINNING:
					int result = JOptionPane.showConfirmDialog(OpenFileFrame.this, "确定导入数据...", "导入数据",
							JOptionPane.OK_CANCEL_OPTION);
					if (result != JOptionPane.OK_OPTION) {
						return;
					}

					end.setEnabled(true);
					clearUIControlData.setEnabled(true);
					b.setForeground(Color.MAGENTA);

					Thread t = new Thread() {
						@Override
						public void run() {

							try (InputStreamReader isr = new InputStreamReader(new FileInputStream(f), "UTF-8");
									BufferedReader br = new BufferedReader(isr);
									PreparedStatement ps = JDBCConnectionUtil.preparedStatement(
											DBConfigUtil.generateInsertSQL(tableName), DBConfigUtil.driverClass(),
											DBConfigUtil.url(), DBConfigUtil.user(), DBConfigUtil.password());) {
								// System.out.println(ps);
								List<ColumnFormatCondition> columsConditions = DBConfigUtil
										.columnsFormatConditions(tableName);
								int columnNum = DBConfigUtil.columnsCount(tableName);
								area.setText(null);
								String s = null;
								int i = 0;
								int j = 0;
								long fileSize = f.length();
								double importSize = 0.0;
								while ((s = br.readLine()) != null && !s.isEmpty()) {
									i++;
									if (i % 100 == 0) {
										area.setText(null);
									}
									area.append(s + "\n");
									importSize += s.getBytes().length;
									int value = (int) (importSize / fileSize * 100);
									progressBar.setValue(value);
									if (j == 0) {
										s = s.replaceAll("[^\\w|,]", "");
										// System.out.println(s);
										String[] ss = CsvFileHandler.lineStr2ColumnsData(s);
										if (!checkTableAndCsvFileConsistency(columsConditions, ss)) {
											JOptionPane.showMessageDialog(OpenFileFrame.this,
													"导入数据的文件与数据库表不对应!请重新选择数据库表或导入数据的文件.", "错误",
													JOptionPane.ERROR_MESSAGE);
											setButtonActionCommand(b, END);
										} else {
											if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
													OpenFileFrame.this,
													"在导入数据之前是否清空数据库表以前的的数据?!\n因为导入的是新数据可能和以前的冲突.\n建议:清除历史数据,导入新数据!",
													"导入数据", JOptionPane.OK_CANCEL_OPTION)) {
												truncateTable(tableName);
											}
											TimeHandler.startTiming(time);
										}

									} else if (j > 0 && j % 1000 == 0) {
										String[] ss = CsvFileHandler.lineStr2ColumnsData(s);
										addBatch(columnNum, ps, ss, columsConditions);
										int[] r = ps.executeBatch();
										area.append("再次成功插入" + r.length + "条数据,总共已经插入" + j + "条数据\n");
									} else {
										String[] ss = CsvFileHandler.lineStr2ColumnsData(s);
										addBatch(columnNum, ps, ss, columsConditions);
									}
									j++;
									switch (b.getActionCommand()) {
									case END:
										progressBar.setValue(0);
										fileNameTextField.setText(null);
										state = null;
										setButtonActionCommand(b, BEGINNING);
										b.setText(commandImportDataStart);
										end.setEnabled(false);
										b.setForeground(Color.BLUE);
										Connections.clear();
										TimeHandler.end();
										return;
									case PAUSE:
										while (b.getActionCommand() == PAUSE)
											;
										break;
									}
									// System.out.println(b.getActionCommand());
								}
								int[] r = ps.executeBatch();
								area.append("再次成功插入" + r.length + "条数据,总共已经插入" + (j - 1) + "条数据\n");
							} catch (ClassNotFoundException | IOException | SQLException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(OpenFileFrame.this, "导入数据出错!程序任务终止执行!", "错误",
										JOptionPane.ERROR_MESSAGE);
								progressBar.setValue(0);
								fileNameTextField.setText(null);
								state = null;
								setButtonActionCommand(b, BEGINNING);
								b.setText(commandImportDataStart);
								end.setEnabled(false);
								b.setForeground(Color.BLUE);
								Connections.clear();
								TimeHandler.end();
								return;
							}
							progressBar.setValue(100);
							JOptionPane.showMessageDialog(OpenFileFrame.this, "数据导入完成!");
							// reset
							progressBar.setValue(0);
							fileNameTextField.setText(null);
							state = null;
							b.setActionCommand(BEGINNING);
							b.setText(commandImportDataStart);
							end.setEnabled(false);
							b.setForeground(Color.BLUE);
							Connections.clear();
							TimeHandler.end();
						}

						private void addBatch(int columnNum, PreparedStatement ps, String[] ss,
								List<ColumnFormatCondition> columsConditions) throws SQLException {
							if (columnNum != ss.length) {
								JOptionPane.showMessageDialog(OpenFileFrame.this, "这条数据格式有误和数据表不对应,请核对该数据!\n数据内容:"
										+ Arrays.toString(ss).replaceAll("^\\[|\\]$", "") + "\n请注意数据的对应格式应该是一行一条数据!");
								return;
							}
							for (int i = 0; i < columnNum; i++) {
								ColumnFormatCondition cfc = columsConditions.get(i);
								if (cfc.isInt()) {
									if (ss[i] == null || ss[i].isEmpty()) {
										ps.setInt(i + 1, 0);
									} else {
										ps.setInt(i + 1, Integer.parseInt(ss[i]));
									}

								} else if (cfc.isDate()) {
									if (ss[i] == null || ss[i].isEmpty()) {
										ps.setDate(i + 1, null);
									} else {
										if (cfc.getFormat() != null) {
											SimpleDateFormat format = new SimpleDateFormat(cfc.getFormat());
											try {
												ps.setDate(i + 1, new Date(format.parse(ss[i]).getTime()));
											} catch (ParseException e) {
												e.printStackTrace();
											}
										} else {
											ps.setDate(i + 1, new Date(Date.parse(ss[i])));
										}
									}
								} else if (cfc.isDouble()) {

									if (ss[i] == null || ss[i].isEmpty()) {
										ps.setDouble(i + 1, 0);
									} else {
										ps.setDouble(i + 1, Double.parseDouble(ss[i]));
									}

								} else if (cfc.isFloat()) {
									if (ss[i] == null || ss[i].isEmpty()) {
										ps.setFloat(i + 1, 0);
									} else {
										ps.setFloat(i + 1, Float.parseFloat(ss[i]));
									}
								} else if (cfc.isLong()) {
									if (ss[i] == null || ss[i].isEmpty()) {
										ps.setLong(i + 1, 0);
									} else {
										ps.setLong(i + 1, Long.parseLong(ss[i]));
									}
								} else if (cfc.isString()) {

									if (cfc.getNullContent() != null) {
										ps.setString(i + 1, cfc.getNullContent());
									} else {
										ps.setString(i + 1, ss[i]);
									}
								}
							}
							ps.addBatch();
						}

					};
					t.setPriority(Thread.MAX_PRIORITY);
					t.setName("ImportCSVFileData2DB--Thread");
					t.start();
					// EventQueue.invokeLater(t);
					state.setT(t);
					setButtonActionCommand(b, RUNNING);
					b.setText(commandImportDataPause);
					break;
				case RUNNING:
					setButtonActionCommand(b, PAUSE);
					b.setText(commandImportDataContinue);
					break;
				case PAUSE:
					setButtonActionCommand(b, CONTINUE);
					b.setText(commandImportDataPause);
					break;
				case CONTINUE:
					setButtonActionCommand(b, PAUSE);
					b.setText(commandImportDataContinue);
					break;
				}
				// 主线程放弃processor让子线程有马上响应的机会!
				// 不然有可能出现界面不响应,其实是子线程不响应!
				Thread.yield();
			}
		});

		this.buttonAddListener(end, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(OpenFileFrame.this,
						"确定要结束数据导入数据库吗?!提前结束可能导致导入到数据库的数据不完整,您确定退出吗?!", "提示", JOptionPane.OK_CANCEL_OPTION)) {

					start.setActionCommand(END);
				}

			}
		});
		this.buttonAddListener(exit, this.exitListener());
		this.buttonAddListener(clearUIControlData, this.clearUIControlListener());

		this.buttonAddListener(truncateTables, this.truncateTablesListener());

		JPanel panel = new JPanel(new GridLayout(5, 2));

		JLabel labelTime = this.createLabel("时间:");
		this.time = this.createLabel("00:00:00");
		time.setForeground(Color.GREEN);
		JLabel label = this.createLabel("数据库表:");
		label.setForeground(Color.ORANGE);
		panel.add(labelTime);
		panel.add(time);
		panel.add(label);

		this.comboBox = this.createComboBox();
		this.comboxAddItems(comboBox);
		panel.add(comboBox);
		panel.add(start);
		panel.add(end);
		// panel.add(pause);
		panel.add(truncateTables);
		panel.add(clearUIControlData);
		panel.add(about);
		panel.add(exit);
		this.southPanel.add(panel, BorderLayout.CENTER);
	}

	private synchronized void setButtonActionCommand(JButton b, String command) {
		b.setActionCommand(command);
		state.setState(command);
	}

	private ActionListener truncateTablesListener() {

		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String tableName = getTableName();
				if (state != null) {
					JOptionPane.showMessageDialog(OpenFileFrame.this, "程序正在运行中,无法执行此操作!");
					return;
				}
				if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(OpenFileFrame.this,
						"确定要删除数据库表:" + comboBox.getSelectedItem() + "的所有数据吗?!\n这可是一个危险的操作额!删除后不可恢复额!", "警告",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)) {
					truncateTable(tableName);
				}
			}
		};
	}

	public void truncateTable(String tableName) {
		String sql = DBConfigUtil.generateTruncateTableSQL(tableName);
		try (PreparedStatement ps = JDBCConnectionUtil.preparedStatement(sql, DBConfigUtil.driverClass(),
				DBConfigUtil.url(), DBConfigUtil.user(), DBConfigUtil.password());) {
			ps.execute();
			JOptionPane.showMessageDialog(OpenFileFrame.this, "删除数据库表:" + tableName + "的数据成功!", "成功",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(OpenFileFrame.this, "删除数据库表:" + tableName + "的数据失败!", "失败",
					JOptionPane.WARNING_MESSAGE);

			Connections.clear();
			e.printStackTrace();
		}
	}

	private boolean checkTableAndCsvFileConsistency(List<ColumnFormatCondition> columsConditions, String[] columns) {

		if (columsConditions.size() != columns.length) {
			return false;
		}

		for (int i = 0; i < columns.length; i++) {

			if (!columsConditions.get(i).getColumnName().trim().equalsIgnoreCase(columns[i].trim())) {
				return false;
			}
		}

		return true;
	}

	private boolean checkTableValid(String tableName) {
		InputStream is = OpenFileFrame.class.getClassLoader()
				.getResourceAsStream("tables" + File.separator + tableName + ".properties");
		if (is != null) {
			return true;
		}
		return false;
	}

	private String getTableName() {
		String s = (String) this.comboBox.getSelectedItem();
		if (s.contains(":=")) {
			String[] ss = s.split(":=");
			if (ss.length == 2) {
				return ss[1];
			} else {
				return null;
			}
		} else {
			return s;
		}
	}

	private void comboxAddItems(JComboBox<String> combox) {

		Set<Entry<Object, Object>> set = DBConfigUtil.tables();
		for (Entry<Object, Object> e : set) {
			if (e.getValue() != null && !e.getValue().toString().isEmpty()) {
				combox.addItem(e.getValue() + ":=" + e.getKey());
			} else {
				combox.addItem(e.getKey().toString());
			}
		}
	}

	private JLabel createLabel(String str) {
		JLabel label = new JLabel(str, JLabel.CENTER);
		label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, 25));
		return label;
	}

	private <T> JComboBox<T> createComboBox() {

		JComboBox<T> comboBox = new JComboBox<>();
		comboBox.setFont(new Font(comboBox.getFont().getFontName(), Font.BOLD, 25));
		return comboBox;

	}

	private ActionListener clearUIControlListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				area.setText(null);
			}
		};
	}

	private ActionListener exitListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				System.exit(0);

			}
		};
	}

	private CurrentControlState createCurrentControlState(Object control, String state) {
		return new CurrentControlState(control, state);
	}

	private JButton createButton(String buttonName) {

		JButton b = new JButton(buttonName);

		b.setFont(new Font(b.getFont().getFontName(), Font.BOLD, 25));

		return b;

	}

	private void buttonAddListener(JButton button, ActionListener listener) {
		button.addActionListener(listener);
	}

	private void initLookAndFeel() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

	}

	private void initTextArea() {
		this.area = new JTextArea(10, 35);
		area.setEditable(false);
		Font font = new Font(this.area.getFont().getFontName(), Font.BOLD, 24);
		this.area.setFont(font);
		JScrollPane jScrollPane = new JScrollPane(area);
		this.getContentPane().add(jScrollPane, BorderLayout.SOUTH);
	}

	private void initMenu() {
		JMenu menuOperation = new JMenu("操作");
		JMenu menuConfiguration = new JMenu("配置");

		this.menuBar = new JMenuBar();
		JMenuItem open = new JMenuItem("打开");

		JMenuItem addConfig = new JMenuItem("新建");
		JMenuItem alterConfig = new JMenuItem("修改");
		JMenuItem deleteConfig = new JMenuItem("删除");

		addConfig.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JOptionPane.showMessageDialog(OpenFileFrame.this, "打开配置文件窗口!");

			}
		});
		alterConfig.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JOptionPane.showMessageDialog(OpenFileFrame.this, "打开修改配置文件窗口!");

			}
		});
		deleteConfig.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JOptionPane.showMessageDialog(OpenFileFrame.this, "打开删除配置文件窗口!");

			}
		});

		this.MenuItemAddListener(open, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				fileNameTextField.setText(null);
				if (fileChooser.showOpenDialog(OpenFileFrame.this) == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					fileNameTextField.setText(f.getAbsolutePath());

				}
			}

		});

		menuOperation.add(open);
		menuConfiguration.add(addConfig);
		menuConfiguration.add(alterConfig);
		menuConfiguration.add(deleteConfig);
		this.menuBar.add(menuOperation);
		this.menuBar.add(menuConfiguration);
		this.setJMenuBar(menuBar);

	}

	private void initFileChooser() {
		File f = null;
		String filePath = System.getProperty("user.home");
		if (filePath == null) {
			filePath = ".";
			f = new File(filePath);
		} else {
			filePath += File.separator + "desktop";
			f = new File(filePath);
			if (!f.exists()) {
				filePath += File.separator + "桌面";
				f = new File(filePath);
			}
		}
		this.fileChooser = new JFileChooser(f);
		this.fileChooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "*.csv";
			}

			@Override
			public boolean accept(File f) {

				return f.isDirectory() || f.getName().endsWith(".csv");
			}
		});
	}

	private void MenuItemAddListener(JMenuItem item, ActionListener l) {
		item.addActionListener(l);
	}

	public void display() {
		this.setVisible(true);
	}

	private void initFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("app.png");
		this.setIconImage(image);
	}

}
